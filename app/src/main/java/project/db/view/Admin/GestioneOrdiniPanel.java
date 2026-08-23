package project.db.view.Admin;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.db.controller.ControllerAdmin;
import project.db.view.Ordine.OrdineAdminPanel;

public class GestioneOrdiniPanel extends JDialog {

    private JScrollPane scrollPane;
    private JPanel panelInterno;
    private JButton buttonIndietro;
    private JButton buttonRefresh;
    private ControllerAdmin controllerAdmin;

    public GestioneOrdiniPanel(ControllerAdmin controllerAdmin) {

        this.setLayout(new BorderLayout());
        this.setSize(700, 700);
        this.panelInterno = new JPanel();
        this.controllerAdmin = controllerAdmin;
        this.panelInterno.setLayout(new BoxLayout(panelInterno, BoxLayout.Y_AXIS));
        this.scrollPane = new JScrollPane(panelInterno);
        this.buttonIndietro = new JButton("Indietro");
        this.buttonRefresh = new JButton("Refresh");

        this.add(scrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.add(buttonIndietro);
        southPanel.add(buttonRefresh);
        this.add(southPanel, BorderLayout.SOUTH);

        this.buttonIndietro.addActionListener(e -> this.setVisible(false));

    }

    public void mostraOrdini(List<OrdineAdminPanel> ordini) {
        this.panelInterno.removeAll();
        for (OrdineAdminPanel ordine : ordini) {
            this.panelInterno.add(ordine);
        }
        this.panelInterno.revalidate();
        this.panelInterno.repaint();
    }

}
