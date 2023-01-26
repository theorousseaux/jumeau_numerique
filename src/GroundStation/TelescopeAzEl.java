package src.GroundStation;

import src.util;

import java.util.ArrayList;
import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.RealMatrix;

import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.orekit.bodies.GeodeticPoint;

import org.orekit.estimation.measurements.GroundStation;


import org.orekit.frames.TopocentricFrame;
import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.events.GroundFieldOfViewDetector;


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

	/** Limites de Field of View */
	public int[] azimuthField = {0 ,180};
	public int[] elevationField = {0 ,180};
	public int elevationLimit = 30;
	public int angularPrecision = 90;

	/*****************/
	/** Constructor */
    public TelescopeAzEl(double[] mean, double angularIncertitude, double[] sigma, double[] baseWeight, double latitude, double longitude, double altitude) {

		this.mean = mean;
		this.angularIncertitude = angularIncertitude;
		this.sigma = sigma;
		this.baseWeight = baseWeight;
		
		// bruit de mesures 
		double[] covarianceDiag = {angularIncertitude*Math.PI/180, angularIncertitude*Math.PI/180};//incertitude de mesure : 0.3°
    	RealMatrix covariance = new DiagonalMatrix(covarianceDiag);
    	RandomGenerator randomGenerator = new RandomDataGenerator();
    	GaussianRandomGenerator gaussianRandomGenerator = new GaussianRandomGenerator(randomGenerator);
    	CorrelatedRandomVectorGenerator noiseSource = new CorrelatedRandomVectorGenerator(mean, covariance, 0, gaussianRandomGenerator);//mesures parfaites:null

		this.covariance = covariance;
		this.noiseSource = noiseSource;
		

		//groundstation, topocentricFrame 
		GeodeticPoint stationLocation = new GeodeticPoint(latitude, longitude, altitude) ;
    	TopocentricFrame topocentricFrame = new TopocentricFrame(util.getEarth(), stationLocation, "stationTopo");
    	GroundStation station = new GroundStation(topocentricFrame);
		
		this.topocentricFrame = topocentricFrame;
		this.station = station;

		//EventDetector, conditions for the observations
		//elevation detector
		ElevationDetector elevationDetector = util.getElevationDetector(topocentricFrame);
  
    	//Night detector
    	GroundAtNightDetector nightDetector = util.getNightDetector(topocentricFrame);

    	BooleanDetector finalDetector = BooleanDetector.andCombine(elevationDetector, nightDetector);
		this.finalDetector = finalDetector;
	}
	
	/**************************** */
	/* GroundFieldOfViewDetectors*/
	public List<GroundFieldOfViewDetector> getFieldOfViewDetectors() {

		    //cadrillage du ciel en azimuth,elevation
            List<List<Integer>> azElskyCuadrilled  = util.getazElskyCuadrilled(this.azimuthField, this.elevationField, this.angularPrecision);
            
            System.out.println("coucou c'est le cadrillage’”");
            System.out.println(azElskyCuadrilled);
    
            //Liste des groundFieldofViewDetector 
            List<GroundFieldOfViewDetector> fovDetectorsList  = new ArrayList<>();
            
            for (int i = 0; i < azElskyCuadrilled.size(); i++){
                    
                List<Integer> aePosition = azElskyCuadrilled.get(i);
                int azimuth = aePosition.get(0);
                int elevation = aePosition.get(1);
    
                Vector3D vectorCenter = new Vector3D(azimuth*Math.PI/180, elevation*Math.PI/180);
                DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, util.axis1, (angularPrecision*Math.PI/180)/2, util.axis2, (angularPrecision*Math.PI/180)/2, 0.);
                GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector(this.topocentricFrame, fov);

                fovDetectorsList.add(fovDetector);
				
            }


			return fovDetectorsList;
	}

	
	




}
