package project.db.controller;

import project.db.view.Client.RigaCarrello;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import project.db.data.Cliente;
import project.db.data.CompRigaOrdineMenu;
import project.db.data.Ingrediente;
import project.db.data.RigaOrdine;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Client.ClientPanel;
import project.db.view.Client.DomicilioPanel;
import project.db.view.Client.Carrello;
import project.db.view.Client.RecensioniPanel;



public class ControllerClientPanel {

    private final ReadingModel modelReading;
    private final WritingModel modelWriting;
    private final ClientPanel clientPanel;
    private final MainController mainController;
    private final Carrello carrello;
    private Cliente utenteLoggato;
    private final RecensioniPanel recensioniPanel;
    private DomicilioPanel domicilioPanel;


    public ControllerClientPanel(final ReadingModel modelReading, final WritingModel modelWriting, final ClientPanel clientPanel, final MainController mainController){

        this.modelReading = modelReading;
        this.modelWriting = modelWriting;
        this.clientPanel = clientPanel;
        this.mainController = mainController;
        this.clientPanel.setController(this);
        this.domicilioPanel = new DomicilioPanel(this);
        this.carrello = new Carrello(clientPanel, this);
        this.recensioniPanel = new RecensioniPanel(clientPanel, this);
        this.clientPanel.setCarrelloPanel(carrello);
        this.clientPanel.setRecensioniPanel(recensioniPanel);
    }

    public void setUtenteLoggato(final Cliente utenteLoggato) {
        this.utenteLoggato = utenteLoggato;
    }

    public void userRequestedCatalogo() {
        try {
            final var prodotti = this.modelReading.loadProdotti();
            this.clientPanel.showCatalogo(prodotti);
            System.out.println("Catalogo caricato con successo");
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    public void userRequestIngredientiProd(final String codice_prodotto) {

        System.out.println("Richiesta ingredienti per il prodotto con codice: " + codice_prodotto);
        final var ingredienti = this.modelReading.loadIngredienti(codice_prodotto);
        this.clientPanel.mostraIngredienti(ingredienti);
    }


    public void userRequestOrdiniRecensibili(){
        final var ordiniRecensibili = this.modelReading.loadOrdiniRecensibili(this.mainController.getControllerLogin().getUtente().getCodiceUtente());
        System.out.println("Ordini recensibili caricati con successo: " + ordiniRecensibili.size());
        this.recensioniPanel.mostraRecensioni(ordiniRecensibili);
    }

    public void userRequestIngredientiMenu(final String codiceProdottoMenu){
        this.clientPanel.mostraIngredientiMenu(this.modelReading.loadIngredientiMenu(codiceProdottoMenu));

    }

    public void userRequestIngredientiFromRigaCarrello(final String codiceProdotto, final RigaCarrello rigaCarrello){
        final var ingredienti = this.modelReading.loadIngredientiFromRigaCarrello(codiceProdotto);
        rigaCarrello.mostraIngredienti(ingredienti);
    }

    /**
     * Richiede la composizione di un menu (nome componente -> suoi ingredienti)
     * per una specifica riga del carrello, così l'utente può scegliere quale
     * prodotto contenuto nel menu vuole modificare.
     */
    public void userRequestComponentiMenu(final String codiceProdottoMenu, final RigaCarrello rigaCarrello) {
        final Map<String, List<String>> componenti = this.modelReading.loadIngredientiMenu(codiceProdottoMenu);
        System.out.println("Componenti del menu caricate con successo: " + componenti.size());
        rigaCarrello.mostraComponentiMenu(componenti);
    }

    /**
     * Restituisce i nomi di tutti gli ingredienti disponibili a catalogo,
     * usati per popolare la sezione "aggiungi ingrediente" nel pannello di
     * modifica del carrello.
     *
     * NOTA: assume che Ingrediente esponga un metodo getNomeIngrediente().
     * Se il getter si chiama diversamente, va corretto qui.
     */
    public List<String> userRequestIngredientiDisponibili() {
        final var ingredientiDisponibili = this.modelReading.loadIngredientiDisponibili();
        final List<String> nomiIngredienti = new ArrayList<>();
        for (final Ingrediente ingrediente : ingredientiDisponibili) {
            nomiIngredienti.add(ingrediente.getNomeIngrediente());
        }
        return nomiIngredienti;
    }

        public void userRequestCreateRecensione(String codiceOrdine, int votoRider, int votoOrdine, String testoRecensione) {
            this.modelWriting.inserisciRecensione(codiceOrdine, testoRecensione, votoOrdine, votoRider);
        }

        /**
         * Crea un nuovo ordine a partire dal contenuto del carrello: inserisce prima
         * l'ordine (con i dati del domicilio) e poi, una per una, tutte le righe
         * prodotto associate. Se l'inserimento dell'ordine o di una qualsiasi riga
         * fallisce, l'operazione viene interrotta e il metodo ritorna false, senza
         * svuotare il carrello (così l'utente non perde ciò che aveva selezionato
         * e può riprovare).
         *
         * @return true se l'ordine e tutte le sue righe sono stati creati con successo.
         */
    public boolean userCreateOrdine() {
        final Map<String, String> datiDomicilio = domicilioPanel.getCampiDomicilio();
        final String codiceUtente = this.utenteLoggato.getCodiceUtente();
        final Connection conn = this.modelWriting.getConnection();

        try {
            conn.setAutoCommit(false);

            final String codiceOrdine = this.modelReading.getNextOrdineCode();

            final boolean ordineCreato = this.modelWriting.createOrdine(datiDomicilio, codiceUtente, codiceOrdine);
            if (!ordineCreato) {
                System.out.println("Creazione ordine fallita per codice: " + codiceOrdine);
                conn.rollback();
                return false;
            }

            for (final RigaCarrello riga : carrello.getRigheCarrello()) {

                final String codiceRiga = RigaOrdine.DAO.getNextCodiceRiga(conn, codiceOrdine);

                final boolean rigaInserita = this.modelWriting.createRigaOrdine(
                        codiceOrdine,
                        riga.getCodiceProdotto(),
                        riga.getQuantita(),
                        riga.getPrezzo(),
                        riga.isMenu(),
                        codiceRiga);

                if (!rigaInserita) {
                    System.out.println("Inserimento riga fallito per il prodotto: " + riga.getCodiceProdotto());
                    conn.rollback();
                    return false;
                }

            }

            conn.commit();
            carrello.svuotaCarrello();
            System.out.println("Ordine " + codiceOrdine + " creato con successo");
            return true;

        } catch (final DAOException | SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (final SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setVisibleDomicilioPanel(boolean visible) {
        if (domicilioPanel != null) {
            domicilioPanel.setVisible(visible);
        }
    }

    public void logout(){
        this.utenteLoggato = null;
    }

}