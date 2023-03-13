package src.App.ObserverTab;

import src.App.GSTab.GSController;
import src.Data.Observer.ReadObserverFile;
import src.Data.Observer.WriteObserverFile;
import src.Kalman.Radar;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.ObserverNetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObserverController {
    public List<TelescopeAzEl> telescopeAzElList;
    public List<Radar> radarList;
    public ReadObserverFile readObserverFile;
    public WriteObserverFile writeObserverFile;
    public int numberOfTelescope = 0;
    public int numberOfRadar = 0;

    public ObserverNetwork observerNetwork;

    public ObserverController ( GSController gsController ) throws IOException {

        this.telescopeAzElList = new ArrayList<> ( );
        this.readObserverFile = new ReadObserverFile ( );
        this.writeObserverFile = new WriteObserverFile ( );

        this.telescopeAzElList = readObserverFile.readTelescopesFromCSV ( "src/Data/Observer/Observer.csv" , gsController.groundStationList );
        this.radarList = readObserverFile.readRadarsFromCSV ( "src/Data/Observer/Observer.csv" , gsController.groundStationList );

        this.numberOfTelescope = telescopeAzElList.size ( );
        this.numberOfRadar = radarList.size ( );

    }

    public void addTelescope ( TelescopeAzEl newTelescope ) {
        telescopeAzElList.add ( newTelescope );
    }

    public void addRadar ( Radar newRadar ) {
        radarList.add ( newRadar );
    }

    public int getNumberOfTelescopePerStation ( Station station ) {
        int numberOfTelescope = 0;
        for (TelescopeAzEl telescopeAzEl : telescopeAzElList) {
            if (telescopeAzEl.getStation ( ).equals ( station )) {
                numberOfTelescope++;
            }
        }
        return numberOfTelescope;
    }

    public int getNumberOfRadarPerStation ( Station station ) {
        int numberOfRadar = 0;
        for (Radar radar : radarList) {
            if (radar.getStation ( ).equals ( station )) {
                numberOfRadar++;
            }
        }
        return numberOfRadar;
    }
}
