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

    /**
     * Richiede al controller la composizione del menu (nome componente -> suoi
     * ingredienti) associato a questa riga carrello, così l'utente può scegliere
     * quale prodotto del menu modificare.
     */
    public void requestComponentiMenu(final RigaCarrello rigaCarrello) {
        controllerClientPanel.userRequestComponentiMenu(rigaCarrello.getCodiceProdotto(), rigaCarrello);
    }

    /**
     * Restituisce i nomi di tutti gli ingredienti disponibili a catalogo,
     * usati per popolare la sezione "aggiungi ingrediente".
     */
    public List<String> requestIngredientiDisponibili() {
        return controllerClientPanel.userRequestIngredientiDisponibili();
    }

    public List<RigaCarrello> getRigheCarrello() {
        return righeCarrello;
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

}