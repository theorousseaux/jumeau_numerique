package src.Kalman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.geometry.fov.FieldOfView;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeComponents;
import org.orekit.utils.PVCoordinatesProvider;

public class TelescopeAzEl {
	
	public String ID;

	/** noiseSource */
	public CorrelatedRandomVectorGenerator noiseSource;

	/** sigma */
	public double[] sigma;

	/** Measures weight */
	public double[] baseWeight;

	/** GroundStation */
	public Station station;

	/** Limites de Field of View */
	public double[] azimuthField;
	public double[] elevationField;

	public double angularFoV; // cote du carre du FoV

	public double stepMeasure;
	
	public int breakTime;
	
	LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap;

	/** Constructor */
    public TelescopeAzEl(String ID, double[] mean, double[] angularIncertitude, double elevationLimit, double angularFoV, double stepMeasure, int breakTime) {

    	this.ID  = ID;

		this.sigma = angularIncertitude;
		this.baseWeight = new double[]{1., 1.};

		// ATTENTION, ne marche pas si angularFoV>180-2*limitElevation
		this.angularFoV = angularFoV;
		this.azimuthField = new double[]{0., Math.PI};
		this.elevationField = new double[]{0. + elevationLimit, Math.PI - elevationLimit};

		// bruit de mesures
		double[] covarianceDiag = {Math.pow(angularIncertitude[0],2), Math.pow(angularIncertitude[1],2)};
    	RealMatrix covariance = MatrixUtils.createRealDiagonalMatrix(covarianceDiag);
    	RandomGenerator randomGenerator = new RandomDataGenerator();
    	GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
    	CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-10, gaussianRandomGenerator);//mesures parfaites:null
		this.noiseSource = noiseSource;

		this.station = null;
		this.stepMeasure = stepMeasure;
		this.breakTime = breakTime;
		
		this.skyCoveringMap = createSkyCoveringMap();
	}
    
    public void updateStation(Station station) {
    	this.station = station;
    }

    public BooleanDetector createDetector(LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap) {
    	//EventDetector, conditions for the observations
    	//elevation detector
    	//un detecteur pour plein de satellites ?
    	ElevationDetector elevationDetector = new ElevationDetector(station.getBaseFrame()); //visible quand positif
    	elevationDetector = elevationDetector.withHandler(
    			(s, detector, increasing) -> {
    				return increasing ? Action.CONTINUE : Action.CONTINUE;
    	        });
    	elevationDetector = elevationDetector.withConstantElevation(elevationField[0]);
    	//Night detector
    	GroundAtNightDetector nightDetector = new GroundAtNightDetector(station.getBaseFrame(), constants.Sun, GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION, constants.refractionModel); // nuit quand positif
    	nightDetector = nightDetector.withHandler(
    			(s, detector, increasing) -> {
    				return increasing ? Action.CONTINUE : Action.CONTINUE;
    	        });
    	//FOV detector
    	CustomGroundFieldOfViewDetector fovDetector = new CustomGroundFieldOfViewDetector(station.getBaseFrame(), skyCoveringMap); // positif quand c'est visible
    	fovDetector = fovDetector.withHandler(
    			(s, detector, increasing) -> {
    				return increasing ? Action.CONTINUE : Action.CONTINUE;
    	        });

    	BooleanDetector finalDetector = BooleanDetector.andCombine(elevationDetector, nightDetector, fovDetector);
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
    
    
    public List<List<Double>> createAzElSkyCovering() {    
        List<List<Double>> azElSkyCovering  = new ArrayList<>();
        int cpt = 0;
        for (double elevation = elevationField[0]+angularFoV/2; elevation <= elevationField[1]-angularFoV/2; elevation+=angularFoV){
            cpt+=1;
            if (cpt%2 == 1){
                for (double azimuth = azimuthField[0]+angularFoV/2; azimuth <= azimuthField[1]-angularFoV/2; azimuth+=angularFoV) {
                    List<Double> aePosition = new ArrayList<Double>();
                    aePosition.add(azimuth);
                    aePosition.add(elevation);
                    azElSkyCovering.add(aePosition);
                }	
            }
            else {
                for (double azimuth = azimuthField[1]-angularFoV/2; azimuth >= azimuthField[0]+angularFoV/2; azimuth-=angularFoV){
                    List<Double> aePosition = new ArrayList<Double>();
                    aePosition.add(azimuth);
                    aePosition.add(elevation);
                    azElSkyCovering.add(aePosition);
                }	
            }
        }
        
        for(List<Double> aePosition : azElSkyCovering) {
        	//System.out.println(aePosition.get(0));
        	//System.out.println(aePosition.get(1));        	
        }
        return azElSkyCovering;
    }

    public LinkedHashMap<TimeComponents, FieldOfView> createSkyCoveringMap() {
    	List<List<Double>> azElSkyCovering  = createAzElSkyCovering();

        //Liste des FieldofView
        List<FieldOfView> fovList  = new ArrayList<>();
        
        for (int i = 0; i < azElSkyCovering.size(); i++){
                
            List<Double> aePosition = azElSkyCovering.get(i);
            double azimuth = aePosition.get(0);
            double elevation = aePosition.get(1);

            Vector3D vectorCenter = new Vector3D(azimuth, elevation);
            Vector3D axis1 = new Vector3D(1,0,0);
            Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
            DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, axis1, angularFoV/2, axis2, angularFoV/2, 0.);

            fovList.add(fov);
        }

        LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap = new LinkedHashMap<TimeComponents, FieldOfView>();
        int sc = 0;
        int cpt = 0;
        //quid de la dernière seconde ?
        while(sc <=3600*24 - breakTime) {
        	TimeComponents time = new TimeComponents(sc);
        	FieldOfView fov = fovList.get(cpt);
        	cpt+=1;
        	if(cpt==fovList.size()) {
        		cpt=0;
        	}
        	skyCoveringMap.put(time, (FieldOfView) fov);
        	sc = sc + breakTime;
        }
        skyCoveringMap.put(new TimeComponents(24*3600), (FieldOfView) fovList.get(cpt)); //pb résolution ici !!!!!!!!!
        
		return skyCoveringMap;
    }

    public EventBasedScheduler createEventBasedScheduler(ObservableSatellite satellite, Propagator propagator) {
    	BooleanDetector detector = createDetector(this.skyCoveringMap);
    	FixedStepSelector selector = createDateSelector();
    	AngularAzElBuilder builder  = createAzElBuilder(satellite);
       	EventBasedScheduler scheduler = new EventBasedScheduler(builder, selector, propagator, detector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
    	return scheduler;
    }
}
