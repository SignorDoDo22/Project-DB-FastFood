package project.db.view.InformazioniAggregate;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MiglioriRider extends JDialog {

    public MiglioriRider() {
        this.setTitle("Migliori Rider");
        this.setSize(400, 300);
        this.setModal(true);
        this.setLocationRelativeTo(null);
    }

    public void mostraClassifica(Map<String, String> classifica, Map<String, Integer> punteggi) {
        this.setSize(700,700);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Classifica migliori rider:\n\n");
        int rank = 1;
        for (Map.Entry<String, String> entry : classifica.entrySet()) {
            sb.append(rank).append(". ").append(entry.getKey()).append(" - ").append(entry.getValue()).append(" (").append(punteggi.get(entry.getKey())).append(" punti)").append("\n");
            rank++;
        }
        textArea.setText(sb.toString());

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setVisible(true);
    }
}