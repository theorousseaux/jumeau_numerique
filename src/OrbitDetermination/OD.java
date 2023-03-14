package src.OrbitDetermination;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;
import src.constants;

import java.util.LinkedHashMap;
import java.util.SortedSet;


public class OD {
    public final ObservableSatellite object;
    public final Propagator propagator;
    public final SortedSet<ObservedMeasurement<?>> measurementsSet;
    final AbsoluteDate initialDate;
    final AbsoluteDate finalDate;
    public OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder;
    public double[] variance;

    public OD ( ObservableSatellite object , Propagator propagator , OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder , SortedSet<ObservedMeasurement<?>> measurementsSet , AbsoluteDate initialDate , AbsoluteDate finalDate , double stdPos , double stdV ) {
        this.object = object;
        this.propagator = propagator;
        this.estimatedPropagatorBuilder = estimatedPropagatorBuilder; // MUST BE NUMERICAL ?
        this.measurementsSet = measurementsSet;
        this.initialDate = initialDate;
        this.variance = new double[]{Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 )};
        this.finalDate = finalDate;
    }


    public static double[] paramOrbitaux ( Orbit orbit ) {
        KeplerianOrbit nOrbit = new KeplerianOrbit ( orbit );
        return new double[]{nOrbit.getA ( ) , nOrbit.getE ( ) , Math.IEEEremainder ( nOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};
    }


    public static double[][] createDiagonalMatrix ( double[] diagonal ) {
        int size = diagonal.length;
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = diagonal[i];
        }
        return matrix;
    }

    public LinkedHashMap<ObservedMeasurement<?>, Propagator> Kalman ( ConstantProcessNoise processNoise ) { //en vrai, ça peut marcher pour plein de propagateurs à la fois
        System.out.println ( "KALMAN" );

        KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder ( );

        estimatorBuilder.addPropagationConfiguration ( estimatedPropagatorBuilder , processNoise );
        KalmanEstimator kalmanEstimator = estimatorBuilder.build ( );

        LinkedHashMap<ObservedMeasurement<?>, Propagator> mapMeasurePropagator = new LinkedHashMap<ObservedMeasurement<?>, Propagator> ( );
        if (measurementsSet.size ( ) > 0) {
            for (ObservedMeasurement<?> measure : measurementsSet) {

                Propagator[] propagatorEstimated = kalmanEstimator.estimationStep ( measure );
                mapMeasurePropagator.put ( measure , propagatorEstimated[0] );
                //
            }
        }
        System.out.println ( "Kalman DOne" );
        return mapMeasurePropagator;
    }

}