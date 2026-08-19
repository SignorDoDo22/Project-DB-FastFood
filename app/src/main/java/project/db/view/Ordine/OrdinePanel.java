package project.db.view.Ordine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import project.db.controller.ControllerRider;

public class OrdinePanel extends JPanel {

    private final String codiceOrdine;
    private final String via;
    private final String civico;
    private JButton prendiInCaricoButton = new JButton("Prendi in carico");
    private JButton consegnaButton = new JButton("Consegna");
    private ControllerRider controller;
    private JPanel azionePanel = new JPanel();

    public OrdinePanel(ControllerRider controller, String codiceOrdine, String via, String civico) {
        this.codiceOrdine = codiceOrdine;
        this.via = via;
        this.civico = civico;
        this.controller = controller;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel indirizzoLabel = new JLabel(
                String.format("Ordine %s - %s, %s", codiceOrdine, via, civico));
        this.add(indirizzoLabel, BorderLayout.WEST);
        this.add(BorderLayout.EAST, azionePanel);
        azionePanel.add(prendiInCaricoButton);
        azionePanel.add(consegnaButton);

        this.prendiInCaricoButton.addActionListener(e -> controller.prendiInCaricoOrdine(this));
        this.consegnaButton.addActionListener(e -> controller.consegnaOrdine(codiceOrdine));
    }

    public void enablePanel() {
        this.prendiInCaricoButton.setEnabled(false);
        this.consegnaButton.setEnabled(false);
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public String getVia() {
        return via;
    }

    public String getCivico() {
        return civico;
    }

}