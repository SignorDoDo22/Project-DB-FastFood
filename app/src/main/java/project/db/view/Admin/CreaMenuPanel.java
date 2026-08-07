package project.db.view.Admin;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import project.db.controller.ControllerAdmin;

public class CreaMenuPanel extends CreateProdottoPanel {

    private JComboBox<String> prodottiDisponibili = new JComboBox<>();
    private JSpinner quantitaSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
    private JButton aggiungiProdotto = new JButton("Aggiungi Prodotto");
    private JButton confermaCreazione = new JButton("Conferma Creazione");
    private JPanel prodottiAggiuntiPanel = new JPanel();
    private Map<String, Integer> prodottiSelezionati = new LinkedHashMap<>();

    public CreaMenuPanel(ControllerAdmin controllerProdotto) {

        super(controllerProdotto);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        // Riga 4: selezione prodotto + quantità + aggiungi, tutti vicini
        gbc.gridx = 0; gbc.gridy = 4;
        this.add(prodottiDisponibili, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        this.add(quantitaSpinner, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        this.add(aggiungiProdotto, gbc);

        this.aggiungiProdotto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String prodotto = (String) prodottiDisponibili.getSelectedItem();
                int quantita = (int) quantitaSpinner.getValue();
                if (prodotto == null) return;

                prodottiSelezionati.put(prodotto, quantita);
                aggiornaListaVisibile();
            }

        });

        this.confermaCreazione.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controllerProdotto.createMenu();
            }

        });

        // Riga 5: lista prodotti aggiunti, sotto ai controlli, larga quanto tutta la riga
        prodottiAggiuntiPanel.setLayout(new GridLayout(4, 2));
        JScrollPane scrollPane = new JScrollPane(prodottiAggiuntiPanel);
        scrollPane.setPreferredSize(new Dimension(300, 120));

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, gbc);

        // Riga 6: conferma finale, separata e centrata sotto la lista
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(confermaCreazione, gbc);
    }

    public void addProdotto(String name, int quantita) {
        prodottiSelezionati.put(name, quantita);
    }

    private void aggiornaListaVisibile() {
        prodottiAggiuntiPanel.removeAll();
        for (Map.Entry<String, Integer> entry : prodottiSelezionati.entrySet()) {
            String prodotto = entry.getKey();
            int quantita = entry.getValue();
            prodottiAggiuntiPanel.add(new JLabel(prodotto + " x" + quantita));
        }
        prodottiAggiuntiPanel.revalidate();
        prodottiAggiuntiPanel.repaint();
    }

    public void caricaProdottiDisponibili(List<String> prodotti) {
        for (String prodotto : prodotti) {
            prodottiDisponibili.addItem(prodotto);
        }
    }

    public void caricaJComboBoxProdottiDisponibili() {
        this.controllerProdotto.requestIngredientiDisponibili();
    }

    public Map<String, Integer> getProdottiSelezionati() {
        return prodottiSelezionati;
    }

}