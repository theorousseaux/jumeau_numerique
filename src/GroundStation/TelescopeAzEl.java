package src.GroundStation;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.measurements.generation.SignSemantic;
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
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.bodies.CelestialBodyFactory;

public class TelescopeAzEl {
    
    /** biais */
	public double[] mean;

	/** Angular incertitude of the Telescope */
	public double angularIncertitude;

	/** Covariance */
	public RealMatrix covariance;

	/** noiseSource */
	public CorrelatedRandomVectorGenerator noiseSource;

	/** sigma - normalisation. */
	public double[] sigma;

	/** GroundStation - TopocentricFrame */
	public GroundStation station;

	/** FinalDetector to determine the observation's conditions */
	public EventDetector finalDetector;

	/** Measures weight */
	public double[] baseWeight;


	/** Constructor */
    public TelescopeAzEl(double[] mean, double angularIncertitude, double[] sigma, double[] baseWeight, double latitude, double longitude, double altitude) {

		this.mean = mean;
		this.angularIncertitude = angularIncertitude;
		this.sigma = sigma;
		this.baseWeight = baseWeight;
		
		// bruit de mesures 
		double[] covarianceDiag = {angularIncertitude*3.14/180, angularIncertitude*3.14/180};//incertitude de mesure : 0.3°
    	RealMatrix covariance = new DiagonalMatrix(covarianceDiag);
    	RandomGenerator randomGenerator = new RandomDataGenerator();
    	GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
    	CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 0, gaussianRandomGenerator);//mesures parfaites:null

		this.covariance = covariance;
		this.noiseSource = noiseSource;
		
		//Celest bodies
		//Earth
		final double ae = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
		final double f = 0.;
    	OneAxisEllipsoid earthShape = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF(ITRFVersion.ITRF_2000, IERSConventions.IERS_2010, false));
		//Sun 
		PVCoordinatesProvider Sun = CelestialBodyFactory.getSun();
		AtmosphericRefractionModel refractionModel = null;

		//groundstation, topocentricFrame 
		GeodeticPoint station1Location = new GeodeticPoint(latitude, longitude, altitude) ;
    	TopocentricFrame topocentricFrame = new TopocentricFrame(earthShape, station1Location, "station");
    	GroundStation station = new GroundStation(topocentricFrame);
		
		this.station = station;

		//EventDetector, conditions for the observations
		//elevation detector
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
    	//Night detector
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
		this.finalDetector = finalDetector;
	}

	
	
	/** Method : Create scheduled observations for an orbital object */
	//////////////////////////////////////////////////////////////////
	public SortedSet<ObservedMeasurement<?>> scheduledObservations(ObservableSatellite object, Propagator propagator, FixedStepSelector dateSelector, AbsoluteDate initialDate, AbsoluteDate finalDate) throws Exception {

		AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(this.noiseSource, this.station, this.sigma, this.baseWeight, object);
		EventBasedScheduler scheduler = new EventBasedScheduler(mesuresBuilder, dateSelector, propagator, finalDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);

    	//Generator
    	Generator generator = new Generator();
    	generator.addPropagator(propagator);
    	generator.addScheduler(scheduler);

    	SortedSet<ObservedMeasurement<?>> list_measurement = generator.generate(initialDate, finalDate);
		
		return list_measurement;
	}




}
