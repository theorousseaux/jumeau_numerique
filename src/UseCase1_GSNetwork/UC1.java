package UseCase1_GSNetwork;

import Kalman.station;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;  // Import the Scanner class
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hipparchus.distribution.multivariate.MultivariateNormalDistribution;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.QRDecomposer;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.ode.events.Action;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.hipparchus.optim.nonlinear.vector.leastsquares.GaussNewtonOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.hipparchus.random.CorrelatedRandomVectorGenerator;
import org.hipparchus.random.GaussianRandomGenerator;
import org.hipparchus.random.JDKRandomGenerator;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.random.RandomGenerator;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.estimation.iod.IodLaplace;
import org.orekit.estimation.leastsquares.BatchLSEstimator;
import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.AngularRaDec;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.generation.AngularAzElBuilder;
import org.orekit.estimation.measurements.generation.EventBasedScheduler;
import org.orekit.estimation.measurements.generation.Generator;
import org.orekit.estimation.measurements.generation.SignSemantic;
import org.orekit.estimation.sequential.ConstantProcessNoise;
import org.orekit.estimation.sequential.KalmanEstimator;
import org.orekit.estimation.sequential.KalmanEstimatorBuilder;
import org.orekit.estimation.sequential.UnivariateProcessNoise;
import org.orekit.forces.drag.DragForce;
import org.orekit.forces.drag.IsotropicDrag;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;
import org.orekit.models.AtmosphericRefractionModel;
import org.orekit.models.earth.atmosphere.SimpleExponentialAtmosphere;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.conversion.DormandPrince853IntegratorBuilder;
import org.orekit.propagation.conversion.KeplerianPropagatorBuilder;
import org.orekit.propagation.conversion.NumericalPropagatorBuilder;
import org.orekit.propagation.events.BooleanDetector;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.GroundAtNightDetector;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FixedStepSelector;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.PVCoordinatesProvider;
import org.orekit.utils.ParameterDriver;
import org.orekit.utils.ParameterDriversList;
import org.orekit.utils.TimeStampedPVCoordinates;
import org.orekit.bodies.CelestialBodyFactory;

public class UC1 {

    public static void main(String[] args) throws NumberFormatException, IOException {
        
        File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
        
        
        /*
        * -------------------------------------------
        * Targets selection
        * -------------------------------------------
        * 
        * In this part the user chooses which satellites/ debris he wants to observe
        * The selection could be based on size, orbit properties, belonging to a constelation...
        */
            
        // a first example with only one satellite

        // ISS
        // double i = 51.6416*Math.PI/180;
        // double raan = 247.4627*Math.PI/180;
        // double e = 0.0006703;//(enlever le 1 normalement)
        // double pa = 130.5360*Math.PI/180;
        // double anomaly = 261.6196828895407;//261.6196828895407;
        // double rev_day = 15.72125391;
        // double T = 3600*24/rev_day;
        // double a = Math.cbrt(Math.pow(T, 2)*Kalman.constants.mu/(4*Math.pow(Math.PI,2))); //6730.960 km, soit h=352.823 km
        // double[] listParamOrbitaux = {a,e,Math.IEEEremainder(i, 2*Math.PI),Math.IEEEremainder(raan, 2*Math.PI),Math.IEEEremainder(pa, 2*Math.PI),Math.IEEEremainder(anomaly, 2*Math.PI)};
        // AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, Kalman.constants.utc);//AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, Kalman.constants.utc);
        // KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, Kalman.constants.type, Kalman.constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
        
        // double t = initialOrbit.getKeplerianPeriod();

        // AbsoluteDate finalDate = initialDate.shiftedBy(t * 450.); // on propagera sur 300 orbites
        
        // ObservableSatellite satellite = new ObservableSatellite(0); 

        // a more complex version will allow queries into the satellite database

        /*
        * -------------------------------------------
        * Ground stations choice
        * -------------------------------------------
        * 
        * The user chooses the different configurations he wants to compare.
        */

        GSNetwork network = new GSNetwork();
        /*
        * ------------------------------------------
        * Best network computation
        * ------------------------------------------
        *
        * The algorithm computes which of the specified networks gives the best observations
        */



    }


}
