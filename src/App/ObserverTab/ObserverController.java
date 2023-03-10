package src.App.ObserverTab;

import src.App.GSTab.GSController;
import src.Data.Observer.ReadObserverFile;
import src.Data.Observer.WriteObserverFile;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.GSNetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObserverController {
    public List<TelescopeAzEl> telescopeAzElList;
    public ReadObserverFile readObserverFile;
    public WriteObserverFile writeObserverFile;
    public int numberOfTelescope = 0;
    public int numberOfRadar = 0;

    public ObserverController(GSController gsController) throws IOException {

        this.telescopeAzElList = new ArrayList<>();
        this.readObserverFile = new ReadObserverFile();
        this.writeObserverFile = new WriteObserverFile();

        this.telescopeAzElList = readObserverFile.readTelescopesFromCSV("src/Data/Observer/Observer.csv", gsController.groundStationList);

        this.numberOfTelescope = telescopeAzElList.size();

    }

    public void addTelescope(TelescopeAzEl newTelescope) {
        telescopeAzElList.add(newTelescope);
    }

    public int getNumberOfTelescopePerStation(Station station) {
        int numberOfTelescope = 0;
        for (TelescopeAzEl telescopeAzEl : telescopeAzElList) {
            if (telescopeAzEl.getStation().equals(station)) {
                numberOfTelescope++;
            }
        }
        return numberOfTelescope;
    }
}
