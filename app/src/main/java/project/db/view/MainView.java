package project.db.view;

import java.awt.*;
import javax.swing.*;
import project.db.controller.MainController;
import project.db.view.Admin.AdminPanel;
import project.db.view.Client.ClientPanel;
import project.db.view.Registration.RegistrazionePanel;
import project.db.view.Rider.RiderPanel;

public class MainView extends JFrame {

    private SceltaLogin sceltaPanel;
    private RiderPanel riderPanel;
    private CardLayout cardLayout;
    private ClientPanel client;
    private AdminPanel adminPanel;
    private RegistrazionePanel registrationPanel;
    private MainController mainController;
    private String currentPanel;

    public MainView(final MainController mainController){

        this.setTitle("Fast Food");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainController = mainController;

        this.sceltaPanel = new SceltaLogin(this);
        this.client = new ClientPanel(this);
        this.cardLayout = new CardLayout();
        this.riderPanel = new RiderPanel( this);
        this.adminPanel = new AdminPanel(this);
        this.setLayout(cardLayout);
        this.add(sceltaPanel, "scelta");
        this.add(client, "client");
        this.add(riderPanel, "rider");
        this.add(adminPanel, "admin");
    }

    public void requestChangePanel(final String panelName, final String currentPanel){
       this.mainController.changePanel(panelName, currentPanel);
    }

     public void requestInformazioniAggregate(final String infoType) {
        System.out.println("Requesting Informazioni Aggregate for type: " + infoType);
        this.mainController.changePanelInformazioniAggregate(infoType);
    }

    public void changePanel(final String panelName){
        this.cardLayout.show(this.getContentPane(), panelName);
        this.currentPanel = panelName;
    }

    public ClientPanel getClientPanel(){
        return this.client;
    }

    public RegistrazionePanel getRegistrazionePanel() {
        return this.registrationPanel;
    }


    public RiderPanel getRiderPanel() {
        return this.riderPanel;
    }

    public AdminPanel getAdminPanel() {
        return this.adminPanel;
    }

    public String getCurrentPanel() {
        return this.currentPanel;
    }

    public void addPanelToCardLayout(final JPanel panel, final String name){
        this.add(panel, name);
    }

}
