package project.db.view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import project.db.controller.ControllerLogin;

public class LoginPanelRider extends LoginPanel {

    public LoginPanelRider( ControllerLogin controller) {
        super(controller);

        this.loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.tryLoginRider()){
                    JOptionPane.showMessageDialog(null, "Errore nel login come Rider!");
                }
            }

        });

        this.registratiButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.changePanel("registrationRider","loginRider");
            }

        });
    }

}
