package project.db.view.Client;

import java.util.Map;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;

import project.db.controller.ControllerClientPanel;

import javax.swing.JButton;

public class DomicilioPanel extends JDialog {

    private Map<String, TextField> campiDomicilio = new HashMap<>();
    private JButton confermaButton = new JButton("Conferma  Domicilio e ordina");
    private ControllerClientPanel controllerClientPanel;

    public DomicilioPanel(ControllerClientPanel controllerClientPanel) {

        this.controllerClientPanel = controllerClientPanel;

        this.setSize(700, 700);
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        this.setTitle("Inserisci il tuo domicilio");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        c.gridx = 0;
        c.gridy = 0;
        JLabel viaLabel = new JLabel("Via:");
        this.add(viaLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        TextField viaField = new TextField(15);
        this.add(viaField, c);
        campiDomicilio.put("via", viaField);

        c.gridx = 0;
        c.gridy = 1;
        JLabel cittaLabel = new JLabel("Città:");
        this.add(cittaLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        TextField cittaField = new TextField(15);
        this.add(cittaField, c);
        campiDomicilio.put("citta", cittaField);

        c.gridx = 0;
        c.gridy = 2;
        JLabel civicoLabel = new JLabel("Civico:");
        this.add(civicoLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        TextField civicoField = new TextField(15);
        this.add(civicoField, c);
        campiDomicilio.put("civico", civicoField);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        this.add(confermaButton, c);

        this.confermaButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controllerClientPanel.userCreateOrdine();
            }

        });

    }

    public Map<String, String> getCampiDomicilio() {
        Map<String, String> datiDomicilio = new HashMap<>();
        for (Map.Entry<String, TextField> entry : campiDomicilio.entrySet()) {
            System.out.println("Campo: " + entry.getKey() + ", Valore: " + entry.getValue().getText());
            datiDomicilio.put(entry.getKey(), entry.getValue().getText());
        }
        return datiDomicilio;
    }
}
