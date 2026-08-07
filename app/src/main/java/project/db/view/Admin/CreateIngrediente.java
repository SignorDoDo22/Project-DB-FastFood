package project.db.view.Admin;

import javax.swing.JDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import project.db.controller.ControllerAdmin;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class CreateIngrediente extends JDialog {

    private Map<JLabel, JTextField> dataIngrediente = new HashMap<>();
    private Map<String, JCheckBox> ingredientiPresentiCheckBox = new HashMap<>();
    private ControllerAdmin controller;


    public CreateIngrediente(ControllerAdmin controller) {
        this.controller = controller;
        this.setSize(700,700);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nomeIngrediente = new JLabel("Nome Ingrediente:");
        this.add(nomeIngrediente, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField nomeIngredienteField = new JTextField(15);
        this.add(nomeIngredienteField, gbc);
        this.dataIngrediente.put(nomeIngrediente, nomeIngredienteField);
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel labelVegano = new JLabel("Vegano:");
        gbc.gridx = 0; gbc.gridy = 3;
        this.add(labelVegano, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JCheckBox veganoCheckBox = new JCheckBox();
        this.ingredientiPresentiCheckBox.put("Vegano", veganoCheckBox);
        this.add(veganoCheckBox, gbc);
        JLabel labelGlutenFree = new JLabel("Gluten Free:");
        gbc.gridx = 0; gbc.gridy = 4;
        this.add(labelGlutenFree, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JCheckBox glutenFreeCheckBox = new JCheckBox();
        this.ingredientiPresentiCheckBox.put("Gluten Free", glutenFreeCheckBox);
        this.add(glutenFreeCheckBox, gbc);

        JLabel lattosioLabel = new JLabel("Lattosio:");
        gbc.gridx = 0; gbc.gridy = 5;
        this.add(lattosioLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JCheckBox lattosioCheckBox = new JCheckBox();
        this.ingredientiPresentiCheckBox.put("Lattosio", lattosioCheckBox);
        this.add(lattosioCheckBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        JButton creaIngredienteButton = new JButton("Crea Ingrediente");
        this.add(creaIngredienteButton, gbc);
        creaIngredienteButton.addActionListener(e -> {

            if( controller.createIngrediente(dataIngrediente.get(nomeIngrediente).getText(), ingredientiPresentiCheckBox)){
                JOptionPane.showMessageDialog(this, "Ingrediente creato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, "Errore nella creazione dell'ingrediente. Potrebbe già esistere.", "Errore", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    Map<JLabel, JTextField> getDataIngrediente() {
        return dataIngrediente;
    }

    Map<String, JCheckBox> getIngredientiPresentiCheckBox() {
        return ingredientiPresentiCheckBox;
    }

}
