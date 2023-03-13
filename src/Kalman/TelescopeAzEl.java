package src.Kalman;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TelescopeAzEl {

    LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap;
    /**
     * ID (station:type:id)
     */
    private String ID;
    /**
     * noiseSource
     */
    private CorrelatedRandomVectorGenerator noiseSource;
    /**
     * noiseSource mean
     */
    private double[] mean;
    /**
     * noiseSource variance
     */
    private double[] angularIncertitude;
    /**
     * sigma
     */
    private double[] sigma;
    /**
     * Measures weight
     */
    private double[] baseWeight;
    /**
     * GroundStation
     */
    private Station station;
    /**
     * Limites de Field of View
     */
    private double[] azimuthField;
    private double[] elevationField;
    private double angularFoV; // cote du carre du FoV
    private double stepMeasure;
    private double breakTime;
    private Boolean GEO;
    private double alphaGEO;

    /**
     * Constructor
     */
    public TelescopeAzEl ( String ID , double[] mean , double[] angularIncertitude , double elevationLimit , double angularFoV , double stepMeasure , double breakTime , Station station , Boolean GEO ) {

        this.ID = ID;
        this.station = station;

        this.sigma = angularIncertitude;
        this.baseWeight = new double[]{1. , 1.};
        this.GEO = GEO;
        this.alphaGEO = 0.;

        // Mise en place field of view

        this.angularFoV = angularFoV;
        this.azimuthField = new double[]{0. , Math.PI};
        this.elevationField = new double[]{0. + elevationLimit , Math.PI - elevationLimit};

        if (this.GEO == true) {

            //calcul de l'élévation de pointage telescope pour les GEO
            double latitude = station.getLatitude ( );

            double Rt = 6400 * 1000;
            double D = Rt * Math.sin ( latitude );
            double b = Rt * Math.cos ( latitude );
            double a = 35786 + 6378;
            double c = a - b;
            double beta = Math.atan ( D / c );
            double alpha = Math.PI / 2 - (beta + latitude);

            alphaGEO = alpha;

        }
        // bruit de mesures
        this.mean = mean;
        this.angularIncertitude = angularIncertitude;
        double[] covarianceDiag = {Math.pow ( angularIncertitude[0] , 2 ) , Math.pow ( angularIncertitude[1] , 2 )};
        RealMatrix covariance = MatrixUtils.createRealDiagonalMatrix ( covarianceDiag );
        RandomGenerator randomGenerator = new RandomDataGenerator ( );
        GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator ( randomGenerator );
        CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator ( mean , covariance , 1.0e-10 , gaussianRandomGenerator );//mesures parfaites:null
        this.noiseSource = noiseSource;

        this.stepMeasure = stepMeasure;
        this.breakTime = breakTime;
        this.skyCoveringMap = createSkyCoveringMap ( );

        station.listTelescopes.add ( this );
    }

    public String getID ( ) {
        return this.ID;
    }

    public double[] getMean ( ) {
        return this.mean;
    }

    public double[] getAngularIncertitude ( ) {
        return this.angularIncertitude;
    }

    public Station getStation ( ) {
        return this.station;
    }

    public double getBreakTime ( ) {
        return this.breakTime;
    }

    public double getStepMeasure ( ) {
        return this.stepMeasure;
    }

    public double getAngularFoV ( ) {
        return this.angularFoV;
    }

    public double getElevationLimit ( ) {
        return this.elevationField[0];
    }

    public Boolean getGEO ( ) {
        return this.GEO;
    }

    public double getAlphaGEO ( ) {
        return this.alphaGEO;
    }


    public BooleanDetector createDetector ( LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap ) {
        //EventDetector, conditions for the observations
        //elevation detector
        //un detecteur pour plein de satellites ?
        ElevationDetector elevationDetector = new ElevationDetector ( station.getBaseFrame ( ) ); //visible quand positif
        elevationDetector = elevationDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );
        elevationDetector = elevationDetector.withConstantElevation ( elevationField[0] );
        //Night detector
        GroundAtNightDetector nightDetector = new GroundAtNightDetector ( station.getBaseFrame ( ) , constants.Sun , GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION , constants.refractionModel ); // nuit quand positif
        nightDetector = nightDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );

        //FOV detector
        CustomGroundFieldOfViewDetector fovDetector = new CustomGroundFieldOfViewDetector ( station.getBaseFrame ( ) , skyCoveringMap ); // positif quand c'est visible
        fovDetector = fovDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );

        //FinalDetector
        BooleanDetector finalDetector = BooleanDetector.andCombine ( elevationDetector , nightDetector , fovDetector );


        return finalDetector;
    }

    public BooleanDetector createDetectorGEO ( ) {

        //ElevationDetector
        ElevationDetector elevationDetector = new ElevationDetector ( station.getBaseFrame ( ) ); //visible quand positif
        elevationDetector = elevationDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );
        elevationDetector = elevationDetector.withConstantElevation ( elevationField[0] );

        //Night detector
        GroundAtNightDetector nightDetector = new GroundAtNightDetector ( station.getBaseFrame ( ) , constants.Sun , GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION , constants.refractionModel ); // nuit quand positif
        nightDetector = nightDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );

        //GEO Field of View Detector
        double alpha = this.alphaGEO;
        Vector3D vectorCenter = new Vector3D ( 0 , alpha );
        Vector3D axis1 = new Vector3D ( 1 , 0 , 0 );
        Vector3D axis2 = new Vector3D ( 0 , Math.sqrt ( 2 ) / 2 , Math.sqrt ( 2 ) / 2 );
        DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView ( vectorCenter , axis1 , angularFoV / 2 , axis2 , angularFoV / 2 , 0. );

        GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector ( station.getBaseFrame ( ) , fov ); // positif quand c'est visible
        fovDetector = fovDetector.withHandler (
                ( s , detector , increasing ) -> {
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                } );

        BooleanDetector finalDetector = BooleanDetector.andCombine ( elevationDetector , nightDetector , fovDetector );

        return finalDetector;

    }

    public FixedStepSelector createDateSelector ( ) {
        FixedStepSelector dateSelector = new FixedStepSelector ( this.stepMeasure , constants.utc );
        return dateSelector;
    }

    public AngularAzElBuilder createAzElBuilder ( ObservableSatellite satellite ) {
        AngularAzElBuilder mesuresBuilder = new AngularAzElBuilder ( this.noiseSource , this.station , this.sigma , this.baseWeight , satellite );
        return mesuresBuilder;
    }


    public List<List<Double>> createAzElSkyCovering ( ) {
        List<List<Double>> azElSkyCovering = new ArrayList<> ( );
        int cpt = 0;
        for (double elevation = elevationField[0] + angularFoV / 2; elevation <= elevationField[1] - angularFoV / 2; elevation += angularFoV) {
            cpt += 1;
            if (cpt % 2 == 1) {
                for (double azimuth = azimuthField[0] + angularFoV / 2; azimuth <= azimuthField[1] - angularFoV / 2; azimuth += angularFoV) {
                    List<Double> aePosition = new ArrayList<Double> ( );
                    aePosition.add ( azimuth );
                    aePosition.add ( elevation );
                    azElSkyCovering.add ( aePosition );
                }
            } else {
                for (double azimuth = azimuthField[1] - angularFoV / 2; azimuth >= azimuthField[0] + angularFoV / 2; azimuth -= angularFoV) {
                    List<Double> aePosition = new ArrayList<Double> ( );
                    aePosition.add ( azimuth );
                    aePosition.add ( elevation );
                    azElSkyCovering.add ( aePosition );
                }
            }
        }

        return azElSkyCovering;
    }

    public LinkedHashMap<TimeComponents, FieldOfView> createSkyCoveringMap ( ) {
        List<List<Double>> azElSkyCovering = createAzElSkyCovering ( );

        //Liste des FieldofView
        List<FieldOfView> fovList = new ArrayList<> ( );

        for (int i = 0; i < azElSkyCovering.size ( ); i++) {

            List<Double> aePosition = azElSkyCovering.get ( i );
            double azimuth = aePosition.get ( 0 );
            double elevation = aePosition.get ( 1 );

            Vector3D vectorCenter = new Vector3D ( azimuth , elevation );
            Vector3D axis1 = new Vector3D ( 1 , 0 , 0 );
            Vector3D axis2 = new Vector3D ( 0 , Math.sqrt ( 2 ) / 2 , Math.sqrt ( 2 ) / 2 );
            DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView ( vectorCenter , axis1 , angularFoV / 2 , axis2 , angularFoV / 2 , 0. );

            fovList.add ( fov );
        }

        LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap = new LinkedHashMap<TimeComponents, FieldOfView> ( );
        double sc = 0;
        int cpt = 0;
        //quid de la dernière seconde ?
        while (sc <= 3600 * 24 - breakTime) {
            TimeComponents time = new TimeComponents ( sc );
            FieldOfView fov = fovList.get ( cpt );
            cpt += 1;
            if (cpt == fovList.size ( )) {
                cpt = 0;
            }
            skyCoveringMap.put ( time , (FieldOfView) fov );
            sc = sc + breakTime;
        }
        skyCoveringMap.put ( new TimeComponents ( 24 * 3600 ) , (FieldOfView) fovList.get ( cpt ) ); //pb résolution ici !!!!!!!!!

        return skyCoveringMap;
    }

    public CustomEventBasedScheduler createCustomEventBasedScheduler ( ObservableSatellite satellite , Propagator propagator ) {
        BooleanDetector detector = createDetector ( this.skyCoveringMap );
        if (this.GEO == true) {
            detector = createDetectorGEO ( );
        }
        FixedStepSelector selector = createDateSelector ( );
        AngularAzElBuilder builder = createAzElBuilder ( satellite );
        CustomEventBasedScheduler scheduler = new CustomEventBasedScheduler ( builder , selector , propagator , detector , SignSemantic.FEASIBLE_MEASUREMENT_WHEN_POSITIVE );
        return scheduler;
    }
}
