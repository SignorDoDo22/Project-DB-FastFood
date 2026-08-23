package project.db.view.Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.db.controller.ControllerClientPanel;
import project.db.data.Pair;
import project.db.data.Prodotto;
import project.db.view.MainView;
import project.db.view.ProdottoCatalogo.ProdottoCard;

public class ClientPanel extends JPanel {

    private final JPanel pannelloInferiore;
    private final MainView mainView;
    private final JScrollPane pannelloCentrale;
    private final JPanel pannelScorrevole;
    private JButton refreshRecensioniButton;
    private Carrello carrello;
    private RecensioniPanel recensioniPanel;
    private final JButton buttonIndietro;
    private final JButton buttonProcedi;
    private ControllerClientPanel controllerClientPanel;

    public ClientPanel(final MainView mainView) {
        this.mainView = mainView;
        this.setLayout(new BorderLayout());

        this.pannelScorrevole = new JPanel();
        this.pannelScorrevole.setLayout(new BoxLayout(pannelScorrevole, BoxLayout.Y_AXIS));

        this.pannelloCentrale = new JScrollPane(pannelScorrevole);
        this.add(pannelloCentrale, BorderLayout.CENTER);

        this.pannelloInferiore = new JPanel();
        this.pannelloInferiore.setLayout(new GridLayout(1, 3));

        this.buttonIndietro = new JButton("Indietro");
        this.buttonProcedi = new JButton("Procedi e ordina");

        this.refreshRecensioniButton = new JButton("Aggiorna Recensioni");
        this.refreshRecensioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controllerClientPanel.userRequestOrdiniRecensibili();
                revalidate();
                repaint();
            }
        });

        this.pannelloInferiore.add(refreshRecensioniButton);

        this.pannelloInferiore.add(buttonIndietro);
        this.pannelloInferiore.add(new JPanel());
        this.pannelloInferiore.add(buttonProcedi);
        this.add(pannelloInferiore, BorderLayout.SOUTH);

        this.buttonIndietro.addActionListener(e -> {
            controllerClientPanel.logout();
            this.mainView.requestChangePanel("scelta", "client");
        });

        this.buttonProcedi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controllerClientPanel.setVisibleDomicilioPanel(true);
            }
        });
    }

    public void showCatalogo(List<Prodotto> prodotti) {

        this.pannelScorrevole.removeAll();
        for (Prodotto p : prodotti) {

            this.pannelScorrevole.add(new ProdottoCard(p.getCodiceProdotto(), p.getNomeProdotto(),
                    p.getPrezzoOriginario(), p.isDisponibile(), p.getMenu(), this));
        }
    }

    public void requestIngredienti(String codice_prodotto) {
        controllerClientPanel.userRequestIngredientiProd(codice_prodotto);
    }

    public List<String> getIngredientiProdotto(List<String> ingredienti) {
        return ingredienti;
    }

    public void setController(final ControllerClientPanel controllerClientPanel) {
        Objects.requireNonNull(controllerClientPanel);
        this.controllerClientPanel = controllerClientPanel;
    }

    public void mostraIngredienti(List<String> ingredienti) {
        if (ingredienti.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun ingrediente trovato.");
            return;
        }

        StringBuilder testo = new StringBuilder();
        for (String ingrediente : ingredienti) {
            testo.append(ingrediente).append("\n");
        }
        JOptionPane.showMessageDialog(this, "Ingredienti: " + testo.toString());
    }

    public void requestIngredientiMenu(String codiceProdottoMenu) {
        controllerClientPanel.userRequestIngredientiMenu(codiceProdottoMenu);
    }

    public void mostraIngredientiMenu(Map<Pair<String, Integer>, List<String>> ingredientiMenu) {
        if (ingredientiMenu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun ingrediente trovato per il menu.");
            return;
        }

        StringBuilder testo = new StringBuilder();
        for (Map.Entry<Pair<String, Integer>, List<String>> entry : ingredientiMenu.entrySet()) {
            String prodotto = entry.getKey().getFirst();
            List<String> ingredienti = entry.getValue();
            testo.append(prodotto).append(": ").append(String.join(", ", ingredienti)).append("\n");
        }

        JOptionPane.showMessageDialog(this, "Ingredienti del menu:\n" + testo.toString());
    }

    public void addRigaCarrello(int quantita, String codiceProdotto, String nomeProdotto, float prezzoUnitario,
            boolean menu) {
        carrello.addRigaCarrello(codiceProdotto, quantita, nomeProdotto, prezzoUnitario, menu);
    }

    public void setCarrelloPanel(Carrello carrello) {
        this.carrello = carrello;
        this.add(carrello, BorderLayout.EAST);
    }

    public void setRecensioniPanel(RecensioniPanel recensioniPanel) {
        this.recensioniPanel = recensioniPanel;
        this.add(recensioniPanel, BorderLayout.WEST);
    }

    public void setControllerClientPanel(ControllerClientPanel controllerClientPanel) {
        this.controllerClientPanel = controllerClientPanel;
    }

    public void createRecensioni(String numOrdine, int votoRider, int votoOrdine, String testoRecensione) {
        controllerClientPanel.userRequestCreateRecensione(numOrdine, votoRider, votoOrdine, testoRecensione);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }

}