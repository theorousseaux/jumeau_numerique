package src.UseCase1_GSNetwork;

import src.Kalman.Station;
import src.Kalman.Observation;
import src.Kalman.OD;
import src.Kalman.constants;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;  // Import the Scanner class
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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

import src.Data.ReadFile;

import org.orekit.bodies.CelestialBodyFactory;
import src.Kalman.TelescopeAzEl;

public class test {

    public static void main(String[] args) throws NumberFormatException, IOException, NoSuchElementException {
        
        File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
        
        
        // Networks generation

        GSNetwork network1 = new GSNetwork("net1",Arrays.asList("Toulouse","Singapour"));
        network1.display();
        

        GSNetwork network2 = new GSNetwork("net2",Arrays.asList("Kiruna"));
        network2.display();

        // Adding telescopes to stations
        List<TelescopeAzEl> telescopesList = new ArrayList<>();
        for (TelescopeAzEl tel :network1.getTelescopes() ){
            telescopesList.add(tel);
        }
        for (TelescopeAzEl tel :network2.getTelescopes() ){
            telescopesList.add(tel);
        }

        // Interval of the simulation

        AbsoluteDate initialDate = new AbsoluteDate(2014, 6, 27, 15, 28, 10, constants.utc);
        AbsoluteDate finalDate = initialDate.shiftedBy(60*60*24);

        // Satellites generation

        int n = 40; //nb of satellites
        List<String> satNames = new ArrayList<>();
        List<ObservableSatellite> objectsList = new ArrayList<ObservableSatellite>();
        for (int i =0; i<n; i++){
            satNames.add("Sat"+String.valueOf(i));
            objectsList.add(new ObservableSatellite(i));
        }

        List<Propagator> propagatorsList = new ArrayList<Propagator>();
        ReadFile fetcher = new ReadFile();

        propagatorsList = fetcher.readSat("src/Data/Sat.csv", initialDate,n);

        // Making observations

        Observation observation1 = new Observation(telescopesList, objectsList, propagatorsList, initialDate, finalDate);
        List<SortedSet<ObservedMeasurement<?>>> measurementsSetsList1 = observation1.measure(false);

        // Evaluating networks performances

        System.out.println("Number of observations by network 1:");
        System.out.println(network1.countObservations(measurementsSetsList1));
        System.out.println("Number of observations by network 2:");
        System.out.println(network2.countObservations(measurementsSetsList1));

    }


}
