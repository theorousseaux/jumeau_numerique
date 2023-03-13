package src.App.EstimationTab;

import org.orekit.orbits.KeplerianOrbit;
import src.App.MainFrame;
import src.Kalman.constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DisplayTruePanel extends JPanel {

    MainFrame parent;

    public DisplayTruePanel ( MainFrame parent ) {
        this.parent = parent;

    }

    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 0; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    public void update ( ) {
        removeAllExceptTop ( );
        // Display of the measurements
        GridBagConstraints gc = new GridBagConstraints ( );
        gc.gridx = 0;
        gc.gridy = 0;

        gc.gridy++;
        ArrayList<String> elements2 = new ArrayList<String> ( );
        int j = 0;
        for (String satname : parent.simuController.model.getSatellitesNames ( )) {
            if (parent.estimationController.model.getObservedSat ( ).contains ( satname )) {
                KeplerianOrbit nOrbit = (KeplerianOrbit) parent.estimationController.model.getPropagators ( ).get ( j ).getInitialState ( ).getOrbit ( );
                double[] param = {nOrbit.getA ( ) , nOrbit.getE ( ) , Math.IEEEremainder ( nOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};


                elements2.add ( "Sat " + satname + "    : [" + Arrays.toString ( param ) );
            }
            j++;
        }
        String[] arr2 = {};
        arr2 = elements2.toArray ( arr2 );
        JList<String> liste2 = new JList<> ( arr2 );
        JScrollPane satellites2 = new JScrollPane ( liste2 );
        this.add ( satellites2 , gc );
    }
}
