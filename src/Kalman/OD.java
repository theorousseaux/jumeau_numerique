package src.Kalman;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.SortedSet;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.Array2DRowRealMatrix;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.orekit.attitudes.LofOffset;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.AbstractKalmanEstimator;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.frames.LOFType;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.Transform;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.CartesianDerivativesFilter;
import org.orekit.utils.TimeStampedPVCoordinates;

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
    
    public LinkedHashMap<ObservedMeasurement<?>, Propagator> Kalman(ConstantProcessNoise processNoise) { //en vrai, ça peut marcher pour plein de propagateurs à la fois
    	System.out.println("KALMAN");

    	KalmanEstimatorBuilder estimatorBuilder = new KalmanEstimatorBuilder();

    	estimatorBuilder.addPropagationConfiguration(estimatedPropagatorBuilder, processNoise);
    	KalmanEstimator kalmanEstimator = estimatorBuilder.build();
    	
    	LinkedHashMap<ObservedMeasurement<?>, Propagator> mapMeasurePropagator = new LinkedHashMap<ObservedMeasurement<?>, Propagator>();
    	
    	for(ObservedMeasurement<?> measure : measurementsSet) {
    		Propagator[] propagatorEstimated = kalmanEstimator.estimationStep(measure);
    		mapMeasurePropagator.put(measure, propagatorEstimated[0]);
    		//RESULTAT
    		//covarianceAnalysis(propagatorEstimated[0], kalmanEstimator);
        	results(propagator, propagatorEstimated);
    	}
    	
    	return mapMeasurePropagator;
    }



    static void results(Propagator propagator, Propagator[] propagatorEstimated) {
    	System.out.println("COMPARISON (true/OD) :");
    	System.out.println(propagatorEstimated[0].getInitialState().getDate());
    	System.out.println(Arrays.toString(paramOrbitaux(propagator.propagate(propagatorEstimated[0].getInitialState().getDate()).getOrbit())));
    	System.out.println(Arrays.toString(paramOrbitaux(propagatorEstimated[0].getInitialState().getOrbit())));    	
    	incertitudes(propagator,propagatorEstimated[0]);
    	//incertitudes2(propagator,propagatorEstimated[0]);
    	//incertitudes3(propagator,propagatorEstimated[0]);
    }
    
    static void covarianceAnalysis(Propagator estimatedPropagator, AbstractKalmanEstimator estimator) {
    	System.out.println("COVARIANCE :");
    	LocalOrbitalFrame LOFrame = new LocalOrbitalFrame(constants.gcrf, LOFType.LVLH, estimatedPropagator, "LOF");
    	RealMatrix covMat_eci = estimator.getPhysicalEstimatedCovarianceMatrix();
    	Transform eci2lof_frozen = constants.gcrf.getTransformTo(LOFrame, estimatedPropagator.getInitialState().getDate()).freeze();    	
    	double[][] jacobian = new double[6][6];
    	eci2lof_frozen.getJacobian(CartesianDerivativesFilter.USE_PV, jacobian);
    	RealMatrix jacobianMat = new Array2DRowRealMatrix(jacobian);
    	RealMatrix covMat_lof = jacobianMat.transpose().multiply(covMat_eci).multiply(jacobianMat);
    	for(int i=0;i<6;i++) {
    		System.out.printf("%.2f ; %.2f ; %.2f ; %.2f ; %.2f ; %.2f %n",covMat_eci.getData()[i][0],covMat_eci.getData()[i][1],covMat_eci.getData()[i][2],covMat_eci.getData()[i][3],covMat_eci.getData()[i][4],covMat_eci.getData()[i][5]);
       	}
    	System.out.println("-------------------------------------------------------");
    	for(int i=0;i<6;i++) {
    		System.out.printf("%.2f ; %.2f ; %.2f ; %.2f ; %.2f ; %.2f %n",covMat_lof.getData()[i][0],covMat_lof.getData()[i][1],covMat_lof.getData()[i][2],covMat_lof.getData()[i][3],covMat_lof.getData()[i][4],covMat_lof.getData()[i][5]);
       	}
    }

    static double[] paramOrbitaux(Orbit orbit){
    	KeplerianOrbit nOrbit = new KeplerianOrbit(orbit);
		double[] param = {nOrbit.getA(), nOrbit.getE(), Math.IEEEremainder(nOrbit.getI(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getRightAscensionOfAscendingNode(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getPerigeeArgument(), 2 * Math.PI), Math.IEEEremainder(nOrbit.getAnomaly(constants.type), 2 * Math.PI)};
		return param;
    }
    
    static void incertitudes(Propagator propagator , Propagator estimatedPropagator) {
    	LocalOrbitalFrame LOFrame = new LocalOrbitalFrame(constants.gcrf, LOFType.LVLH, propagator, "LOF");
    	AbsoluteDate date = estimatedPropagator.getInitialState().getDate();
    	SpacecraftState estimatedState = estimatedPropagator.propagate(date);
    	SpacecraftState trueState = propagator.propagate(date);
    	Transform eci2lof_frozen = constants.gcrf.getTransformTo(LOFrame, date).freeze();    	
    	Vector3D dV_eci = estimatedState.getPVCoordinates().getVelocity().subtract(trueState.getPVCoordinates().getVelocity());
    	Vector3D dP_eci = estimatedState.getPVCoordinates().getPosition().subtract(trueState.getPVCoordinates().getPosition());
    	Vector3D dV_lof = eci2lof_frozen.transformVector(dV_eci);
    	Vector3D dP_lof = eci2lof_frozen.transformVector(dP_eci);
  	  	System.out.println("norm of deltaV(m/s) : " + dV_lof.getNorm());
  	  	System.out.println("norm of deltaP(m) : " + dP_lof.getNorm());
    }
    
    static void incertitudes2(Propagator propagator , Propagator estimatedPropagator) {
    	AbsoluteDate date = estimatedPropagator.getInitialState().getDate();
    	SpacecraftState state1 = estimatedPropagator.propagate(date);
    	SpacecraftState state2 = propagator.propagate(date);
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
    
    static void incertitudes3(Propagator propagator , Propagator estimatedPropagator) {
    	AbsoluteDate date = estimatedPropagator.getInitialState().getDate();
    	SpacecraftState s1 = estimatedPropagator.propagate(date);
    	SpacecraftState s2 = propagator.propagate(date);
    	LofOffset lvlh = new LofOffset(s1.getFrame(), LOFType.LVLH);
    	SpacecraftState converted = new SpacecraftState(s1.getOrbit(), lvlh.getAttitude(s1.getOrbit(), s1.getDate(), s1.getFrame()));
    	TimeStampedPVCoordinates pv2 = converted.toTransform().transformPVCoordinates(s2.getPVCoordinates(s1.getFrame()));
    	Vector3D dV_lof = pv2.getVelocity();
    	Vector3D dP_lof = pv2.getPosition();
  	  	System.out.println("norm of deltaV(m/s) : " + dV_lof.getNorm());
  	  	System.out.println("norm of deltaP(m) : " + dP_lof.getNorm());
    }
}