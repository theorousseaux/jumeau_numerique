package src;

import java.util.ArrayList;
import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.ode.events.Action;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;

public class util {
    
    /*refraction Model GroundStation */
    public static AtmosphericRefractionModel refractionModel = null; 

    /*axis1 & axis2 for FOV */
    public static Vector3D axis1 = new Vector3D(1,0,0);
    public static Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
    
    /*getting Earth for GroundStation */
    public static OneAxisEllipsoid getEarth() {

        final double ae = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
        final double f = 0.;
        OneAxisEllipsoid earthShape = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF(ITRFVersion.ITRF_2000, IERSConventions.IERS_2010, false));

        return earthShape;
    }

    /*getting Sun for GroundStation */
    public static PVCoordinatesProvider Sun() {

        PVCoordinatesProvider Sun = CelestialBodyFactory.getSun();
        return Sun;
    }

    /*elevation Detector */
    public static ElevationDetector getElevationDetector(TopocentricFrame topocentricFrame){

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
        
        return elevationDetector;
    }

    /* GroundAtNight Detector */
    public static GroundAtNightDetector getNightDetector(TopocentricFrame topocentricFrame) {

        GroundAtNightDetector nightDetector = new GroundAtNightDetector(topocentricFrame, Sun(), GroundAtNightDetector.ASTRONOMICAL_DAWN_DUSK_ELEVATION, refractionModel);
    	nightDetector = nightDetector.withHandler(
                (s, detector, increasing) -> {
                    System.out.println(
                            " Night " +
                                    (increasing ? " begins at " : " ends at ") +
                                    s.getDate()
                    );
                    return increasing ? Action.CONTINUE : Action.CONTINUE;
                });
        
        return nightDetector;
    }

    /*Sky separation */
    public static List<List<Integer>> getazElskyCuadrilled(int[] azimuthField, int[] elevationField, int angularPrecision) {
     
        List<List<Integer>> azElskyCuadrilled  = new ArrayList<>();
        int cpt = 0;
        for (int elevation = elevationField[0]+angularPrecision/2; elevation <= elevationField[1]-angularPrecision/2; elevation+=angularPrecision){
            cpt+=1;
            
            if (cpt%2 == 1){
                
                for (int azimuth = azimuthField[0]+angularPrecision/2; azimuth <= azimuthField[1]-angularPrecision/2; azimuth+=angularPrecision) {
                    System.out.println(azimuth);
                    List<Integer> aePosition = new ArrayList<Integer>();
                    aePosition.add(azimuth);
                    aePosition.add(elevation);
                    azElskyCuadrilled.add(aePosition);
                }	
            }
            else {
                for (int azimuth = azimuthField[1]-angularPrecision/2; azimuth >= azimuthField[0]+angularPrecision/2; azimuth-=angularPrecision){
                    List<Integer> aePosition = new ArrayList<Integer>();
                    aePosition.add(azimuth);
                    aePosition.add(elevation);
                    azElskyCuadrilled.add(aePosition);
                }	
            }
        }
        return azElskyCuadrilled;
    }
}
