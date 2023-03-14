package src.GUI.SatelliteTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.Propagator;
import src.GUI.UpdateSatelliteDBTab.UpdateDBController;

import java.util.ArrayList;
import java.util.List;

public class SatelliteModel {

    private List<Propagator> satellitesList = new ArrayList<> ( );

    private List<String> idSatellitesList = new ArrayList<> ( );

    private List<ObservableSatellite> observableSatellitesList = new ArrayList<> ( );

    private UpdateDBController updateDBController;

    public List<Propagator> getSatellitesList ( ) {
        return satellitesList;
    }

    public void setSatellitesList ( List<Propagator> satellitesList ) {
        this.satellitesList = satellitesList;
    }

    public List<String> getIdSatellitesList ( ) {
        return idSatellitesList;
    }

    public void setIdSatellitesList ( List<String> idSatellitesList ) {
        this.idSatellitesList = idSatellitesList;
    }

    public List<ObservableSatellite> getObservableSatellitesList ( ) {
        return observableSatellitesList;
    }

    public void setObservableSatellitesList ( List<ObservableSatellite> observableSatellitesList ) {
        this.observableSatellitesList = observableSatellitesList;
    }

    public UpdateDBController getUpdateDBController ( ) {
        return updateDBController;
    }

    public void setUpdateDBController ( UpdateDBController updateDBController ) {
        this.updateDBController = updateDBController;
    }
}
