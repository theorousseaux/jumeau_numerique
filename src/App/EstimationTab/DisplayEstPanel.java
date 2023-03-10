package src.App.EstimationTab;

import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservedMeasurement;
import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class DisplayEstPanel extends JPanel {

    MainFrame parent;
    public DisplayEstPanel( MainFrame parent ){
        this.parent = parent;

    }

    public void update() {

        // Deleting object except title

        // Display of the measurements
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.WEST;

        List<String> measurementsList = this.parent.estimationController.model.estimationsList;

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String>();

        for (String line : measurementsList){
            elements.add(line);
        }
        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        JScrollPane satellites = new JScrollPane(liste);
        this.add(satellites, gc);

        // Display the number of observation
        /*
        JLabel nObs = new JLabel("Number of observations: " + String.valueOf ( this.parent.obserController.observerNetwork.countObservations(this.parent.simuController.model.getMeasurementsSetsList())));
        gc.gridy ++;
        gc.insets = new Insets(20, 10, 20, 10);
        this.add(nObs,gc);

         */
    }
}
