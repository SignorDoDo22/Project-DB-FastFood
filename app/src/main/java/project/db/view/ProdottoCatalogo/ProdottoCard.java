package project.db.view.ProdottoCatalogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import project.db.view.Client.ClientPanel;

public class ProdottoCard extends JPanel {

    private final JButton aggiungiProdotto;
    private final JButton infoProdotto;
    private final JButton modificaProdotto;
    private final String codiceProdotto;
    private final String isMenu;
    private ClientPanel clientPanel;

    public ProdottoCard(final String codiceProdotto, final String nomeProdotto, final float prezzo,
            final boolean disponibile, final String isMenu, final ClientPanel clientPanel) {

        this.codiceProdotto = codiceProdotto;
        this.isMenu = isMenu;
        this.clientPanel = clientPanel;
        this.infoProdotto = new JButton("info");
        this.modificaProdotto = new JButton("Modifica e Aggiungi ");
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)));
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(nomeProdotto));
        this.add(infoPanel, BorderLayout.WEST);

        JPanel azionePanel = new JPanel();
        azionePanel.add(new JLabel(String.format("€%.2f", prezzo)));

        this.aggiungiProdotto = new JButton("Aggiungi");
        this.aggiungiProdotto.setEnabled(disponibile);
        azionePanel.add(aggiungiProdotto);
        azionePanel.add(infoProdotto);

        this.add(azionePanel, BorderLayout.EAST);

        if (!disponibile) {
            this.setBackground(new Color(253, 235, 235));
            this.setOpaque(true);
        }

        this.infoProdotto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMenu != null) {
                    clientPanel.requestIngredientiMenu(codiceProdotto);
                } else {
                    clientPanel.requestIngredienti(codiceProdotto);
                }
            }
        });

        this.aggiungiProdotto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // 2) Chiedo la quantità desiderata
                String input = JOptionPane.showInputDialog(
                        null,
                        "Inserisci la quantità desiderata:",
                        "Quantità prodotto",
                        JOptionPane.QUESTION_MESSAGE);

                if (input == null) {
                    return;
                }

                int quantita;
                try {
                    quantita = Integer.parseInt(input.trim());
                    if (quantita <= 0) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Inserisci un numero maggiore di zero.",
                                "Valore non valido",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Devi inserire un numero valido.",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                clientPanel.addRigaCarrello(quantita, codiceProdotto, nomeProdotto, prezzo, isMenu != null);
            }
        });
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public JButton getAggiungiProdotto() {
        return aggiungiProdotto;
    }

}