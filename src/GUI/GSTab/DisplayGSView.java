package src.GUI.GSTab;

import org.hipparchus.util.Precision;
import src.GUI.MainFrame;
import src.GroundStation.Station;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DisplayGSView extends JPanel {
    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );
    JScrollPane stations;

    public DisplayGSView ( MainFrame parent ) {
        this.parent = parent;
        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel tittle = new JLabel ( "Ground stations list" );
        tittle.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground ( Color.BLUE ); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add ( tittle , gc );

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.gridwidth = 1;
        ArrayList<String> elements = new ArrayList<String> ( );

        for (Station groundStation : parent.getGsController ( ).getModel ().getGroundStationList ()) {
            // Affichage de la liste des stations sol dans une fenêtre de dialogue
            String sb = groundStation.getName ( ) +
                    " : " +
                    "Latitude = " +
                    Precision.round ( groundStation.getLatitude ( ) * 180 / Math.PI , 2 ) +
                    ", " +
                    "Longitude = " +
                    Precision.round ( groundStation.getLongitude ( ) * 180 / Math.PI , 2 ) +
                    ", " +
                    "Altitude = " +
                    groundStation.getAltitude ( );

            elements.add ( sb );

        }
        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        stations = new JScrollPane ( liste );
        this.add ( stations , gc , 1 );
    }

    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 1; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    public void displayNewStation ( ) {
        removeAllExceptTop ( );
        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel tittle = new JLabel ( "Ground stations list" );
        tittle.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground ( Color.BLUE ); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add ( tittle , gc );

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.gridwidth = 1;
        ArrayList<String> elements = new ArrayList<String> ( );

        for (Station groundStation : parent.getGsController ( ).getModel ( ).getGroundStationList ( )) {
            // Affichage de la liste des stations sol dans une fenêtre de dialogue
            String sb = groundStation.getName ( ) +
                    " : " +
                    "Latitude = " +
                    Precision.round ( groundStation.getLatitude ( ) * 180 / Math.PI , 2 ) +
                    ", " +
                    "Longitude = " +
                    Precision.round ( groundStation.getLongitude ( ) * 180 / Math.PI , 2 ) +
                    ", " +
                    "Altitude = " +
                    groundStation.getAltitude ( );

            elements.add ( sb );
        }
        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        stations = new JScrollPane ( liste );
        this.add ( stations , gc , 1 );
    }
}
