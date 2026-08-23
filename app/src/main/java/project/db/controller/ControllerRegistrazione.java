package project.db.controller;

import java.util.HashMap;
import java.util.Map;

import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Registration.RegistrazioneClientPanel;
import project.db.view.Registration.RegistrazioneRiderPanel;

public class ControllerRegistrazione {

    private final MainController mainController;
    private final ReadingModel modelReading;
    private WritingModel modelWriting;
    private RegistrazioneClientPanel registrationPanel;
    private RegistrazioneRiderPanel registrationPanelRider;

    public ControllerRegistrazione(MainController mainController, ReadingModel model, WritingModel modelWriting) {
        this.mainController = mainController;
        this.modelReading = model;
        this.modelWriting = modelWriting;
        this.registrationPanel = new RegistrazioneClientPanel(this, mainController);
        this.registrationPanelRider = new RegistrazioneRiderPanel(this, mainController);
    }

    public void tryRegistration() {

        Map<String, String> data = new HashMap<>();
        for (var entry : registrationPanel.getUserData().entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText());
        }

        var username = data.get("username");
        var password = data.get("password");
        var email = data.get("email");
        var nome = data.get("nome");
        var cognome = data.get("cognome");
        var dataDiNascita = java.sql.Date.valueOf(data.get("dataDiNascita"));
        var telefono = data.get("telefono");

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || nome.isEmpty() || cognome.isEmpty()
                || telefono.isEmpty()) {
            registrationPanel.showErrorMessage("Compila tutti i campi");
        }

        if (modelReading.getCliente(email, password) != null) {
            registrationPanel.showErrorMessage("Utente già esistente");
        }

        if (modelWriting.userRequestRegistration(mainController.getConnection(), data)) {
            registrationPanel.showSuccessMessage("Registrazione completata con successo");
            mainController.getMainView().requestChangePanel("login", "registration");
        }
    }

    public void tryRegistrazioneRider() {

        Map<String, String> data = new HashMap<>();
        for (var entry : registrationPanelRider.getUserData().entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText());
        }

        var username = data.get("username");
        var password = data.get("password");
        var email = data.get("email");
        var nome = data.get("nome");
        var cognome = data.get("cognome");
        var dataDiNascita = java.sql.Date.valueOf(data.get("dataDiNascita"));
        var telefono = data.get("telefono");

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || nome.isEmpty() || cognome.isEmpty()
                || telefono.isEmpty() || dataDiNascita == null) {
            registrationPanelRider.showErrorMessage("Compila tutti i campi");
            return;
        }

        if (modelReading.checkEmailRiderExists(email)) {
            registrationPanelRider.showErrorMessage("Utente già esistente");
            return;
        }

        if (modelReading.getRider(email, password) != null) {
            registrationPanelRider.showErrorMessage("Utente già esistente");
        }

        if (modelWriting.inserisciRider(data)) {
            registrationPanelRider.showSuccessMessage("Registrazione completata con successo");
            mainController.getMainView().requestChangePanel("login", "registrationRider");
        }
    }

}
