package project.db.view.Admin;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import project.db.controller.ControllerAdmin;


public class CreaProdottoSingPanel extends CreateProdottoPanel {

    private JComboBox<String> ingredientiDisponibili = new JComboBox<>();
    private JSpinner quantitaSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private JButton aggiungiIngredienteButton = new JButton("Aggiungi Ingrediente");
    private JPanel ingredientiAggiuntiPanel = new JPanel();
    private JButton confermaCreazione = new JButton("Conferma Creazione");

    private Map<String, Integer> ingredientiSelezionati = new LinkedHashMap<>();

    public CreaProdottoSingPanel(ControllerAdmin controllerProdotto) {

        super(controllerProdotto);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 4;
        this.add(ingredientiDisponibili, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        this.add(quantitaSpinner, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        this.add(aggiungiIngredienteButton, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        this.add(confermaCreazione, gbc);

        ingredientiAggiuntiPanel.setLayout(new BoxLayout(ingredientiAggiuntiPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ingredientiAggiuntiPanel);
        scrollPane.setPreferredSize(new Dimension(300, 120));

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, gbc);

        aggiungiIngredienteButton.addActionListener(e -> {
            String ingrediente = (String) ingredientiDisponibili.getSelectedItem();
            int quantita = (int) quantitaSpinner.getValue();
            if (ingrediente == null) return;

            ingredientiSelezionati.put(ingrediente, quantita);
            aggiornaListaVisibile();
        });

        confermaCreazione.addActionListener(e -> {
            controllerProdotto.createProdottoSingolo();
        });

    }

    private void aggiornaListaVisibile() {
        ingredientiAggiuntiPanel.removeAll();
        for (Map.Entry<String, Integer> entry : ingredientiSelezionati.entrySet()) {
            ingredientiAggiuntiPanel.add(new JLabel(entry.getKey() + "  x" + entry.getValue()));
        }
        ingredientiAggiuntiPanel.revalidate();
        ingredientiAggiuntiPanel.repaint();
    }

    public void setIngredientiDisponibili(List<String> ingredienti) {
        ingredientiDisponibili.removeAllItems();
        for (String ingrediente : ingredienti) {
            ingredientiDisponibili.addItem(ingrediente);
        }
    }

    public Map<String, Integer> getIngredientiSelezionati() {
        return ingredientiSelezionati;
    }

}