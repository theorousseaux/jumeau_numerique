package src.GroundStation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;


public class Observations {

    public List<TelescopeAzEl> stations;

    public List<ObservableSatellite> objects;

    public List<Propagator> propagatorList;

    public AbsoluteDate startDate;

    public AbsoluteDate endDate;

    //public Generator generator;

    public List <SortedSet<ObservedMeasurement<?>>> measurements;

    public FixedStepSelector dateSelector;

    /* Constructor */
    public Observations(AbsoluteDate startDate, AbsoluteDate endDate, FixedStepSelector dateSelector, List<TelescopeAzEl> stations, List<ObservableSatellite> objects, List<Propagator> propagatorList) {

        this.stations = stations;
        this.objects = objects;
        this.propagatorList = propagatorList;

        this.startDate = startDate;
        this.endDate = endDate;
        this.dateSelector = dateSelector;

        //Generator generator = new Generator();
        List <SortedSet<ObservedMeasurement<?>>> measurementsList = new ArrayList<>();
        int nbMeasures = 0;

        for (int i = 0; i < stations.size(); i++) {
            
            TelescopeAzEl telescope = stations.get(i);
            System.out.println("wesh");
            System.out.println(telescope.topocentricFrame);
            System.out.println("alors");
            CorrelatedRandomVectorGenerator noiseSource = telescope.noiseSource;
            GroundStation station = telescope.station;
            double[] sigma = telescope.sigma;
            double[] baseWeight = telescope.baseWeight;
            EventDetector finalDetector = telescope.finalDetector;
            List<GroundFieldOfViewDetector> fovDetectorsList = telescope.getFieldOfViewDetectors();

            AbsoluteDate date1 = startDate;
            double durationFov = (24*3600)/fovDetectorsList.size();
            AbsoluteDate date2 = date1.shiftedBy(durationFov);
            System.out.println(fovDetectorsList.size());
            System.out.println(durationFov);

            for (int k = 0; k < fovDetectorsList.size(); k++) {
                System.out.println("bonjour");
                GroundFieldOfViewDetector fovDetector = fovDetectorsList.get(k);
                
                for (int j = 0; j < objects.size(); j++){
                    System.out.println("ok");
                    ObservableSatellite object = objects.get(j);
                    int index = object.getPropagatorIndex();
                    Propagator propagator = propagatorList.get(index);
                    BooleanDetector fusionDetector = BooleanDetector.andCombine(finalDetector, fovDetector);

                    AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station, sigma, baseWeight, object);

                    EventBasedScheduler scheduler = new EventBasedScheduler(mesuresBuilder, dateSelector, propagator, finalDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
                    
                    Generator generator = new Generator();
                    generator.addPropagator(propagator);
                    generator.addScheduler(scheduler);
                    
                    System.out.println(date1);
                    System.out.println(date2);
                    SortedSet<ObservedMeasurement<?>> listMeasurements = generator.generate(date1, date2);
                    System.out.println(listMeasurements.size());
                    measurementsList.add(listMeasurements);
                    for(ObservedMeasurement<?> measure : listMeasurements) {
                        nbMeasures = nbMeasures+1;
                        System.out.println("mesure");
                        System.out.println(nbMeasures);
                        System.out.println(Arrays.toString(measure.getObservedValue()));
                        System.out.println(measure.getDate());
                        
                    }
                    //System.out.println(measurementsList);

                }

                date1 = date1.shiftedBy(durationFov);
                date2 = date2.shiftedBy(durationFov);
            }
        }

        //this.generator = generator;
        this.measurements = measurementsList;
    }


    /*Methods */

    public List <SortedSet<ObservedMeasurement<?>>> getMeasurements() {

        return measurements;
    }
}
