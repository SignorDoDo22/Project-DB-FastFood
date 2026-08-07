package project.db.controller;

import project.db.data.Ordine;
import project.db.model.ReadingModel;
import project.db.view.Rider.RiderPanel;

import java.util.List;

public class ControllerRider {

    private MainController mainController;
    private ReadingModel modelReading;
    private RiderPanel riderPanel;

    public ControllerRider(MainController mainController, ReadingModel modelReading, RiderPanel riderPanel) {
        this.mainController = mainController;
        this.modelReading = modelReading;
        this.riderPanel = riderPanel;
    }

    public void showOrders(){
        List<Ordine> ordini = modelReading.loadOrdini();
        riderPanel.showOrdersReady(ordini);
    }

}
