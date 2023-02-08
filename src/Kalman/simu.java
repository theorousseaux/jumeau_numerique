package src.Kalman;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.frames.Transform;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;

public class simu {
	public static void main(String[] args) throws Exception{
    	
    	//importation des donnees de base (toujour mettre ça en début de programme)
    	File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
    	
    	// ORBITE SIMULEE (ISS)
    	ObservableSatellite satellite_ISS = new ObservableSatellite(0);
    	
    	double i = 51.6416*Math.PI/180;
    	double raan = 247.4627*Math.PI/180;
    	double e = 0.0006703;
    	double pa = 130.5360*Math.PI/180;
    	double anomaly = 261.6196828895407;
    	double rev_day = 15.72125391;
    	double T = 3600*24/rev_day;
    	double a = Math.cbrt(Math.pow(T, 2)*constants.mu/(4*Math.pow(Math.PI,2))); //6730.960 km, soit h=352.823 km
    	double[] listParamOrbitaux = {a,e,Math.IEEEremainder(i, 2*Math.PI),Math.IEEEremainder(raan, 2*Math.PI),Math.IEEEremainder(pa, 2*Math.PI),Math.IEEEremainder(anomaly, 2*Math.PI)};
    	AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
    	double t = initialOrbit.getKeplerianPeriod();
    	final KeplerianPropagator propagator_ISS = new KeplerianPropagator(initialOrbit);
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////
    	SpacecraftState state1 = new SpacecraftState(initialOrbit);
    	Vector3D VInInertialFrame = state1.getPVCoordinates().getVelocity();
    	Vector3D PInInertialFrame = state1.getPVCoordinates().getPosition();
    	double[] mean = {PInInertialFrame.getX(), PInInertialFrame.getY(), PInInertialFrame.getZ(), VInInertialFrame.getX(), VInInertialFrame.getY(), VInInertialFrame.getZ()};
    	//double[] variance = {Math.pow(0.01,2),Math.pow(0.01,2),Math.pow(0.01,2),Math.pow(0.01,2),Math.pow(0.01,2),Math.pow(0.01,2)};
    	double[] variance = {.1e-4, 4.e-3, 1.e-3, 5.e-3, 6.e-5, 1.e-4};
    	double[][] covariance = createDiagonalMatrix(variance); // matrice de covariance
    	MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, covariance);
    	double[] new_param = distribution.sample(); // Générez un nombre aléatoire selon la loi gaussienne
    	Vector3D VIODInInertialFrame = new Vector3D(Arrays.copyOfRange(new_param,3,6));
    	Vector3D PIODInInertialFrame = new Vector3D(Arrays.copyOfRange(new_param,0,3));
    	PVCoordinates pv = new PVCoordinates(PIODInInertialFrame, VIODInInertialFrame);
    	TimeStampedPVCoordinates tspv = new TimeStampedPVCoordinates(initialDate, 1., pv);
    	KeplerianOrbit iiodOrbit = new KeplerianOrbit(tspv, constants.gcrf, constants.mu);
    	double[] param = {iiodOrbit.getA(), iiodOrbit.getE(), Math.IEEEremainder(iiodOrbit.getI(), 2 * Math.PI), Math.IEEEremainder(iiodOrbit.getRightAscensionOfAscendingNode(), 2 * Math.PI), Math.IEEEremainder(iiodOrbit.getPerigeeArgument(), 2 * Math.PI), Math.IEEEremainder(iiodOrbit.getAnomaly(constants.type), 2 * Math.PI)};  	
    	double aIod = param[0];
    	double eIod = param[1];
    	double iIod = param[2];
    	double raanIod = param[3];
    	double paIod = param[4];
    	double anomalyIod = param[5];
    	double[] listNewParamOrbitaux = {aIod,eIod,Math.IEEEremainder(iIod, 2*Math.PI),Math.IEEEremainder(raanIod, 2*Math.PI),Math.IEEEremainder(paIod, 2*Math.PI),Math.IEEEremainder(anomalyIod, 2*Math.PI)};
    	
    	System.out.println("RESULTS (true/iod)");
    	System.out.println(Arrays.toString(listParamOrbitaux));
    	System.out.println(Arrays.toString(listNewParamOrbitaux));
    	
    	KeplerianOrbit iodOrbitK = new KeplerianOrbit(aIod, eIod, iIod, paIod, raanIod, anomalyIod, constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
    	CartesianOrbit iodOrbit =new CartesianOrbit(iodOrbitK);
    	
    	incertitudes(propagator_ISS.getInitialState(), new SpacecraftState(iodOrbit));
    	
    	
    	double prop_min_step = 0.001;// # s
    	double prop_max_step = 300.0;// # s
    	double prop_position_error = 10.0;// # m
    	double estimator_position_scale = 1.0;// # m
    	DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder(prop_min_step, prop_max_step, prop_position_error);
    	NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder(iodOrbit, integratorBuilder, constants.type, estimator_position_scale);
    	//OrbitType orbitType = OrbitType.CARTESIAN;
    	// initial covariance matrix
    	double[] ip = variance;
    	//double[] ip = {0.,0.,0.,0.,0.,0.};
    	RealMatrix initialP = MatrixUtils.createRealDiagonalMatrix(ip);
    	// process noise matrix 
    	//double[] q = {1.e-4, 1.e-4, 1.e-4, 1.e-10, 1.e-10, 1.e-10};
    	double[] q = {1.e-1, 1.e-1, 1.e-1, 1.e-3, 1.e-3, 1.e-3};
    	//double[] q = {1, 1, 1, 1, 1, 1};
    	//double[] q = {0, 0, 0, 0, 0, 0};
    	RealMatrix Q = MatrixUtils.createRealDiagonalMatrix(q);

    	/*
    	final RealMatrix measurementP = MatrixUtils.createRealDiagonalMatrix(new double [] {
    		1., 1.
    		});
    	final RealMatrix measurementQ = MatrixUtils.createRealDiagonalMatrix(new double [] {
    		1e-6, 1e-6
    		});
    	
    	final ParameterDriversList estimatedMeasurementsParameters = new ParameterDriversList();
        for (ObservedMeasurement<?> measurement : list_measurement) {
            final List<ParameterDriver> drivers = measurement.getParametersDrivers();
            for (ParameterDriver driver : drivers) {
                if (driver.isSelected()) {
                    // Add the driver
                    estimatedMeasurementsParameters.add(driver);
                }
            }
        }
        */
    	ConstantProcessNoise processNoise = new ConstantProcessNoise(initialP, Q);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	AbsoluteDate finalDate = initialDate.shiftedBy(t * 450.); // on propagera sur 450 orbites
    	
     	List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
    	List<Propagator> propagatorsList = new ArrayList<Propagator>();
    	objectsList.add(satellite_ISS);
    	propagatorsList.add(propagator_ISS);
    	
    	
    	// STATIONS ET TELESCOPES
    	Station station_Paris = new Station("PARIS", 48.866667*Math.PI/180, 2.333333*Math.PI/180, 0.);
    	//TelescopeAzEl(mean, angularIncertitude, elevationLimit, angularFoV, stepMeasure, breakTime, station)
    	station_Paris.addTelescope(new TelescopeAzEl(new double[]{0.,0.}, new double[]{0.3*Math.PI/180, 0.3*Math.PI/180}, 30*Math.PI/180, 119*Math.PI/180, 10, 10, station_Paris));
    	List<TelescopeAzEl> telescopesList = station_Paris.getListTelescope();
    	
    	Observation observation = new Observation(telescopesList, objectsList, propagatorsList, initialDate, finalDate);
    	List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList = observation.measure(true);
    	
    	
	}
	
    static double[][] createDiagonalMatrix(double[] diagonal) {
        int size = diagonal.length;
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = diagonal[i];
        }
        return matrix;
    }
    
    static void incertitudes(SpacecraftState state1, SpacecraftState state2) {
  	  	Vector3D deltaVInInertialFrame = state2.getPVCoordinates().getVelocity().
  	                                   subtract(state1.getPVCoordinates().getVelocity());
  	  	Vector3D deltaPInInertialFrame = state2.getPVCoordinates().getPosition().
                subtract(state1.getPVCoordinates().getPosition());
  	  	Transform inertialToSpacecraftFrame = state1.toTransform();
  	  	Vector3D deltaVProjected = inertialToSpacecraftFrame.transformVector(deltaVInInertialFrame);
  	  	Vector3D deltaPProjected = inertialToSpacecraftFrame.transformVector(deltaPInInertialFrame);
  	  	System.out.println("norm of deltaV(m/s) : " + deltaVProjected.getNorm());
  	  	System.out.println("norm of deltaP(m) : " + deltaPProjected.getNorm());
    }
}


