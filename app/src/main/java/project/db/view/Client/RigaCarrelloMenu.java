package project.db.view.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.db.controller.ControllerClientPanel;
import project.db.data.Pair;
import project.db.view.ProdottoCatalogo.Selezionacomponentemenupanel;

public class RigaCarrelloMenu extends RigaCarrello {

    private Map<Integer, String> componentiMenuSelezionati = new HashMap<>();
    private Map<Pair<String, Integer>, Map<String, Pair<String, Integer>>> ingredientiModificati = new HashMap<>();
    private Pair<String, Integer> prodottoMenuSelezionato;

    private Selezionacomponentemenupanel selezionaComponenteMenuPanel;

    public RigaCarrelloMenu(boolean menu, float prezzo, int quantita, String nomeProdotto, String codiceProdotto,
            Carrello carrello, ControllerClientPanel controllerClientPanel, String codiceRiga) {
        super(menu, prezzo, quantita, nomeProdotto, codiceProdotto, carrello, controllerClientPanel, codiceRiga);

        selezionaComponenteMenuPanel = new Selezionacomponentemenupanel(this);
        this.controllerClientPanel.userRequestComponentiMenuCaricamento(codiceProdotto, this);
        this.buttonModificaRiga.addActionListener(e -> {
            this.controllerClientPanel.userRequestModificaRigaCarrello(this);
        });
    }

    public void requestIngredienti() {
        controllerClientPanel.userRequestIngredientiDisponibili(this);
    }

    public void mostraComponentiMenu(Map<Pair<String, Integer>, List<String>> componenti) {
        selezionaComponenteMenuPanel.caricaComponenti(componenti);
        selezionaComponenteMenuPanel.setVisible(true);
    }

    public void InserisciIngrediente(String nomeIngrediente, Integer quantita) {

        if (!ingredientiModificati.containsKey(prodottoMenuSelezionato)) {
            ingredientiModificati.put(prodottoMenuSelezionato, new HashMap<>());
        }
        ingredientiModificati.get(prodottoMenuSelezionato).put(nomeIngrediente, new Pair<>("Aggiungi", quantita));
    }

    public void EliminaIngrediente(String nomeIngrediente, Integer quantita) {

        if (!controllerClientPanel.verificaEliminazioneIngrediente(prodottoMenuSelezionato.getFirst(),
                nomeIngrediente, quantita)) {
            modificaIngredientiPanel
                    .showErrorMessage(
                            "Quantità richiesta per l'eliminazione dell'ingrediente non disponibile o ingrediente non presente.");
            return;
        }

        if (!ingredientiModificati.containsKey(prodottoMenuSelezionato)) {
            ingredientiModificati.put(prodottoMenuSelezionato, new HashMap<>());
        }
        ingredientiModificati.get(prodottoMenuSelezionato).put(nomeIngrediente, new Pair<>("Elimina", quantita));
    }

    public void setProdottoMenuSelezionato(Pair<String, Integer> prodottoMenuSelezionato) {
        this.prodottoMenuSelezionato = new Pair<>(prodottoMenuSelezionato.getFirst(),
                prodottoMenuSelezionato.getSecond() + 1);
    }

    public Map<Pair<String, Integer>, Map<String, Pair<String, Integer>>> getIngredientiModificati() {
        return ingredientiModificati;
    }

    public void caricaProdottiMenu(Map<Pair<String, Integer>, List<String>> componentiMenuSelezionati) {
        for (Pair<String, Integer> nomeComponente : componentiMenuSelezionati.keySet()) {
            this.componentiMenuSelezionati.put(nomeComponente.getSecond(), nomeComponente.getFirst());
        }
    }

    public Map<Integer, String> getComponentiMenu() {
        return componentiMenuSelezionati;
    }

}