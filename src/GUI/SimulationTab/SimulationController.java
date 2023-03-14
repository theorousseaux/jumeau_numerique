package src.GUI.SimulationTab;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.SpacecraftState;
import src.GUI.MainFrame;
import src.Observer.Observation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

public class SimulationController {

    public SimulationView view;
    private SimulationModel model = new SimulationModel ( );

    public SimulationModel getModel ( ) {
        return model;
    }

    public void setModel ( SimulationModel model ) {
        this.model = model;
    }

    @Override
    public String toString ( ) {
        return "SimulationController [model=" + model + ", view=" + view + "]";
    }

    public void loadSimulation ( MainFrame parent ) throws IllegalArgumentException {
        model.setSimulationParameters ( parent.getParamController ( ).model );

        model.setObserverNetwork ( parent.getObserController ( ).getModel ( ).getObserverNetwork ( ) );


        model.setSatellites ( parent.getSatController ( ).getModel ().getSatellitesList ( ) );
        model.setSatellitesNames ( parent.getSatController ( ).getModel ().getIdSatellitesList ( ) );
        model.setObservableSatellites ( parent.getSatController ( ).getModel ().getObservableSatellitesList ( ) );
        System.out.println ( "Parameters : " );
        System.out.println ( parent.getParamController ( ).model.toString ( ) );
        System.out.println ( "Telescopes : " );
        model.getObserverNetwork ( ).display ( );
        System.out.println ( "Satellites : " );
        System.out.println ( model.getSatellitesNames ( ).toString ( ) );

    }

    public void runSimulation ( MainFrame parent ) {
        model.setObservations ( new Observation ( model.getObserverNetwork ( ).getTelescopes ( ) , model.getObservableSatellites ( ) , model.getSatellites ( ) , model.getSimulationParameters ( ).getStartDate ( ) , model.getSimulationParameters ( ).getEndDate ( ) ) );
        model.setMeasurementsSetsList ( model.getObservations ( ).measure ( true ) );
        Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> pair = model.getMeasurementsSetsList ( );
        List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList = pair.getFirst ( );
        HashMap<ObservedMeasurement, SpacecraftState> trueStatesList = pair.getSecond ( );

        System.out.println ( trueStatesList.size ( ) );
        System.out.println ( measurementsSetsList.size ( ) );


        System.out.println ( "Simulation done" );
    }


}
