package project.db.controller;

import project.db.data.Ordine;
import project.db.data.Rider;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;
import project.db.view.Rider.OrdineRiderPanel;
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
    private OrdineRiderPanel ordinePresoInCarico;
    private List<OrdineRiderPanel> ordiniPanels = new ArrayList<>();

    public ControllerRider(MainController mainController, ReadingModel modelReading, WritingModel writingModel, RiderPanel riderPanel) {
        this.mainController = mainController;
        this.modelReading = modelReading;
        this.riderPanel = riderPanel;
        this.writingModel = writingModel;
    }

    public void showOrders(){
        List<Ordine> ordini = modelReading.loadOrdini();
        ordiniPanels.clear();
        System.out.println("Ordini caricati con successo: " + ordini.size());
        for (Ordine ordine : ordini) {

            if (ordine != null) {
                ordiniPanels.add(new OrdineRiderPanel(
                        this,
                        ordine.getCodiceOrdine(),
                        ordine.getIndVia(),
                        ordine.getIndCivico()
                ));
            }
        }
        riderPanel.showOrdersReady(ordiniPanels);
    }

    public void riderPrendeInCarico(OrdineRiderPanel ordineRiderPanel) {
        this.ordinePresoInCarico = ordineRiderPanel;
        writingModel.prendeInCaricoOrdine(ordineRiderPanel.getCodiceOrdine(), riderLoggato.getCodiceRider());
        for (OrdineRiderPanel ordine : ordiniPanels) {
            if (ordine != ordinePresoInCarico) {
                ordine.setEnable(false);
                ordine.repaint();
            }
        }
    }

    public void riderCompletaConsegna(OrdineRiderPanel ordineRiderPanel) {

        int value = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler completare la consegna?", "Conferma", JOptionPane.YES_NO_OPTION);
        if (value == JOptionPane.YES_OPTION) {

            if (ordinePresoInCarico == ordineRiderPanel) {
                writingModel.riderCompletaConsegna(ordineRiderPanel.getCodiceOrdine());

                this.showOrders();
                this.ordinePresoInCarico = null;
            }else{
                JOptionPane.showMessageDialog(null, "Non puoi completare la consegna di un ordine che non hai preso in carico.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setRiderLoggato(final Rider riderLoggato) {
        this.riderLoggato = riderLoggato;
    }

}