package src.GUI.EstimationTab;

import org.hipparchus.util.Pair;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;

import java.util.*;

public class EstimationModel {

    List<ObservableSatellite> satellites;

    List<Propagator> propagators;
    Set<String> observedSat = new HashSet<> ( );
    List<String> satellitesNames;
    List<OrbitDeterminationPropagatorBuilder> propagatorBuilders;
    Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> measurements;
    AbsoluteDate initialDate;
    AbsoluteDate finalDate;
    List<String> estimationsList = new ArrayList<> ( );
    double noiseLevelPos;
    double noiseLevelV;
    double stdPos;
    double stdV;

    public Set<String> getObservedSat ( ) {
        return observedSat;
    }

    public void setObservedSat ( Set<String> observedSat ) {
        this.observedSat = observedSat;
    }

    public List<String> getSatellitesNames ( ) {
        return satellitesNames;
    }

    public void setSatellitesNames ( List<String> satellitesNames ) {
        this.satellitesNames = satellitesNames;
    }

    public List<String> getEstimationsList ( ) {
        return estimationsList;
    }

    public void setEstimationsList ( List<String> estimationsList ) {
        this.estimationsList = estimationsList;
    }

    public double getStdPos ( ) {
        return stdPos;
    }

    public void setStdPos ( double stdP ) {
        this.stdPos = stdP;
    }

    public double getNoiseLevelPos ( ) {
        return noiseLevelPos;
    }

    public void setNoiseLevelPos ( double noiseLevelPos ) {
        this.noiseLevelPos = noiseLevelPos;
    }

    public double getNoiseLevelV ( ) {
        return noiseLevelV;
    }

    public void setNoiseLevelV ( double noiseLevelV ) {
        this.noiseLevelV = noiseLevelV;
    }

    public double getStdP ( ) {
        return stdPos;
    }

    public double getStdV ( ) {
        return stdV;
    }

    public void setStdV ( double stdV ) {
        this.stdV = stdV;
    }

    public List<ObservableSatellite> getSatellites ( ) {
        return satellites;
    }

    public void setSatellites ( List<ObservableSatellite> satellites ) {
        this.satellites = satellites;
    }

    public List<Propagator> getPropagators ( ) {
        return propagators;
    }


    public void setPropagators ( List<Propagator> propagators ) {
        this.propagators = propagators;
    }

    public List<OrbitDeterminationPropagatorBuilder> getPropagatorBuilders ( ) {
        return propagatorBuilders;
    }

    public void setPropagatorBuilders ( List<OrbitDeterminationPropagatorBuilder> propagatorBuilders ) {
        this.propagatorBuilders = propagatorBuilders;
    }

    public Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> getMeasurements ( ) {
        return measurements;
    }

    public void setMeasurements ( Pair<List<SortedSet<ObservedMeasurement<?>>>, HashMap<ObservedMeasurement, SpacecraftState>> measurements ) {
        this.measurements = measurements;
    }

    public AbsoluteDate getInitialDate ( ) {
        return initialDate;
    }

    public void setInitialDate ( AbsoluteDate initialDate ) {
        this.initialDate = initialDate;
    }

    public AbsoluteDate getFinalDate ( ) {
        return finalDate;
    }

    public void setFinalDate ( AbsoluteDate finalDate ) {
        this.finalDate = finalDate;
    }
}
