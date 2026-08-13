package project.db.controller;
import project.db.view.InformazioniAggregate.MiglioriRider;
import project.db.view.InformazioniAggregate.RecensioniNegative;
import project.db.view.InformazioniAggregate.ClassificaMiglioriProdotti;
import project.db.model.ReadingModel;

public class ControllerInformazioniAggregate {

    private MiglioriRider miglioriRider;
    private RecensioniNegative recensioniNegative;
    private ClassificaMiglioriProdotti classificaProdottiPiuVenduti;
    private ReadingModel modelReading;

    public ControllerInformazioniAggregate(final ReadingModel modelReading) {
        this.modelReading = modelReading;
        this.miglioriRider = new MiglioriRider();
        this.recensioniNegative = new RecensioniNegative();
        this.classificaProdottiPiuVenduti = new ClassificaMiglioriProdotti();
    }

    public void showMiglioriRider() {
        miglioriRider.setVisible(true);
    }

    public void showRecensioniNegative() {
        recensioniNegative.setVisible(true);
    }

    public void showClassificaProdottiPiuVenduti() {
        classificaProdottiPiuVenduti.mostraClassificaProdottiPiuVenduti(modelReading.getClassificaProdottiPiuVenduti());
    }


}
