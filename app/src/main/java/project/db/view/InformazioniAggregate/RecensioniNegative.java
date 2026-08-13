package project.db.view.InformazioniAggregate;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RecensioniNegative extends JDialog {

    public RecensioniNegative() {
        this.setTitle("Recensioni Negative");
        this.setSize(400, 300);
        this.setModal(true); // blocca l'interazione con la finestra sottostante
        this.setLocationRelativeTo(null); // centra sullo schermo
    }

    public void mostraRecensioniNegative(List<String> recensioni, Map<String, Integer> punteggi) {

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Ultime recensioni negative:\n\n");

        if (recensioni.isEmpty()) {
            sb.append("Nessuna recensione negativa trovata.");
        } else {
            int rank = 1;
            for (String recensione : recensioni) {
                sb.append(rank).append(". ").append(recensione).append("\n");
                rank++;
            }
        }

        textArea.setText(sb.toString());

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.setVisible(true); // mostra IL TUO dialog, non uno di JOptionPane
    }
}