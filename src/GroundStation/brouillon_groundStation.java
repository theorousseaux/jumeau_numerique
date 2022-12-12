package src.GroundStation;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

public class brouillon_groundStation {
    
    final double ae = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
    final double f = 0.;
    OneAxisEllipsoid earthShape = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF(ITRFVersion.ITRF_2000, IERSConventions.IERS_2010, false));
    GeodeticPoint station1Location = new GeodeticPoint(0., 0., 0.) ;
    TopocentricFrame topocentricFrame = new TopocentricFrame(earthShape, station1Location, "station_1");
    GroundStation station1 = new GroundStation(topocentricFrame);
}
