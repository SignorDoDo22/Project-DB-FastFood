package project.db.controller;

import project.db.view.Client.RigaCarrello;

import java.util.ArrayList;
import java.util.List;

import project.db.data.Prodotto;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Client.ClientPanel;
import project.db.view.Client.Carrello;
import project.db.view.Client.RecensioniPanel;



public class ControllerClientPanel {

    private final ReadingModel modelReading;
    private final WritingModel modelWriting;
    private final ClientPanel clientPanel;
    private final MainController mainController;
    private final Carrello carrello;
    private final RecensioniPanel recensioniPanel;
    private List<Prodotto> prodotti = new ArrayList<>();


    public ControllerClientPanel(final ReadingModel modelReading, final WritingModel modelWriting, final ClientPanel clientPanel, final MainController mainController){
        this.modelReading = modelReading;
        this.modelWriting = modelWriting;
        this.clientPanel = clientPanel;
        this.mainController = mainController;
        this.clientPanel.setController(this);

        this.carrello = new Carrello(clientPanel, this);
        this.recensioniPanel = new RecensioniPanel(clientPanel, this);

        this.clientPanel.setCarrelloPanel(carrello);
        this.clientPanel.setRecensioniPanel(recensioniPanel);
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

    public void userRequestCreateRecensione(String codiceOrdine, int votoRider, int votoOrdine, String testoRecensione) {
        this.modelWriting.inserisciRecensione(codiceOrdine, testoRecensione, votoOrdine, votoRider);
    }

}


