package project.db.view.Rider;

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

public class OrdineRiderPanel extends JPanel {

    private final JButton prendiInCaricoButton;
    private final JButton consegnaButton;
    private final String codiceOrdine;
    private final String via;
    private final String civico;
    private ControllerRider controller;

    public OrdineRiderPanel(ControllerRider controller, String codiceOrdine, String via, String civico) {
        this.codiceOrdine = codiceOrdine;
        this.via = via;
        this.civico = civico;
        this.controller = controller;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel indirizzoLabel = new JLabel(
                String.format("Ordine %s - %s, %s", codiceOrdine, via, civico));
        this.add(indirizzoLabel, BorderLayout.WEST);

        JPanel azionePanel = new JPanel();
        this.prendiInCaricoButton = new JButton("Prendi in carico");
        this.consegnaButton = new JButton("Consegna");
        azionePanel.add(prendiInCaricoButton);
        azionePanel.add(consegnaButton);
        this.add(azionePanel, BorderLayout.EAST);

        this.prendiInCaricoButton.addActionListener(e -> {
            controller.riderPrendeInCarico(this);
        });

        this.consegnaButton.addActionListener(e -> {
            controller.riderCompletaConsegna(this);
        });
    }


    public void setEnable(boolean enabled) {
        this.prendiInCaricoButton.setEnabled(enabled);
        this.consegnaButton.setEnabled(enabled);
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

}