package project.db.view.Client;

import java.util.HashMap;
import java.util.Map;

import project.db.controller.ControllerClientPanel;
import project.db.data.Pair;

public class RigaCarrelloSingolo extends RigaCarrello {

    private Map<String, Pair<String, Integer>> ingredientiModificatiProdSingolo = new HashMap<>();

    public RigaCarrelloSingolo(boolean menu, float prezzo, int quantita, String nomeProdotto, String codiceProdotto,
            Carrello carrello, ControllerClientPanel controllerClientPanel, String codiceRiga) {
        super(menu, prezzo, quantita, nomeProdotto, codiceProdotto, carrello, controllerClientPanel, codiceRiga);

        this.buttonModificaRiga.addActionListener(e -> {
            this.controllerClientPanel.userRequestModificaRigaCarrello(this);
        });
    }

    public void InserisciIngredienteProdSingolo(String nomeIngrediente, Integer quantita) {

        if (ingredientiModificatiProdSingolo.size() >= 4) {
            modificaIngredientiPanel.showErrorMessage("Non puoi aggiungere più di 4 ingredienti al prodotto singolo.");
            return;
        }

        if (!ingredientiModificatiProdSingolo.containsKey(nomeIngrediente)) {
            ingredientiModificatiProdSingolo.put(nomeIngrediente, new Pair<>("Aggiungi", quantita));
        } else {
            ingredientiModificatiProdSingolo.put(nomeIngrediente, new Pair<>("Aggiungi", quantita));
        }
    }

    public void EliminaIngredienteProdSingolo(String nomeIngrediente, Integer quantita) {

        if (!ingredientiModificatiProdSingolo.containsKey(nomeIngrediente)) {
            ingredientiModificatiProdSingolo.put(nomeIngrediente, new Pair<>("Elimina", quantita));
        } else {
            ingredientiModificatiProdSingolo.put(nomeIngrediente, new Pair<>("Elimina", quantita));
        }
    }

    public Map<String, Pair<String, Integer>> getIngredientiModificatiProdSingolo() {
        return ingredientiModificatiProdSingolo;
    }

}