package src;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
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
    public final static TimeScale UTC = TimeScalesFactory.getUTC ( );
    public final static Frame GCRF = FramesFactory.getGCRF ( );//FramesFactory.getEME2000();
    public final static double MU = Constants.EGM96_EARTH_MU; //=M*G
    public final static Frame ITRF = FramesFactory.getITRF ( IERSConventions.IERS_2010 , false );
    //Corps celestes
    public final static double R_EARTH = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
    public final static OneAxisEllipsoid EARTH_SHAPE = new OneAxisEllipsoid ( R_EARTH , 0 , ITRF );
    public final static double REF_ALT = 400e3;
    public final static double DENSITY = 2e-12; // 1430.
    public final static double H_SCALE = .05;
    public final static SimpleExponentialAtmosphere ATMOSPHERE = new SimpleExponentialAtmosphere ( constants.EARTH_SHAPE , REF_ALT , DENSITY , H_SCALE );
    public final static PVCoordinatesProvider SUN = CelestialBodyFactory.getSun ( );
    public final static AtmosphericRefractionModel REFRACTION_MODEL = null;


}
