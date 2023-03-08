package src.App.GSTab;

import javax.swing.JPanel;

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

        // Affichage du réseau
        DisplayNetworkPannel displayNetworkPannel = new DisplayNetworkPannel(parent);

        // Choix des stations sol pour le réseau
        CreateNetworkPannel createNetworkPannel = new CreateNetworkPannel(parent, displayNetworkPannel, worldMapPanel);
        // Affichage du formulaire d'ajout d'une station sol
        NewGSPannel newGSPannel = new NewGSPannel(parent, displayGSPannel, createNetworkPannel, worldMapPanel);

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

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(createNetworkPannel, gc);

        // Affichage du réseau
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(displayNetworkPannel, gc);

    }
}