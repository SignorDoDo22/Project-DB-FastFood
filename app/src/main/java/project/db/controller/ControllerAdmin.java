package project.db.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JCheckBox;
import project.db.data.Ingrediente;
import project.db.data.Prodotto;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Admin.AdminPanel;
import project.db.view.Admin.CreaMenuPanel;
import project.db.view.Admin.CreaProdottoSingPanel;
import project.db.view.Admin.CreateIngrediente;
import project.db.view.Admin.CreaCategoria;


/**
 * Controller per le operazioni dell'Admin sul catalogo: visualizzare,
 * modificare ed eliminare prodotti. Segue lo stesso pattern degli altri
 * controller del progetto (riceve Reading/WritingModel, non parla mai
 * direttamente con JDBC).
 */
public class ControllerAdmin {

    private final ReadingModel modelReading;
    private final WritingModel writingModel;
    private final AdminPanel adminPanel;
    private CreaMenuPanel createMenuPanel;
    private CreaProdottoSingPanel createProdottoPanel;
    private CreateIngrediente createIngredientePanel;
    private CreaCategoria creaCategoriaPanel;

    public ControllerAdmin(final ReadingModel modelReading, final WritingModel writingModel, final AdminPanel adminPanel) {
        this.modelReading = modelReading;
        this.writingModel = writingModel;
        this.adminPanel = adminPanel;
        this.createMenuPanel = new CreaMenuPanel(this);
        this.createProdottoPanel = new CreaProdottoSingPanel(this);
        this.createIngredientePanel = new CreateIngrediente(this);
        this.creaCategoriaPanel = new CreaCategoria(this);
        this.adminPanel.setController(this);
    }


    public void userRequestCreateIngredientePanel() {
        this.createIngredientePanel.setVisible(true);
    }

    public void userRequestCreateCategoriaPanel() {
        this.creaCategoriaPanel.setVisible(true);
    }


    public void userRequestedCatalogo() {
        try {
            List<Prodotto> catalogo = this.modelReading.loadProdotti();
            this.adminPanel.mostraCatalogo(catalogo);
        } catch (final DAOException e) {
            e.printStackTrace();
            adminPanel.mostraErrore("Errore nel caricamento del catalogo.");
        }
    }

    public void userRequestedModifica(final String codiceProdotto, final String nome,
                                       final String descrizione, final float prezzo, final boolean disponibile) {
        try {
            boolean ok = this.writingModel.aggiornaProdotto(codiceProdotto, nome, descrizione, prezzo, disponibile);
            if (ok) {
                adminPanel.mostraMessaggio("Prodotto aggiornato con successo.");
                userRequestedCatalogo();
            } else {
                adminPanel.mostraErrore("Nessun prodotto trovato con quel codice.");
            }
        } catch (final DAOException e) {
            e.printStackTrace();
            adminPanel.mostraErrore("Errore durante l'aggiornamento del prodotto.");
        }
    }

    /**
     * Tenta l'eliminazione fisica. Se il prodotto e' gia' stato ordinato, blocca
     * e propone il soft-delete. Se e' componente di altri Menu, avvisa e chiede
     * se disabilitare anche quelli.
     */

    public void userRequestedEliminazione(final String codiceProdotto) {
        try {

            if(this.modelReading.isProdottoMenu(codiceProdotto)){
                System.out.println("Il prodotto con codice " + codiceProdotto + " è un menu, procedo con l'eliminazione dal menu.");
                if (this.modelReading.isProdottoOrdinato(codiceProdotto)) {
                    boolean vuoleSoftDelete = adminPanel.chiediSoftDelete(
                        "Questo prodotto e' gia' stato ordinato in passato, non puo' essere eliminato. "
                        + "Vuoi renderlo non disponibile invece?");
                    if (vuoleSoftDelete) {
                        this.writingModel.rendiNonDisponibile(codiceProdotto);
                        adminPanel.mostraMessaggio("Prodotto reso non disponibile.");
                        userRequestedCatalogo();
                    }
                    return;
                }

                if(this.writingModel.eliminaProdottoDalMenu(codiceProdotto) && this.writingModel.eliminaMenu(codiceProdotto)){
                    adminPanel.mostraMessaggio("Prodotto eliminato dal menu.");
                    userRequestedCatalogo();
                } else {
                    adminPanel.mostraErrore("Errore durante l'eliminazione del prodotto dal menu.");
                }

                return;

            }


            if (this.modelReading.isProdottoOrdinato(codiceProdotto)) {
                boolean vuoleSoftDelete = adminPanel.chiediSoftDelete(
                        "Questo prodotto e' gia' stato ordinato in passato, non puo' essere eliminato. "
                        + "Vuoi renderlo non disponibile invece?");
                if (vuoleSoftDelete) {
                    this.writingModel.rendiNonDisponibile(codiceProdotto);
                    adminPanel.mostraMessaggio("Prodotto reso non disponibile.");
                    userRequestedCatalogo();
                }
                return;
            }

            Map<String, String> menuCoinvolti = this.modelReading.trovaMenuCheContengono(codiceProdotto);
            if (!menuCoinvolti.isEmpty()) {
                boolean vuoleDisabilitareMenu = adminPanel.chiediSoftDelete(
                        "Questo prodotto e' componente di " + menuCoinvolti.size() + " menu attivi. "
                        + "Vuoi renderli non disponibili prima di procedere con l'eliminazione?");
                if (vuoleDisabilitareMenu) {
                    this.writingModel.rendiNonDisponibiliMenu(new java.util.ArrayList<>(menuCoinvolti.keySet()));
                }
            }

            this.writingModel.eliminaProdottoSingolo(codiceProdotto);
            adminPanel.mostraMessaggio("Prodotto eliminato.");
            userRequestedCatalogo();

        } catch (final DAOException e) {
            e.printStackTrace();
            adminPanel.mostraErrore("Errore durante l'eliminazione del prodotto.");
        }
    }


    public void requestProdottiDisponibili(){
        this.modelReading.loadProdotti();
    }

    public boolean createIngrediente(String nomeIngrediente, Map<String, JCheckBox> ingredientiPresentiCheckBox) {

        Map<String, Boolean> ingredientiPresenti = new java.util.HashMap<>();
        for (Map.Entry<String, JCheckBox> entry : ingredientiPresentiCheckBox.entrySet()) {

            if(ingredientiPresentiCheckBox.get(entry.getKey()) == null){
                ingredientiPresenti.put(entry.getKey(), false);
            } else {
                ingredientiPresenti.put(entry.getKey(), entry.getValue().isSelected());
            }
        }

        if(nomeIngrediente == null || nomeIngrediente.isEmpty()){
           adminPanel.mostraErrore("Il nome dell'ingrediente non puo' essere vuoto.");
            return false;
        }

        return this.writingModel.createIngrediente(nomeIngrediente, ingredientiPresenti);
    }

    public void requestIngredientiDisponibili(){

        List<String> prodottiDisponibili = new ArrayList<>();
        for (Prodotto prodotto : modelReading.loadProdotti()) {
            System.out.println("Prodotto disponibile: " + prodotto.getNomeProdotto());
            prodottiDisponibili.add(prodotto.getNomeProdotto());
        }
        this.createMenuPanel.caricaProdottiDisponibili(prodottiDisponibili);
    }

    public void showCreateProdottoPanel(int val){

        if(val == 0){
            System.out.println("Mostro il pannello di creazione prodotto singolo");
            this.createProdottoPanel.caricaCategoriePossibili(modelReading.loadCategorie());
            List<String> ingredientiDisponibili = new ArrayList<>();
            for (Ingrediente prodotto : modelReading.loadIngredientiDisponibili()) {
                ingredientiDisponibili.add(prodotto.getNomeIngrediente());
            }
            this.createProdottoPanel.setIngredientiDisponibili(ingredientiDisponibili);
            this.createProdottoPanel.caricaCategoriePossibili(modelReading.loadCategorie());
            this.createProdottoPanel.setVisible(true);
        } else if(val == 1){

            this.createMenuPanel.caricaCategoriePossibili(modelReading.loadCategorie());

            List<String> prodottiDisponibili = new ArrayList<>();
            for (Prodotto prodotto : modelReading.loadProdotti()) {
                prodottiDisponibili.add(prodotto.getNomeProdotto());
            }
            this.createMenuPanel.caricaProdottiDisponibili(prodottiDisponibili);
            this.createMenuPanel.caricaCategoriePossibili(modelReading.loadCategorie());
            this.createMenuPanel.setVisible(true);
        }
    }

    public void caricaCategoriePossibili() {
        this.createProdottoPanel.caricaCategoriePossibili(modelReading.loadCategorie());
    }

    public boolean createMenu(){

        Map<String, Integer> prodottiSelezionati = this.createMenuPanel.getProdottiSelezionati();
        int quantitaTotale = 0;
        for (Map.Entry<String, Integer> entry : prodottiSelezionati.entrySet()) {
            quantitaTotale += entry.getValue();
        }

        if(prodottiSelezionati.isEmpty() || prodottiSelezionati == null ){
            adminPanel.mostraErrore("Devi selezionare almeno un prodotto per creare un menu.");
            return false;
        }

        if (quantitaTotale > 4){
            adminPanel.mostraErrore("Non puoi selezionare piu' di 4 prodotti per creare un menu.");
            return false;
        }

        String codiceMenu = this.modelReading.getNextProdottoCode();

        if(this.writingModel.inserisciMenu(this.createMenuPanel.getDataProdotto(), "M", codiceMenu)){

            for (Map.Entry<String, Integer> entry : prodottiSelezionati.entrySet()) {
                String nomeProdotto = entry.getKey();
                int quantita = entry.getValue();
                this.writingModel.inserisciProdottoNelMenu(nomeProdotto, quantita, codiceMenu);

            }

            adminPanel.mostraMessaggio("Menu creato con successo.");
            return true;
        } else {
            adminPanel.mostraErrore("Errore durante la creazione del menu.");
            return false;
        }

    }

    public boolean createProdottoSingolo(){

        Map<String, String> dataProdotto = this.createProdottoPanel.getDataProdotto();
        Map<String, Integer> ingredientiSelezionati = this.createProdottoPanel.getIngredientiSelezionati();

        if(dataProdotto.get("nomeProdotto") == null || dataProdotto.get("nomeProdotto").isEmpty()){
            adminPanel.mostraErrore("Il nome del prodotto non puo' essere vuoto.");
            return false;
        }

        if(dataProdotto.get("prezzo") == null || dataProdotto.get("prezzo").isEmpty() || dataProdotto.get("prezzo").equals("0.0") || dataProdotto.get("descrizione") == null || dataProdotto.get("descrizione").isEmpty()){
            adminPanel.mostraErrore("Il prezzo e la descrizione del prodotto non possono essere vuoti o nulli.");
            return false;
        }

        String codiceProdotto = this.modelReading.getNextProdottoCode();

        if(this.writingModel.inserisciProdottoSingolo(codiceProdotto, null, dataProdotto)){

            for (Map.Entry<String, Integer> entry : ingredientiSelezionati.entrySet()) {
                String nomeIngrediente = entry.getKey();
                int quantita = entry.getValue();
                this.writingModel.inserisciIngredienteNelProdotto(codiceProdotto, nomeIngrediente, quantita);
            }

            adminPanel.mostraMessaggio("Prodotto singolo creato con successo.");
            return true;
        } else {
            adminPanel.mostraErrore("Errore durante la creazione del prodotto singolo.");
            return false;
        }
    }

    public void createCategoria(String nomeCategoria) {

        if(this.modelReading.checkCategoriaExists(nomeCategoria)){
            adminPanel.mostraErrore("La categoria con nome " + nomeCategoria + " esiste gia'.");
            return;
        }


        if(writingModel.createCategoria(nomeCategoria)){
            adminPanel.mostraMessaggio("Categoria creata con successo.");
            this.creaCategoriaPanel.setVisible(false);

        } else {
            adminPanel.mostraErrore("Errore durante la creazione della categoria.");

        }
    }

}
