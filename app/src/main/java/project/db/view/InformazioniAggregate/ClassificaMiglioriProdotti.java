package project.db.view.InformazioniAggregate;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import project.db.data.Pair;

public class ClassificaMiglioriProdotti extends JDialog {

    private JPanel panelScorrevole;
    private JScrollPane scrollPane;

    public ClassificaMiglioriProdotti() {
        this.setTitle("Prodotti più venduti");
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.panelScorrevole = new JPanel();
        panelScorrevole.setLayout(new BoxLayout(panelScorrevole, BoxLayout.Y_AXIS));
        this.scrollPane = new JScrollPane(panelScorrevole);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    public void mostraClassificaProdottiPiuVenduti(Map<Pair<String,String>,Integer> classifica) {

        for (Map.Entry<Pair<String,String>, Integer> entry : classifica.entrySet()) {
            System.out.println("Prodotto: " + entry.getKey().getFirst() + " - " + entry.getKey().getSecond() + ", Quantità venduta: " + entry.getValue());
            Pair<String,String> prodotto = entry.getKey();
            Integer quantitaVenduta = entry.getValue();
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText(prodotto.getFirst() + " - " + prodotto.getSecond() + " (" + quantitaVenduta + " venduti)");
            panelScorrevole.add(textArea);
        }
        this.revalidate();
        this.repaint();
        this.setVisible(true);

    }
}
