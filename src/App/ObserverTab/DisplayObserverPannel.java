package src.App.ObserverTab;

import src.App.MainFrame;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DisplayObserverPannel extends JPanel {
    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints ( );

    public DisplayObserverPannel ( MainFrame parent ) {
        this.parent = parent;
        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel tittle = new JLabel ( "Observers list" );
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

        for (TelescopeAzEl telescopeAzEl : parent.obserController.telescopeAzElList) {
            // Affichage de la liste des telescopes
            String sb = telescopeAzEl.getID ( ) +
                    " : " +
                    "Mean :" +
                    Arrays.toString ( telescopeAzEl.getMean ( ) ) +
                    ", " +
                    "Angular incertitude :" +
                    Arrays.toString ( telescopeAzEl.getAngularIncertitude ( ) ) +
                    ", " +
                    "eleVationLimit :" +
                    telescopeAzEl.getElevationLimit ( ) +
                    ", " +
                    "angularFoV :" +
                    telescopeAzEl.getAngularFoV ( ) +
                    ", " +
                    "stepMeasure :" +
                    telescopeAzEl.getStepMeasure ( ) +
                    ", " +
                    "breakTime :" +
                    telescopeAzEl.getBreakTime ( ) +
                    ", " +
                    "GEO :" +
                    telescopeAzEl.getGEO ( );
            elements.add ( sb );
        }

        for (Radar radar : parent.obserController.radarList) {
            // Affichage de la liste des telescopes
            String sb = radar.getID ( ) +
                    " : " +
                    "Mean :" +
                    Arrays.toString ( radar.getMean ( ) ) +
                    ", " +
                    "Angular incertitude :" +
                    Arrays.toString ( radar.getAngularIncertitude ( ) ) +
                    ", " +
                    "angularFoV :" +
                    radar.getAngularFoV ( ) +
                    ", " +
                    "stepMeasure :" +
                    radar.getStepMeasure ( );
            elements.add ( sb );
        }

        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        JScrollPane scrollPane = new JScrollPane ( liste );
        this.add ( scrollPane , gc , 1 );
    }

    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 1; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    public void displayNewObserver ( ) {
        removeAllExceptTop ( );
        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel tittle = new JLabel ( "Observers list" );
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

        for (TelescopeAzEl telescopeAzEl : parent.obserController.telescopeAzElList) {
            // Affichage de la liste des telescopes
            String sb = telescopeAzEl.getID ( ) +
                    " : " +
                    "Mean :" +
                    Arrays.toString ( telescopeAzEl.getMean ( ) ) +
                    ", " +
                    "Angular incertitude :" +
                    Arrays.toString ( telescopeAzEl.getAngularIncertitude ( ) ) +
                    ", " +
                    "eleVationLimit :" +
                    telescopeAzEl.getElevationLimit ( ) +
                    ", " +
                    "angularFoV :" +
                    telescopeAzEl.getAngularFoV ( ) +
                    ", " +
                    "stepMeasure :" +
                    telescopeAzEl.getStepMeasure ( ) +
                    ", " +
                    "breakTime :" +
                    telescopeAzEl.getBreakTime ( ) +
                    ", " +
                    "GEO :" +
                    telescopeAzEl.getGEO ( );
            elements.add ( sb );
        }

        for (Radar radar : parent.obserController.radarList) {
            // Affichage de la liste des telescopes
            String sb = radar.getID ( ) +
                    " : " +
                    "Mean :" +
                    Arrays.toString ( radar.getMean ( ) ) +
                    ", " +
                    "Angular incertitude :" +
                    Arrays.toString ( radar.getAngularIncertitude ( ) ) +
                    ", " +
                    "angularFoV :" +
                    radar.getAngularFoV ( ) +
                    ", " +
                    "stepMeasure :" +
                    radar.getStepMeasure ( );
            elements.add ( sb );
        }

        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        JScrollPane scrollPane = new JScrollPane ( liste );
        this.add ( scrollPane , gc , 1 );
    }
}
