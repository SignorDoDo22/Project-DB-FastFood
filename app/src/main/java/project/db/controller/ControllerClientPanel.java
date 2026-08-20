package project.db.controller;

import project.db.view.Client.RigaCarrello;
import project.db.view.Client.RigaCarrelloMenu;
import project.db.view.Client.RigaCarrelloSingolo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import project.db.data.Cliente;
import project.db.data.Ingrediente;
import project.db.data.Pair;
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
    private List<RigaCarrello> prodottiCarrello = new ArrayList<>();

    public ControllerClientPanel(final ReadingModel modelReading, final WritingModel modelWriting,
            final ClientPanel clientPanel, final MainController mainController) {

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

        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    public void userRequestIngredientiProd(final String codice_prodotto) {

        final var ingredienti = this.modelReading.loadIngredienti(codice_prodotto);
        this.clientPanel.mostraIngredienti(ingredienti);
    }

    public void userRequestOrdiniRecensibili() {
        final var ordiniRecensibili = this.modelReading
                .loadOrdiniRecensibili(this.mainController.getControllerLogin().getUtente().getCodiceUtente());
        this.recensioniPanel.mostraRecensioni(ordiniRecensibili);
    }

    public void userRequestIngredientiMenu(final String codiceProdottoMenu) {
        this.clientPanel.mostraIngredientiMenu(this.modelReading.loadIngredientiMenu(codiceProdottoMenu));

    }

    public void userRequestComponentiMenu(final String codiceProdottoMenu, final RigaCarrelloMenu rigaCarrello) {
        final Map<Pair<String, Integer>, List<String>> componenti = this.modelReading
                .loadIngredientiMenu(codiceProdottoMenu);
        rigaCarrello.mostraComponentiMenu(componenti);
    }

    public void userRequestComponentiMenuCaricamento(final String codiceProdottoMenu,
            final RigaCarrelloMenu rigaCarrello) {

        final Map<Pair<String, Integer>, List<String>> componenti = this.modelReading
                .loadIngredientiMenu(codiceProdottoMenu);
        rigaCarrello.caricaProdottiMenu(componenti);
    }

    public void userRequestIngredientiDisponibili(RigaCarrello riga) {
        final var ingredientiDisponibili = this.modelReading.loadIngredientiDisponibili();
        final List<String> nomiIngredienti = new ArrayList<>();
        for (final Ingrediente ingrediente : ingredientiDisponibili) {
            nomiIngredienti.add(ingrediente.getNomeIngrediente());
        }
        if (riga instanceof RigaCarrelloMenu) {
            RigaCarrelloMenu rigaMenu = (RigaCarrelloMenu) riga;
            rigaMenu.mostraIngredientiDisponibili(nomiIngredienti);
        } else {
            riga.mostraIngredientiDisponibili(nomiIngredienti);
        }
    }

    public void userRequestModificaRigaCarrello(RigaCarrello riga) {
        if (riga.isMenu()) {
            RigaCarrelloMenu rigaMenu = (RigaCarrelloMenu) riga;
            userRequestComponentiMenu(rigaMenu.getCodiceProdotto(), rigaMenu);
        } else {
            userRequestIngredientiDisponibili(riga);
        }

    }

    public void userRequestCreateRecensione(String codiceOrdine, int votoRider, int votoOrdine,
            String testoRecensione) {
        this.modelWriting.inserisciRecensione(codiceOrdine, testoRecensione, votoOrdine, votoRider);
        this.modelWriting.aggiornaRaitingRider(votoRider, this.modelReading.getRiderCodeByOrdine(codiceOrdine));
    }

    public boolean userCreateOrdine() {
        final Map<String, String> datiDomicilio = domicilioPanel.getCampiDomicilio();
        final String codiceUtente = this.utenteLoggato.getCodiceUtente();
        final Connection conn = this.modelWriting.getConnection();

        try {
            conn.setAutoCommit(false);

            final String codiceOrdine = this.modelReading.getNextOrdineCode();

            final boolean ordineCreato = this.modelWriting.createOrdine(datiDomicilio, codiceUtente, codiceOrdine);
            if (!ordineCreato) {

                conn.rollback();
                return false;
            }

            if (this.modelWriting.aggiornaStatoOrdine(conn, codiceOrdine, "In Preparazione")) {

            } else {

                conn.rollback();
                return false;
            }

            for (final RigaCarrello riga : carrello.getRigheCarrello()) {

                final boolean rigaInserita = this.modelWriting.createRigaOrdine(
                        codiceOrdine,
                        riga.getCodiceProdotto(),
                        riga.getQuantita(),
                        riga.getPrezzo(),
                        riga.isMenu(),
                        riga.getCodiceRiga());

                if (!rigaInserita) {

                    conn.rollback();
                    return false;
                }

                if (riga.isMenu()) {

                    modelWriting.inserireMenuRiga(conn, codiceOrdine, riga.getCodiceRiga(), riga.getCodiceProdotto());
                    RigaCarrelloMenu rigaMenu = (RigaCarrelloMenu) riga;
                    Map<Integer, String> componentiMenu = rigaMenu.getComponentiMenu();
                    for (Map.Entry<Integer, String> entry : componentiMenu.entrySet()) {
                        String codiceProdotto = modelReading.getCodiceProdottoByNome(entry.getValue());
                        this.modelWriting.inserisciCompMenuRiga(conn, codiceOrdine, riga.getCodiceRiga(),
                                codiceProdotto,
                                entry.getKey());
                    }

                    Map<Pair<String, Integer>, Map<String, Pair<String, Integer>>> ingredientiModificati = rigaMenu
                            .getIngredientiModificati();
                    for (Pair<String, Integer> prodotto : ingredientiModificati.keySet()) {
                        Integer numRowComp = prodotto.getSecond();
                        Map<String, Pair<String, Integer>> modifiche = ingredientiModificati.get(prodotto);
                        for (String modifica : modifiche.keySet()) {

                            Integer quantita = modifiche.get(modifica).getSecond();
                            String tipo = modifiche.get(modifica).getFirst();

                            this.modelWriting.inserisciModificaCompMenu(conn, codiceOrdine, rigaMenu.getCodiceRiga(),
                                    numRowComp,
                                    modelReading.getCodiceIngredienteByNome(modifica), quantita, tipo);

                        }

                    }

                } else {
                    RigaCarrelloSingolo rigaSingolo = (RigaCarrelloSingolo) riga;
                    modelWriting.inserireRigaSingolo(conn, codiceOrdine, rigaSingolo.getCodiceRiga(),
                            rigaSingolo.getCodiceProdotto());

                    for (Map.Entry<String, Pair<String, Integer>> entry : rigaSingolo
                            .getIngredientiModificatiProdSingolo().entrySet()) {
                        String nomeIngrediente = entry.getKey();
                        String tipo = entry.getValue().getFirst();
                        Integer quantita = entry.getValue().getSecond();
                        modelWriting.inserisciModificaRigSingolo(conn, codiceOrdine, rigaSingolo.getCodiceRiga(),
                                modelReading.getCodiceIngredienteByNome(nomeIngrediente), quantita, tipo);
                    }

                }

            }

            conn.commit();
            carrello.svuotaCarrello();
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

    public String generaProssimoCodiceRiga() {
        int numero = carrello.getRigheCarrello().size() + 1;
        return String.format("R%03d", numero);
    }

    public boolean verificaEliminazioneIngrediente(String nomeProdotto, String nomeIngrediente,
            int quantitaRichiesta) {
        return modelReading.controllaQuantitaIngrediente(modelReading.getCodiceIngredienteByNome(nomeIngrediente),
                quantitaRichiesta, nomeProdotto);
    }

    public void setVisibleDomicilioPanel(boolean visible) {
        if (domicilioPanel != null) {
            domicilioPanel.setVisible(visible);
        }
    }

    public void addRigaCarrello(RigaCarrello riga) {
        prodottiCarrello.add(riga);
    }

    public void logout() {
        this.utenteLoggato = null;
    }

}