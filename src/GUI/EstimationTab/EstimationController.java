package src.GUI.EstimationTab;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;
import src.GUI.MainFrame;
import src.OrbitDetermination.OD;
import src.constants;

import java.io.IOException;
import java.util.*;

import static src.OrbitDetermination.OD.paramOrbitaux;

/**
 * Cette classe gère l'estimation d'une orbite à partir de données de mesures.
 */
public class EstimationController {

    /**
     * Le modèle de l'estimation.
     */
    public final EstimationModel model = new EstimationModel ( );

    /**
     * Charge les données d'estimation à partir du contrôleur parent.
     *
     * @param parent le contrôleur parent
     * @throws IllegalArgumentException si une exception est levée
     */
    public void loadEstimation ( MainFrame parent ) throws IllegalArgumentException {
        model.setSatellites ( parent.getSimuController ( ).getModel ( ).getObservableSatellites ( ) );
        model.setPropagators ( parent.getSimuController ( ).getModel ( ).getSatellites ( ) );
        model.setFinalDate ( parent.getSimuController ( ).getModel ( ).getSimulationParameters ( ).getEndDate ( ) );
        model.setInitialDate ( parent.getSimuController ( ).getModel ( ).getSimulationParameters ( ).getStartDate ( ) );
        model.setMeasurements ( parent.getSimuController ( ).getModel ( ).getMeasurementsSetsList ( ) );
        model.setSatellitesNames ( parent.getSimuController ( ).getModel ( ).getSatellitesNames ( ) );
    }

    /**
     * Lance l'estimation à partir du contrôleur parent.
     *
     * @param parent le contrôleur parent
     * @throws IOException si une exception est levée
     */
    public void runEstimation ( MainFrame parent ) throws IOException {
        this.loadEstimation ( parent );
        int j = 0;
        for (SortedSet<ObservedMeasurement<?>> measureListInit : this.model.getMeasurements ( ).getFirst ( )) {
            performEstimation ( j , measureListInit , model.getMeasurements ( ).getSecond ( ) );
            j++;
        }
    }

    /**
     * Effectue une estimation de l'orbite à partir des mesures observées pour un satellite donné.
     *
     * @param j               l'indice du satellite à estimer.
     * @param measureListInit l'ensemble des mesures observées.
     * @param states          la carte des états initiaux des satellites.
     */
    public void performEstimation ( int j , SortedSet<ObservedMeasurement<?>> measureListInit , HashMap<ObservedMeasurement, SpacecraftState> states ) {
        if (measureListInit.size ( ) > 0) {
            Propagator propagator = this.model.getPropagators ( ).get ( j );

            SpacecraftState trueState = new SpacecraftState ( propagator.getInitialState ( ).getOrbit ( ) );
            Vector3D V_InertialFrame = trueState.getPVCoordinates ( ).getVelocity ( );
            Vector3D P_InertialFrame = trueState.getPVCoordinates ( ).getPosition ( );
            double[] mean = {P_InertialFrame.getX ( ) , P_InertialFrame.getY ( ) , P_InertialFrame.getZ ( ) , V_InertialFrame.getX ( ) , V_InertialFrame.getY ( ) , V_InertialFrame.getZ ( )};
            double[] variance = {Math.pow ( this.model.stdPos , 2 ) , Math.pow ( this.model.stdPos , 2 ) , Math.pow ( this.model.stdPos , 2 ) , Math.pow ( this.model.stdV , 2 ) , Math.pow ( this.model.stdV , 2 ) , Math.pow ( this.model.stdV , 2 )};
            double[][] covariance = src.OrbitDetermination.OD.createDiagonalMatrix ( variance );
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution ( mean , covariance );
            double[] estimatedParameters = distribution.sample ( ); // Générez un nombre aléatoire selon la loi gaussienne
            Vector3D estimatedV_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 3 , 6 ) );
            Vector3D estimatedP_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 0 , 3 ) );
            PVCoordinates estimatedPV = new PVCoordinates ( estimatedP_InertialFrame , estimatedV_InertialFrame );
            TimeStampedPVCoordinates estimatedTSPV = new TimeStampedPVCoordinates ( this.model.initialDate , 1. , estimatedPV );
            KeplerianOrbit estimatedOrbit = new KeplerianOrbit ( estimatedTSPV , constants.GCRF , constants.MU );
            double[] estimatedParamOrbitaux = {estimatedOrbit.getA ( ) , estimatedOrbit.getE ( ) , Math.IEEEremainder ( estimatedOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};
            double estimatedA = estimatedParamOrbitaux[0];
            double estimatedE = estimatedParamOrbitaux[1];
            double estimatedI = estimatedParamOrbitaux[2];
            double estimatedRaan = estimatedParamOrbitaux[3];
            double estimatedPa = estimatedParamOrbitaux[4];
            double estimatedAnomaly = estimatedParamOrbitaux[5];
            KeplerianOrbit estimatedOrbit__ = new KeplerianOrbit ( estimatedA , estimatedE , estimatedI , estimatedPa , estimatedRaan , estimatedAnomaly , constants.type , constants.GCRF , this.model.initialDate , Constants.EGM96_EARTH_MU );
            CartesianOrbit estimatedOrbit_ = new CartesianOrbit ( estimatedOrbit__ );


            double prop_min_step = 0.001;// # s
            double prop_max_step = 300.0;// # s
            double prop_position_error = 10.0;// # m
            double estimator_position_scale = 1.0;// # m
            DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder ( prop_min_step , prop_max_step , prop_position_error );
            NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder ( estimatedOrbit_ , integratorBuilder , constants.type , estimator_position_scale );

            ObservableSatellite sat = new ObservableSatellite ( 0 );
            SortedSet<ObservedMeasurement<?>> measureList = new TreeSet<ObservedMeasurement<?>> ( );
            for (ObservedMeasurement<?> measure : measureListInit) {
                AngularAzEl m = new AngularAzEl ( ((AngularAzEl) measure).getStation ( ) , measure.getDate ( ) , measure.getObservedValue ( ) , measure.getTheoreticalStandardDeviation ( ) , measure.getBaseWeight ( ) , sat );
                measureList.add ( m );
                model.getObservedSat ( ).add ( this.model.satellitesNames.get ( j ) );
            }

            OD estimator = new OD ( this.model.getSatellites ( ).get ( j ) , propagator , numericalPropagatorBuilder , measureList , this.model.getInitialDate ( ) , this.model.getFinalDate ( ) , this.model.stdPos , this.model.stdV );


            RealMatrix initialP = MatrixUtils.createRealDiagonalMatrix ( estimator.variance );
            // process noise matrix
            double[] q = {this.model.noiseLevelPos , this.model.noiseLevelPos , this.model.noiseLevelPos , this.model.noiseLevelV , this.model.noiseLevelV , this.model.noiseLevelV};
            RealMatrix Q = MatrixUtils.createRealDiagonalMatrix ( q );
            ConstantProcessNoise processNoise = new ConstantProcessNoise ( initialP , Q );

            LinkedHashMap<ObservedMeasurement<?>, Propagator> estimation = estimator.Kalman ( processNoise );

            for (Map.Entry<ObservedMeasurement<?>, Propagator> entry : estimation.entrySet ( )) {
                this.model.estimationsList.add ( "Sat " + model.getSatellitesNames ( ).get ( j ) + ": " + Arrays.toString ( paramOrbitaux ( entry.getValue ( ).getInitialState ( ).getOrbit ( ) ) ) );

            }

        }
    }
}
