package src.GUI.GSTab;

import src.GUI.MainFrame;
import src.GUI.WorldMapTab.WorldMapView;

import javax.swing.*;
import java.awt.*;

public class GSView extends JPanel {

    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );

    public GSView ( MainFrame parent ) {

        this.parent = parent;
        WorldMapView worldMapView = (WorldMapView) parent.getGlobePanel ( );
        setLayout ( new GridBagLayout ( ) );

        // Affichage des stations sol
        DisplayGSView displayGSView = new DisplayGSView ( parent );

        // Affichage du formulaire d'ajout d'une station sol
        NewGSView newGSView = new NewGSView ( parent , displayGSView , worldMapView );

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add ( newGSView , gc );

        gc.gridx++;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add ( displayGSView , gc );

        ImageIcon imageIcon = new ImageIcon ( "src/GUI/img/station_sol_icon.png" ); // load the image to a imageIcon
        Image image = imageIcon.getImage ( ); // transform it
        Image newimg = image.getScaledInstance ( 60 , 80 , java.awt.Image.SCALE_SMOOTH ); // scale it the smooth way
        imageIcon = new ImageIcon ( newimg );  // transform it back

        // Ajout de l'image
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.gridheight = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( new JLabel ( imageIcon ) , gc );
    }
}