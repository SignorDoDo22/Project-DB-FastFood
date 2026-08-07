package project.db.view.Login;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import project.db.controller.ControllerLogin;

public class LoginPanel extends JPanel {

    private JTextField name;
    private JPasswordField password;
    private JTextField email;
    private JPanel panelInterno;
    private JButton indietroButton;
    protected JButton loginButton;
    protected JButton registratiButton;
    private Map<String, JTextField> userData;
    protected ControllerLogin controller;

    public LoginPanel(final ControllerLogin controller) {
        this.controller = controller;
        userData = new HashMap<>();
        this.setLayout(new BorderLayout());
        this.panelInterno = new JPanel();
        this.panelInterno.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        this.name = new JTextField(15);       // 15 colonne = dimensione ragionevole
        this.password = new JPasswordField(15);
        this.email = new JTextField(15);
        this.indietroButton = new JButton("Indietro");
        this.loginButton = new JButton("Login");
        this.registratiButton = new JButton("Registrati");

        gbc.gridx = 0; gbc.gridy = 1;
        panelInterno.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panelInterno.add(email, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelInterno.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panelInterno.add(password, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelInterno.add(registratiButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panelInterno.add(indietroButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelInterno.add(loginButton, gbc);

        // Aggiungendo panelInterno al CENTER, GridBagLayout dentro di lui
        // centra automaticamente il "blocco" di componenti nello spazio disponibile,
        // senza stirare i singoli campi
        this.add(panelInterno, BorderLayout.CENTER);

        this.indietroButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.changePanel("scelta","login");
            }
        });
    }

    public Map<String, JTextField> getUserData() {
        userData.put("email", email);
        userData.put("password", password);
        return userData;
    }
}