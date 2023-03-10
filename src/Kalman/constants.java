package src.Kalman;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.models.earth.atmosphere.SimpleExponentialAtmosphere;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;

public class constants {
	
	public final static PositionAngle type = PositionAngle.TRUE;
	public final static TimeScale utc = TimeScalesFactory.getUTC();
	public final static  Frame gcrf = FramesFactory.getGCRF();//FramesFactory.getEME2000();
    final static Frame itrf = FramesFactory.getITRF(IERSConventions.IERS_2010, false);
	
	//Corps celestes
	final static double r = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
	final static OneAxisEllipsoid earthShape = new OneAxisEllipsoid(r, 0, itrf);
	public final static double mu =  Constants.EGM96_EARTH_MU; //=M*G
	final static PVCoordinatesProvider Sun = CelestialBodyFactory.getSun();
	final static AtmosphericRefractionModel refractionModel = null;

	final static double refAlt = 400e3;
	final static double density = 2e-12; // 1430.
	final static double hScale = .05;
	final static SimpleExponentialAtmosphere atmosphere = new SimpleExponentialAtmosphere(constants.earthShape, refAlt, density, hScale);

}
