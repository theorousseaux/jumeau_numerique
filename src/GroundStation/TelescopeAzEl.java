package src.GroundStation;


import java.util.ArrayList;
import java.util.List;

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
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;
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

	/** Measures weight */
	public double[] baseWeight;

	/** GroundStation - TopocentricFrame */
	public GroundStation station;

	/**Topocentric frame of the station */
	public TopocentricFrame topocentricFrame;

	/** FinalDetector to determine the observation's conditions */
	public EventDetector finalDetector;

 


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
		GeodeticPoint stationLocation = new GeodeticPoint(latitude, longitude, altitude) ;
    	TopocentricFrame topocentricFrame = new TopocentricFrame(earthShape, stationLocation, "station");
    	GroundStation station = new GroundStation(topocentricFrame);
		
		this.topocentricFrame = topocentricFrame;
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
	

	//GroundFieldOfViewDetectors
	public List<GroundFieldOfViewDetector> getFieldOfViewDetectors() {

		            //cadrillage du ciel en azimuth,elevation
            /////////////////////////////////////////
            List<List<Integer>> azElskyCuadrilled  = new ArrayList<>();
            int cpt = 0;
            for (int elevation = 35; elevation <= 145; elevation+=10){
                cpt+=1;
                
                if (cpt%2 == 1){
                    
                    for (int azimuth = 5; azimuth <= 175; azimuth+=10) {
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
    
                else {
                    for (int azimuth = 175; azimuth >= 5; azimuth-=10){
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
            }
            System.out.println("coucou c'est le cadrillage’”");
            System.out.println(azElskyCuadrilled);
    
            //Liste des groundFieldofViewDetector 
            List<Vector3D> vectorSkyCuadrilled  = new ArrayList<>();
            List<GroundFieldOfViewDetector> fovDetectorsList  = new ArrayList<>();
            
            Vector3D axis1 = new Vector3D(1,0,0);
            Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
            
            for (int i = 0; i < azElskyCuadrilled.size(); i++){
                    
                List<Integer> aePosition = azElskyCuadrilled.get(i);
                int azimuth = aePosition.get(0);
                int elevation = aePosition.get(1);
    
                Vector3D vectorCenter = new Vector3D(azimuth*Math.PI/180, elevation*Math.PI/180);
                DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, axis1, 10*Math.PI/180, axis2, 10*Math.PI/180, 0.);
                GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector(this.topocentricFrame, fov);

                vectorSkyCuadrilled.add(vectorCenter);
                fovDetectorsList.add(fovDetector);

            }
            System.out.println(vectorSkyCuadrilled);
            System.out.println(fovDetectorsList);

			return fovDetectorsList;
	}

	
	




}
