package src.App.ObserverTab;

import src.App.GSTab.GSController;
import src.App.MainFrame;
import src.Kalman.Radar;
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

    public NewObserverPannel(MainFrame parent, DisplayObserverPannel displayObserverPannel, CreateNetworkPanel createNetworkPannel) {

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
                updateFormPanel(displayObserverPannel, createNetworkPannel);
            }
        });
        add(cbType, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(10, 0, 0, 0);

        formPanel = new JPanel();
        updateFormPanel(displayObserverPannel, createNetworkPannel);
        add(formPanel, gc);
    }

    private void updateFormPanel(DisplayObserverPannel displayObserverPannel, CreateNetworkPanel createNetworkPannel) {
        String selectedType = (String) cbType.getSelectedItem();
        assert selectedType != null;
        if (selectedType.equals("Telescope")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(11, 2));

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

            JLabel geoLabel = new JLabel("GEO :");
            formPanel.add(geoLabel);
            JCheckBox geoCheckBox = new JCheckBox();
            formPanel.add(geoCheckBox);

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

                    boolean geo = geoCheckBox.isSelected();

                    String ID = station.getName() + ":telescope:" + parent.obserController.getNumberOfTelescopePerStation(station);
                    TelescopeAzEl newTelescope = new TelescopeAzEl(ID, mean, angularIncertitude, Double.parseDouble(elevation), Double.parseDouble(angularoV), Double.parseDouble(stepMeasure), Double.parseDouble(breakTime), station, geo);

                    // Ajout du telescope au controller
                    parent.obserController.addTelescope(newTelescope);

                    // update
                    parent.obserController.writeObserverFile.writeObserverTelescope(newTelescope);

                    // Mise à jour du panneau d'affichage des stations sol
                    displayObserverPannel.displayNewObserver();
                    displayObserverPannel.repaint();
                    displayObserverPannel.revalidate();

                    // Mise à jour du panneau de choix des telescopes
                    createNetworkPannel.update();
                }
            });

            formPanel.revalidate();
            formPanel.repaint();
        } else if (selectedType.equals("Radar")) {
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

            JLabel angularoVLabel = new JLabel("Angular field of view :");
            formPanel.add(angularoVLabel);
            JTextField angularoVField = new JTextField();
            formPanel.add(angularoVField);

            JLabel stepMeasureLabel = new JLabel("Step measure :");
            formPanel.add(stepMeasureLabel);
            JTextField stepMeasureField = new JTextField();
            formPanel.add(stepMeasureField);

            JLabel stationLabel = new JLabel("Station :");
            formPanel.add(stationLabel);
            this.stationComboBox = new JComboBox<>();
            for(Station s : parent.gsController.groundStationList){
                stationComboBox.addItem(s);
            }
            formPanel.add(stationComboBox);

            JButton addRadarButton = new JButton("Add radar");
            formPanel.add(addRadarButton);

            addRadarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String mean1 = mean1Field.getText();
                    String mean2 = mean2Field.getText();
                    double[] mean = {Double.parseDouble(mean1), Double.parseDouble(mean2)};

                    String angularIncertitude1 = angularIncertitude1Field.getText();
                    String angularIncertitude2 = angularIncertitude2Field.getText();
                    double[] angularIncertitude = {Double.parseDouble(angularIncertitude1), Double.parseDouble(angularIncertitude2)};

                    String angularoV = angularoVField.getText();
                    String stepMeasure = stepMeasureField.getText();
                    Station station = (Station) stationComboBox.getSelectedItem();
                    assert station != null;

                    String ID = station.getName() + ":radar:" + parent.obserController.getNumberOfRadarPerStation(station);
                    Radar newRadar = new Radar(ID, mean, angularIncertitude, Double.parseDouble(angularoV), Double.parseDouble(stepMeasure), station);

                    // Ajout du telescope au controller
                    parent.obserController.addRadar(newRadar);

                    // update
                    parent.obserController.writeObserverFile.writeObserverRadar(newRadar);

                    // Mise à jour du panneau d'affichage
                    displayObserverPannel.displayNewObserver();
                    displayObserverPannel.repaint();
                    displayObserverPannel.revalidate();

                    // Mise à jour du panneau de choix des radars
                    createNetworkPannel.update();
                }
            });

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
