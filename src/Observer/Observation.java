package src.Observer;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import src.OrbitDetermination.CustomEventBasedScheduler;
import src.OrbitDetermination.CustomGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class Observation {

    public List<TelescopeAzEl> telescopesList;
    public List<ObservableSatellite> objectsList;
    public List<Propagator> propagatorsList;
    public AbsoluteDate initialDate;
    public AbsoluteDate finalDate;

    public Observation ( List<TelescopeAzEl> telescopesList , List<ObservableSatellite> objectsList , List<Propagator> propagatorsList , AbsoluteDate initialDate , AbsoluteDate finalDate ) {
        this.telescopesList = telescopesList;
        this.objectsList = objectsList;
        this.propagatorsList = propagatorsList;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> measure ( boolean plot ) {
        CustomGenerator generator = new CustomGenerator ( );
        for (int i = 0; i < this.objectsList.size ( ); i++) {
            generator.addPropagator ( propagatorsList.get ( i ) );
        }
        for (TelescopeAzEl telescope : this.telescopesList) {
            for (int i = 0; i < this.objectsList.size ( ); i++) {
                System.out.println ( "considering object : " + i );
                CustomEventBasedScheduler scheduler = telescope.createCustomEventBasedScheduler ( this.objectsList.get ( i ) , this.propagatorsList.get ( i ) );
                generator.addScheduler ( scheduler );
                //generator.addPropagator(propagatorsList.get(i));
            }
        }
        System.out.println ( "generating measurements : " );
        Pair<SortedSet<ObservedMeasurement<?>>, HashMap<ObservedMeasurement, SpacecraftState>> pair = generator.generate ( this.initialDate , this.finalDate );
        SortedSet<ObservedMeasurement<?>> measurementsList = pair.getFirst ( );
        HashMap<ObservedMeasurement, SpacecraftState> trueStatesList = pair.getSecond ( );
        System.out.println ( "TAILE generate " + measurementsList.size ( ) );

        System.out.println ( "TAILE generate " + trueStatesList.size ( ) );
        System.out.println ( "done" );

        if (plot) {
            System.out.println ( "MESURES" );
            for (ObservedMeasurement<?> measure : measurementsList) {
                System.out.println ( "----------------------------------------------" );
                System.out.println ( measure.getDate ( ) );
                System.out.println ( Arrays.stream ( measure.getObservedValue ( ) ).map ( iii -> iii * 180 / Math.PI ).boxed ( ).collect ( Collectors.toList ( ) ) );
                System.out.println ( ((AngularAzEl) measure).getStation ( ).getBaseFrame ( ).getName ( ) );
                System.out.println ( measure.getSatellites ( ).get ( 0 ).getPropagatorIndex ( ) );

            }
            System.out.println ( "Nombre total de mesures : " + measurementsList.size ( ) );
            System.out.println ( "END MESURES" );
        }

        List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList = new ArrayList<SortedSet<ObservedMeasurement<?>>> ( );
        for (int i = 0; i < propagatorsList.size ( ); i++) {
            measurementsSetsList.add ( new TreeSet<ObservedMeasurement<?>> ( ) );
        }

        int i = 0;
        for (ObservedMeasurement<?> measure : measurementsList) {
            measurementsSetsList.get ( measure.getSatellites ( ).get ( 0 ).getPropagatorIndex ( ) ).add ( measure );
            i = i + 1;
        }

        return new Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> ( measurementsSetsList , trueStatesList );
    }

}
