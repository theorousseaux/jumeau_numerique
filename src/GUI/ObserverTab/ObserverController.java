package src.GUI.ObserverTab;

import src.Data.Observer.ReadObserverFile;
import src.Data.Observer.WriteObserverFile;
import src.GUI.GSTab.GSController;
import src.GroundStation.Station;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;

import java.util.ArrayList;

public class ObserverController {

    private ObserverModel model = new ObserverModel ( );

    public ObserverController ( GSController gsController ) {

        this.getModel ( ).setTelescopeAzElList ( new ArrayList<> ( ) );
        this.getModel ( ).setRadarList ( new ArrayList<> ( ) );

        this.getModel ( ).setReadObserverFile ( new ReadObserverFile ( ) );
        this.getModel ( ).setWriteObserverFile ( new WriteObserverFile ( ) );

        this.getModel ( ).getTelescopeAzElList ( ).addAll ( this.getModel ( ).getReadObserverFile ( ).readTelescopesFromCSV ( "src/Data/Observer/Observer.csv" , gsController.getModel ( ).getGroundStationList ( ) ) );
        this.getModel ( ).getRadarList ( ).addAll ( this.getModel ( ).getReadObserverFile ( ).readRadarsFromCSV ( "src/Data/Observer/Observer.csv" , gsController.getModel ( ).getGroundStationList ( ) ) );

        this.getModel ( ).setNumberOfTelescope ( this.getModel ( ).getTelescopeAzElList ( ).size ( ) );
        this.getModel ( ).setNumberOfRadar ( this.getModel ( ).getRadarList ( ).size ( ) );

    }

    public ObserverModel getModel ( ) {
        return model;
    }

    public void setModel ( ObserverModel model ) {
        this.model = model;
    }

    public void addTelescope ( TelescopeAzEl newTelescope ) {
        this.getModel ( ).getTelescopeAzElList ( ).add ( newTelescope );
    }

    public void addRadar ( Radar newRadar ) {
        this.getModel ( ).getRadarList ( ).add ( newRadar );
    }

    public int getNumberOfTelescopePerStation ( Station station ) {
        int numberOfTelescope = 0;
        for (TelescopeAzEl telescopeAzEl : this.getModel ( ).getTelescopeAzElList ( )) {
            if (telescopeAzEl.getStation ( ).equals ( station )) {
                numberOfTelescope++;
            }
        }
        return numberOfTelescope;
    }

    public int getNumberOfRadarPerStation ( Station station ) {
        int numberOfRadar = 0;
        for (Radar radar : this.getModel ( ).getRadarList ( )) {
            if (radar.getStation ( ).equals ( station )) {
                numberOfRadar++;
            }
        }
        return numberOfRadar;
    }
}
