package src.OrbitDetermination;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.Array2DRowRealMatrix;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.orekit.attitudes.LofOffset;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.AbstractKalmanEstimator;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.frames.LOFType;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.Transform;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.CartesianDerivativesFilter;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;
import src.constants;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.SortedSet;


public class OD {
    public final ObservableSatellite object;
    public final Propagator propagator;
    public OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder;
    public final SortedSet<ObservedMeasurement<?>> measurementsSet;
    public double[] variance;
    final AbsoluteDate initialDate;
    final AbsoluteDate finalDate;

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