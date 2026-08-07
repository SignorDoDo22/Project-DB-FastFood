package project.db.view.Client;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.db.controller.ControllerClientPanel;

import java.awt.BorderLayout;
import java.util.List;
import project.db.data.Ordine;
import project.db.view.Recensione.CardRecensione;

public class RecensioniPanel extends JPanel {

    private JLabel titoloPanel;
    private JPanel panneloScorrevole;
    private JScrollPane pannelloCentrale;
    private ClientPanel clientPanel;
    private ControllerClientPanel controllerClient;


    public RecensioniPanel(final ClientPanel clientPanel, final ControllerClientPanel controllerClient) {

        this.setLayout(new BorderLayout());
        this.clientPanel = clientPanel;
        this.controllerClient = controllerClient;
        this.panneloScorrevole = new JPanel();
        this.pannelloCentrale = new JScrollPane(panneloScorrevole);
        this.panneloScorrevole.setLayout(new BoxLayout(panneloScorrevole, BoxLayout.Y_AXIS));
        this.add(pannelloCentrale, BorderLayout.CENTER);
        this.titoloPanel = new JLabel("Recensioni");
        this.add(titoloPanel, BorderLayout.NORTH);

    }

    public void refresh(){
        this.controllerClient.userRequestOrdiniRecensibili();
    }

    public void mostraRecensioni(List<Ordine> ordini) {
        System.out.println("RecensioniPanel: ordini recensibili: " + ordini.size());
        panneloScorrevole.removeAll();
        for(Ordine ordine : ordini){
            CardRecensione cardRecensione = new CardRecensione(ordine.getCodiceOrdine(), clientPanel, this);
            panneloScorrevole.add(cardRecensione);
        }
        panneloScorrevole.revalidate();
        panneloScorrevole.repaint();
    }

}
