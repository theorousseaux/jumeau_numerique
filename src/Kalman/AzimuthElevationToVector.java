package src.Kalman;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.geometry.DirectPosition;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

public class AzimuthElevationToVector {

    public static void main(String[] args) {

        // Set the latitude, longitude, and altitude of the telescope
        double latitude = 45.0;
        double longitude = -75.0;
        double altitude = 0.0;

        // Set the azimuth and elevation angles
        double azimuth = 30.0;
        double elevation = 45.0;
        
      //Corps celestes
      final double ae = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
      final double f = 0.;
      OneAxisEllipsoid earthShape = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF(ITRFVersion.ITRF_2000, IERSConventions.IERS_2010, false));

        
        
               
        // Create a topocentric frame centered on the telescope
        GeodeticPoint gp = new GeodeticPoint(Math.toRadians(latitude), Math.toRadians(longitude), altitude);
        TopocentricFrame topoFrame = new TopocentricFrame(earthShape, gp, "telescope");
        
        GeodeticPoint pt = topoFrame.pointAtDistance(azimuth, elevation, 0.);
        
        pt.getX()


        System.out.println("X: " + x + " Y: " + y + " Z: " + z);
    }
}
