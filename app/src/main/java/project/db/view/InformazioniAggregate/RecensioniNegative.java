package project.db.view.InformazioniAggregate;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import project.db.data.Pair;

public class RecensioniNegative extends JDialog {

    public RecensioniNegative() {
        this.setTitle("Recensioni Negative");
        this.setSize(400, 300);
        this.setModal(true);
        this.setLocationRelativeTo(null);
    }

    public void mostraRecensioniNegative(Map<Pair<String,String>,Pair<Integer,Integer>> recensioniNegative) {

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Recensioni negative:\n\n");
        for (Map.Entry<Pair<String,String>,Pair<Integer,Integer>> entry : recensioniNegative.entrySet()) {
            Pair<String,String> prodottoKey = entry.getKey();
            Pair<Integer,Integer> prodottoStats = entry.getValue();
            sb.append("- ").append(prodottoKey.getFirst()).append(" ").append(prodottoKey.getSecond())
              .append(" (Voto Ordine: ").append(prodottoStats.getFirst())
              .append(", Voto Rider: ").append(prodottoStats.getSecond()).append(")\n");
        }
        textArea.setText(sb.toString());

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setVisible(true);
    }
}