package project.db.view.Recensione;

import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import project.db.view.Client.ClientPanel;
import project.db.view.Client.RecensioniPanel;

public class CardRecensione extends JPanel {

    private final JDialog dialogRecensione;
    private JButton confermaRecensione;
    private final JButton creaRecensione;
    private final JTextArea testoRecensione;
    private final JComboBox<Integer> votoOrdineSelector;
    private final JComboBox<Integer> votoRiderSelector;
    private final String numOrdine;
    private final ClientPanel clientPanel;
    private final RecensioniPanel recensioniPanel;

    public CardRecensione(final String numOrdine, final ClientPanel clientPanel,
            final RecensioniPanel recensioniPanel) {
        this.recensioniPanel = recensioniPanel;
        this.numOrdine = numOrdine;
        this.clientPanel = clientPanel;
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)));
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titoloPanel = new JLabel("Creazione Recensione");
        JLabel labelCodOrdine = new JLabel("Codice Ordine: " + numOrdine);

        infoPanel.add(titoloPanel);
        infoPanel.add(labelCodOrdine);

        this.add(infoPanel, BorderLayout.WEST);

        JPanel azionePanel = new JPanel();
        azionePanel.setLayout(new BoxLayout(azionePanel, BoxLayout.Y_AXIS));

        Integer[] voti = { 1, 2, 3, 4, 5 };
        this.votoOrdineSelector = new JComboBox<>(voti);
        this.votoRiderSelector = new JComboBox<>(voti);

        JPanel votoOrdinePanel = new JPanel();
        votoOrdinePanel.add(new JLabel("Voto Ordine:"));
        votoOrdinePanel.add(votoOrdineSelector);

        JPanel votoRiderPanel = new JPanel();
        votoRiderPanel.add(new JLabel("Voto Rider:"));
        votoRiderPanel.add(votoRiderSelector);

        this.testoRecensione = new JTextArea(5, 20);

        this.creaRecensione = new JButton("Crea Recensione");
        azionePanel.add(creaRecensione);

        this.add(azionePanel, BorderLayout.EAST);

        this.dialogRecensione = new JDialog();
        this.dialogRecensione.setTitle("Crea Recensione");
        this.dialogRecensione.setLayout(new BorderLayout());
        this.dialogRecensione.add(votoRiderPanel, BorderLayout.WEST);
        this.dialogRecensione.add(votoOrdinePanel, BorderLayout.EAST);
        this.dialogRecensione.add(new JScrollPane(testoRecensione), BorderLayout.CENTER);
        this.confermaRecensione = new JButton("Conferma Recensione");
        this.dialogRecensione.add(confermaRecensione, BorderLayout.SOUTH);
        this.dialogRecensione.setModal(true);
        this.dialogRecensione.pack();
        this.dialogRecensione.setLocationRelativeTo(null);

        this.confermaRecensione.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                clientPanel.createRecensioni(numOrdine, (int) votoRiderSelector.getSelectedItem(),
                        (int) votoOrdineSelector.getSelectedItem(), testoRecensione.getText());

                recensioniPanel.refresh();
                dialogRecensione.setVisible(false);
            }

        });

        this.creaRecensione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogRecensione.setVisible(true);
            }
        });
    }

    public String getTestoRecensione() {
        return testoRecensione.getText();
    }

    public int getVotoOrdine() {
        return (int) votoOrdineSelector.getSelectedItem();
    }

    public int getVotoRider() {
        return (int) votoRiderSelector.getSelectedItem();
    }

    public String getNumOrdine() {
        return numOrdine;
    }
}