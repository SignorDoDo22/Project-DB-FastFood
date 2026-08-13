package project.db.view.Rider;

import java.util.List;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.db.controller.ControllerRider;
import project.db.data.Ordine;
import project.db.view.MainView;

public class RiderPanel extends JPanel {

    private JScrollPane scrollPane;
    private JPanel panelInterno;
    private JButton buttonIndietro;
    private JButton buttonRefresh;
    private ControllerRider controllerRider;
    private MainView mainView;

    public RiderPanel( MainView mainView) {
        this.mainView = mainView;

        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.panelInterno = new JPanel();
        this.panelInterno.setLayout(new BoxLayout(panelInterno, BoxLayout.Y_AXIS));
        this.scrollPane = new JScrollPane(panelInterno);
        this.buttonIndietro = new JButton("Indietro");
        this.buttonRefresh = new JButton("Refresh");

        this.add(scrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.add(buttonIndietro);
        southPanel.add(buttonRefresh);
        this.add(southPanel, BorderLayout.SOUTH);

        this.buttonRefresh.addActionListener(e -> this.controllerRider.showOrders());
        this.buttonIndietro.addActionListener(e -> mainView.changePanel("scelta"));
    }

    public void showOrdersReady(List<OrdineRiderPanel> ordini) {
        panelInterno.removeAll();
        for (OrdineRiderPanel ordinePanel : ordini) {
            panelInterno.add(ordinePanel);
        }

        panelInterno.revalidate();
        panelInterno.repaint();
    }

    public void setControllerRider(ControllerRider controllerRider) {
        this.controllerRider = controllerRider;
    }
}
