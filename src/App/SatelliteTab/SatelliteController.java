package src.App.SatelliteTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.Propagator;
import src.App.UpdateSatelliteDBTab.UpdateDBController;
import src.Kalman.Object;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SatelliteController {

    List<Propagator> satellitesList = new ArrayList<>();

    List<String> idSatellitesList = new ArrayList<>();

    List<ObservableSatellite> observableSatellitesList = new ArrayList<>();

    UpdateDBController updateDBController;

    public SatelliteController(UpdateDBController updateDBController) {
        this.updateDBController = updateDBController;
    }

    public void setSatellitesList(String type, Integer number) throws SQLException {
        // We remove all the satellites from the list
        this.satellitesList.clear();
        this.idSatellitesList.clear();
        this.observableSatellitesList.clear();

        int i = 0;
        List<Object> objectList = this.updateDBController.db.selectSatellites(type, number);
        for (Object object : objectList) {
            this.satellitesList.add(object.getPropagator());
            this.idSatellitesList.add(object.getId());
            this.observableSatellitesList.add(new ObservableSatellite(i));
            i++;
        }
    }
}
