package project.db.view.Admin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import project.db.controller.ControllerAdmin;

public class CreaCategoria extends JDialog {

    private JFormattedTextField nomeCategoriaField;
    private JLabel nomeCategoriaLabel;
    private JButton creaCategoriaButton;
    private ControllerAdmin controller;

    public CreaCategoria(final ControllerAdmin controller) {
        this.controller = controller;
        this.setSize(700, 700);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.nomeCategoriaLabel = new JLabel("Nome Categoria:");
        this.add(nomeCategoriaLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.nomeCategoriaField = new JFormattedTextField();
        this.nomeCategoriaField.setColumns(20);
        this.add(nomeCategoriaField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.creaCategoriaButton = new JButton("Crea Categoria");
        this.add(creaCategoriaButton, gbc);
        this.creaCategoriaButton.addActionListener(e -> this.controller.createCategoria(nomeCategoriaField.getText()));

    }

    public void showErrorMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "Successo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }
}
