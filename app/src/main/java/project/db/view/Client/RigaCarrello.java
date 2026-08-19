package project.db.view.Client;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.db.controller.ControllerClientPanel;
import project.db.view.ProdottoCatalogo.ModificaIngredientiPanel;

public class RigaCarrello extends JPanel {

    protected float prezzo;
    protected int quantita;
    protected String nomeProdotto;
    protected Carrello carrello;
    protected final String codiceProdotto;
    protected JLabel nomeProdottoLabel;
    protected JLabel quantitaLabel;
    protected JLabel prezzoLabel;
    protected JButton buttonModificaRiga;
    protected JButton buttonEliminaRiga;
    protected boolean menu;
    protected String codiceRiga;
    protected ControllerClientPanel controllerClientPanel;

    // Accesso protected (non più private) perché EliminaIngrediente,
    // ora spostato in RigaCarrelloMenu, deve poterlo usare.
    protected ModificaIngredientiPanel modificaIngredientiPanel;

    public RigaCarrello(boolean menu, float prezzo, int quantita, String nomeProdotto, String codiceProdotto,
            Carrello carrello, ControllerClientPanel controllerClientPanel, String codiceRiga) {
        this.prezzo = prezzo;
        this.codiceProdotto = codiceProdotto;
        this.codiceRiga = codiceRiga;
        this.quantita = quantita;
        this.carrello = carrello;
        this.nomeProdotto = nomeProdotto;
        this.menu = menu;
        this.controllerClientPanel = controllerClientPanel;

        this.modificaIngredientiPanel = new ModificaIngredientiPanel(this);
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

        this.buttonEliminaRiga.addActionListener(e -> carrello.rimuoviProdottoCarrello(this));

        this.buttonModificaRiga.addActionListener(e -> {
            this.controllerClientPanel.userRequestModificaRigaCarrello(this);
        });
    }

    public void mostraIngredientiDisponibili(List<String> ingredientiDisponibili) {
        modificaIngredientiPanel.caricaIngredientiDisponibili(ingredientiDisponibili);
        modificaIngredientiPanel.setVisible(true);
    }

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

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public void setVisibleModificaIngredientiPanel(boolean visible) {
        modificaIngredientiPanel.setVisible(visible);
    }

}