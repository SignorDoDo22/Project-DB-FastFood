package project.db.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SceltaLogin extends JPanel {

    private JButton adminButton;
    private JButton riderButton;
    private JButton clientButton;
    private MainView mainView;

    public SceltaLogin(final MainView mainView){

        this.setVisible(true);
        this.mainView = mainView;
        this.adminButton = new JButton("ADMIN");
        this.clientButton = new JButton("CLIENT");
        this.riderButton = new JButton("RIDER");

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;


        gbc.gridx = 0; gbc.gridy = 0;
        this.add(adminButton, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        this.add(clientButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(riderButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;


        this.clientButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.requestChangePanel("loginClient", "scelta");
            }

        });

        this.riderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.requestChangePanel("loginRider", "scelta");
            }

        });

        this.adminButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.requestChangePanel("admin", "scelta");
            }

        });

    }

}
