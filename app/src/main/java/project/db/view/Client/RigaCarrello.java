package project.db.view.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.db.view.ProdottoCatalogo.ModificaIngredientiPanel;
import project.db.view.ProdottoCatalogo.Selezionacomponentemenupanel;


public class RigaCarrello extends JPanel {

    private float prezzo;
    private int quantita;
    private String nomeProdotto;
    private Carrello carrello;
    private final String codiceProdotto;
    private JLabel nomeProdottoLabel;
    private JLabel quantitaLabel;
    private JLabel prezzoLabel;
    private JButton buttonModificaRiga;
    private JButton buttonEliminaRiga;
    private boolean menu;

    // Modifiche per un prodotto SINGOLO: nome ingrediente -> nuova quantità.
    private final Map<String, Integer> modificaIngredienti = new HashMap<>();

    // Modifiche per un MENU: nome componente -> (nome ingrediente -> nuova quantità).
    private final Map<String, Map<String, Integer>> modificheIngredientiMenu = new HashMap<>();

    // Contesto attivo nel pannello di modifica al momento: per una riga singolo
    // resta sempre il nome del prodotto stesso; per una riga menu è il nome del
    // componente attualmente selezionato per la modifica.
    private String contestoCorrente;

    // Ingredienti presenti (nome -> quantità) relativi al contesto attivo:
    // per il prodotto singolo arrivano dal DB con la quantità reale; per un
    // componente di un menu non abbiamo la quantità, quindi viene impostata
    // come "illimitata" (vedi selezionaComponenteMenu).
    private Map<String, Integer> ingredientiPresentiCorrenti;

    // Elenco di tutti i nomi ingrediente disponibili a catalogo, usato per la
    // sezione "aggiungi ingrediente" del pannello di modifica. Caricato pigramente
    // la prima volta che serve, poi riutilizzato.
    private List<String> ingredientiDisponibiliTotali;

    private ModificaIngredientiPanel modificaIngredientiPanel;
    private Selezionacomponentemenupanel selezionaComponenteMenuPanel;

    public RigaCarrello(boolean menu, float prezzo, int quantita, String nomeProdotto, String codiceProdotto, Carrello carrello) {
        this.prezzo = prezzo;
        this.codiceProdotto = codiceProdotto;
        this.quantita = quantita;
        this.carrello = carrello;
        this.nomeProdotto = nomeProdotto;
        this.menu = menu;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.nomeProdottoLabel = new JLabel(nomeProdotto);
        this.quantitaLabel = new JLabel(String.valueOf(quantita));
        this.prezzoLabel = new JLabel(String.format(" € %.3f", prezzo));

        this.add(nomeProdottoLabel);
        this.add(quantitaLabel);
        this.add(prezzoLabel);
        this.buttonModificaRiga = new JButton("Modifica");
        this.buttonEliminaRiga = new JButton("Elimina");
        this.add(buttonModificaRiga);
        this.add(buttonEliminaRiga);

        this.buttonModificaRiga.addActionListener(e -> {
            if (this.menu) {
                // Menu: prima si sceglie quale prodotto contenuto modificare.
                carrello.requestComponentiMenu(this);
            } else {
                // Singolo: si va dritti alla modifica degli ingredienti del prodotto.
                requestIngredientiPresenti();
            }
        });

        this.buttonEliminaRiga.addActionListener(e -> carrello.rimuoviProdottoCarrello(this));
    }

    // =====================================================================
    // Flusso PRODOTTO SINGOLO
    // =====================================================================

    public void requestIngredientiPresenti() {
        contestoCorrente = nomeProdotto;
        carrello.requestIngredientiPresenti(this);
    }

    /**
     * Callback chiamata dal controller con gli ingredienti (nome -> quantità
     * disponibile) del prodotto singolo di questa riga.
     */
    public void mostraIngredienti(Map<String, Integer> ingredienti) {
        this.ingredientiPresentiCorrenti = ingredienti;
        apriPannelloModifica(contestoCorrente, ingredienti);
    }

    // =====================================================================
    // Flusso MENU: selezione del componente, poi modifica dei suoi ingredienti
    // =====================================================================

    /**
     * Callback chiamata dal controller con la composizione del menu:
     * nome componente -> lista dei suoi ingredienti.
     */
    public void mostraComponentiMenu(Map<String, List<String>> componenti) {
        if (selezionaComponenteMenuPanel == null) {
            selezionaComponenteMenuPanel = new Selezionacomponentemenupanel(this);
        }
        selezionaComponenteMenuPanel.caricaComponenti(componenti);
        selezionaComponenteMenuPanel.setVisible(true);
    }

    /**
     * Chiamato dal Selezionacomponentemenupanel quando l'utente sceglie un
     * componente del menu da modificare. Non avendo a disposizione la
     * quantità reale degli ingredienti di questo componente (il metodo di
     * lettura della composizione del menu restituisce solo i nomi), la
     * quantità disponibile viene impostata come "illimitata" per non
     * bloccare le modifiche.
     */
    public void selezionaComponenteMenu(String nomeComponente, List<String> ingredientiComponente) {
        contestoCorrente = nomeComponente;

        Map<String, Integer> presenti = new HashMap<>();
        if (ingredientiComponente != null) {
            for (String ingrediente : ingredientiComponente) {
                presenti.put(ingrediente, Integer.MAX_VALUE);
            }
        }
        this.ingredientiPresentiCorrenti = presenti;
        apriPannelloModifica(nomeComponente, presenti);
    }

    // =====================================================================
    // Apertura pannello di modifica (comune a singolo e menu)
    // =====================================================================

    private void apriPannelloModifica(String titolo, Map<String, Integer> ingredientiPresenti) {
        if (ingredientiDisponibiliTotali == null) {
            ingredientiDisponibiliTotali = carrello.requestIngredientiDisponibili();
        }
        if (modificaIngredientiPanel == null) {
            modificaIngredientiPanel = new ModificaIngredientiPanel(this);
        }
        modificaIngredientiPanel.impostaContesto(titolo, ingredientiPresenti, ingredientiDisponibiliTotali);
        modificaIngredientiPanel.setVisible(true);
    }

    // =====================================================================
    // Registrazione modifiche (chiamate dal ModificaIngredientiPanel)
    // =====================================================================

    /**
     * Modifica la quantità di un ingrediente già presente nel contesto
     * attivo (prodotto singolo o componente di menu selezionato).
     */
    public boolean requestModificaIngrediente(String nomeIngrediente, Integer quantita) {
        if (nomeIngrediente == null || quantita == null || ingredientiPresentiCorrenti == null) {
            return false;
        }

        Integer quantitaDisponibile = ingredientiPresentiCorrenti.get(nomeIngrediente);

        if (quantitaDisponibile == null || quantita > quantitaDisponibile) {
            return false;
        }

        mappaModificheContestoCorrente().put(nomeIngrediente, quantita);
        return true;
    }

    /**
     * Aggiunge un ingrediente non presente nel contesto attivo (prodotto
     * singolo o componente di menu selezionato).
     */
    public boolean requestAggiungiIngrediente(String nomeIngrediente, Integer quantita) {
        if (nomeIngrediente == null || quantita == null || quantita <= 0) {
            return false;
        }

        mappaModificheContestoCorrente().put(nomeIngrediente, quantita);

        // L'ingrediente aggiunto ora è "presente" anche nel contesto corrente,
        // così eventuali modifiche successive lo trovano già disponibile.
        if (ingredientiPresentiCorrenti != null) {
            ingredientiPresentiCorrenti.put(nomeIngrediente, quantita);
        }
        return true;
    }

    private Map<String, Integer> mappaModificheContestoCorrente() {
        if (menu) {
            return modificheIngredientiMenu.computeIfAbsent(contestoCorrente, k -> new HashMap<>());
        }
        return modificaIngredienti;
    }

    // =====================================================================
    // Getter
    // =====================================================================

    public int getQuantita() {
        return quantita;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public boolean isMenu() {
        return menu;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    /** Modifiche per un prodotto singolo: nome ingrediente -> nuova quantità. */
    public Map<String, Integer> getModificaIngredienti() {
        return modificaIngredienti;
    }

    /** Modifiche per un menu: nome componente -> (nome ingrediente -> nuova quantità). */
    public Map<String, Map<String, Integer>> getModificheIngredientiMenu() {
        return modificheIngredientiMenu;
    }
}