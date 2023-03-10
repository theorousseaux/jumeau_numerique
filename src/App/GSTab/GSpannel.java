package src.App.GSTab;

import javax.swing.*;

import src.App.MainFrame;
import src.App.WorldMapTab.WorldMapPanel;

import java.awt.*;

public class GSpannel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();

    public GSpannel(MainFrame parent) {

        this.parent = parent;
        WorldMapPanel worldMapPanel = (WorldMapPanel) parent.globePanel;
        setLayout(new GridBagLayout());

        // Affichage des stations sol
        DisplayGSPannel displayGSPannel = new DisplayGSPannel(parent);

        // Affichage du formulaire d'ajout d'une station sol
        NewGSPannel newGSPannel = new NewGSPannel(parent, displayGSPannel, worldMapPanel);

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(newGSPannel, gc);

        gc.gridx ++;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(displayGSPannel, gc);

        ImageIcon imageIcon = new ImageIcon("src/App/img/station_sol_icon.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back

        // Ajout de l'image
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.gridheight = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        this.add(new JLabel(imageIcon), gc);
    }
}