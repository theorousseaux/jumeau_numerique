package src.App.ObserverTab;

import src.App.GSTab.GSController;
import src.App.MainFrame;
import src.Kalman.Station;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewObserverPannel extends JPanel {

    MainFrame parent;
    GSController gsController;
    private JComboBox<String> cbType;
    private JPanel formPanel;

    public NewObserverPannel(MainFrame parent, GSController gsController) {

        this.parent = parent;
        this.gsController = gsController;

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
                updateFormPanel();
            }
        });
        add(cbType, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(10, 0, 0, 0);

        formPanel = new JPanel();
        updateFormPanel();
        add(formPanel, gc);
    }

    private void updateFormPanel() {
        String selectedType = (String) cbType.getSelectedItem();
        assert selectedType != null;
        if (selectedType.equals("Telescope")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(9, 2));

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
            JComboBox<Station> stationComboBox = new JComboBox<>();
            for(Station s : gsController.groundStationList){
                stationComboBox.addItem(s);
            }
            formPanel.add(stationComboBox);

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
}
