package project.db.view.Client;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.db.controller.ControllerClientPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;

public class Carrello extends JPanel {

    private JScrollPane scrollPanel;
    private JPanel panelscorrevole;
    private ClientPanel clientPanel;
    private JLabel carrelloString;
    private JLabel totaleString;
    private JButton buttonProcedi;
    private ControllerClientPanel controllerClientPanel;
    private List<RigaCarrello> righeCarrello = new ArrayList<>();

    public Carrello(final ClientPanel clientPanel, final ControllerClientPanel controllerClientPanel) {
        this.controllerClientPanel = controllerClientPanel;
        this.clientPanel = clientPanel;
        this.setLayout(new BorderLayout());
        this.carrelloString = new JLabel("CART");
        this.totaleString = new JLabel("TOTALE: ");
        this.add(carrelloString, BorderLayout.NORTH);

        this.clientPanel = clientPanel;
        this.panelscorrevole = new JPanel();
        this.panelscorrevole.setLayout(new BoxLayout(panelscorrevole, BoxLayout.Y_AXIS));
        this.scrollPanel = new JScrollPane(panelscorrevole);
        this.add(scrollPanel, BorderLayout.CENTER);
        this.add(totaleString, BorderLayout.SOUTH);

    }

    public void addRigaCarrello(String codiceProdotto, int quantita, String nomeProdotto, float prezzoUnitario, boolean menu){

        RigaCarrello rigaOrdine = new RigaCarrello(menu, prezzoUnitario, quantita, nomeProdotto, codiceProdotto, this);
        this.panelscorrevole.add(rigaOrdine);
        righeCarrello.add(rigaOrdine);
        this.revalidate();
        this.repaint();
    }

    public void requestIngredientiPresenti(final RigaCarrello rigaCarrello){
        controllerClientPanel.userRequestIngredientiFromRigaCarrello(rigaCarrello.getCodiceProdotto(), rigaCarrello);
    }

    public List<RigaCarrello> getRigheCarrello() {
        return righeCarrello;
    }

}
