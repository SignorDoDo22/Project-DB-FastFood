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
    private JButton buttonProcedi;
    private ControllerClientPanel controllerClientPanel;
    private List<RigaCarrello> righeCarrello = new ArrayList<>();

    public Carrello(final ClientPanel clientPanel, final ControllerClientPanel controllerClientPanel) {
        this.controllerClientPanel = controllerClientPanel;
        this.clientPanel = clientPanel;
        this.setLayout(new BorderLayout());
        this.carrelloString = new JLabel("CART");
        this.add(carrelloString, BorderLayout.NORTH);

        this.clientPanel = clientPanel;
        this.panelscorrevole = new JPanel();
        this.panelscorrevole.setLayout(new BoxLayout(panelscorrevole, BoxLayout.Y_AXIS));
        this.scrollPanel = new JScrollPane(panelscorrevole);
        this.add(scrollPanel, BorderLayout.CENTER);

    }

    public void addRigaCarrello(String codiceProdotto, int quantita, String nomeProdotto, float prezzoUnitario,
            boolean menu) {

        Integer lungheza = righeCarrello.size();
        System.out.println("Lunghezza righeCarrello: " + lungheza);
        String codiceRiga = controllerClientPanel.generaProssimoCodiceRiga();
        if (menu) {
            RigaCarrelloMenu rigaOrdine = new RigaCarrelloMenu(menu, prezzoUnitario, quantita, nomeProdotto,
                    codiceProdotto, this,
                    controllerClientPanel, codiceRiga);
            this.panelscorrevole.add(rigaOrdine);
            this.righeCarrello.add(rigaOrdine);
            this.revalidate();
            this.repaint();

            return;
        }

        RigaCarrello rigaOrdine = new RigaCarrelloSingolo(menu, prezzoUnitario, quantita, nomeProdotto, codiceProdotto,
                this,
                controllerClientPanel, codiceRiga);

        this.panelscorrevole.add(rigaOrdine);
        this.righeCarrello.add(rigaOrdine);
        this.revalidate();
        this.repaint();
    }

    public void rimuoviProdottoCarrello(RigaCarrello rigaCarrello) {
        this.panelscorrevole.remove(rigaCarrello);
        righeCarrello.remove(rigaCarrello);
        this.revalidate();
        this.repaint();
    }

    public void svuotaCarrello() {
        this.panelscorrevole.removeAll();
        righeCarrello.clear();
        this.revalidate();
        this.repaint();
    }

    public List<RigaCarrello> getRigheCarrello() {
        return righeCarrello;
    }

}