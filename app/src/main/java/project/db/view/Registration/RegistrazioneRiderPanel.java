package project.db.view.Registration;

import project.db.controller.ControllerRegistrazione;
import project.db.controller.MainController;
import java.awt.event.ActionListener;

public class RegistrazioneRiderPanel extends RegistrazionePanel {

    public RegistrazioneRiderPanel(ControllerRegistrazione controller, MainController maincontroller) {
        super(controller, maincontroller);

        maincontroller.getMainView().addPanelToCardLayout(this, "registrationRider");

        this.registratiButton.addActionListener(e ->{
            controller.tryRegistrazioneRider();
        });

        this.indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                maincontroller.getMainView().requestChangePanel("loginRider", "registrationRider");
            }
        });
    }

    public void setPanelInMain(){
        maincontroller.getMainView().addPanelToCardLayout(this, "registrationRider");
    }

}
