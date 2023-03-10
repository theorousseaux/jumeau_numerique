package src.App.ObserverTab;

import javax.swing.JPanel;

import src.App.MainFrame;

import java.awt.*;

public class ObserverPannel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();

    public ObserverPannel(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Affichage des stations sol
        //DisplayGSPannel displayGSPannel = new DisplayGSPannel(parent);

        // Affichage du réseau
        DisplayObserverPannel displayObserverPannel = new DisplayObserverPannel(parent);

        // Choix des stations sol pour le réseau
        //CreateNetworkPannel createNetworkPannel = new CreateNetworkPannel(parent, displayNetworkPannel);

        // Affichage du formulaire d'ajout d'une station sol
        NewObserverPannel newObserverPannel = new NewObserverPannel(parent, displayObserverPannel);

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(displayObserverPannel, gc);

        gc.gridy ++;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(newObserverPannel, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.PAGE_START;
        //this.add(createNetworkPannel, gc);

        // Affichage du réseau
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        //this.add(displayNetworkPannel, gc);

    }
}