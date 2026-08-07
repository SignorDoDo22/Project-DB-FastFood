package project.db.view.Admin;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import project.db.controller.ControllerAdmin;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JDialog;

public class CreateProdottoPanel extends JDialog {

    private Map<String, JTextField> dataProdotto = new LinkedHashMap<>();
    private JTextArea descrizioneProdottoField;
    protected ControllerAdmin controllerProdotto;
    private JComboBox<String> categoriePossibili = new JComboBox<>();

    public CreateProdottoPanel(ControllerAdmin controllerProdotto) {
        this.controllerProdotto = controllerProdotto;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(700,700));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nomeProdotto = new JLabel("Nome Prodotto:");
        this.add(nomeProdotto, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField nomeProdottoField = new JTextField(15);
        this.add(nomeProdottoField, gbc);
        dataProdotto.put("nomeProdotto", nomeProdottoField);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel descrizioneProdotto = new JLabel("Descrizione Prodotto:");
        this.add(descrizioneProdotto, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        this.descrizioneProdottoField = new JTextArea(5, 15);
        this.add(this.descrizioneProdottoField, gbc);


        gbc.gridx = 0; gbc.gridy = 2;
        JLabel prezzoOriginario = new JLabel("Prezzo Originario:");
        this.add(prezzoOriginario, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JTextField prezzoOriginarioField = new JTextField(15);
        this.add(prezzoOriginarioField, gbc);
        dataProdotto.put("prezzo", prezzoOriginarioField);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel categoriaProdotto = new JLabel("Categoria Prodotto:");
        this.add(categoriaProdotto, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        this.add(categoriePossibili, gbc);


    }

    public Map<String, String> getDataProdotto() {
        Map<String, String> data = new LinkedHashMap<>();
        for (Map.Entry<String, JTextField> entry : dataProdotto.entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText());
        }
        data.put("descrizione", descrizioneProdottoField.getText());
        System.out.println("Categoria selezionata: " + (String) categoriePossibili.getSelectedItem());
        data.put("idCategoria", (String) categoriePossibili.getSelectedItem());
        return data;
    }

    public JTextArea getDescrizioneProdottoField() {
        return descrizioneProdottoField;
    }

    public void caricaCategoriePossibili(List<String> categorie) {
        this.categoriePossibili.removeAllItems();
        for (String categoria : categorie) {
            this.categoriePossibili.addItem(categoria);
        }
    }

}
