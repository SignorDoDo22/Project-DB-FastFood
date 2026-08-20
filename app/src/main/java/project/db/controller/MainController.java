package project.db.controller;

import java.sql.Connection;

import project.db.data.Cliente;
import project.db.data.Rider;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.MainView;

public class MainController {

    private MainView mainView;
    private ReadingModel modelReading;
    private WritingModel writingModel;
    private ControllerAdmin controllerAdmin;
    private Connection connection;
    private ControllerClientPanel controllerClient;
    private ControllerRider controllerRider;
    private final ControllerLogin controllerLogin;
    private final ControllerRegistrazione controllerRegistrazione;
    private ControllerInformazioniAggregate controllerInformazioniAggregate;

    public MainController(final ReadingModel modelReading, final WritingModel writingModel,
            final Connection connection) {

        this.modelReading = modelReading;
        this.writingModel = writingModel;
        this.connection = connection;
        this.mainView = new MainView(this);

        this.controllerLogin = new ControllerLogin(this, modelReading, writingModel);
        this.controllerClient = new ControllerClientPanel(modelReading, writingModel, mainView.getClientPanel(), this);
        this.controllerRider = new ControllerRider(this, modelReading, writingModel, mainView.getRiderPanel());
        this.controllerRegistrazione = new ControllerRegistrazione(this, modelReading, writingModel);
        this.controllerAdmin = new ControllerAdmin(modelReading, writingModel, mainView.getAdminPanel());

        this.mainView.getRiderPanel().setControllerRider(this.controllerRider);
        this.controllerInformazioniAggregate = new ControllerInformazioniAggregate(modelReading);
        this.mainView.setVisible(true);
    }

    public void changePanel(final String panelName, final String currentPanel) {

        if ("scelta".equals(panelName)) {
            mainView.changePanel(panelName);
        }

        if (panelName.equals("loginRider") || panelName.equals("loginClient")) {
            mainView.changePanel(panelName);
        }

        if (currentPanel.equals("scelta") && panelName.equals("loginRider")) {
            mainView.changePanel(panelName);
        }

        if (currentPanel.equals("scelta") && panelName.equals("loginClient")) {
            mainView.changePanel(panelName);
        }

        if ("client".equals(panelName)) {
            mainView.changePanel(panelName);
            this.controllerClient.userRequestOrdiniRecensibili();
            this.controllerClient.userRequestedCatalogo();
        }

        if ("rider".equals(panelName)) {
            mainView.changePanel(panelName);
            this.controllerRider.showOrders();
        }

        if ("registrationRider".equals(panelName) && currentPanel.equals("loginRider")) {
            mainView.changePanel(panelName);
        }

        if ("registrationClient".equals(panelName) && currentPanel.equals("loginClient")) {
            mainView.changePanel(panelName);
        }

        if ("admin".equals(panelName)) {
            mainView.changePanel(panelName);
        }
    }

    public void changePanelInformazioniAggregate(final String panelName) {
        if ("MiglioriRider".equals(panelName)) {
            controllerInformazioniAggregate.showMiglioriRider();
        }

        if ("RecensioniNegative".equals(panelName)) {
            controllerInformazioniAggregate.showRecensioniNegative();
        }

        if ("ClassificaProdottiPiuVenduti".equals(panelName)) {
            controllerInformazioniAggregate.showClassificaProdottiPiuVenduti();
        }
    }

    public void setClientLoggedIn(final Cliente cliente) {
        this.controllerClient.setUtenteLoggato(cliente);
    }

    public void setRiderLoggedIn(final Rider rider) {
        this.controllerRider.setRiderLoggato(rider);
    }

    public ControllerClientPanel getControllerClient() {
        return this.controllerClient;
    }

    public ControllerLogin getControllerLogin() {
        return this.controllerLogin;
    }

    public MainView getMainView() {
        return this.mainView;
    }

    public ReadingModel getModelReading() {
        return this.modelReading;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public ControllerRider getControllerRider() {
        return this.controllerRider;
    }

    public ControllerRegistrazione getControllerRegistrazione() {
        return this.controllerRegistrazione;
    }

}
