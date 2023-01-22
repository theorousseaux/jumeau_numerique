package src.Kalman;

import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.time.FixedStepSelector;

public class telescope {
	
	EventBasedScheduler scheduler;
	
	public telescope(GroundStation station, ObservableSatellite satellite, Propagator propagator) {
		double[] mean = {0.,0.}; //pas de biais
		double SIGMA = 0.3*Math.PI/180; //incertitude de mesure : 0.3° soit 0.00052 rad
		double[] covarianceDiag = {SIGMA*SIGMA,SIGMA*SIGMA};
		RealMatrix covariance = MatrixUtils.createRealDiagonalMatrix(covarianceDiag);
		RandomGenerator randomGenerator = new RandomDataGenerator();
		GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
		CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-10, gaussianRandomGenerator);//mesures parfaites:null
		double[] sigma = {SIGMA, SIGMA};//{1,1};//covarianceDiag;//ca sert juste à normaliser (?)
		double[] baseWeight = {1.,1.};
		//Mesures
		AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder(noiseSource, station, sigma, baseWeight, satellite); // Julie aura besoin d'un objet satellite
		FixedStepSelector dateSelector = new FixedStepSelector(10, constants.utc); // une mesure toutes les 10min
		//Detecteur d'elevation (quid azimuth?)
		ElevationDetector elevationDetector = new ElevationDetector(station.getBaseFrame());
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
		elevationDetector = elevationDetector.withConstantElevation(30*Math.PI/180);
		GroundAtNightDetector nightDetector = new GroundAtNightDetector(station.getBaseFrame(), constants.Sun, GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION, constants.refractionModel);
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
		this.scheduler = new EventBasedScheduler(mesuresBuilder, dateSelector, propagator, finalDetector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
	}
	
	public EventBasedScheduler getScheduler() {
		return this.scheduler;
	}
	
	
	
	
	
	
	

}
