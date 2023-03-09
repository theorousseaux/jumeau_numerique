package src.App.SimulationTab;

import java.util.List;
import java.util.SortedSet;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;

import src.App.ParametersTab.Parameters;
import src.Kalman.Observation;
import src.UseCase1_GSNetwork.GSNetwork;

/**
 * This class regroups all the elements needed to run a simulation
 */
public class SimulationModel {
    GSNetwork groundStationNetwork;
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
        return "simulationModel [groundStationNetwork=" + groundStationNetwork + ", simulationParameters="
                + simulationParameters + ", satellites=" + satellites + ", observations=" + observations
                + ", measurementsSetsList=" + measurementsSetsList + "]";
    }
    
    
    public GSNetwork getGroundStationNetwork() {
        return groundStationNetwork;
    }
    public void setGroundStationNetwork(GSNetwork groundStationNetwork) {
        this.groundStationNetwork = groundStationNetwork;
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
