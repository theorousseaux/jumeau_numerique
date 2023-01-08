package src.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NewGroundStationDialog extends JDialog {

    public NewGroundStationDialog(JFrame parent) {
        super(parent, "Add Ground Station", true);

        // Création des champs de saisie
        JTextField longitudeTextField = new JTextField(10);
        JTextField latitudeTextField = new JTextField(10);
        JTextField altitudeTextField = new JTextField(10);
        JTextField nameTextField = new JTextField(10);

        String[] typeGS ={"Radar","Laser"};
        JComboBox<String> cbType = new JComboBox<>(typeGS);


        // Création du formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Longitude: "), gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(longitudeTextField, gc);
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Latitude: "), gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(latitudeTextField, gc);
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Altitude: "), gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(altitudeTextField, gc);
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Name: "), gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(nameTextField, gc);
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Type: "), gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(cbType, gc);

        // Création du bouton "Add"
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des données saisies
                double longitude = Double.parseDouble(longitudeTextField.getText());
                double latitude = Double.parseDouble(latitudeTextField.getText());
                double altitude = Double.parseDouble(altitudeTextField.getText());
                String name = nameTextField.getText();
                String type = Objects.requireNonNull(cbType.getSelectedItem()).toString();

                // Création de l'objet GroundStation avec les données saisies
                src.App.GroundStation groundStation = new src.App.GroundStation(longitude, latitude, altitude, name, type);

                // Ajout de la station sol à la liste de la fenêtre principale
                MainFrame mainFrame = (MainFrame) getParent();
                mainFrame.addGroundStation(groundStation);

                // Fermeture de la fenêtre de dialogue
                setVisible(false);
            }
        });

        // Création du bouton "Cancel"
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fermeture de la fenêtre de dialogue
                setVisible(false);
            }
        });

        // Création du panel des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Ajout du formulaire et des boutons à la fenêtre de dialogue
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configuration de la fenêtre de dialogue
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
}