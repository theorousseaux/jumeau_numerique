package src.Kalman;

import java.util.Arrays;
import java.util.SortedSet;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.frames.Transform;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;

public class OD {
	public ObservableSatellite object;
	public Propagator propagator;
	public OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder;
	public SortedSet<ObservedMeasurement<?>> measurementsSet;
	AbsoluteDate initialDate;
	AbsoluteDate finalDate;
	

    public OD(ObservableSatellite object, Propagator propagator, OrbitDeterminationPropagatorBuilder estimatedPropagatorBuilder, SortedSet<ObservedMeasurement<?>> measurementsSet, AbsoluteDate initialDate, AbsoluteDate finalDate){
    	this.object = object;
    	this.propagator = propagator;
    	this.estimatedPropagatorBuilder = estimatedPropagatorBuilder; // MUST BE NUMERICAL ?
    	this.measurementsSet = measurementsSet;
    	this.initialDate = initialDate;
    	this.finalDate = finalDate;
    }
    
    public Propagator BLS(){
    	System.out.println("BLS METHOD");
    	
    	QRDecomposer matrixDecomposer = new QRDecomposer(1e-11);
    	LeastSquaresOptimizer optimizer = new GaussNewtonOptimizer(matrixDecomposer, false);
    	
    	BatchLSEstimator BLSEstimator  = new BatchLSEstimator(optimizer, estimatedPropagatorBuilder);
    	
    	double estimator_convergence_thres = 1e-3;
    	int estimator_max_iterations = 25;
    	int estimator_max_evaluations = 35;
    	BLSEstimator.setParametersConvergenceThreshold(estimator_convergence_thres);
    	BLSEstimator.setMaxIterations(estimator_max_iterations);
    	BLSEstimator.setMaxEvaluations(estimator_max_evaluations);
    	
    	for(ObservedMeasurement<?> measure : measurementsSet) {
    		BLSEstimator.addMeasurement(measure);
    	}

    	Propagator[] propagatorEstimated = BLSEstimator.estimate();
    	
    	//RESULTAT
    	results(propagator, propagatorEstimated);
    	
    	return propagatorEstimated[0];
    }
    
    public Propagator Kalman(ConstantProcessNoise processNoise) { //en vrai, ça peut marcher pour plein de propagateurs à la fois
    	System.out.println("KALMAN");

    	KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder();

    	estimatorBuilder.addPropagationConfiguration(estimatedPropagatorBuilder, processNoise);
    	KalmanEstimator kalmanEstimator = estimatorBuilder.build();
    	
    	Propagator[] propagatorEstimated = kalmanEstimator.processMeasurements(measurementsSet);
    	
    	//RESULTAT
    	results(propagator, propagatorEstimated);
    	
    	return propagatorEstimated[0];
    }



    static void results(Propagator propagator, Propagator[] propagatorEstimated) {
    	System.out.println("RESULTS (true/OD)");
    	System.out.println(Arrays.toString(paramOrbitaux(propagator.propagate(propagatorEstimated[0].getInitialState().getDate()).getOrbit())));
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated[0].getInitialState().getOrbit())));    	
    	incertitudes(propagator.propagate(propagatorEstimated[0].getInitialState().getDate()), propagatorEstimated[0].getInitialState());
    }

    static double[] paramOrbitaux(Orbit orbit){
    	KeplerianOrbit nOrbit = new KeplerianOrbit(orbit);
		double[] param = {nOrbit.getA(), nOrbit.getE(), Math.IEEEremainder(nOrbit.getI(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getRightAscensionOfAscendingNode(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getPerigeeArgument(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getAnomaly(constants.type), 2 * Math.PI)};
		return param;
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