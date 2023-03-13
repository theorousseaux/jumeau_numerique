package src.Kalman;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.Scheduler;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.PropagatorsParallelizer;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.MultiSatStepHandler;
import org.orekit.propagation.sampling.OrekitStepInterpolator;
import org.orekit.time.AbsoluteDate;


/** Main generator for {@link ObservedMeasurement observed measurements}.
 * @author Luc Maisonobe
 * @since 9.3
 */
public class CustomGenerator {

    /** Propagators. */
    private final List<Propagator> propagators;

    /** Sequences generators. */
    private final List<CustomEventBasedScheduler<?>> schedulers;

    /** Build a generator with no sequences generator.
     */
    public CustomGenerator() {
        this.propagators = new ArrayList<>();
        this.schedulers  = new ArrayList<>();
    }

    /** Add a propagator.
     * @param propagator to add
     * @return satellite satellite propagated by the propagator
     */
    public ObservableSatellite addPropagator(final Propagator propagator) {
        propagators.add(propagator);
        return new ObservableSatellite(propagators.size() - 1);
    }

    /** Get a registered propagator.
     * @param satellite satellite propagated by the propagator {@link #addPropagator(Propagator)}
     * @return propagator corresponding to satellite
     */
    public Propagator getPropagator(final ObservableSatellite satellite) {
        return propagators.get(satellite.getPropagatorIndex());
    }

    /** Add a sequences generator for a specific measurement type.
     * @param scheduler sequences generator to add
     * @param <T> the type of the measurement
     */
    public <T extends ObservedMeasurement<T>> void addScheduler(final CustomEventBasedScheduler<T> scheduler) {
        schedulers.add(scheduler);
    }

    /** Generate measurements.
     * @param start start of the measurements time span
     * @param end end of the measurements time span
     * @return generated measurements
     */
    public Pair<SortedSet<ObservedMeasurement<?>>, List<SpacecraftState>> generate(final AbsoluteDate start, final AbsoluteDate end) {

        // initialize schedulers
        for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
            scheduler.init(start, end);
        }

        // set up parallelized propagators
        final GeneratorHandler handler = new GeneratorHandler(schedulers);
        final PropagatorsParallelizer parallelizer = new PropagatorsParallelizer(propagators, handler);

        // generate the measurements
        parallelizer.propagate(start, end);   

        return new Pair<SortedSet<ObservedMeasurement<?>>, List<SpacecraftState>>(handler.getMeasurements(),handler.getTrueStates());
    }    

    /** Handler for measurements generation steps. */
    private static class GeneratorHandler implements MultiSatStepHandler {

        /** Sequences generators. */
        private final List<CustomEventBasedScheduler<?>> schedulers;

        /** Set for holding measurements. */
        private final SortedSet<ObservedMeasurement<?>> measurements;
        
        /** Set for true states. */
        private final List<SpacecraftState> trueStates;

        /** Simple constructor.
         * @param schedulers sequences generators
         */
        GeneratorHandler(final List<CustomEventBasedScheduler<?>> schedulers) {
            this.schedulers   = schedulers;
            this.measurements = new TreeSet<>();
            this.trueStates = new ArrayList<>();
        }

        /** {@inheritDoc} */
        @Override
        public void init(final List<SpacecraftState> states0, final AbsoluteDate t) {
            for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
                scheduler.init(states0.get(0).getDate(), t);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void handleStep(final List<OrekitStepInterpolator> interpolators) {
            for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
            	Pair<SortedSet<ObservedMeasurement<?>>, List<SpacecraftState>> pair = scheduler.generate_(interpolators);
                measurements.addAll(pair.getFirst());
                trueStates.addAll(pair.getSecond());          
            }
        }

        /** Get the generated measurements.
         * @return generated measurements
         */
        public SortedSet<ObservedMeasurement<?>> getMeasurements() {
            return measurements;
        }
        
        public List<SpacecraftState> getTrueStates() {
            return trueStates;
        }
    }

}
