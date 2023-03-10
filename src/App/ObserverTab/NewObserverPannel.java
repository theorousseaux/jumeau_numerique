package src.App.ObserverTab;

import src.App.GSTab.GSController;
import src.App.MainFrame;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NewObserverPannel extends JPanel {

    MainFrame parent;
    public JComboBox<Station> stationComboBox;
    private JComboBox<String> cbType;
    private JPanel formPanel;

    public NewObserverPannel(MainFrame parent, DisplayObserverPannel displayObserverPannel) {

        this.parent = parent;

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;

        // Titre
        JLabel addGSLabel = new JLabel("Add a new observer");
        addGSLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        addGSLabel.setForeground(Color.BLUE); // Définit la couleur du texte en bleu
        add(addGSLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(10, 0, 0, 0);
        JLabel typeLabel = new JLabel("Type: ");
        add(typeLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(10, 0, 0, 0);
        String[] typeGS ={"Telescope", "Radar"};
        cbType = new JComboBox<>(typeGS);
        cbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFormPanel(displayObserverPannel);
            }
        });
        add(cbType, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(10, 0, 0, 0);

        formPanel = new JPanel();
        updateFormPanel(displayObserverPannel);
        add(formPanel, gc);
    }

    private void updateFormPanel(DisplayObserverPannel displayObserverPannel) {
        String selectedType = (String) cbType.getSelectedItem();
        assert selectedType != null;
        if (selectedType.equals("Telescope")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(10, 2));

            JLabel mean1Label = new JLabel("Mean 1 :");
            formPanel.add(mean1Label);
            JTextField mean1Field = new JTextField();
            formPanel.add(mean1Field);

            JLabel mean2Label = new JLabel("Mean 2 :");
            formPanel.add(mean2Label);
            JTextField mean2Field = new JTextField();
            formPanel.add(mean2Field);

            JLabel angularIncertitude1Label = new JLabel("Angular incertitude 1 :");
            formPanel.add(angularIncertitude1Label);
            JTextField angularIncertitude1Field = new JTextField();
            formPanel.add(angularIncertitude1Field);

            JLabel angularIncertitude2Label = new JLabel("Angular incertitude 2 :");
            formPanel.add(angularIncertitude2Label);
            JTextField angularIncertitude2Field = new JTextField();
            formPanel.add(angularIncertitude2Field);

            JLabel elevationLabel = new JLabel("Elevation limit :");
            formPanel.add(elevationLabel);
            JTextField elevationField = new JTextField();
            formPanel.add(elevationField);

            JLabel angularoVLabel = new JLabel("Angular field of view :");
            formPanel.add(angularoVLabel);
            JTextField angularoVField = new JTextField();
            formPanel.add(angularoVField);

            JLabel stepMeasureLabel = new JLabel("Step measure :");
            formPanel.add(stepMeasureLabel);
            JTextField stepMeasureField = new JTextField();
            formPanel.add(stepMeasureField);

            JLabel breakTimeLabel = new JLabel("Break time :");
            formPanel.add(breakTimeLabel);
            JTextField breakTimeField = new JTextField();
            formPanel.add(breakTimeField);

            JLabel stationLabel = new JLabel("Station :");
            formPanel.add(stationLabel);
            this.stationComboBox = new JComboBox<>();
            for(Station s : parent.gsController.groundStationList){
                stationComboBox.addItem(s);
            }
            formPanel.add(stationComboBox);

            JButton addTelescopeButton = new JButton("Add telescope");
            formPanel.add(addTelescopeButton);

            addTelescopeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String mean1 = mean1Field.getText();
                    String mean2 = mean2Field.getText();
                    double[] mean = {Double.parseDouble(mean1), Double.parseDouble(mean2)};

                    String angularIncertitude1 = angularIncertitude1Field.getText();
                    String angularIncertitude2 = angularIncertitude2Field.getText();
                    double[] angularIncertitude = {Double.parseDouble(angularIncertitude1), Double.parseDouble(angularIncertitude2)};

                    String elevation = elevationField.getText();
                    String angularoV = angularoVField.getText();
                    String stepMeasure = stepMeasureField.getText();
                    String breakTime = breakTimeField.getText();
                    Station station = (Station) stationComboBox.getSelectedItem();
                    assert station != null;
                    String ID = station.getName() + "Telescope";
                    TelescopeAzEl newTelescope = new TelescopeAzEl(ID, mean, angularIncertitude, Double.parseDouble(elevation), Double.parseDouble(angularoV), Double.parseDouble(stepMeasure), Double.parseDouble(breakTime), station);

                    // Ajout du telescope au controller
                    parent.obserController.addTelescope(newTelescope);

                    // update
                    parent.obserController.writeObserverFile.writeObserverTelescope(newTelescope);

                    // Mise à jour du panneau d'affichage des stations sol
                    displayObserverPannel.displayNewTelescope();
                    displayObserverPannel.repaint();
                    displayObserverPannel.revalidate();

                    // Mise à jour du panneau de choix des telescopes

                }
            });

            formPanel.revalidate();
            formPanel.repaint();
        } else if (selectedType.equals("Radar")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(2, 2));

            JLabel radarLabel = new JLabel("Radar:");
            JTextField radarField = new JTextField();
            JLabel frequencyLabel = new JLabel("Frequency:");
            JTextField frequencyField = new JTextField();

            formPanel.add(radarLabel);
            formPanel.add(radarField);
            formPanel.add(frequencyLabel);
            formPanel.add(frequencyField);

            formPanel.revalidate();
            formPanel.repaint();
        }
    }

    public void updateStationComboBox() {
        stationComboBox.removeAllItems();
        for (Station s : parent.gsController.groundStationList) {
            stationComboBox.addItem(s);
        }
    }
}
