package src.GroundStation;

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
import org.orekit.propagation.events.EventDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;


public class Observations {

    public List<TelescopeAzEl> stations;

    public List<ObservableSatellite> objects;

    public List<Propagator> propagatorList;

    public SortedSet<ObservedMeasurement<?>> measurementList;

    public AbsoluteDate startDate;

    public AbsoluteDate endDate;

    public Generator generator;

    public FixedStepSelector dateSelector;

    public Observations(AbsoluteDate startDate, AbsoluteDate endDate, FixedStepSelector dateSelector, List<TelescopeAzEl> stations, List<ObservableSatellite> objects) {

        this.stations = stations;
        this.objects = objects;

        this.startDate = startDate;
        this.endDate = endDate;
        this.dateSelector = dateSelector;

        for (int i = 0; i < stations.size(); i++) {
            
            TelescopeAzEl telescope = stations.get(i);

            CorrelatedRandomVectorGenerator noiseSource = telescope.noiseSource;
            GroundStation station = telescope.station;
            double[] sigma = telescope.sigma;
            double[] baseWeight = telescope.baseWeight;
            EventDetector finalDetector = telescope.finalDetector;

            for (int j = 0; j < objects.size(); j++) {

                ObservableSatellite object = objects.get(j);
                int index = object.getPropagatorIndex();
                Propagator propagator = propagatorList.get(index);

                AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station, sigma, baseWeight, object);
                
                EventBasedScheduler scheduler = new EventBasedScheduler(mesuresBuilder, dateSelector, propagator, finalDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);

                Generator generator = new Generator();
                generator.addPropagator(propagator);
                generator.addScheduler(scheduler);
        
                SortedSet<ObservedMeasurement<?>> list_measurement = generator.generate(startDate, endDate);
            }

    }

}
