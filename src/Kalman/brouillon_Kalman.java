package src.Kalman;

import java.io.File;

import java.util.Arrays;
import java.util.SortedSet;

import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
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
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.KeplerianPropagatorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.bodies.CelestialBodyFactory;

public class brouillon_Kalman {

    public static void main(String[] args) throws Exception
    {
    	
    	//importation des donnees de base (toujour mettre ça en début de programme)
    	File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));
    	
    	//satellite et son orbite
    	double a = (6370+2500)*2*1000;
    	double e = 0.01;
    	double i = 80*3.14/180;
    	double pa = 0.2;
    	double raan = 0.2;
    	double anomaly = 1;
    	PositionAngle type = PositionAngle.TRUE;
    	double[] listParamOrbitaux = {a,e,i,pa,raan,anomaly};

    	final TimeScale utc = TimeScalesFactory.getUTC();
    	AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 25, utc);
  
    	final Frame frame = FramesFactory.getEME2000();
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, initialDate, Constants.EGM96_EARTH_MU);
    	
    	final double t = initialOrbit.getKeplerianPeriod();
    	final AbsoluteDate finalDate = initialDate.shiftedBy(t * 30.); // on propagera sur 30 orbites
    	
    	ObservableSatellite satellite = new ObservableSatellite(0);
    	
    	//propagateur
    	final KeplerianPropagator propagator = new KeplerianPropagator(initialOrbit);
    	
    	//Corps celestes
		final double ae = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
		final double f = 0.;
    	OneAxisEllipsoid earthShape = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF(ITRFVersion.ITRF_2000, IERSConventions.IERS_2010, false));
    	PVCoordinatesProvider Sun = CelestialBodyFactory.getSun();
    	AtmosphericRefractionModel refractionModel = null;
    	
    	//Station sol
    	GeodeticPoint station1Location = new GeodeticPoint(0., 0., 0.) ;
    	TopocentricFrame topocentricFrame = new TopocentricFrame(earthShape, station1Location, "station_1");
    	GroundStation station1 = new GroundStation(topocentricFrame);
    	    	
    	//Mesures
    	double[] mean = {0.,0.};//pas de biais
    	double[] covarianceDiag = {0.3*3.14/180,0.3*3.14/180};//incertitude de mesure : 0.3°
    	RealMatrix covariance = new DiagonalMatrix(covarianceDiag);
    	RandomGenerator randomGenerator = new RandomDataGenerator();
    	GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
    	CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 0, gaussianRandomGenerator);//mesures parfaites:null
    	double[] sigma = {1,1};//covarianceDiag;//ca sert juste à normaliser (?)
    	double[] baseWeight = {1,1};
    	AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station1, sigma, baseWeight, satellite); // Julie aura besoin d'un objet satellite
    	FixedStepSelector dateSelector = new FixedStepSelector(600, utc); // une mesure toutes les 10min
    	//Detecteur d'elevation (quid azimuth?)
    	ElevationDetector elevationDetector = new ElevationDetector(topocentricFrame);
    	elevationDetector = elevationDetector.withHandler(
                (s, detector, increasing) -> {
                    System.out.println(
                            " Visibility on " +
                                    detector.getTopocentricFrame().getName() +
                                    (increasing ? " begins at " : " ends at ") +
                                    s.getDate()
                    );
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });
    	//Detecteur de nuit
    	GroundAtNightDetector nightDetector = new GroundAtNightDetector(topocentricFrame, Sun, GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION, refractionModel);
    	nightDetector = nightDetector.withHandler(
                (s, detector, increasing) -> {
                    System.out.println(
                            " Night " +
                                    (increasing ? " begins at " : " ends at ") +
                                    s.getDate()
                    );
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });

    	BooleanDetector finalDetector = BooleanDetector.andCombine(elevationDetector, nightDetector);
    	EventBasedScheduler scheduler = new EventBasedScheduler(mesuresBuilder, dateSelector, propagator, finalDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);

    	//Generator
    	Generator generator = new Generator();
    	generator.addPropagator(propagator);
    	generator.addScheduler(scheduler);
    	SortedSet<ObservedMeasurement<?>> list_measurement = generator.generate(initialDate, finalDate);
    	for(ObservedMeasurement<?> measure : list_measurement) {
    		System.out.println(Arrays.toString(measure.getObservedValue()));
    		System.out.println(measure.getDate());
    	}

    	// orbite initiale prédite ?
    	/*
    	double ai = a+1000;
    	double ei = e+0.01;
    	double ii = i+0.01;
    	double pai = pa+0.01;
    	double raani = raan+0.01;
    	double anomalyi = anomaly+0.01;
    	*/

    	double ai = a*1.001;
    	double ei = e*1.001;
    	double ii = i*1.001;
    	double pai = pa*1.001;
    	double raani = raan*1.001;
    	double anomalyi = anomaly*1.001;

    	/*
    	//BLS ne converge pas avec ça mais Kalman si:
    	double ai = a*1.01;
    	double ei = e*1.01;
    	double ii = i*1.01;
    	double pai = pa*1.01;
    	double raani = raan*1.01;
    	double anomalyi = anomaly*1.01;
    	*/
    	AbsoluteDate initialDatei = new AbsoluteDate(2014, 6, 25, utc);
    	KeplerianOrbit initialOrbiti = new KeplerianOrbit(ai, ei, ii, pai, raani, anomalyi, type, frame, initialDatei, Constants.EGM96_EARTH_MU);
    	CartesianOrbit initialOrbitiC =new CartesianOrbit(initialOrbiti);
    	
    	// Propagateur au choix :
    	// numérique :
    	double prop_min_step = 0.001;// # s
    	double prop_max_step = 300.0;// # s
    	double prop_position_error = 10.0;// # m
    	DormandPrince853IntegratorBuilder integratorBuilder = new DormandPrince853IntegratorBuilder(prop_min_step, prop_max_step, prop_position_error);
    	NumericalPropagatorBuilder numericalPropagatorBuilder = new NumericalPropagatorBuilder(initialOrbitiC, integratorBuilder, type, 1.0);
    	// ou Képlerien :
    	KeplerianPropagatorBuilder keplerianPropagatorBuilder = new KeplerianPropagatorBuilder(initialOrbiti, type, 1.0); //initialOrbit : reference orbit from which real orbits will be built

    	//BLS method
    	QRDecomposer matrixDecomposer = new QRDecomposer(1e-11);
    	LeastSquaresOptimizer optimizer = new GaussNewtonOptimizer(matrixDecomposer, false);
    	BatchLSEstimator BLSEstimator = new BatchLSEstimator(optimizer, numericalPropagatorBuilder);
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
    	System.out.println("Batch Least Square Method");
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated_LSE[0].getInitialState().getOrbit())));
    	System.out.println(Arrays.toString(listParamOrbitaux));
    	//RealMatrix physicalCovariance = BLSEstimator.getPhysicalCovariances(0);

    	// Kalman method

    	// initial covariance matrix
    	//double[] ip = {1e-14,1e-14,1e-14,1e-14,1e-14,1e-14};
    	double[] ip = {2000,0.02,0.02,0.02,0.02,0.02};
    	RealMatrix initialP = new DiagonalMatrix(ip);
    	// process noise matrix 
    	double[] q = {1e-13,1e-13,1e-13,1e-13,1e-13,1e-13};
    	RealMatrix Q = new DiagonalMatrix(q);
    	
    	System.out.println("Kalman Method");
    	KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder();
    	ConstantProcessNoise processNoise = new ConstantProcessNoise(initialP, Q);
    	estimatorBuilder.addPropagationConfiguration(numericalPropagatorBuilder, processNoise);
    	KalmanEstimator kalmanEstimator = estimatorBuilder.build();
    	Propagator[] propagatorEstimated_Kalman = kalmanEstimator.processMeasurements(list_measurement);
    	//Estimation des params
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated_Kalman[0].getInitialState().getOrbit())));
    	System.out.println(Arrays.toString(listParamOrbitaux));        
    	// comment il crée la matrice de covariance pour les mesures dans le KALMAN ?
    	// comprendre P et Q
    }

    static double[] paramOrbitaux(Orbit orbit){
    	KeplerianOrbit nOrbit = new KeplerianOrbit(orbit);
		double[] param = {nOrbit.getA(), nOrbit.getE(), nOrbit.getI(), nOrbit.getRightAscensionOfAscendingNode(), nOrbit.getPerigeeArgument()};
		return param;
    }
}
