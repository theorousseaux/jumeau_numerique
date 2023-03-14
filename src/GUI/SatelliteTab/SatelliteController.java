package src.GUI.SatelliteTab;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.Propagator;
import src.GUI.MainFrame;
import src.GUI.UpdateSatelliteDBTab.UpdateDBController;
import src.Satellites.Object;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SatelliteController {


    private SatelliteModel model = new SatelliteModel ();

    JPanel view;

    public SatelliteModel getModel ( ) {
        return model;
    }

    public void setModel ( SatelliteModel model ) {
        this.model = model;
    }

    public SatelliteController ( UpdateDBController updateDBController ) {
        this.getModel ().setUpdateDBController ( updateDBController);
    }



    public void setSatellitesList ( String type , Integer number, MainFrame parent ) throws SQLException {
        // We remove all the satellites from the list
        this.getModel ().getSatellitesList ().clear ( );
        this.getModel ().getIdSatellitesList ().clear ( );
        this.getModel ().getObservableSatellitesList ().clear ( );

        int i = 0;
        List<Object> objectList = this.getModel ().getUpdateDBController ().getModel ().selectSatellites ( type , number, parent.getParamController ( ).getStartDate ());
        for (Object object : objectList) {
            this.getModel ().getSatellitesList ().add ( object.getPropagator ( ) );
            this.getModel ().getIdSatellitesList ().add ( object.getId ( ) );
            this.getModel ().getObservableSatellitesList ().add ( new ObservableSatellite ( i ) );
            i++;
        }
    }
}
