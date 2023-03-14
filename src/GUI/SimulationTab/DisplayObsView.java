package src.GUI.SimulationTab;

import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservedMeasurement;
import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * This class displays all the measurements made by the network
 */
public class DisplayObsView extends JPanel {

    // The parent frame (i.e. the app)
    final MainFrame parent;

    // The display manager
    final GridBagConstraints gc = new GridBagConstraints ( );

    /**
     * Default creator
     *
     * @param parent the parent frame
     */
    public DisplayObsView ( MainFrame parent ) {

        this.parent = parent;

        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel title = new JLabel ( "Measurements" );
        title.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) );
        title.setForeground ( Color.BLUE );

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets ( 20 , 10 , 20 , 10 );
        this.add ( title , gc );
    }

    /**
     * Clears the page except for the title
     */
    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 1; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }


    /**
     * Method to update the display
     */
    public void update ( ) {

        // Deleting object except title
        removeAllExceptTop ( );

        // Display of the measurements
        GridBagConstraints gc = new GridBagConstraints ( );
        gc.gridx = 0;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.WEST;

        List<SortedSet<ObservedMeasurement<?>>> measurementsList = this.parent.getSimuController ( ).getModel ( ).getMeasurementsSetsList ( ).getFirst ( );

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String> ( );

        for (SortedSet<ObservedMeasurement<?>> set : measurementsList) {
            for (ObservedMeasurement<?> obs : set) {
                elements.add ( "Date: " + obs.getDate ( ).toString ( ) + "; Station: " + ((AngularAzEl) obs).getStation ( ).getBaseFrame ( ).getName ( ) + "; Satellite: " + parent.getSimuController ( ).getModel ( ).getSatellitesNames ( ).get ( obs.getSatellites ( ).get ( 0 ).getPropagatorIndex ( ) ) );
            }
        }
        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        JScrollPane satellites = new JScrollPane ( liste );
        this.add ( satellites , gc );

        // Display the number of observation
    }
}