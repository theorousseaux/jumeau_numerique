package src.App.EstimationTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.Propagator;
import src.App.MainFrame;
import src.App.SimulationTab.SimulationModel;
import src.Data.GS.ReadGSFile;
import src.Kalman.Observation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstimationController {

    public EstimationModel model = new EstimationModel();


    public void loadEstimation(MainFrame parent) throws NumberFormatException, IllegalArgumentException, IOException {


        // model.setSatellites(parent.satController.propagators);
        // model.setSatellites(parent.satController.satellites);

        List<String> satNames = new ArrayList<> ();
        List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
        int n = 10;
        for (int i =0; i<n; i++){
            satNames.add("Sat"+String.valueOf(i));
            objectsList.add(new ObservableSatellite(i));
        }

        List<Propagator> propagatorsList = new ArrayList<Propagator>();
        ReadGSFile fetcher = new ReadGSFile();

        propagatorsList = fetcher.readSat("src/Data/Sat.csv", model.getInitialDate (),n);

        model.setPropagators (propagatorsList);

    }
    public void runEstimation( MainFrame parent) throws IOException {
        this.loadEstimation ( parent );
        System.out.println("Simulation done");
    }
}
