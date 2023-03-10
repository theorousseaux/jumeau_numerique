package src.App.SimulationTab;

import java.util.List;
import java.util.SortedSet;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;

import src.App.ParametersTab.Parameters;
import src.Kalman.Observation;
import src.UseCase1_GSNetwork.ObserverNetwork;

public class SimulationModel {
    ObserverNetwork observerNetwork;
    Parameters simulationParameters;
    List<Propagator> satellites;
    Observation observations;
    List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList;
    List<ObservableSatellite> satellitesNames;
    
    public List<ObservableSatellite> getSatellitesNames() {
        return satellitesNames;
    }


    public void setSatellitesNames(List<ObservableSatellite> satellitesNames) {
        this.satellitesNames = satellitesNames;
    }


    @Override
    public String toString() {
        return "simulationModel [observerNetwork=" + observerNetwork + ", simulationParameters="
                + simulationParameters + ", satellites=" + satellites + ", observations=" + observations
                + ", measurementsSetsList=" + measurementsSetsList + "]";
    }
    
    
    public ObserverNetwork getObserverNetwork() {
        return observerNetwork;
    }
    
    public void setObserverNetwork(ObserverNetwork observerNetwork) {
        this.observerNetwork = observerNetwork;
    }
    public Parameters getSimulationParameters() {
        return simulationParameters;
    }
    public void setSimulationParameters(Parameters simulationParameters) {
        this.simulationParameters = simulationParameters;
    }
    public List<Propagator> getSatellites() {
        return satellites;
    }
    public void setSatellites(List<Propagator> satellites) {
        this.satellites = satellites;
    }
    public Observation getObservations() {
        return observations;
    }
    public void setObservations(Observation observations) {
        this.observations = observations;
    }
    public List<SortedSet<ObservedMeasurement<?>>> getMeasurementsSetsList() {
        return measurementsSetsList;
    }
    public void setMeasurementsSetsList(List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList) {
        this.measurementsSetsList = measurementsSetsList;
    }
}