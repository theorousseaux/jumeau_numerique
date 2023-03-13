package src.App.SimulationTab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;

import org.orekit.propagation.SpacecraftState;
import src.App.MainFrame;
import src.Data.GS.ReadGSFile;
import src.Kalman.Observation;

public class SimulationController {

    @Override
    public String toString() {
        return "SimulationController [model=" + model + ", view=" + view + "]";
    }

    public SimulationModel model = new SimulationModel();
    public SimulationView view;

    public void loadSimulation(MainFrame parent) throws NumberFormatException, IllegalArgumentException, IOException{
        model.setSimulationParameters(parent.paramController.model);

        model.setObserverNetwork(parent.obserController.observerNetwork);
        // model.setSatellites(parent.satController.propagators);
        // model.setSatellites(parent.satController.satellites);


        List<String> satNames = new ArrayList<>();
        List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
        int n = 10;
        for (int i =0; i<n; i++){
            satNames.add("Sat"+String.valueOf(i));
            objectsList.add(new ObservableSatellite(i));
        }

        List<Propagator> propagatorsList = new ArrayList<Propagator>();
        ReadGSFile fetcher = new ReadGSFile();

        propagatorsList = fetcher.readSat("src/Data/Sat.csv", model.getSimulationParameters().getStartDate(),n);

        model.setSatellites(propagatorsList);
        model.setSatellitesNames(objectsList);
        System.out.println("Parameters : ");
        System.out.println(parent.paramController.model.toString());
        System.out.println("Telescopes : ");
        model.getObserverNetwork().display();
        System.out.println("Satellites : ");
        System.out.println(model.getSatellitesNames().toString());

    }
    
    public void runSimulation(MainFrame parent){
        model.setObservations(new Observation(model.getObserverNetwork().getTelescopes(), model.getSatellitesNames(), model.getSatellites(), model.getSimulationParameters().getStartDate(), model.getSimulationParameters().getEndDate()));
        model.setMeasurementsSetsList(model.getObservations().measure(true));
        Pair<List<SortedSet<ObservedMeasurement<?>>>, List<SortedSet<SpacecraftState>>> pair = model.getMeasurementsSetsList ();
        List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList = pair.getFirst();
        List<SortedSet<SpacecraftState>> trueStatesList = pair.getSecond();

        System.out.println(trueStatesList.size());
        System.out.println(measurementsSetsList.size());
        for (int k = 0; k< 10; k++){
            System.out.println(trueStatesList.get(k).size());
            System.out.println(measurementsSetsList.get(k).size());
        }

        System.out.println("Simulation done");
    }


    
}
