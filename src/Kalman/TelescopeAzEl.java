import java.util.ArrayList;
import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;
import org.orekit.time.FixedStepSelector;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.bodies.CelestialBodyFactory;

public class TelescopeAzEl {
    	
    /** biais */
	private double[] mean;

	/** Angular incertitude of the Telescope */
	private double[] angularIncertitude;

	/** Covariance */
	private RealMatrix covariance;

	/** noiseSource */
	public CorrelatedRandomVectorGenerator noiseSource;

	/** sigma */
	public double[] sigma;

	/** Measures weight */
	public double[] baseWeight;

	/** GroundStation */
	public Station station;

	/** Limites de Field of View */
	public double[] azimuthField = {0., Math.PI};
	public double[] elevationField;
	public double elevationLimit;
	public double angularFoV;
	
	public double stepMeasure;

	/** Constructor */
    public TelescopeAzEl(double[] mean, double[] angularIncertitude, double elevationLimit, double angularFoV, double stepMeasure, Station station) {

		this.mean = mean;
		this.angularIncertitude = angularIncertitude;
		this.sigma = angularIncertitude;
		this.baseWeight = new double[]{1., 1.};
		
		this.elevationLimit = elevationLimit;
		this.angularFoV = angularFoV;
		this.elevationField = new double[]{0. + elevationLimit, Math.PI - elevationLimit};

		// bruit de mesures 
		double[] covarianceDiag = {Math.pow(angularIncertitude[0],2), Math.pow(angularIncertitude[1],2)};
    	RealMatrix covariance = MatrixUtils.createRealDiagonalMatrix(covarianceDiag);
    	RandomGenerator randomGenerator = new RandomDataGenerator();
    	GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
    	CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-10, gaussianRandomGenerator);//mesures parfaites:null
		this.covariance = covariance;
		this.noiseSource = noiseSource;

		this.station = station;
		this.stepMeasure = stepMeasure;
	}
    
    public BooleanDetector createDetector() {
    	//EventDetector, conditions for the observations
    	//elevation detector
    	//modifier ça
    	ElevationDetector elevationDetector = new ElevationDetector(station.getBaseFrame());
    	elevationDetector = elevationDetector.withHandler(
    			(s, detector, increasing) -> {
    				return increasing ? Action.CONTINUE : Action.CONTINUE;
    	        });
    	elevationDetector = elevationDetector.withConstantElevation(this.elevationLimit);
    	    	//Night detector
    	GroundAtNightDetector nightDetector = new GroundAtNightDetector(station.getBaseFrame(), constants.Sun, GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION, constants.refractionModel);
    	nightDetector = nightDetector.withHandler(
    			(s, detector, increasing) -> {
    				return increasing ? Action.CONTINUE : Action.CONTINUE;
    	        });

    	BooleanDetector finalDetector = BooleanDetector.andCombine(elevationDetector, nightDetector);
    	return finalDetector;
    }
    
    public FixedStepSelector createDateSelector() {
    	FixedStepSelector dateSelector = new FixedStepSelector(this.stepMeasure, constants.utc);
    	return dateSelector;
    }
    
    public AngularAzElBuilder createAzElBuilder(ObservableSatellite satellite) {
    	AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(this.noiseSource, this.station, this.sigma, this.baseWeight, satellite);
    	return mesuresBuilder;
    }

    public EventBasedScheduler createEventBasedScheduler(ObservableSatellite satellite, Propagator propagator) {
    	BooleanDetector detector = createDetector();
    	FixedStepSelector selector = createDateSelector();
    	AngularAzElBuilder builder  = createAzElBuilder(satellite);
    	EventBasedScheduler scheduler = new EventBasedScheduler(builder, selector, propagator, detector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
    	return scheduler;
    }
}
