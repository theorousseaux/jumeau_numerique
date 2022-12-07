package src.Kalman;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.measurements.generation.MeasurementBuilder;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.CovarianceMatrixProvider;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.estimation.sequential.KalmanModel;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.conversion.KeplerianPropagatorBuilder;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.events.handlers.ContinueOnEvent;
import org.orekit.propagation.events.handlers.RecordAndContinue;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.utils.ParameterDriversList;
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

    	final TimeScale utc = TimeScalesFactory.getUTC();
    	AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 25, utc);
  
    	final Frame frame = FramesFactory.getEME2000();
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, initialDate, Constants.EGM96_EARTH_MU);
    	
    	final double t = initialOrbit.getKeplerianPeriod();
    	final AbsoluteDate finalDate = initialDate.shiftedBy(t * 30.); // on propagera sur 30 orbites
    	
    	ObservableSatellite satellite = new ObservableSatellite(0); //à voir pour le 0...
    	
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
    	CorrelatedRandomVectorGenerator noiseSource = null;//mesures parfaites
    	double[] sigma = {2.,2.};//J'ai l'impression que ça a aucun effet
    	double[] baseWeight = {1.,1.};
    	AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station1, sigma, baseWeight, satellite);
    	FixedStepSelector dateSelector = new FixedStepSelector(600,utc); // une mesure toutes les 10min
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
    	for(ObservedMeasurement<?> mesure : list_measurement) {
    		System.out.println(Arrays.toString(mesure.getObservedValue()));
    		System.out.println(mesure.getDate());
    	}
    	
    	KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder();
    	// initial covariance matrix 
    	double[] ip = {0.,0.,0.,0.,0.,0.};
    	RealMatrix initialP = new DiagonalMatrix(ip);
    	// process noise matrix 
    	double[] q = {0.,0.,0.,0.,0.,0.};
    	RealMatrix Q = new DiagonalMatrix(q);
    	
    	ConstantProcessNoise processNoise = new ConstantProcessNoise(initialP, Q);
    	KeplerianPropagatorBuilder keplerianPropagatorBuilder = new KeplerianPropagatorBuilder(initialOrbit, type, 1.); //initialOrbit : reference orbit from which real orbits will be built
    	estimatorBuilder.addPropagationConfiguration(keplerianPropagatorBuilder, processNoise);
    	KalmanEstimator kalmanEstimator = estimatorBuilder.build();
    	Propagator[] propagatorEstimated = kalmanEstimator.processMeasurements(list_measurement);
    	//Estimation du paramètre i (seulement)
    	System.out.println(propagatorEstimated[0].getInitialState().getOrbit().getI());
    }	

}
