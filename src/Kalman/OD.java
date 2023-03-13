package src.Kalman;

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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.SortedSet;


public class OD {
    public ObservableSatellite object;
    public Propagator propagator;
    public OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder;
    public SortedSet<ObservedMeasurement<?>> measurementsSet;
    public double[] variance;
    AbsoluteDate initialDate;
    AbsoluteDate finalDate;

    public OD ( ObservableSatellite object , Propagator propagator , OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder , SortedSet<ObservedMeasurement<?>> measurementsSet , AbsoluteDate initialDate , AbsoluteDate finalDate , double stdPos , double stdV ) {
        this.object = object;
        this.propagator = propagator;
        this.estimatedPropagatorBuilder = estimatedPropagatorBuilder; // MUST BE NUMERICAL ?
        this.measurementsSet = measurementsSet;
        this.initialDate = initialDate;
        double[] mat = {Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 )};
        this.variance = mat;
        this.finalDate = finalDate;
    }


    public OD ( ObservableSatellite object , Propagator propagator , SortedSet<ObservedMeasurement<?>> measurementsSet , AbsoluteDate initialDate , AbsoluteDate finalDate , double stdPos , double stdV ) {
        this.object = object;
        this.propagator = propagator;
        this.setEstimatedPropagatorBuilder ( propagator , stdPos , stdV );
        double[] mat = {Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 )};
        this.variance = mat;
        this.measurementsSet = measurementsSet;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public OD ( ObservableSatellite object , Propagator propagator , OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder , SortedSet<ObservedMeasurement<?>> measurementsSet , AbsoluteDate initialDate , AbsoluteDate finalDate ) {
        this.object = object;
        this.propagator = propagator;
        this.measurementsSet = measurementsSet;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    static void results ( Propagator propagator , Propagator[] propagatorEstimated ) {
        System.out.println ( "COMPARISON (true/OD) :" );
        System.out.println ( propagatorEstimated[0].getInitialState ( ).getDate ( ) );
        System.out.println ( Arrays.toString ( paramOrbitaux ( propagator.propagate ( propagatorEstimated[0].getInitialState ( ).getDate ( ) ).getOrbit ( ) ) ) );
        System.out.println ( Arrays.toString ( paramOrbitaux ( propagatorEstimated[0].getInitialState ( ).getOrbit ( ) ) ) );
        incertitudes ( propagator , propagatorEstimated[0] );
        //incertitudes2(propagator,propagatorEstimated[0]);
        //incertitudes3(propagator,propagatorEstimated[0]);
    }

    static void covarianceAnalysis ( Propagator estimatedPropagator , AbstractKalmanEstimator estimator ) {
        System.out.println ( "COVARIANCE :" );
        LocalOrbitalFrame LOFrame = new LocalOrbitalFrame ( constants.gcrf , LOFType.LVLH , estimatedPropagator , "LOF" );
        RealMatrix covMat_eci = estimator.getPhysicalEstimatedCovarianceMatrix ( );
        Transform eci2lof_frozen = constants.gcrf.getTransformTo ( LOFrame , estimatedPropagator.getInitialState ( ).getDate ( ) ).freeze ( );
        double[][] jacobian = new double[6][6];
        eci2lof_frozen.getJacobian ( CartesianDerivativesFilter.USE_PV , jacobian );
        RealMatrix jacobianMat = new Array2DRowRealMatrix ( jacobian );
        RealMatrix covMat_lof = jacobianMat.transpose ( ).multiply ( covMat_eci ).multiply ( jacobianMat );
        for (int i = 0; i < 6; i++) {
            System.out.printf ( "%.2f ; %.2f ; %.2f ; %.2f ; %.2f ; %.2f %n" , covMat_eci.getData ( )[i][0] , covMat_eci.getData ( )[i][1] , covMat_eci.getData ( )[i][2] , covMat_eci.getData ( )[i][3] , covMat_eci.getData ( )[i][4] , covMat_eci.getData ( )[i][5] );
        }
        System.out.println ( "-------------------------------------------------------" );
        for (int i = 0; i < 6; i++) {
            System.out.printf ( "%.2f ; %.2f ; %.2f ; %.2f ; %.2f ; %.2f %n" , covMat_lof.getData ( )[i][0] , covMat_lof.getData ( )[i][1] , covMat_lof.getData ( )[i][2] , covMat_lof.getData ( )[i][3] , covMat_lof.getData ( )[i][4] , covMat_lof.getData ( )[i][5] );
        }
    }

    public static double[] paramOrbitaux ( Orbit orbit ) {
        KeplerianOrbit nOrbit = new KeplerianOrbit ( orbit );
        double[] param = {nOrbit.getA ( ) , nOrbit.getE ( ) , Math.IEEEremainder ( nOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};
        return param;
    }

    public static double[] incertitudes ( Propagator propagator , Propagator estimatedPropagator ) {
        LocalOrbitalFrame LOFrame = new LocalOrbitalFrame ( constants.gcrf , LOFType.LVLH , propagator , "LOF" );
        AbsoluteDate date = estimatedPropagator.getInitialState ( ).getDate ( );
        SpacecraftState estimatedState = estimatedPropagator.getInitialState ( );
        System.out.println ( "estimation propagée" );
        SpacecraftState trueState = propagator.getInitialState ( );
        System.out.println ( "vraie propagée" );
        Transform eci2lof_frozen = constants.gcrf.getTransformTo ( LOFrame , date ).freeze ( );
        Vector3D dV_eci = estimatedState.getPVCoordinates ( ).getVelocity ( ).subtract ( trueState.getPVCoordinates ( ).getVelocity ( ) );
        Vector3D dP_eci = estimatedState.getPVCoordinates ( ).getPosition ( ).subtract ( trueState.getPVCoordinates ( ).getPosition ( ) );
        Vector3D dV_lof = eci2lof_frozen.transformVector ( dV_eci );
        Vector3D dP_lof = eci2lof_frozen.transformVector ( dP_eci );
        System.out.println ( "norm of deltaV(m/s) : " + dV_lof.getNorm ( ) );
        System.out.println ( "norm of deltaP(m) : " + dP_lof.getNorm ( ) );
        double[] errors = {dP_lof.getNorm ( ) , dV_lof.getNorm ( )};
        return errors;
    }

    public static double[] incertitudes2 ( SpacecraftState trueState , Propagator estimatedPropagator ) {
        SpacecraftState estimatedState = estimatedPropagator.getInitialState ( );
        System.out.println ( trueState = null );
        Vector3D deltaVInInertialFrame = trueState.getPVCoordinates ( ).getVelocity ( ).
                subtract ( estimatedState.getPVCoordinates ( ).getVelocity ( ) );
        Vector3D deltaPInInertialFrame = trueState.getPVCoordinates ( ).getPosition ( ).
                subtract ( estimatedState.getPVCoordinates ( ).getPosition ( ) );
        Transform inertialToSpacecraftFrame = estimatedState.toTransform ( );
        System.out.println ( trueState.getDate ( ) );
        System.out.println ( estimatedState.getDate ( ) );
        System.out.println ( Arrays.toString ( paramOrbitaux ( trueState.getOrbit ( ) ) ) );
        System.out.println ( Arrays.toString ( paramOrbitaux ( estimatedState.getOrbit ( ) ) ) );
        System.out.println ( "---------------------------------------" );

        Vector3D deltaVProjected = inertialToSpacecraftFrame.transformVector ( deltaVInInertialFrame );
        Vector3D deltaPProjected = inertialToSpacecraftFrame.transformVector ( deltaPInInertialFrame );
        //System.out.println("norm of deltaV(m/s) : " + deltaVProjected.getNorm());
        //System.out.println("norm of deltaP(m) : " + deltaPProjected.getNorm());
        double[] errors = {deltaPProjected.getNorm ( ) , deltaVProjected.getNorm ( )};
        return errors;
    }

    static void incertitudes3 ( Propagator propagator , Propagator estimatedPropagator ) {
        AbsoluteDate date = estimatedPropagator.getInitialState ( ).getDate ( );
        SpacecraftState s1 = estimatedPropagator.propagate ( date );
        SpacecraftState s2 = propagator.propagate ( date );
        LofOffset lvlh = new LofOffset ( s1.getFrame ( ) , LOFType.LVLH );
        SpacecraftState converted = new SpacecraftState ( s1.getOrbit ( ) , lvlh.getAttitude ( s1.getOrbit ( ) , s1.getDate ( ) , s1.getFrame ( ) ) );
        TimeStampedPVCoordinates pv2 = converted.toTransform ( ).transformPVCoordinates ( s2.getPVCoordinates ( s1.getFrame ( ) ) );
        Vector3D dV_lof = pv2.getVelocity ( );
        Vector3D dP_lof = pv2.getPosition ( );
        System.out.println ( "norm of deltaV(m/s) : " + dV_lof.getNorm ( ) );
        System.out.println ( "norm of deltaP(m) : " + dP_lof.getNorm ( ) );
    }

    public static double[][] createDiagonalMatrix ( double[] diagonal ) {
        int size = diagonal.length;
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = diagonal[i];
        }
        return matrix;
    }

    static void incertitudes ( SpacecraftState state1 , SpacecraftState state2 ) {
        Vector3D deltaVInInertialFrame = state2.getPVCoordinates ( ).getVelocity ( ).
                subtract ( state1.getPVCoordinates ( ).getVelocity ( ) );
        Vector3D deltaPInInertialFrame = state2.getPVCoordinates ( ).getPosition ( ).
                subtract ( state1.getPVCoordinates ( ).getPosition ( ) );
        Transform inertialToSpacecraftFrame = state1.toTransform ( );
        Vector3D deltaVProjected = inertialToSpacecraftFrame.transformVector ( deltaVInInertialFrame );
        Vector3D deltaPProjected = inertialToSpacecraftFrame.transformVector ( deltaPInInertialFrame );
        System.out.println ( "norm of deltaV(m/s) : " + deltaVProjected.getNorm ( ) );
        System.out.println ( "norm of deltaP(m) : " + deltaPProjected.getNorm ( ) );
    }

    public void setEstimatedPropagatorBuilder ( Propagator propagator , double stdPos , double stdV ) {
        this.estimatedPropagatorBuilder = createPrior ( propagator , stdPos , stdV );
    }

    public Propagator BLS ( ) {
        System.out.println ( "BLS METHOD" );

        QRDecomposer matrixDecomposer = new QRDecomposer ( 1e-11 );
        LeastSquaresOptimizer optimizer = new GaussNewtonOptimizer ( matrixDecomposer , false );

        BatchLSEstimator BLSEstimator = new BatchLSEstimator ( optimizer , estimatedPropagatorBuilder );

        double estimator_convergence_thres = 1e-3;
        int estimator_max_iterations = 25;
        int estimator_max_evaluations = 35;
        BLSEstimator.setParametersConvergenceThreshold ( estimator_convergence_thres );
        BLSEstimator.setMaxIterations ( estimator_max_iterations );
        BLSEstimator.setMaxEvaluations ( estimator_max_evaluations );

        for (ObservedMeasurement<?> measure : measurementsSet) {
            BLSEstimator.addMeasurement ( measure );
        }

        Propagator[] propagatorEstimated = BLSEstimator.estimate ( );

        //RESULTAT
        results ( propagator , propagatorEstimated );

        return propagatorEstimated[0];
    }

    public LinkedHashMap<ObservedMeasurement<?>, Propagator> Kalman ( ConstantProcessNoise processNoise ) { //en vrai, ça peut marcher pour plein de propagateurs à la fois
        System.out.println ( "KALMAN" );

        KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder ( );

        estimatorBuilder.addPropagationConfiguration ( estimatedPropagatorBuilder , processNoise );
        KalmanEstimator kalmanEstimator = estimatorBuilder.build ( );

        LinkedHashMap<ObservedMeasurement<?>, Propagator> mapMeasurePropagator = new LinkedHashMap<ObservedMeasurement<?>, Propagator> ( );
        if (measurementsSet.size ( ) > 0) {
            for (ObservedMeasurement<?> measure : measurementsSet) {
                //System.out.println ( "Mesure dans Kalman : " +measure);
                //System.out.println(measure.getSatellites ().get(0));
                //System.out.println("Nb satellite ds la mesure " + measure.getSatellites ().size());

                Propagator[] propagatorEstimated = kalmanEstimator.estimationStep ( measure );
                mapMeasurePropagator.put ( measure , propagatorEstimated[0] );
                //RESULTAT
                //covarianceAnalysis(propagatorEstimated[0], kalmanEstimator);
                //results ( propagator , propagatorEstimated );
            }
        }
        System.out.println ( "Kalman DOne" );
        return mapMeasurePropagator;
    }

    public NumericalPropagatorBuilder createPrior ( Propagator propagator , double stdPos , double stdV ) {


        KeplerianOrbit trueOrbit = new KeplerianOrbit ( propagator.getInitialState ( ).getOrbit ( ) );

///////////////////////////////////////////////////////////////////////////////////////////////////////
        SpacecraftState trueState = new SpacecraftState ( trueOrbit );
        Vector3D V_InertialFrame = trueState.getPVCoordinates ( ).getVelocity ( );
        Vector3D P_InertialFrame = trueState.getPVCoordinates ( ).getPosition ( );
        double[] mean = {P_InertialFrame.getX ( ) , P_InertialFrame.getY ( ) , P_InertialFrame.getZ ( ) , V_InertialFrame.getX ( ) , V_InertialFrame.getY ( ) , V_InertialFrame.getZ ( )};
        double[] variance = {Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 )};

        double[][] covariance = createDiagonalMatrix ( variance );
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution ( mean , covariance );
        double[] estimatedParameters = distribution.sample ( ); // Générez un nombre aléatoire selon la loi gaussienne
        Vector3D estimatedV_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 3 , 6 ) );
        Vector3D estimatedP_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 0 , 3 ) );
        PVCoordinates estimatedPV = new PVCoordinates ( estimatedP_InertialFrame , estimatedV_InertialFrame );
        TimeStampedPVCoordinates estimatedTSPV = new TimeStampedPVCoordinates ( initialDate , 1. , estimatedPV );
        KeplerianOrbit estimatedOrbit = new KeplerianOrbit ( estimatedTSPV , constants.gcrf , constants.mu );
        double[] estimatedParamOrbitaux = {estimatedOrbit.getA ( ) , estimatedOrbit.getE ( ) , Math.IEEEremainder ( estimatedOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};
        double estimatedA = estimatedParamOrbitaux[0];
        double estimatedE = estimatedParamOrbitaux[1];
        double estimatedI = estimatedParamOrbitaux[2];
        double estimatedRaan = estimatedParamOrbitaux[3];
        double estimatedPa = estimatedParamOrbitaux[4];
        double estimatedAnomaly = estimatedParamOrbitaux[5];
        KeplerianOrbit estimatedOrbit__ = new KeplerianOrbit ( estimatedA , estimatedE , estimatedI , estimatedPa , estimatedRaan , estimatedAnomaly , constants.type , constants.gcrf , initialDate , Constants.EGM96_EARTH_MU );
        CartesianOrbit estimatedOrbit_ = new CartesianOrbit ( estimatedOrbit__ );


        double prop_min_step = 0.001;// # s
        double prop_max_step = 300.0;// # s
        double prop_position_error = 10.0;// # m
        double estimator_position_scale = 1.0;// # m
        DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder ( prop_min_step , prop_max_step , prop_position_error );
        NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder ( estimatedOrbit_ , integratorBuilder , constants.type , estimator_position_scale );
        return numericalPropagatorBuilder;
    }
}