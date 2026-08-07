package project.db.view.ProdottoCatalogo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class ModificaIngredientiPanel extends JDialog {

    private JComboBox<String> ingredientiPresentiComboBox = new JComboBox<>();
    private JSpinner quantitaTextField = new JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
    private JLabel quantitaLabel = new JLabel("Quantità:");
    private JButton confermaButton = new JButton("Conferma");


    public ModificaIngredientiPanel(JDialog parent, String codiceProdotto, java.util.Map<String, Integer> ingredientiPresenti) {

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        this.add(ingredientiPresentiComboBox, c);

        c.gridx = 1;
        c.gridy = 0;
        this.add(quantitaLabel, c);

        c.gridx = 2;
        c.gridy = 0;
        this.add(quantitaTextField, c);

        c.gridx = 1;
        c.gridy = 1;
        this.add(confermaButton, c);

        this.confermaButton.addActionListener(e -> {


        });




    }


}