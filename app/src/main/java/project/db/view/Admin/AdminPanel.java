package project.db.view.Admin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import project.db.controller.ControllerAdmin;
import project.db.data.Prodotto;
import project.db.view.MainView;

public class AdminPanel extends JPanel {

    private ControllerAdmin controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField campoCodice;
    private final JTextField campoNome;
    private final JTextArea campoDescrizione;
    private final JTextField campoPrezzo;
    private final JCheckBox campoDisponibile;

    public AdminPanel(final MainView mainView) {

        this.setLayout(new BorderLayout(10, 10));

        String[] colonne = {"Codice", "Nome", "Tipo", "Prezzo", "Disponibile"};
        this.tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(tableModel);
        this.table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                caricaProdottoSelezionato();
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Codice:"));
        this.campoCodice = new JTextField();
        this.campoCodice.setEditable(false);
        form.add(campoCodice);

        form.add(new JLabel("Nome:"));
        this.campoNome = new JTextField();
        form.add(campoNome);

        form.add(new JLabel("Descrizione:"));
        this.campoDescrizione = new JTextArea(2, 20);
        form.add(new JScrollPane(campoDescrizione));

        form.add(new JLabel("Prezzo:"));
        this.campoPrezzo = new JTextField();
        form.add(campoPrezzo);

        form.add(new JLabel("Disponibile:"));
        this.campoDisponibile = new JCheckBox();
        form.add(campoDisponibile);

        JButton btnSalva = new JButton("Salva modifiche");
        btnSalva.addActionListener(e -> salvaModifiche());
        form.add(new JLabel());
        form.add(btnSalva);

        JButton btnElimina = new JButton("Elimina dal catalogo");
        btnElimina.addActionListener(e -> eliminaProdotto());
        form.add(new JLabel());
        form.add(btnElimina);

        JButton createProdottoButton = new JButton("Crea Prodotto");
        createProdottoButton.addActionListener(e -> {

            Object[] options = {"Singolo", "Menu", "Annulla"};

            int result = JOptionPane.showOptionDialog(this, "Che tipo di prodotto vuoi creare?", "Crea Prodotto",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[2]);

            this.controller.showCreateProdottoPanel(result);
        });

        form.add(createProdottoButton);

        JButton creaIngredienteButton = new JButton("Crea Ingrediente");
        creaIngredienteButton.addActionListener(e -> this.controller.userRequestCreateIngredientePanel()
        );

        form.add(creaIngredienteButton);

        var splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(table), form);
        splitPane.setResizeWeight(0.6);
        this.add(splitPane, BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna elenco");
        btnAggiorna.addActionListener(e -> controller.userRequestedCatalogo());
        this.add(btnAggiorna, BorderLayout.NORTH);
    }

    public void setController(final ControllerAdmin controller) {
        this.controller = controller;
    }

    public void mostraCatalogo(final List<Prodotto> catalogo) {
        tableModel.setRowCount(0);
        for (Prodotto p : catalogo) {
            tableModel.addRow(new Object[]{
                    p.getCodiceProdotto(), p.getNomeProdotto(),
                    p.isMenu() ? "Menu" : "Singolo",
                    p.getPrezzoOriginario(), p.isDisponibile()
            });
        }
    }

    private void caricaProdottoSelezionato() {

        int riga = table.getSelectedRow();

        if (riga == -1) {
            return;
        }

        campoCodice.setText((String) tableModel.getValueAt(riga, 0));
        campoNome.setText((String) tableModel.getValueAt(riga, 1));
        campoPrezzo.setText(String.valueOf(tableModel.getValueAt(riga, 3)));
        campoDisponibile.setSelected((Boolean) tableModel.getValueAt(riga, 4));
        campoDescrizione.setText("");
    }

    private void salvaModifiche() {
        String codice = campoCodice.getText();
        if (codice.isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un prodotto dalla tabella.");
            return;
        }
        try {
            float prezzo = Float.parseFloat(campoPrezzo.getText().trim());
            controller.userRequestedModifica(codice, campoNome.getText(), campoDescrizione.getText(),
                    prezzo, campoDisponibile.isSelected());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Il prezzo deve essere un numero valido (es. 6.50).",
                    "Formato non valido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminaProdotto() {
        String codice = campoCodice.getText();
        if (codice.isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un prodotto dalla tabella.");
            return;
        }
        int conferma = JOptionPane.showConfirmDialog(this,
                "Eliminare definitivamente questo prodotto dal catalogo?",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            controller.userRequestedEliminazione(codice);
        }
    }

    public boolean chiediSoftDelete(final String messaggio) {
        int scelta = JOptionPane.showConfirmDialog(this, messaggio,
                "Attenzione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return scelta == JOptionPane.YES_OPTION;
    }

    public void mostraMessaggio(final String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio);
    }

    public void mostraErrore(final String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }


}
