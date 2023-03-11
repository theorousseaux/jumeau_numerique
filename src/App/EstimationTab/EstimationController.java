package src.App.EstimationTab;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;
import src.App.MainFrame;
import src.App.SimulationTab.SimulationModel;
import src.Data.GS.ReadGSFile;
import src.Kalman.OD;
import src.Kalman.Observation;
import src.Kalman.constants;

import java.io.IOException;
import java.util.*;

public class EstimationController {

    public EstimationModel model = new EstimationModel();


    public void loadEstimation(MainFrame parent) throws NumberFormatException, IllegalArgumentException, IOException {
                model.setSatellites(parent.simuController.model.getSatellitesNames ( ) );
        model.setPropagators ( parent.simuController.model.getSatellites ( ) );
        model.setFinalDate ( parent.simuController.model.getSimulationParameters ().getEndDate () );
        model.setInitialDate ( parent.simuController.model.getSimulationParameters ().getStartDate () );
        model.setMeasurements ( parent.simuController.model.getMeasurementsSetsList () );
    }
    public void runEstimation( MainFrame parent) throws IOException {
        this.loadEstimation ( parent );
        int j = 0;
        double stdPos = 100;
        double stdV = 0.01;
        double noiseLevelPos = 0.01;
        double noiseLevelV = 0.001;
        System.out.println("Nb propagators : " + model.getPropagators ().size ());
        System.out.println("Nb sorted sets : " + model.getMeasurements ().size());
        System.out.println("Nb satellites : "+model.getSatellites ().size());
        for (SortedSet<ObservedMeasurement<?>> measure : this.model.getMeasurements ( )){
            System.out.println ( j );
            System.out.println( measure.size());
            if (measure.size()>1) {
                Propagator propagator = this.model.getPropagators ( ).get ( j );

                SpacecraftState trueState = new SpacecraftState ( propagator.getInitialState ( ).getOrbit ( ) );
                Vector3D V_InertialFrame = trueState.getPVCoordinates ( ).getVelocity ( );
                Vector3D P_InertialFrame = trueState.getPVCoordinates ( ).getPosition ( );
                double[] mean = {P_InertialFrame.getX ( ) , P_InertialFrame.getY ( ) , P_InertialFrame.getZ ( ) , V_InertialFrame.getX ( ) , V_InertialFrame.getY ( ) , V_InertialFrame.getZ ( )};
                double[] variance = {Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdPos , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 ) , Math.pow ( stdV , 2 )};
                double[][] covariance = src.Kalman.simu.createDiagonalMatrix ( variance );
                MultivariateNormalDistribution distribution = new MultivariateNormalDistribution ( mean , covariance );
                double[] estimatedParameters = distribution.sample ( ); // Générez un nombre aléatoire selon la loi gaussienne
                Vector3D estimatedV_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 3 , 6 ) );
                Vector3D estimatedP_InertialFrame = new Vector3D ( Arrays.copyOfRange ( estimatedParameters , 0 , 3 ) );
                PVCoordinates estimatedPV = new PVCoordinates ( estimatedP_InertialFrame , estimatedV_InertialFrame );
                TimeStampedPVCoordinates estimatedTSPV = new TimeStampedPVCoordinates ( this.model.initialDate , 1. , estimatedPV );
                KeplerianOrbit estimatedOrbit = new KeplerianOrbit ( estimatedTSPV , constants.gcrf , constants.mu );
                double[] estimatedParamOrbitaux = {estimatedOrbit.getA ( ) , estimatedOrbit.getE ( ) , Math.IEEEremainder ( estimatedOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( estimatedOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};
                double estimatedA = estimatedParamOrbitaux[0];
                double estimatedE = estimatedParamOrbitaux[1];
                double estimatedI = estimatedParamOrbitaux[2];
                double estimatedRaan = estimatedParamOrbitaux[3];
                double estimatedPa = estimatedParamOrbitaux[4];
                double estimatedAnomaly = estimatedParamOrbitaux[5];
                KeplerianOrbit estimatedOrbit__ = new KeplerianOrbit ( estimatedA , estimatedE , estimatedI , estimatedPa , estimatedRaan , estimatedAnomaly , constants.type , constants.gcrf , this.model.initialDate , Constants.EGM96_EARTH_MU );
                CartesianOrbit estimatedOrbit_ = new CartesianOrbit ( estimatedOrbit__ );


                double prop_min_step = 0.001;// # s
                double prop_max_step = 300.0;// # s
                double prop_position_error = 10.0;// # m
                double estimator_position_scale = 1.0;// # m
                DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder ( prop_min_step , prop_max_step , prop_position_error );
                NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder ( estimatedOrbit_ , integratorBuilder , constants.type , estimator_position_scale );


                OD estimator = new OD ( this.model.getSatellites ( ).get ( j ) , propagator , numericalPropagatorBuilder , measure , this.model.getInitialDate ( ) , this.model.getFinalDate ( ) , stdPos , stdV );


                RealMatrix initialP = MatrixUtils.createRealDiagonalMatrix ( estimator.variance );
                // process noise matrix
                double[] q = {noiseLevelPos , noiseLevelPos , noiseLevelPos , noiseLevelV , noiseLevelV , noiseLevelV};
                RealMatrix Q = MatrixUtils.createRealDiagonalMatrix ( q );
                ConstantProcessNoise processNoise = new ConstantProcessNoise ( initialP , Q );
                System.out.println ( measure );

                LinkedHashMap<ObservedMeasurement<?>, Propagator> estimation = estimator.Kalman ( processNoise );

                for (ObservedMeasurement<?> key : estimation.keySet ( )) {
                    double[] errors = OD.incertitudes ( propagator , estimation.get ( key ) );
                    System.out.println ( errors.toString ( ) );
                    this.model.estimationsList.add ( "Satellite: " + String.valueOf ( j ) + "; Date: " + key.getDate ( ).toString ( ) + "; dP: " + String.valueOf ( errors[0] ) + "; dV: " + errors[1] );
                }

            }
            j ++;
        }
        System.out.println("Simulation done");
    }
}
