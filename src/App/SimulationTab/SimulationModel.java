package src.App.SimulationTab;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import src.App.ParametersTab.Parameters;
import src.Kalman.Observation;
import src.UseCase1_GSNetwork.ObserverNetwork;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

public class SimulationModel {
    ObserverNetwork observerNetwork;
    Parameters simulationParameters;
    List<Propagator> satellites;
    Observation observations;
    Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> measurementsSetsList;
    List<ObservableSatellite> observableSatellites;

    List<String> satellitesNames;

    public List<ObservableSatellite> getObservableSatellites ( ) {
        return observableSatellites;
    }

    public void setObservableSatellites ( List<ObservableSatellite> observableSatellites ) {
        this.observableSatellites = observableSatellites;
    }

    public List<String> getSatellitesNames ( ) {
        return satellitesNames;
    }


    public void setSatellitesNames ( List<String> satellitesNames ) {
        this.satellitesNames = satellitesNames;
    }


    @Override
    public String toString ( ) {
        return "simulationModel [observerNetwork=" + observerNetwork + ", simulationParameters="
                + simulationParameters + ", satellites=" + satellites + ", observations=" + observations
                + ", measurementsSetsList=" + measurementsSetsList + "]";
    }


    public ObserverNetwork getObserverNetwork ( ) {
        return observerNetwork;
    }

    public void setObserverNetwork ( ObserverNetwork observerNetwork ) {
        this.observerNetwork = observerNetwork;
    }

    public Parameters getSimulationParameters ( ) {
        return simulationParameters;
    }

    public void setSimulationParameters ( Parameters simulationParameters ) {
        this.simulationParameters = simulationParameters;
    }

    public List<Propagator> getSatellites ( ) {
        return satellites;
    }

    public void setSatellites ( List<Propagator> satellites ) {
        this.satellites = satellites;
    }

    public Observation getObservations ( ) {
        return observations;
    }

    public void setObservations ( Observation observations ) {
        this.observations = observations;
    }

    public Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> getMeasurementsSetsList ( ) {
        return measurementsSetsList;
    }

    public void setMeasurementsSetsList ( Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> measurementsSetsList ) {
        this.measurementsSetsList = measurementsSetsList;
    }
}