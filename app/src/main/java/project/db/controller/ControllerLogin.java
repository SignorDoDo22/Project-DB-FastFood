package project.db.controller;

import project.db.data.Cliente;
import project.db.data.Rider;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.MainView;
import project.db.view.Login.LoginPanelClient;
import project.db.view.Login.LoginPanelRider;

public class ControllerLogin {

    private final MainController mainController;
    private Cliente utente;
    private Rider rider;
    private LoginPanelClient loginPanelClient;
    private LoginPanelRider loginPanelRider;
    private ReadingModel modelReading;
    private WritingModel writingModel;
    private MainView mainView;

    public ControllerLogin(final MainController mainController, ReadingModel modelReading, WritingModel writingModel) {
        this.mainController = mainController;
        this.loginPanelClient = new LoginPanelClient(this);
        this.loginPanelRider = new LoginPanelRider(this);
        this.modelReading = modelReading;
        this.writingModel = writingModel;
        mainController.getMainView().addPanelToCardLayout(loginPanelClient, "loginClient");
        mainController.getMainView().addPanelToCardLayout(loginPanelRider, "loginRider");
        this.mainView = mainController.getMainView();
    }

    public boolean tryLoginClient() {
        final var modelReading = this.mainController.getModelReading();
        var data = loginPanelClient.getUserData();
        var email = data.get("email").getText();
        var password = data.get("password").getText();
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        if(modelReading.findClient(email, password)){
            this.utente = modelReading.getCliente(email, password);
            this.mainController.setClientLoggedIn(utente);
            this.mainView.requestChangePanel("client", "login");
        } else {
            System.out.println("Utente non trovato");
            this.utente = null;
        }
        return this.utente != null;
    }

    public boolean tryLoginRider() {
        final var modelReading = this.mainController.getModelReading();
        var data = loginPanelRider.getUserData();
        var email = data.get("email").getText();
        var password = data.get("password").getText();
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        if(modelReading.findRider(email, password)){
            this.rider = modelReading.getRider(email, password);
            this.mainController.setRiderLoggedIn(rider);
            this.mainView.requestChangePanel("rider", "login");
        } else {
            this.rider = null;
        }
        return this.rider != null;
    }

    public void changePanel(final String panelName, final String currentPanel) {
        this.mainController.changePanel(panelName, currentPanel);
    }


    public void logOut(){
        this.utente = null;
    }

    public void setUtente(final Cliente utente) {
        this.utente = utente;
    }

    public Cliente getUtente() {
        return utente;
    }

}
