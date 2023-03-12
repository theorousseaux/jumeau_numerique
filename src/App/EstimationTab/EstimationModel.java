package src.App.EstimationTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.conversion.OrbitDeterminationPropagatorBuilder;
import org.orekit.time.AbsoluteDate;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class EstimationModel {

    List<ObservableSatellite> satellites;

    List<Propagator> propagators;

    List<OrbitDeterminationPropagatorBuilder> propagatorBuilders;

    List<SortedSet<ObservedMeasurement<?>>> measurements;
    AbsoluteDate initialDate;

    AbsoluteDate finalDate;

    List<String> estimationsList = new ArrayList<> (  );


    double noiseLevelPos;

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

    double noiseLevelV;

    public double getStdP ( ) {
        return stdPos;
    }

    public void setStdPos ( double stdP ) {
        this.stdPos = stdP;
    }

    public double getStdV ( ) {
        return stdV;
    }

    public void setStdV ( double stdV ) {
        this.stdV = stdV;
    }

    double stdPos;

    double stdV;

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

    public List<SortedSet<ObservedMeasurement<?>>> getMeasurements ( ) {
        return measurements;
    }

    public void setMeasurements ( List<SortedSet<ObservedMeasurement<?>>> measurements ) {
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
