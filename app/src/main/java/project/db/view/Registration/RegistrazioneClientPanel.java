package project.db.view.Registration;

import project.db.controller.ControllerRegistrazione;
import project.db.controller.MainController;
import java.awt.event.ActionListener;

public class RegistrazioneClientPanel extends RegistrazionePanel {

    public RegistrazioneClientPanel(ControllerRegistrazione controller, MainController maincontroller) {
        super(controller, maincontroller);
        maincontroller.getMainView().addPanelToCardLayout(this, "registrationClient");

        this.registratiButton.addActionListener(e ->{
            controller.tryRegistration();
        });

        this.indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                maincontroller.getMainView().requestChangePanel("scelta", "registrationClient");
            }
        });
    }

    public void setPanelInMain(){
        maincontroller.getMainView().addPanelToCardLayout(this, "registrationClient");
    }
}
