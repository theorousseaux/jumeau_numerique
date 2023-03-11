package src.Kalman;

import java.util.ArrayList;
import java.util.List;

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
import org.orekit.estimation.measurements.generation.RangeBuilder;
import org.orekit.estimation.measurements.generation.RangeRateBuilder;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.geometry.fov.FieldOfView;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.AltitudeDetector;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;
import org.orekit.propagation.events.NegateDetector;
import org.orekit.time.FixedStepSelector;


public class Radar {

    /** ID (station:type:id) */
    private String ID;

    /** noiseSource */
    public CorrelatedRandomVectorGenerator noiseSource;

    private double[] mean;

    private double[] angularIncertitude;

    /** sigma */
    public double[] sigma;

    /** Measures weight */
    public double[] baseWeight;

    /** GroundStation */
    public Station station;

    /** taille angulaire fov */
    public double angularFoV;

    /** nombre de secondes entre deux mesures */
    public double stepMeasure;

    /** Programmation satellite LEO/GEO */
    public FieldOfView fov;

    public double baseWeightRadar;

    public double sigmaRadar;

    /** Constructor */
    public Radar(String ID, double[] mean, double[] angularIncertitude, double angularFoV, double stepMeasure, Station station) {

        this.ID = ID;
        this.station = station;

        this.sigma = angularIncertitude;
        this.baseWeight = new double[]{1., 1.};
        this.sigmaRadar = 30.;
        this.baseWeightRadar = 1.;

        this.mean = mean;
        this.angularIncertitude = angularIncertitude;

        // Mise en place du field of view du radar
        this.angularFoV = angularFoV;
        Vector3D vectorCenter = new Vector3D(0, Math.PI/2);
        Vector3D axis1 = new Vector3D(1,0,0);
        Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
        DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, axis1, angularFoV/2, axis2, angularFoV/2, 0.);
        this.fov = fov;


        // bruit de mesures
        double[] covarianceDiag = {Math.pow(angularIncertitude[0],2), Math.pow(angularIncertitude[1],2)};
        RealMatrix covariance = MatrixUtils.createRealDiagonalMatrix(covarianceDiag);
        RandomGenerator randomGenerator = new RandomDataGenerator();
        GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
        CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-10, gaussianRandomGenerator);//mesures parfaites:null
        this.noiseSource = noiseSource;

        this.stepMeasure = stepMeasure;

    }

    /* Récupération des attributs */
    public String getID() {
        return this.ID;
    }

    public double[] getMean() {
        return this.mean;
    }

    public double[] getAngularIncertitude() {
        return this.angularIncertitude;
    }

    public CorrelatedRandomVectorGenerator getNoiseSource(){
        return this.noiseSource;
    }

    public double[] getSigma(){
        return this.sigma;
    }

    public double[] getBaseWeight(){
        return this.baseWeight;
    }

    public Station getStation(){
        return this.station;
    }

    public double getAngularFoV(){
        return this.angularFoV;
    }

    public double getStepMeasure() {
        return this.stepMeasure;
    }

    public FieldOfView getFov(){
        return this.fov;
    }

    public double getBaseWeightRadar(){
        return this.baseWeightRadar;
    }

    public double getSigmaRadar(){
        return this.sigmaRadar;
    }


    /* Création Final Detector */
    public BooleanDetector createRadarDetector() {

        //Elevation Detector
        System.out.println(station.getBaseFrame());
        ElevationDetector elevationDetector = new ElevationDetector(station.getBaseFrame()); //visible quand positif
        elevationDetector = elevationDetector.withHandler(
                (s, detector, increasing) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });

        //AltitudeDetector
        AltitudeDetector altitudeDetector = new AltitudeDetector(2000000, constants.earthShape);
        altitudeDetector = altitudeDetector.withHandler(
                (s, detector, increasing) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });
        NegateDetector newAltitudeDetector = new NegateDetector(altitudeDetector);


        //FOV detector
        GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector(station.getBaseFrame(), fov); // positif quand c'est visible
        fovDetector = fovDetector.withHandler(
                (s, detector, increasing) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });
        NegateDetector newFovDetector = new NegateDetector(fovDetector);

        //Final Detector
        BooleanDetector finalDetector = BooleanDetector.andCombine(elevationDetector, newAltitudeDetector, newFovDetector);
        return finalDetector;
    }


    /* StepSelector */
    public FixedStepSelector createDateSelector() {
        FixedStepSelector dateSelector = new FixedStepSelector(this.stepMeasure, constants.utc);
        return dateSelector;
    }

    /* Creation des mesureBuilder */
    public AngularAzElBuilder createAzElBuilder(ObservableSatellite satellite) {
        AngularAzElBuilder azElBuilder = new AngularAzElBuilder(this.noiseSource, this.station, this.sigma, this.baseWeight, satellite);
        return azElBuilder;
    }
    public RangeBuilder createRangeBuilder(ObservableSatellite satellite){
        RangeBuilder rangeBuilder = new RangeBuilder(noiseSource, station, true, sigmaRadar, baseWeightRadar, satellite);
        return rangeBuilder;
    }

    public RangeRateBuilder createRangeRateBuilder(ObservableSatellite satellite){
        RangeRateBuilder rangeRateBuilder = new RangeRateBuilder(noiseSource, station, true, sigmaRadar, baseWeightRadar, satellite);
        return rangeRateBuilder;
    }


    /*Création de l'EventBasedScheduler final */
    public List<EventBasedScheduler> createEventBasedScheduler(ObservableSatellite satellite, Propagator propagator) {

        BooleanDetector detector = this.createRadarDetector();
        FixedStepSelector selector = this.createDateSelector();

        AngularAzElBuilder azElbuilder  = createAzElBuilder(satellite);
        RangeBuilder rangeBuilder = createRangeBuilder(satellite);
        RangeRateBuilder rangeRateBuilder =  createRangeRateBuilder(satellite);

        List<EventBasedScheduler> schedulerList  = new ArrayList<>();

        EventBasedScheduler schedulerAzEl = new EventBasedScheduler(azElbuilder, selector, propagator, detector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
        EventBasedScheduler schedulerRange = new EventBasedScheduler(rangeBuilder, selector, propagator, detector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);
        EventBasedScheduler schedulerRangeRate = new EventBasedScheduler(rangeRateBuilder, selector, propagator, detector, SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE);

        schedulerList.add(schedulerAzEl);
        schedulerList.add(schedulerRange);
        schedulerList.add(schedulerRangeRate);

        return schedulerList;
    }
}
