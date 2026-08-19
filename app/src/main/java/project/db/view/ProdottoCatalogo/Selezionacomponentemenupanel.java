package project.db.view.ProdottoCatalogo;

import project.db.data.Pair;
import project.db.view.Client.RigaCarrelloMenu;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

/**
 * Dialog usato SOLO per le righe del carrello di tipo menu: mostra i prodotti
 * che compongono il menu e permette di scegliere quale modificare. Dopo la
 * selezione, delega a RigaCarrello l'apertura del pannello di modifica
 * ingredienti per il componente scelto. Resta aperto dopo la selezione, così
 * l'utente può modificare più componenti dello stesso menu senza dover
 * riaprire il dialog da capo.
 */
public class Selezionacomponentemenupanel extends JDialog {

    private final JComboBox<String> componentiComboBox = new JComboBox<>();
    private final JButton selezionaButton = new JButton("Modifica ingredienti di questo prodotto");
    private final JButton chiudiButton = new JButton("Chiudi");
    private final RigaCarrelloMenu rigaCarrello;
    private Map<Pair<String, Integer>, List<String>> componenti;

    public Selezionacomponentemenupanel(RigaCarrelloMenu rigaCarrello) {
        this.rigaCarrello = rigaCarrello;

        this.setTitle("Seleziona prodotto del menu da modificare");
        this.setSize(500, 200);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 8, 8);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add(new JLabel("Prodotto:"), c);

        c.gridy = 1;
        this.add(componentiComboBox, c);

        c.gridy = 2;
        c.gridwidth = 1;
        this.add(selezionaButton, c);

        c.gridx = 1;
        this.add(chiudiButton, c);

        selezionaButton.addActionListener(e -> {
            String componenteSelezionato = (String) componentiComboBox.getSelectedItem();

            if (componenteSelezionato == null || componenti == null) {
                JOptionPane.showMessageDialog(this, "Nessun prodotto selezionato.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            rigaCarrello.setProdottoMenuSelezionato(
                    new Pair<String, Integer>(componenteSelezionato, componentiComboBox.getSelectedIndex()));
            rigaCarrello.requestIngredienti();

        });

        chiudiButton.addActionListener(e -> this.setVisible(false));
    }

    public void caricaComponenti(Map<Pair<String, Integer>, List<String>> componenti) {
        this.componenti = componenti;
        componentiComboBox.removeAllItems();
        if (componenti != null) {
            for (Pair<String, Integer> nomeComponente : componenti.keySet()) {
                componentiComboBox.addItem(nomeComponente.getFirst());
                System.out.println("Caricato componente menu: " + nomeComponente.getFirst() + ", numRowComp: "
                        + nomeComponente.getSecond());
            }
        }
    }

    public Map<Pair<String, Integer>, List<String>> getComponenti() {
        return componenti;
    }
}