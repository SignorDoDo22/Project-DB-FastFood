package project.db.view.ProdottoCatalogo;

import project.db.view.Client.RigaCarrello;
import project.db.view.Client.RigaCarrelloMenu;
import project.db.view.Client.RigaCarrelloSingolo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ModificaIngredientiPanel extends JDialog {

    private final JLabel titoloLabel = new JLabel();

    private final JComboBox<String> ingredientiDisponibiliComboBox = new JComboBox<>();
    private final JSpinner quantitaAggiungiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JButton confermaAggiungiButton = new JButton("Aggiungi ingrediente");

    private final JButton eliminaIngredienteButton = new JButton("Elimina ingrediente");

    private final RigaCarrello rigaCarrello;
    private Map<String, Integer> ingredientiPresentiCorrenti;

    public ModificaIngredientiPanel(RigaCarrello rigaCarrello) {
        this.rigaCarrello = rigaCarrello;

        this.setSize(650, 300);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 6, 6);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        this.add(titoloLabel, c);
        c.gridwidth = 1;

        // Sezione: aggiungi ingrediente non presente
        c.gridx = 0;
        c.gridy = 4;
        this.add(new JLabel("Aggiungi/Elimina ingrediente:"), c);

        c.gridy = 5;
        this.add(ingredientiDisponibiliComboBox, c);

        c.gridx = 1;
        this.add(new JLabel("Quantità:"), c);

        c.gridx = 2;
        this.add(quantitaAggiungiSpinner, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        this.add(confermaAggiungiButton, c);

        c.gridy = 7;
        this.add(eliminaIngredienteButton, c);

        this.confermaAggiungiButton.addActionListener(e -> {
            if (rigaCarrello instanceof RigaCarrelloMenu) {
                RigaCarrelloMenu rigaMenu = (RigaCarrelloMenu) rigaCarrello;
                rigaMenu.InserisciIngrediente((String) ingredientiDisponibiliComboBox.getSelectedItem(),
                        (Integer) quantitaAggiungiSpinner.getValue());
            } else {
                RigaCarrelloSingolo rigaSingolo = (RigaCarrelloSingolo) rigaCarrello;
                rigaSingolo.InserisciIngredienteProdSingolo((String) ingredientiDisponibiliComboBox.getSelectedItem(),
                        (Integer) quantitaAggiungiSpinner.getValue());
            }
        });

        this.eliminaIngredienteButton.addActionListener(e -> {
            if (rigaCarrello instanceof RigaCarrelloMenu) {
                RigaCarrelloMenu rigaMenu = (RigaCarrelloMenu) rigaCarrello;
                rigaMenu.EliminaIngrediente((String) ingredientiDisponibiliComboBox.getSelectedItem(),
                        (Integer) quantitaAggiungiSpinner.getValue());
            } else {
                RigaCarrelloSingolo rigaSingolo = (RigaCarrelloSingolo) rigaCarrello;
                rigaSingolo.EliminaIngredienteProdSingolo((String) ingredientiDisponibiliComboBox.getSelectedItem(),
                        (Integer) quantitaAggiungiSpinner.getValue());
            }
        });

    }

    // CARICA TUTTI GLI INGREDIENTI DISPONIBILI
    public void caricaIngredientiDisponibili(List<String> ingredientiDisponibili) {
        ingredientiDisponibiliComboBox.removeAllItems();
        if (ingredientiDisponibili != null) {
            for (String ingrediente : ingredientiDisponibili) {
                ingredientiDisponibiliComboBox.addItem(ingrediente);
            }
        }
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}