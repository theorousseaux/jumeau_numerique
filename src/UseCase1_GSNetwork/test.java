package src.UseCase1_GSNetwork;

import src.Kalman.Observation;
import src.Kalman.constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.time.AbsoluteDate;

import src.Data.GS.ReadGSFile;

import src.Kalman.TelescopeAzEl;

public class test {

    public static void main(String[] args) throws NumberFormatException, IOException, NoSuchElementException {
        
        File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
        
        
        // Networks generation

        GSNetwork network1 = new GSNetwork("net1",Arrays.asList("Toulouse","Singapour"));
        network1.display();
        

        GSNetwork network2 = new GSNetwork("net2",Arrays.asList("Kiruna"));
        network2.display();

        // Adding telescopes to stations
        List<TelescopeAzEl> telescopesList = new ArrayList<>();
        for (TelescopeAzEl tel :network1.getTelescopes() ){
            telescopesList.add(tel);
        }
        for (TelescopeAzEl tel :network2.getTelescopes() ){
            telescopesList.add(tel);
        }

        // Interval of the simulation

        AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
        AbsoluteDate finalDate = initialDate.shiftedBy(60*60*24);

        // Satellites generation

        int n = 40; //nb of satellites
        List<String> satNames = new ArrayList<>();
        List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
        for (int i =0; i<n; i++){
            satNames.add("Sat"+String.valueOf(i));
            objectsList.add(new ObservableSatellite(i));
        }

        List<Propagator> propagatorsList = new ArrayList<Propagator>();
        ReadGSFile fetcher = new ReadGSFile();

        propagatorsList = fetcher.readSat("src/Data/Sat.csv", initialDate,n);

        // Making observations

        Observation observation1 = new Observation(telescopesList, objectsList, propagatorsList, initialDate, finalDate);
        List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList1 = observation1.measure(false);

        // Evaluating networks performances

        System.out.println("Number of observations by network 1:");
        System.out.println(network1.countObservations(measurementsSetsList1));
        System.out.println("Number of observations by network 2:");
        System.out.println(network2.countObservations(measurementsSetsList1));

    }


}
