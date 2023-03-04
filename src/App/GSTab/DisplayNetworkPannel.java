package src.App.GSTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;

public class DisplayNetworkPannel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();;

    public DisplayNetworkPannel(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Tittre
        JLabel tittle = new JLabel("Network used for the simulation");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);
    }

    public void removeAllExceptTop() {
        Component[] components = this.getComponents();
        for (int i = 1; i < components.length; i++) {
            this.remove(components[i]);
        }
    }


    public void update() {

        // Suppression des éléments sauf le titre
        removeAllExceptTop();
        // Affichage du réseau
        GridBagConstraints gc = new GridBagConstraints(); // Réinitialisation de gc pour chaque nouvel élément
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        JLabel networkNameLabel = new JLabel("Network name :");
        this.add(networkNameLabel, gc);

        gc = new GridBagConstraints(); // Réinitialisation de gc pour chaque nouvel élément
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        JLabel networkName = new JLabel(parent.gsNetwork.getName());
        this.add(networkName, gc);

        gc = new GridBagConstraints(); // Réinitialisation de gc pour chaque nouvel élément
        gc.gridx = 0;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.PAGE_START;
        JLabel numberOfStationLabel = new JLabel("Number of ground stations :");
        this.add(numberOfStationLabel, gc);

        gc = new GridBagConstraints(); // Réinitialisation de gc pour chaque nouvel élément
        gc.gridx = 1;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.PAGE_START;
        JLabel numberOfStation = new JLabel(String.valueOf(parent.gsNetwork.getNetwork().size()));
        this.add(numberOfStation, gc);
    }

}
