package src.App.SimulationTab;

import src.App.MainFrame;
import src.Kalman.Observation;

public class SimulationController {

    public SimulationModel model;
    public SimulationView view;

    public void loadSimulation(MainFrame parent){
        model.setGroundStationNetwork(parent.gsController.gsNetwork);
        model.setSatellites(parent.satController.propagators);
        model.setSatellites(parent.satController.satellites);
        model.setSimulationParameters(parent.paramController.getModel());
    }
    
    public void runSimulation(MainFrame parent){
        model.setObservations(new Observation(model.getGroundStationNetwork().getTelescopes(), model.getSatellitesNames(), model.getSatellites(), model.getSimulationParameters().getStartDate(), model.getSimulationParameters().getEndDate()));
        model.setMeasurementsSetsList(model.getObservations().measure(false));
    }
    
}
