package src.GroundStation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.orekit.time.DatesSelector;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;


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
            
            int cptFov = 0;
            
            Map<Integer, Propagator> evolutionPropagator = new HashMap<>(); 
            for (int p = 0; p < propagatorList.size(); p++) {
                
                evolutionPropagator.put(p, propagatorList.get(p));
            }

            TelescopeAzEl telescope = stations.get(i);
            CorrelatedRandomVectorGenerator noiseSource = telescope.noiseSource;
            GroundStation station = telescope.station;
            double[] sigma = telescope.sigma;
            double[] baseWeight = telescope.baseWeight;
            EventDetector finalDetector = telescope.finalDetector;
            List<GroundFieldOfViewDetector> fovDetectorsList = telescope.getFieldOfViewDetectors();

            List<DatesSelector> dateSelectorList = new ArrayList<>();
            for (int d = 0; d < fovDetectorsList.size(); d++) {
                
                TimeScale utc = TimeScalesFactory.getUTC();
                FixedStepSelector dateSelectorSpe = new FixedStepSelector(60 + 10*d, utc);
                dateSelectorList.add(dateSelectorSpe);
            }
            System.out.println("nombre de dateSelector : " + dateSelectorList.size());

            AbsoluteDate date1 = startDate;
            double durationFov = (3*3600);
            AbsoluteDate date2 = date1.shiftedBy(durationFov);
            System.out.println(fovDetectorsList.size());
            System.out.println(durationFov);
            //SpacecraftState spacecraftState = propagatorList.get(0).propagate(date2);

            for (int k = 0; k < fovDetectorsList.size(); k++) {
                System.out.println("considering fovDetector : " + k);
                GroundFieldOfViewDetector fovDetector = fovDetectorsList.get(k);
                
                
                for (int j = 0; j < objects.size(); j++){
                    
                    System.out.println("considering object : " + j);
                    ObservableSatellite object = objects.get(j);
                    int index = object.getPropagatorIndex();
                    Propagator propagatorF = evolutionPropagator.get(index);

                    SpacecraftState spacecraftState0 = propagatorF.propagate(date1);
                    System.out.println("stats initiaux à date : " + date1 + " sont : " + spacecraftState0.toString());
                    
                    Generator generator = new Generator();
                    generator.addPropagator(propagatorF);
                    
                    if (cptFov==k) {
                        System.out.println("période du fov numéro : " + cptFov + " et examen du fov : " + k);

                        DatesSelector dateSelectorSpe = dateSelectorList.get(k);

                        BooleanDetector fusionDetector = BooleanDetector.andCombine(finalDetector, fovDetector);

                        AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station, sigma, baseWeight, object);

                        EventBasedScheduler scheduler = new EventBasedScheduler(mesuresBuilder, dateSelectorSpe, propagatorF, fusionDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
                        
                        generator.addScheduler(scheduler);

                    }
                    
                    System.out.println(" période début d'observation : " + date1);
                    System.out.println("période de fin d'observation : " + date2);
                    SortedSet<ObservedMeasurement<?>> listMeasurements = generator.generate(date1, date2);
                    System.out.println(listMeasurements.size());
                    measurementsList.add(listMeasurements);
                    for(ObservedMeasurement<?> measure : listMeasurements) {
                        nbMeasures = nbMeasures+1;
                        System.out.println("mesure numéro : " + nbMeasures);
                        System.out.println(Arrays.toString(measure.getObservedValue()));
                        System.out.println(measure.getDate());
                        
                    }
                    SpacecraftState spacecraftState = propagatorF.propagate(date2);
                    System.out.println("stats objects à date : " + date2 + " sont : " + spacecraftState.toString());
                    propagatorF.resetInitialState(spacecraftState);
                    evolutionPropagator.put(index, propagatorF);
                    //System.out.println(measurementsList);

                }

                date1 = date1.shiftedBy(durationFov);
                date2 = date2.shiftedBy(durationFov);
                cptFov = cptFov +1;
                
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
