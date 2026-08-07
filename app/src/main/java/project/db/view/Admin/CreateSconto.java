package project.db.view.Admin;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import project.db.controller.ControllerAdmin;

import java.util.Map;
import java.util.LinkedHashMap;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CreateSconto extends JDialog {

    private Map<String, JTextField> dataSconto = new LinkedHashMap<>();

    public CreateSconto(final ControllerAdmin controller) {

        this.setSize(700,700);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nomeSconto = new JLabel("Nome Sconto:");
        this.add(nomeSconto, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField nomeScontoField = new JTextField(15);
        this.add(nomeScontoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel percentualeSconto = new JLabel("Percentuale Sconto:");
        this.add(percentualeSconto, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField percentualeScontoField = new JTextField(15);
        this.add(percentualeScontoField, gbc);

        JLabel dataInizioSconto = new JLabel("Data Inizio Sconto (YYYY-MM-DD):");
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(dataInizioSconto, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField dataInizioScontoField = new JTextField(15);
        this.add(dataInizioScontoField, gbc);

        JLabel dataFineSconto = new JLabel("Data Fine Sconto (YYYY-MM-DD):");
        gbc.gridx = 0; gbc.gridy = 3;
        this.add(dataFineSconto, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField dataFineScontoField = new JTextField(15);
        this.add(dataFineScontoField, gbc);

        JLabel quotaOrdini = new JLabel("Quota Ordini:");
        gbc.gridx = 0; gbc.gridy = 4;
        this.add(quotaOrdini, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JTextField quotaOrdiniField = new JTextField(15);
        this.add(quotaOrdiniField, gbc);

    }
}
