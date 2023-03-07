package src.Kalman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.propagation.Propagator;
import org.orekit.time.AbsoluteDate;

public class ObservationPlus {

	public List<TelescopeAzEl> telescopesList;
    public List<Radar> radarsList;
	public List<ObservableSatellite> objectsList;
	public List<Propagator> propagatorsList;
	public AbsoluteDate initialDate;
	public AbsoluteDate finalDate;

	public ObservationPlus(List<TelescopeAzEl> telescopesList, List<Radar> radarsList, List<ObservableSatellite> objectsList, List<Propagator> propagatorsList, AbsoluteDate initialDate, AbsoluteDate finalDate){
		
        this.telescopesList = telescopesList;
        this.radarsList =  radarsList;
		this.objectsList = objectsList;
		this.propagatorsList = propagatorsList;
		this.initialDate = initialDate;
		this.finalDate = finalDate;
	}
	
	public List<SortedSet<ObservedMeasurement<?>>> measure(boolean plot){
		Generator generator = new Generator();
		for(int i = 0; i < this.objectsList.size(); i++) {
			generator.addPropagator(propagatorsList.get(i));
		}
	    for(TelescopeAzEl telescope : this.telescopesList) {
	    	for(int i = 0; i < this.objectsList.size(); i++) {
				System.out.println("considering object : " + i);
	    		EventBasedScheduler scheduler = telescope.createEventBasedScheduler(this.objectsList.get(i), this.propagatorsList.get(i));
	        	generator.addScheduler(scheduler);
	        }
	    }
        for(Radar radar : this.radarsList) {
	    	for(int i = 0; i < this.objectsList.size(); i++) {
				System.out.println("considering object : " + i);
	    		
                List<EventBasedScheduler> schedulerList = radar.createEventBasedScheduler(this.objectsList.get(i), this.propagatorsList.get(i));
                EventBasedScheduler schedulerAzEl = schedulerList.get(0);
                EventBasedScheduler schedulerRange = schedulerList.get(1);
                EventBasedScheduler schedulerRangeRate = schedulerList.get(2);
                
                generator.addScheduler(schedulerAzEl);
                generator.addScheduler(schedulerRange);
                generator.addScheduler(schedulerRangeRate);

	        	//generator.addPropagator(propagatorsList.get(i));
	        }
	    }

		System.out.println("generating measurements : ");
	    SortedSet<ObservedMeasurement<?>> measurementsList = generator.generate(this.initialDate, this.finalDate);
		System.out.println("done");
	    
	    if(plot) {
	    	System.out.println("MESURES");
	    	for(ObservedMeasurement<?> measure : measurementsList) {
	    		System.out.println("----------------------------------------------");
	    		System.out.println(measure.getDate());
	    		System.out.println(Arrays.stream(measure.getObservedValue()).map(iii -> iii * 180/Math.PI).boxed().collect(Collectors.toList()));
	    		System.out.println(((AngularAzEl) measure).getStation().getBaseFrame().getName());
	    		System.out.println(((AngularAzEl) measure).getSatellites().get(0).getPropagatorIndex());
 
	    	}
			System.out.println("Nombre total de mesures : " + measurementsList.size());
	    	System.out.println("END MESURES");
	    }
	    
	    List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList = new ArrayList<SortedSet<ObservedMeasurement<?>>>();
	    for (int i = 0; i < propagatorsList.size(); i++) {
	    	measurementsSetsList.add(new TreeSet<ObservedMeasurement<?>>());
	    }
	    
	    for(ObservedMeasurement<?> measure : measurementsList){
	    	measurementsSetsList.get(measure.getSatellites().get(0).getPropagatorIndex()).add(measure);
	    }
	    
	    return measurementsSetsList;
	}
    
}
