package src.App.EstimationTab;

import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservedMeasurement;
import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class DisplayEstPanel {


    public DisplayEstPanel( MainFrame parent ){
        gc.gridx = 0;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.WEST;

        List<SortedSet<ObservedMeasurement<?>>> measurementsList = this.parent.simuController.model.getMeasurementsSetsList();

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String>();

        for (SortedSet<ObservedMeasurement<?>> set : measurementsList){
            for (ObservedMeasurement<?> obs : set){
                elements.add("Date: " + obs.getDate().toString() + "; Station: " + ((AngularAzEl) obs).getStation().getBaseFrame().getName() + "; Satellite: " + ((AngularAzEl) obs).getSatellites().get(0).getPropagatorIndex());
            }
        }
        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        JScrollPane satellites = new JScrollPane(liste);
        this.add(satellites, gc);
    }
}
