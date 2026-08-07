package project.db.view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import project.db.controller.ControllerLogin;

public class LoginPanelClient extends LoginPanel {

    public LoginPanelClient( ControllerLogin controller) {
        super(controller);

        this.loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.tryLoginClient()){
                    javax.swing.JOptionPane.showMessageDialog(null, "Errore nel login come Cliente!");
                }
            }

        });

        this.registratiButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.changePanel("registrationClient","loginClient");
            }

        });
    }

}
