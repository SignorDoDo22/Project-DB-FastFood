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

import project.db.controller.ControllerAdmin;

public class OrdineAdminPanel extends JPanel {

    private JButton button = new JButton("Pronto per la consegna");
    private JPanel azionePanel = new JPanel();
    private String codiceOrdine;
    private String via;
    private String civico;
    private ControllerAdmin controller;

    public OrdineAdminPanel(ControllerAdmin controller, String codiceOrdine, String via, String civico) {
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

        azionePanel.add(button);
        this.add(azionePanel, BorderLayout.EAST);

        button.addActionListener(e -> {
            this.controller.changeOrdineStatusReady(this.codiceOrdine);
        });
    }

}