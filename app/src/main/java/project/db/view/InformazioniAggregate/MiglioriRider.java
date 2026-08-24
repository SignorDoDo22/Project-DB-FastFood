package project.db.view.InformazioniAggregate;

import javax.swing.*;

import project.db.data.Pair;

import java.awt.*;
import java.util.Map;

public class MiglioriRider extends JDialog {

    public MiglioriRider() {
        this.setTitle("Migliori Rider");
        this.setSize(400, 300);
        this.setModal(true);
        this.setLocationRelativeTo(null);
    }

    public void mostraClassifica(Map<Pair<String, String>, Pair<Float, Integer>> classifica) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Classifica migliori rider:\n\n");
        int rank = 1;
        for (Pair<String, String> entry : classifica.keySet()) {
            Pair<Float, Integer> punteggi = classifica.get(entry);
            sb.append(rank).append(". ").append(entry.getFirst()).append(" - ").append(entry.getSecond()).append(" (")
                    .append(punteggi.getFirst()).append(" punti, ").append(punteggi.getSecond()).append(" Guadagno)")
                    .append("\n");
            rank++;
        }
        textArea.setText(sb.toString());

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setVisible(true);
    }
}