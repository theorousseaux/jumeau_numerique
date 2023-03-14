package src.App.SatelliteTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.Propagator;
import src.App.MainFrame;
import src.App.UpdateSatelliteDBTab.UpdateDBController;
import src.Satellites.Object;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SatelliteController {

    List<Propagator> satellitesList = new ArrayList<> ( );

    List<String> idSatellitesList = new ArrayList<> ( );

    List<ObservableSatellite> observableSatellitesList = new ArrayList<> ( );

    UpdateDBController updateDBController;

    JPanel view;

    public SatelliteController ( UpdateDBController updateDBController ) {
        this.updateDBController = updateDBController;
    }

    public List<Propagator> getSatellitesList ( ) {
        return satellitesList;
    }

    public List<String> getIdSatellitesList ( ) {
        return idSatellitesList;
    }

    public List<ObservableSatellite> getObservableSatellitesList ( ) {
        return observableSatellitesList;
    }

    public UpdateDBController getUpdateDBController ( ) {
        return updateDBController;
    }

    public void setSatellitesList ( String type , Integer number, MainFrame parent ) throws SQLException {
        // We remove all the satellites from the list
        this.satellitesList.clear ( );
        this.idSatellitesList.clear ( );
        this.observableSatellitesList.clear ( );

        int i = 0;
        List<Object> objectList = this.updateDBController.db.selectSatellites ( type , number, parent.paramController.getStartDate ());
        for (Object object : objectList) {
            this.satellitesList.add ( object.getPropagator ( ) );
            this.idSatellitesList.add ( object.getId ( ) );
            this.observableSatellitesList.add ( new ObservableSatellite ( i ) );
            i++;
        }
    }
}
