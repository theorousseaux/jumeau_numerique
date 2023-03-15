package src.GUI.ObserverTab;

import src.GUI.MainFrame;
import src.GUI.WorldMapTab.WorldMapView;

import javax.swing.*;
import java.awt.*;

public class ObserverView extends JPanel {

    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );
    private NewObserverView newObserverView;
    public ObserverView ( MainFrame parent ) {

        this.parent = parent;

        setLayout ( new GridBagLayout ( ) );

        // Affichage des stations sol
        //DisplayGSPannel displayGSPannel = new DisplayGSPannel(parent);

        // Affichage du réseau
        DisplayObserverView displayObserverView = new DisplayObserverView ( parent );
        DisplayNetworkView displayNetworkPannel = new DisplayNetworkView ( parent );
        WorldMapView worldMapView = (WorldMapView) parent.getGlobePanel ( );
        // Choix des stations sol pour le réseau
        //CreateNetworkPannel createNetworkPannel = new CreateNetworkPannel(parent, displayNetworkPannel);
        CreateNetworkView createNetworkPannel = new CreateNetworkView ( parent , displayNetworkPannel , worldMapView );

        // Affichage du formulaire d'ajout d'une station sol
        this.setNewObserverView (new NewObserverView ( parent , displayObserverView , createNetworkPannel ));


        // Choix des stations sol pour le réseau

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add ( displayObserverView , gc );

        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( this.getNewObserverView () , gc );

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


    public NewObserverView getNewObserverView ( ) {
        return newObserverView;
    }

    public void setNewObserverView ( NewObserverView newObserverView ) {
        this.newObserverView = newObserverView;
    }
}