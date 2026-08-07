package project.db.view.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.db.view.ProdottoCatalogo.ModificaIngredientiPanel;


public class RigaCarrello extends JPanel {

    private float prezzo;
    private int quantita;
    private String nomeProdotto;
    private Carrello carrello;
    private final String codiceProdotto;
    private JLabel nomeProdottoLabel;
    private JLabel quantitaLabel;
    private JLabel prezzoLabel;
    private JButton buttonModificaRiga;
    private JButton buttonEliminaRiga;
    private Map<String, Integer> ingredientiPresenti;
    private boolean menu;
    private ModificaIngredientiPanel modificaIngredientiPanel;

    public RigaCarrello(boolean menu, float prezzo, int quantita, String nomeProdotto, String codiceProdotto, Carrello carrello) {
        this.prezzo = prezzo;
        this.codiceProdotto = codiceProdotto;
        this.quantita = quantita;
        this.carrello = carrello;
        this.nomeProdotto = nomeProdotto;
        this.menu = menu;
        this.requestIngredientiPresenti();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.nomeProdottoLabel = new JLabel(nomeProdotto);
        this.quantitaLabel = new JLabel(String.valueOf(quantita));
        this.prezzoLabel = new JLabel(String.format("€%.2f", prezzo));
        this.modificaIngredientiPanel = new ModificaIngredientiPanel(null, codiceProdotto, ingredientiPresenti);

        this.add(nomeProdottoLabel);
        this.add(quantitaLabel);
        this.add(prezzoLabel);
        this.buttonModificaRiga = new JButton("Modifica");
        this.buttonEliminaRiga = new JButton("Elimina");
        this.add(buttonModificaRiga);
        this.add(buttonEliminaRiga);

        this.buttonModificaRiga.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                modificaIngredientiPanel.setVisible(true);
            }
        });
    }

    public void requestIngredientiPresenti(){
        carrello.requestIngredientiPresenti(this);
    }

    public void mostraIngredienti(Map<String, Integer> ingredienti) {

    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setIngredientiPresenti(Map<String, Integer> ingredientiPresenti) {
        this.ingredientiPresenti = ingredientiPresenti;
    }

}
