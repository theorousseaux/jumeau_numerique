package src.Kalman;

import java.io.File;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.JDKRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.iod.IodLaplace;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.AngularRaDec;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.estimation.sequential.UnivariateProcessNoise;
import org.orekit.forces.drag.DragForce;
import org.orekit.forces.drag.IsotropicDrag;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.models.earth.atmosphere.SimpleExponentialAtmosphere;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.KeplerianPropagatorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.utils.ParameterDriver;
import org.orekit.utils.ParameterDriversList;
import org.orekit.utils.TimeStampedPVCoordinates;
import org.orekit.bodies.CelestialBodyFactory;

public class brouillon_Kalman {

    public static void main(String[] args) throws Exception
    {
    	
    	//importation des donnees de base (toujour mettre ça en début de programme)
    	File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
    	
/******************************************************************************************************************************************************************************************************/
    	// ORBITE SIMULEE
/******************************************************************************************************************************************************************************************************/
    	
    	// ISS
    	double i = 51.6416*Math.PI/180;
    	double raan = 247.4627*Math.PI/180;
    	double e = 0.0006703;//(enlever le 1 normalement)
    	double pa = 130.5360*Math.PI/180;
    	//double anomaly = 22.50288*Math.PI/180;//20 ça marche
    	double anomaly = 261.6196828895407;//261.6196828895407;
    	double rev_day = 15.72125391;
    	double T = 3600*24/rev_day;
    	double a = Math.cbrt(Math.pow(T, 2)*constants.mu/(4*Math.pow(Math.PI,2))); //6730.960 km, soit h=352.823 km
    	double[] listParamOrbitaux = {a,e,Math.IEEEremainder(i, 2*Math.PI),Math.IEEEremainder(raan, 2*Math.PI),Math.IEEEremainder(pa, 2*Math.PI),Math.IEEEremainder(anomaly, 2*Math.PI)};
    	//AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 25, constants.utc);
    	AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);//AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
    	
    	/*
    	//AbsoluteDate date = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
    	SpacecraftState st = new SpacecraftState(initialOrbit);
    	//SpacecraftState st = propagator.propagate(date);
    	PVCoordinates ff = st.getPVCoordinates(constants.itrf);
    	Vector3D v3 = ff.getPosition();
    	System.out.println(v3.getAlpha()*180/Math.PI);//longitude
    	System.out.println(v3.getDelta()*180/Math.PI);//latitude
    	*/
    	
    	// Geometrie
    	double crossSection = 0.004;
    	double dragCoeff = 1.;
    	
    	double t = initialOrbit.getKeplerianPeriod();

    	AbsoluteDate finalDate = initialDate.shiftedBy(t * 450.); // on propagera sur 300 orbites
    	
    	ObservableSatellite satellite = new ObservableSatellite(0); 
    	
/******************************************************************************************************************************************************************************************************/
    	// PROPAGATEURS +++
/******************************************************************************************************************************************************************************************************/

    	// propagateur Keplerien
    	final KeplerianPropagator propagator = new KeplerianPropagator(initialOrbit);
    	
    	//AbsoluteDate date = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
    	//SpacecraftState st = propagator.propagate(date);
    	//System.out.println(((KeplerianOrbit) st.getOrbit()).getAnomaly(constants.type));

    	// propagateur numérique (+ modèle atmosphérique)
    	numericalPropagator atm_propagator_model = new numericalPropagator(initialOrbit);
    	NumericalPropagator atm_propagator = atm_propagator_model.getNumericalPropagator();
    	//atm_propagator.setOrbitType(OrbitType.CIRCULAR);
    	// on le complexifie :
    	// potentiel terrestre
    	NormalizedSphericalHarmonicsProvider gravityProvider = GravityFieldFactory.getNormalizedProvider(8, 8);
    	atm_propagator.addForceModel(new HolmesFeatherstoneAttractionModel(constants.earthShape.getBodyFrame(), gravityProvider));
    	// atmosphère :
    	CustomIsotropicDrag dragModel = new CustomIsotropicDrag(crossSection, dragCoeff); // propriété du satellite !!!!!!!!!!!!!!!! 
    	DragForce dragForce = new DragForce(constants.atmosphere, dragModel);
    	//atm_propagator.addForceModel(dragForce);

/******************************************************************************************************************************************************************************************************/
    	// STATIONS SOLS
/******************************************************************************************************************************************************************************************************/
    	
    	//Station sol
    	// Paris
    	station parisModel = new station("Paris", 48.866667*Math.PI/180, 2.333333*Math.PI/180);
    	GroundStation parisStation = parisModel.getGroundStation();
    	//autre
    	//station parisModel = new station("Paris", 20.875783154826724*Math.PI/180, 137.0978027323785*Math.PI/180);
    	//GroundStation parisStation = parisModel.getGroundStation();
    	//Singapour
    	station singapourModel = new station("Singapour", 1.352083*Math.PI/180, 103.819836*Math.PI/180);
    	GroundStation singapourStation = singapourModel.getGroundStation();    	

/******************************************************************************************************************************************************************************************************/
    	// MESURES IOD
/******************************************************************************************************************************************************************************************************/
    	/*
    	System.out.println("IOD MEASUREMENTS");

    	//Mesures
    	telescope2 parisTelescope2 = new telescope2(parisStation, satellite, propagator);
    	EventBasedScheduler parisScheduler2 = parisTelescope2.getScheduler();

    	Generator iodGenerator = new Generator();
    	iodGenerator.addPropagator(propagator);
    	iodGenerator.addScheduler(parisScheduler2);
    	System.out.println("start propagation");
    	SortedSet<ObservedMeasurement<?>> iod_list_measurement = iodGenerator.generate(initialDate, finalDate);
    	Integer nbMeas = 0;
    	ObservedMeasurement<?>[] measures3 = new ObservedMeasurement<?>[3];
    	for(ObservedMeasurement<?> measure : iod_list_measurement) {
    		if(nbMeas<3) {
    			measures3[nbMeas] = measure;
    			nbMeas = nbMeas + 1;
    		}
    		System.out.println(measure.getDate());
    		System.out.println(Arrays.stream(measure.getObservedValue()).map(iii -> iii * 180/Math.PI).boxed().collect(Collectors.toList()));
    		System.out.println(((AngularRaDec) measure).getStation().getBaseFrame().getName());
    	}
    	System.out.println("stop propagation");
		*/
/******************************************************************************************************************************************************************************************************/
    	// IOD
/******************************************************************************************************************************************************************************************************/
    	System.out.println("IOD");

    	//V1 :
    	/*  	
    	IodLaplace iodLaplace = new IodLaplace(constants.mu);
    	
    	//AngularRaDec raDec1 = AzEl_to_RaDec((AngularAzEl) measures3[0]);
    	//AngularRaDec raDec2 = AzEl_to_RaDec((AngularAzEl) measures3[1]);
    	//AngularRaDec raDec3 = AzEl_to_RaDec((AngularAzEl) measures3[2]);
    	AngularRaDec raDec1 = (AngularRaDec) measures3[0];
    	AngularRaDec raDec2 = (AngularRaDec) measures3[1];
    	AngularRaDec raDec3 = (AngularRaDec) measures3[2];

    	TimeStampedPVCoordinates obsPva = parisStation.getBaseFrame().getPVCoordinates(raDec2.getDate(), constants.gcrf);
        CartesianOrbit iodOrbit = iodLaplace.estimate(constants.gcrf, obsPva, raDec1, raDec2, raDec3);
        double[] variance = {Math.pow(a*0.00001,2),Math.pow(e*0.01,2),Math.pow(i*0.001,2),Math.pow(raan*0.001,2),Math.pow(pa*0.001,2),Math.pow(anomaly*0.001,2)}; // bof...

        System.out.println("RESULTS (true/iod)");
    	System.out.println(Arrays.toString(listParamOrbitaux));
    	System.out.println(Arrays.toString(paramOrbitaux(iodOrbit)));
    	*/

    	//V2 :
    	
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
		
    	//V2bis :
    	/*
		double[] mean = {a, e, i, raan, pa, anomaly};
    	double[] variance = {Math.pow(a*0.1,2),Math.pow(e*0.01,2),Math.pow(i*0.01,2),Math.pow(raan*0.01,2),Math.pow(pa*0.01,2),Math.pow(anomaly*0.01,2)};
    	double[][] covariance = createDiagonalMatrix(variance); // matrice de covariance
    	MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, covariance);
    	double[] new_param = distribution.sample(); // Générez un nombre aléatoire selon la loi gaussienne
    	double aIod = new_param[0];
    	double eIod = new_param[1];
    	double iIod = new_param[2];
    	double raanIod = new_param[3];
    	double paIod = new_param[4];
    	double anomalyIod = new_param[5];
		*/
    	
    	//V3 :
    	/*
    	double[] variance = {0,0,0,0,0,0};
    	double aIod = a;
    	double eIod = e;
    	double iIod = i;
    	double raanIod = raan;
    	double paIod = pa;
    	double anomalyIod = anomaly;
    	*/
    	
    	
    	double[] listNewParamOrbitaux = {aIod,eIod,Math.IEEEremainder(iIod, 2*Math.PI),Math.IEEEremainder(raanIod, 2*Math.PI),Math.IEEEremainder(paIod, 2*Math.PI),Math.IEEEremainder(anomalyIod, 2*Math.PI)};
    	
    	System.out.println("RESULTS (true/iod)");
    	System.out.println(Arrays.toString(listParamOrbitaux));
    	System.out.println(Arrays.toString(listNewParamOrbitaux));
    	
    	KeplerianOrbit iodOrbitK = new KeplerianOrbit(aIod, eIod, iIod, paIod, raanIod, anomalyIod, constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
    	CartesianOrbit iodOrbit =new CartesianOrbit(iodOrbitK);
    	
    	incertitudes(propagator.getInitialState(), new SpacecraftState(iodOrbit));

/******************************************************************************************************************************************************************************************************/
    	// MESURES OD
/******************************************************************************************************************************************************************************************************/

    	System.out.println("OD MEASUREMENTS");
    	
    	//Mesures
    	telescope parisTelescope = new telescope(parisStation, satellite, propagator);
    	EventBasedScheduler parisScheduler = parisTelescope.getScheduler();
    	telescope singapourTelescope = new telescope(singapourStation, satellite, propagator);
    	EventBasedScheduler singapourScheduler = singapourTelescope.getScheduler();

    	// on peut ajouter plein de propagateurs et de event based scheduler au generator : solution ?
    	//Generator
    	Generator generator = new Generator(); // regarder ce qu'on peut rajouter là dedans : plusieurs satellites ou plusieurs stations sol ?
    	generator.addPropagator(propagator);//on renseigne 2 fois le propagateur ?
    	generator.addScheduler(parisScheduler);
    	generator.addScheduler(singapourScheduler);
    	AbsoluteDate odInitialDate = initialDate;//new AbsoluteDate(2018, 6, 25, constants.utc);
    	AbsoluteDate odFinalDate = finalDate;//odInitialDate.shiftedBy(t * 450.); 
    	System.out.println("start propagation");
    	
    	SortedSet<ObservedMeasurement<?>> list_measurement = generator.generate(odInitialDate, odFinalDate);
    	for(ObservedMeasurement<?> measure : list_measurement) {
    		System.out.println(measure.getDate());
    		System.out.println(Arrays.stream(measure.getObservedValue()).map(iii -> iii * 180/Math.PI).boxed().collect(Collectors.toList()));
    		System.out.println(((AngularAzEl) measure).getStation().getBaseFrame().getName());
    	}
    	System.out.println("stop propagation");

/******************************************************************************************************************************************************************************************************/
    	// OD
/******************************************************************************************************************************************************************************************************/

    	System.out.println("OD");
    	
/******************************************************************************************************************************************************************************************************/
    	// PROPAGATEURS ---
/******************************************************************************************************************************************************************************************************/

    	// numérique
    	double prop_min_step = 0.001;// # s
    	double prop_max_step = 300.0;// # s
    	double prop_position_error = 10.0;// # m
    	double estimator_position_scale = 1.0;// # m
    	DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder(prop_min_step, prop_max_step, prop_position_error);
    	NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder(iodOrbit, integratorBuilder, constants.type, estimator_position_scale);

    	// propagateur numérique (+ modèle atmosphérique)
    	NumericalPropagatorBuilder atm_numericalPropagatorBuilder = new NumericalPropagatorBuilder(iodOrbit, integratorBuilder, constants.type, 1.0);
    	atm_numericalPropagatorBuilder.addForceModel(new HolmesFeatherstoneAttractionModel(constants.earthShape.getBodyFrame(), gravityProvider));
    	//ParameterDriver crossSectionv2 = new ParameterDriver("CSM", 0.004, 1., 0., 10.);
    	//crossSectionv2.setSelected(true);
    	CustomIsotropicDrag dragModelv2 = new CustomIsotropicDrag(0.004, dragCoeff); // propriété du satellite !!!!!!!!!!!!!!!! 
    	DragForce dragForcev2 = new DragForce(constants.atmosphere, dragModelv2);
    	dragModelv2.getDragParametersDrivers().get(0).setSelected(true);
    	atm_numericalPropagatorBuilder.addForceModel(dragForcev2);
    	
/******************************************************************************************************************************************************************************************************/
    	// BLS Method
/******************************************************************************************************************************************************************************************************/
    	/*
    	System.out.println("BLS METHOD");
    	QRDecomposer matrixDecomposer = new QRDecomposer(1e-11);
    	LeastSquaresOptimizer optimizer = new GaussNewtonOptimizer(matrixDecomposer, false);
    	BatchLSEstimator BLSEstimator  = new BatchLSEstimator(optimizer, numericalPropagatorBuilder);
    	for(ObservedMeasurement<?> measure : list_measurement) {
    		BLSEstimator.addMeasurement(measure);
    	}
    	double estimator_convergence_thres = 1e-3;
    	int estimator_max_iterations = 25;
    	int estimator_max_evaluations = 35;
    	BLSEstimator.setParametersConvergenceThreshold(estimator_convergence_thres);
    	BLSEstimator.setMaxIterations(estimator_max_iterations);
    	BLSEstimator.setMaxEvaluations(estimator_max_evaluations);


    	Propagator[] propagatorEstimated_LSE = BLSEstimator.estimate();
    	//Estimation des params
    	System.out.println("RESULTS (true/od)");
    	System.out.println(Arrays.toString(paramOrbitaux(propagator.propagate(propagatorEstimated_LSE[0].getInitialState().getDate()).getOrbit())));
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated_LSE[0].getInitialState().getOrbit())));    	
    	incertitudes(propagator.propagate(propagatorEstimated_LSE[0].getInitialState().getDate()), propagatorEstimated_LSE[0].getInitialState());

    	System.out.println(BLSEstimator.getPropagatorParametersDrivers(true).getNbParams());//getDrivers().get(0).getRawDrivers().get(0).getValue());
    	//RealMatrix physicalCovariance = BLSEstimator.getPhysicalCovariances(0);
    	*/

/******************************************************************************************************************************************************************************************************/
    	// Kalman method
/******************************************************************************************************************************************************************************************************/
    	
    	System.out.println("KALMAN");

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
        
        
    	        
    	KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder();
    	ConstantProcessNoise processNoise = new ConstantProcessNoise(initialP, Q);
    	estimatorBuilder.addPropagationConfiguration(numericalPropagatorBuilder, processNoise);
    	//estimatorBuilder.estimatedMeasurementsParameters(estimatedMeasurementsParameters, new ConstantProcessNoise(measurementP, measurementQ));
    	KalmanEstimator kalmanEstimator = estimatorBuilder.build();

    	//Propagator[] propagatorEstimated_Kalman = kalmanEstimator.estimationStep(list_measurement.first());
    	Propagator[] propagatorEstimated_Kalman = kalmanEstimator.processMeasurements(list_measurement);
    	//Estimation des params
    	System.out.println("RESULTS (true/od)");
    	//System.out.println(Arrays.toString(listParamOrbitaux));
    	System.out.println(Arrays.toString(paramOrbitaux(propagator.propagate(propagatorEstimated_Kalman[0].getInitialState().getDate()).getOrbit())));
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated_Kalman[0].getInitialState().getOrbit())));
    	incertitudes(propagator.propagate(propagatorEstimated_Kalman[0].getInitialState().getDate()), propagatorEstimated_Kalman[0].getInitialState());
    	
    	System.out.println(kalmanEstimator.getPropagationParametersDrivers(true).getNbParams());
    	//System.out.println(kalmanEstimator.getPropagationParametersDrivers(true).findByName("CSM").getValue());
    	//System.out.println(kalmanEstimator.getPropagationParametersDrivers(true).getDrivers().get(0).getValue());
    	// comment il crée la matrice de covariance pour les mesures dans le KALMAN ?
    	// comprendre P et Q
    	
    	
    } 

    static double[] paramOrbitaux(Orbit orbit){
    	KeplerianOrbit nOrbit = new KeplerianOrbit(orbit);
		double[] param = {nOrbit.getA(), nOrbit.getE(), Math.IEEEremainder(nOrbit.getI(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getRightAscensionOfAscendingNode(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getPerigeeArgument(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getAnomaly(constants.type), 2 * Math.PI)};
		return param;
    }
    
    static double[][] createDiagonalMatrix(double[] diagonal) {
        int size = diagonal.length;
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = diagonal[i];
        }
        return matrix;
    }
    
    static AngularRaDec AzEl_to_RaDec(AngularAzEl measure) {
    	double Az = measure.getObservedValue()[0];
    	double El = measure.getObservedValue()[1];
		Frame topocentricFrame = measure.getStation().getBaseFrame();
		AbsoluteDate date = measure.getDate();

		Transform transform = topocentricFrame.getTransformTo(constants.gcrf, date);
		Vector3D topocentricDirection = new Vector3D(FastMath.cos(El)*FastMath.cos(Az), FastMath.cos(El)*FastMath.sin(Az), FastMath.sin(El));
		Vector3D inertialDirection = transform.transformVector(topocentricDirection);
		double declination = inertialDirection.getDelta();
		double rightAscension = inertialDirection.getAlpha();
		double[] RaDecList = {rightAscension, declination};
		AngularRaDec RaDec = new AngularRaDec(measure.getStation(), constants.gcrf, date, RaDecList, measure.getTheoreticalStandardDeviation(), measure.getBaseWeight(), measure.getSatellites().get(0));
		return RaDec;
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
    
    static void incertitudes2(Propagator propagator1, Orbit orbit2) {
    	SpacecraftState stateBefore = propagator1.getInitialState();
  	  	Vector3D deltaVInInertialFrame = orbit2.getPVCoordinates().getVelocity().
  	                                   subtract(stateBefore.getPVCoordinates().getVelocity());
  	  	Vector3D deltaPInInertialFrame = orbit2.getPVCoordinates().getPosition().
                subtract(stateBefore.getPVCoordinates().getPosition());
  	  	Transform inertialToSpacecraftFrame = stateBefore.toTransform();
  	  	Vector3D deltaVProjected = inertialToSpacecraftFrame.transformVector(deltaVInInertialFrame);
  	  	Vector3D deltaPProjected = inertialToSpacecraftFrame.transformVector(deltaPInInertialFrame);
  	  	System.out.println("norm of deltaV(m/s) : " + deltaVProjected.getNorm());
  	  	System.out.println("norm of deltaP(m) : " + deltaPProjected.getNorm());
    }
}


