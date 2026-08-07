package project.db.view.Rider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.db.data.Ordine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class OrdineRiderPanel extends JPanel {

    private final JButton prendiInCaricoButton;
    private final JButton consegnaButton;
    private final Ordine ordine;

    public OrdineRiderPanel(Ordine ordine) {

        this.ordine = ordine;


        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel indirizzoLabel = new JLabel(
                String.format("Ordine %s - %s, %s (%s)", ordine.getCodiceOrdine(),
                ordine.getIndVia(), ordine.getIndCivico(), ordine.getIndCitta()));
        this.add(indirizzoLabel, BorderLayout.WEST);

        JPanel azionePanel = new JPanel();
        this.prendiInCaricoButton = new JButton("Prendi in carico");
        this.consegnaButton = new JButton("Consegna");
        azionePanel.add(prendiInCaricoButton);
        azionePanel.add(consegnaButton);
        this.add(azionePanel, BorderLayout.EAST);

        this.prendiInCaricoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });

    }

    public JButton getPrendiInCaricoButton() {
        return prendiInCaricoButton;
    }

    public JButton getConsegnaButton() {
        return consegnaButton;
    }
}