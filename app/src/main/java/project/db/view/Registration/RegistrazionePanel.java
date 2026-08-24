package project.db.view.Registration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.text.DateFormatter;
import project.db.controller.ControllerRegistrazione;
import project.db.controller.MainController;
import javax.swing.JPasswordField;
import java.util.Map;

public class RegistrazionePanel extends JPanel {

    protected ControllerRegistrazione controller;
    protected MainController maincontroller;
    protected JButton registratiButton;
    protected JButton indietroButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JFormattedTextField dataDiNascitaField;
    private JTextField telefonoField;
    private Map<String, JTextField> userData;

    public RegistrazionePanel(ControllerRegistrazione controller, MainController maincontroller) {
        this.controller = controller;
        this.maincontroller = maincontroller;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.usernameField = new JTextField(15);
        this.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        this.passwordField = new JPasswordField(15);
        this.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.emailField = new JTextField(15);
        this.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.nomeField = new JTextField(15);
        this.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.cognomeField = new JTextField(15);
        this.add(cognomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(new JLabel("Data di Nascita (AAAA-MM-GG):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        var dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        this.dataDiNascitaField = new JFormattedTextField(new DateFormatter(dateFormat));
        this.dataDiNascitaField.setColumns(15);
        this.add(dataDiNascitaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        this.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        this.telefonoField = new JTextField(15);
        this.add(telefonoField, gbc);

        this.registratiButton = new JButton("Registrati");
        this.indietroButton = new JButton("Indietro");
        gbc.gridx = 0;
        gbc.gridy = 8;
        this.add(registratiButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 9;
        this.add(indietroButton, gbc);

        this.userData = Map.of(
                "username", usernameField,
                "password", passwordField,
                "email", emailField,
                "nome", nomeField,
                "cognome", cognomeField,
                "dataDiNascita", dataDiNascitaField,
                "telefono", telefonoField);

    }

    public Map<String, JTextField> getUserData() {
        return userData;
    }

    public java.util.Date getDataDiNascita() {
        Object value = dataDiNascitaField.getValue();
        return (value instanceof java.util.Date) ? (java.util.Date) value : null;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore nella creazione", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registrazione completata", JOptionPane.INFORMATION_MESSAGE);
    }

}