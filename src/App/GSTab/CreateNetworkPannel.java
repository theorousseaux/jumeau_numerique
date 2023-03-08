package src.App.GSTab;

import src.App.MainFrame;
import src.App.WorldMapTab.WorldMapPanel;
import src.Kalman.Station;
import src.UseCase1_GSNetwork.GSNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateNetworkPannel extends JPanel {

    MainFrame parent;
    DisplayNetworkPannel displayNetworkPannel;

    public CreateNetworkPannel(MainFrame parent, DisplayNetworkPannel displayNetworkPannel, WorldMapPanel worldMapPanel) {

        this.parent = parent;
        this.displayNetworkPannel = displayNetworkPannel;
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Choose ground stations to create the network");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);

        for (Station station : parent.gsController.groundStationList) {
            JCheckBox checkBox = new JCheckBox(station.getName());
            gc.gridy ++;
            gc.anchor = GridBagConstraints.WEST;
            gc.insets = new Insets(5, 0, 0, 0); // Ajoute 5 pixels de padding en bas
            this.add(checkBox, gc);
        }

        JLabel label = new JLabel("Network name : ");
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridheight = 3;
        gc.anchor = GridBagConstraints.EAST;
        this.add(label, gc);

        JTextField textField = new JTextField(10);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridheight = 3;
        gc.anchor = GridBagConstraints.EAST;
        this.add(textField, gc);


        JButton button = new JButton("Create network");
        gc.gridx = 1;
        gc.gridy =3;
        gc.gridheight = GridBagConstraints.REMAINDER;
        gc.anchor = GridBagConstraints.EAST;
        this.add(button, gc);

        // Si le boutton add est cliqué, on crée un réseau avec les stations sélectionnées
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> stations = new ArrayList<>();
                for (Component component : CreateNetworkPannel.this.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            stations.add(checkBox.getText());
                        }
                    }
                }
                try {
                    parent.gsController.gsNetwork = new GSNetwork(textField.getText(), stations);
                    displayNetworkPannel.update();
                    displayNetworkPannel.repaint();
                    displayNetworkPannel.revalidate();

                    worldMapPanel.displayNewGS(parent);
                    worldMapPanel.repaint();
                    worldMapPanel.revalidate();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void displayNewGS(Station groundStation) {
        GridBagConstraints newGC = new GridBagConstraints();
        newGC.gridx = 0;
        newGC.gridy = parent.gsController.numberOfGS+1;

        JCheckBox checkBox = new JCheckBox(groundStation.getName());
        newGC.gridy ++;
        newGC.anchor = GridBagConstraints.WEST;
        newGC.insets = new Insets(5, 0, 0, 0); // Ajoute 5 pixels de padding en bas
        this.add(checkBox, newGC);
    }
}