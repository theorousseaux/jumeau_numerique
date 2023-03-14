package src.OrbitDetermination;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.PropagatorsParallelizer;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.MultiSatStepHandler;
import org.orekit.propagation.sampling.OrekitStepInterpolator;
import org.orekit.time.AbsoluteDate;

import java.util.*;


/**
 * Main generator for {@link ObservedMeasurement observed measurements}.
 *
 * @author Luc Maisonobe
 * @since 9.3
 */
public class CustomGenerator {

    /**
     * Propagators.
     */
    private final List<Propagator> propagators;

    /**
     * Sequences generators.
     */
    private final List<CustomEventBasedScheduler<?>> schedulers;

    /**
     * Build a generator with no sequences generator.
     */
    public CustomGenerator ( ) {
        this.propagators = new ArrayList<> ( );
        this.schedulers = new ArrayList<> ( );
    }

    /**
     * Add a propagator.
     *
     * @param propagator to add
     */
    public void addPropagator ( final Propagator propagator ) {
        propagators.add ( propagator );
        new ObservableSatellite ( propagators.size ( ) - 1 );
    }

    /**
     * Add a sequences generator for a specific measurement type.
     *
     * @param scheduler sequences generator to add
     * @param <T>       the type of the measurement
     */
    public <T extends ObservedMeasurement<T>> void addScheduler ( final CustomEventBasedScheduler<T> scheduler ) {
        schedulers.add ( scheduler );
    }

    /**
     * Generate measurements.
     *
     * @param start start of the measurements time span
     * @param end   end of the measurements time span
     * @return generated measurements
     */
    public Pair<SortedSet<ObservedMeasurement<?>>, HashMap<ObservedMeasurement, SpacecraftState>> generate ( final AbsoluteDate start , final AbsoluteDate end ) {

        // initialize schedulers
        for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
            scheduler.init ( start , end );
        }

        // set up parallelized propagators
        final GeneratorHandler handler = new GeneratorHandler ( schedulers );
        final PropagatorsParallelizer parallelizer = new PropagatorsParallelizer ( propagators , handler );

        // generate the measurements
        parallelizer.propagate ( start , end );

        return new Pair<SortedSet<ObservedMeasurement<?>>, HashMap<ObservedMeasurement, SpacecraftState>> ( handler.getMeasurements ( ) , handler.getTrueStates ( ) );
    }

    /**
     * Handler for measurements generation steps.
     */
    private static class GeneratorHandler implements MultiSatStepHandler {

        /**
         * Sequences generators.
         */
        private final List<CustomEventBasedScheduler<?>> schedulers;

        /**
         * Set for holding measurements.
         */
        private final SortedSet<ObservedMeasurement<?>> measurements;

        /**
         * Set for true states.
         */
        private final HashMap<ObservedMeasurement, SpacecraftState> trueStates;

        /**
         * Simple constructor.
         *
         * @param schedulers sequences generators
         */
        GeneratorHandler ( final List<CustomEventBasedScheduler<?>> schedulers ) {
            this.schedulers = schedulers;
            this.measurements = new TreeSet<> ( );
            this.trueStates = new HashMap<ObservedMeasurement, SpacecraftState> ( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void init ( final List<SpacecraftState> states0 , final AbsoluteDate t ) {
            for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
                scheduler.init ( states0.get ( 0 ).getDate ( ) , t );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handleStep ( final List<OrekitStepInterpolator> interpolators ) {
            for (final CustomEventBasedScheduler<?> scheduler : schedulers) {
                Pair<SortedSet<ObservedMeasurement<?>>, HashMap<ObservedMeasurement, SpacecraftState>> pair = scheduler.generate_ ( interpolators );
                measurements.addAll ( pair.getFirst ( ) );
                trueStates.putAll ( pair.getSecond ( ) );
            }
        }

        /**
         * Get the generated measurements.
         *
         * @return generated measurements
         */
        public SortedSet<ObservedMeasurement<?>> getMeasurements ( ) {
            return measurements;
        }

        public HashMap<ObservedMeasurement, SpacecraftState> getTrueStates ( ) {
            return trueStates;
        }
    }

}
