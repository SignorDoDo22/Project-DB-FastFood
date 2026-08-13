package project.db.view.ProdottoCatalogo;

import project.db.view.Client.RigaCarrello;
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

    private final JComboBox<String> ingredientiPresentiComboBox = new JComboBox<>();
    private final JSpinner quantitaModificaSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JButton confermaModificaButton = new JButton("Conferma modifica quantità");

    private final JComboBox<String> ingredientiDisponibiliComboBox = new JComboBox<>();
    private final JSpinner quantitaAggiungiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JButton confermaAggiungiButton = new JButton("Aggiungi ingrediente");

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


        c.gridx = 0;
        c.gridy = 1;
        this.add(new JLabel("Ingrediente presente:"), c);

        c.gridy = 2;
        this.add(ingredientiPresentiComboBox, c);

        c.gridx = 1;
        this.add(new JLabel("Nuova quantità:"), c);

        c.gridx = 2;
        this.add(quantitaModificaSpinner, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        this.add(confermaModificaButton, c);
        c.gridwidth = 1;



        // Sezione: aggiungi ingrediente non presente
        c.gridx = 0;
        c.gridy = 4;
        this.add(new JLabel("Aggiungi ingrediente:"), c);

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

        confermaModificaButton.addActionListener(e -> {
            String ingredienteSelezionato = (String) ingredientiPresentiComboBox.getSelectedItem();

            if (ingredienteSelezionato == null) {
                showErrorMessage("Nessun ingrediente selezionato.");
                return;
            }

            if (rigaCarrello.requestModificaIngrediente(ingredienteSelezionato, (Integer) quantitaModificaSpinner.getValue())) {
                JOptionPane.showMessageDialog(this, "Quantità modificata con successo.");
            } else {
                showErrorMessage("Quantità non valida. Riprova.");
            }
        });

        confermaAggiungiButton.addActionListener(e -> {
            String ingredienteSelezionato = (String) ingredientiDisponibiliComboBox.getSelectedItem();

            if (ingredienteSelezionato == null) {
                showErrorMessage("Nessun ingrediente selezionato.");
                return;
            }

            if (rigaCarrello.requestAggiungiIngrediente(ingredienteSelezionato, (Integer) quantitaAggiungiSpinner.getValue())) {
                JOptionPane.showMessageDialog(this, "Ingrediente aggiunto con successo.");
                // Rimuovo l'ingrediente appena aggiunto dalla lista dei "disponibili da aggiungere",
            // dato che ora è già stato aggiunto a questo prodotto/componente.||||||||||||||||||||||||||||||||||
                ingredientiDisponibiliComboBox.removeItem(ingredienteSelezionato);
            } else {
                showErrorMessage("Impossibile aggiungere l'ingrediente. Riprova.");
            }
        });
    }


    public void impostaContesto(String titoloProdotto, Map<String, Integer> ingredientiPresenti,
                                 List<String> ingredientiDisponibiliTot) {
        titoloLabel.setText("Modifica ingredienti: " + titoloProdotto);
        this.ingredientiPresentiCorrenti = ingredientiPresenti;
        caricaIngredientiPresenti(ingredientiPresenti);
        caricaIngredientiDisponibili(ingredientiPresenti, ingredientiDisponibiliTot);
    }

    private void caricaIngredientiPresenti(Map<String, Integer> ingredientiPresenti) {
        ingredientiPresentiComboBox.removeAllItems();
        if (ingredientiPresenti != null) {
            for (String ingrediente : ingredientiPresenti.keySet()) {
                ingredientiPresentiComboBox.addItem(ingrediente);
            }
        }
    }

    private void caricaIngredientiDisponibili(Map<String, Integer> ingredientiPresenti, List<String> tuttiDisponibili) {
        ingredientiDisponibiliComboBox.removeAllItems();
        if (tuttiDisponibili == null) {
            return;
        }
        for (String nomeIngrediente : tuttiDisponibili) {
            boolean giaPresente = ingredientiPresenti != null && ingredientiPresenti.containsKey(nomeIngrediente);
            if (!giaPresente) {
                ingredientiDisponibiliComboBox.addItem(nomeIngrediente);
            }
        }
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}