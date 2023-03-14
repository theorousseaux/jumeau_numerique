package src.App.ObserverTab;

import src.App.MainFrame;
import src.App.WorldMapTab.WorldMapPanel;

import javax.swing.*;
import java.awt.*;

public class ObserverPannel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints ( );

    public ObserverPannel ( MainFrame parent ) {

        this.parent = parent;

        setLayout ( new GridBagLayout ( ) );

        // Affichage des stations sol
        //DisplayGSPannel displayGSPannel = new DisplayGSPannel(parent);

        // Affichage du réseau
        DisplayObserverPannel displayObserverPannel = new DisplayObserverPannel ( parent );
        DisplayNetworkPanel displayNetworkPannel = new DisplayNetworkPanel ( parent );
        WorldMapPanel worldMapPanel = (WorldMapPanel) parent.getGlobePanel ( );
        // Choix des stations sol pour le réseau
        //CreateNetworkPannel createNetworkPannel = new CreateNetworkPannel(parent, displayNetworkPannel);
        CreateNetworkPanel createNetworkPannel = new CreateNetworkPanel ( parent , displayNetworkPannel , worldMapPanel );

        // Affichage du formulaire d'ajout d'une station sol
        NewObserverPannel newObserverPannel = new NewObserverPannel ( parent , displayObserverPannel , createNetworkPannel );


        // Choix des stations sol pour le réseau

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add ( displayObserverPannel , gc );

        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( newObserverPannel , gc );

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( createNetworkPannel , gc );

        // Affichage du réseau
        gc.gridx = 0;
        gc.gridy++;
        gc.gridheight = GridBagConstraints.REMAINDER;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( displayNetworkPannel , gc );

    }
}