package project.db.controller;

import project.db.data.Ordine;
import project.db.data.Rider;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Ordine.OrdinePanel;
import project.db.view.Rider.RiderPanel;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;

public class ControllerRider {

    private MainController mainController;
    private ReadingModel modelReading;
    private RiderPanel riderPanel;
    private WritingModel writingModel;
    private Rider riderLoggato;
    private OrdinePanel ordinePresoInCarico;
    private List<OrdinePanel> ordiniPanels = new ArrayList<>();

    public ControllerRider(MainController mainController, ReadingModel modelReading, WritingModel writingModel,
            RiderPanel riderPanel) {
        this.mainController = mainController;
        this.modelReading = modelReading;
        this.riderPanel = riderPanel;
        this.writingModel = writingModel;
    }

    public void showOrders() {
        List<Ordine> ordini = modelReading.loadOrdini();
        ordiniPanels.clear();
        System.out.println("Ordini caricati con successo: " + ordini.size());
        for (Ordine ordine : ordini) {

            if (ordine != null) {
                ordiniPanels.add(new OrdinePanel(
                        this,
                        ordine.getCodiceOrdine(),
                        ordine.getIndVia(),
                        ordine.getIndCivico()));
            }
        }
        riderPanel.showOrdersReady(ordiniPanels);
    }

    public void prendiInCaricoOrdine(OrdinePanel ordineRiderPanel) {

        this.riderPanel.showInfoMessage("Ordine preso in carico !");

        for (OrdinePanel ordinePanel : ordiniPanels) {
            if (!ordinePanel.getCodiceOrdine().equals(ordineRiderPanel.getCodiceOrdine())) {
                ordinePanel.enablePanel();
            } else {
                ordinePresoInCarico = ordinePanel;
            }
        }
    }

    public void consegnaOrdine(String codiceOrdine) {

        if (ordinePresoInCarico == null) {
            JOptionPane.showMessageDialog(riderPanel, "Nessun ordine è stato preso in carico.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (codiceOrdine.equals(ordinePresoInCarico.getCodiceOrdine())
                && writingModel.aggiornaStatoOrdine(writingModel.getConnection(), codiceOrdine, "Consegnato")) {
            this.riderPanel.showInfoMessage("Ordine consegnato con successo!");
        } else {
            this.riderPanel.showErrorMessage("Errore durante la consegna dell'ordine.");
        }

        showOrders();
        ordinePresoInCarico = null;

    }

    public void setRiderLoggato(final Rider riderLoggato) {
        this.riderLoggato = riderLoggato;
    }

}