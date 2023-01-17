package src.GroundStation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;

import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;





public class simulation_main {
    
    public static void main(String[] args) throws Exception {

        File orekitData = new File("lib/orekit-data-master");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
	    manager.addProvider(new DirectoryCrawler(orekitData));

        /*Liste des propagateurs-satellites */
        List<Propagator> propagatorList = new ArrayList<>();
        List<ObservableSatellite> objects = new ArrayList<>();
        List<TelescopeAzEl> stations = new ArrayList<>();

        final TimeScale utc = TimeScalesFactory.getUTC();
        FixedStepSelector dateSelector = new FixedStepSelector(60, utc); //mesure toutes les 1min
        AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 25, utc);
        final AbsoluteDate finalDate = new AbsoluteDate(2014, 6, 26, utc); 

        /* Satellites */
        //Satellite 1
        double a = (6370+2500)*2*1000;
    	double e = 0.01;
    	double i = 80*Math.PI/180;
    	double pa = 0.2;
    	double raan = 0.2;
    	double anomaly = 1;
    	PositionAngle type = PositionAngle.TRUE;
  
    	final Frame frame = FramesFactory.getEME2000();
    	KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, initialDate, Constants.EGM96_EARTH_MU);
    	
    	ObservableSatellite satellite = new ObservableSatellite(0);
        KeplerianPropagator propagator = new KeplerianPropagator(initialOrbit);

        propagatorList.add(propagator);
        objects.add(satellite);

        /* Telescopes */
        //telescope1
        double[] mean = {0.,0.};
        double angularIncertitude = 0.3;
        double[] sigma = {1,1};
    	double[] baseWeight = {1,1};
        double latitude = 0.;
        double longitude = 0.;
        double altitude = 0.;

        TelescopeAzEl telescope1 = new TelescopeAzEl(mean, angularIncertitude, sigma, baseWeight, latitude, longitude, altitude);
        stations.add(telescope1);


        /*Observations */
        Observations observations = new Observations(initialDate, finalDate, dateSelector, stations, objects, propagatorList);
        List <SortedSet<ObservedMeasurement<?>>> measurements = observations.getMeasurements();
        //System.out.println(measurements);
     }  
        
}

