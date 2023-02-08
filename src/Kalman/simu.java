package src.Kalman;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;

public class simu {
	public static void main(String[] args) throws Exception{
    	
    	//importation des donnees de base (toujour mettre ça en début de programme)
    	File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
    	
    	// ORBITE SIMULEE (ISS)
    	double i = 51.6416*Math.PI/180;
    	double raan = 247.4627*Math.PI/180;
    	double e = 0.0006703;
    	double pa = 130.5360*Math.PI/180;
    	double anomaly = 261.6196828895407;
    	double rev_day = 15.72125391;
    	double T = 3600*24/rev_day;
    	double a = Math.cbrt(Math.pow(T, 2)*constants.mu/(4*Math.pow(Math.PI,2))); //6730.960 km, soit h=352.823 km
    	double[] listParamOrbitaux = {a,e,Math.IEEEremainder(i, 2*Math.PI),Math.IEEEremainder(raan, 2*Math.PI),Math.IEEEremainder(pa, 2*Math.PI),Math.IEEEremainder(anomaly, 2*Math.PI)};
    	AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
    	double t = initialOrbit.getKeplerianPeriod();

    	AbsoluteDate finalDate = initialDate.shiftedBy(t * 450.); // on propagera sur 450 orbites
    	
    	ObservableSatellite satellite_ISS = new ObservableSatellite(0); 
    	
    	final KeplerianPropagator propagator_ISS = new KeplerianPropagator(initialOrbit);
    	
    	List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
    	List<Propagator> propagatorsList = new ArrayList<Propagator>();
    	objectsList.add(satellite_ISS);
    	propagatorsList.add(propagator_ISS);
    	
    	
    	// STATIONS ET TELESCOPES
    	Station station_Paris = new Station("PARIS", 48.866667*Math.PI/180, 2.333333*Math.PI/180, 0.);
    	//TelescopeAzEl(mean, angularIncertitude, elevationLimit, angularFoV, stepMeasure, breakTime, station)
    	station_Paris.addTelescope(new TelescopeAzEl(new double[]{0.,0.}, new double[]{0.3*Math.PI/180, 0.3*Math.PI/180}, 30*Math.PI/180, 119*Math.PI/180, 10, 10, station_Paris));
    	List<TelescopeAzEl> telescopesList = station_Paris.getListTelescope();
    	
    	// MESURES
    	System.out.println("MESURES");
    	SortedSet<ObservedMeasurement<?>> measuresList = measure(telescopesList, objectsList, propagatorsList, initialDate, finalDate);
    	
    	for(ObservedMeasurement<?> measure : measuresList) {
    		System.out.println("----------------------------------------------");
    		System.out.println(measure.getDate());
    		System.out.println(Arrays.stream(measure.getObservedValue()).map(iii -> iii * 180/Math.PI).boxed().collect(Collectors.toList()));
    		System.out.println(((AngularAzEl) measure).getStation().getBaseFrame().getName());
    		System.out.println(((AngularAzEl) measure).getSatellites().get(0).getPropagatorIndex());

    	} 	
	}
	
	public static SortedSet<ObservedMeasurement<?>> measure(List<TelescopeAzEl> telescopesList, List<ObservableSatellite> objectsList, List<Propagator> propagatorsList, AbsoluteDate initialDate, AbsoluteDate finalDate){
		Generator generator = new Generator();
	    for(TelescopeAzEl telescope : telescopesList) {
	    	for(int i = 0; i < objectsList.size(); i++) {
	    		EventBasedScheduler scheduler = telescope.createEventBasedScheduler(objectsList.get(i), propagatorsList.get(i));
	        	generator.addScheduler(scheduler);
	        	generator.addPropagator(propagatorsList.get(i));
	        }
	    }
	    SortedSet<ObservedMeasurement<?>> measurementsList = generator.generate(initialDate, finalDate);
	    return measurementsList;
	}
}


