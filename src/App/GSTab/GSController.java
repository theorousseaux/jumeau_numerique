package src.App.GSTab;

import src.Data.GS.ReadGSFile;
import src.Data.GS.WriteGSFile;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.GSNetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GSController {
    public List<Station> groundStationList;
    public ReadGSFile GSReader;
    public WriteGSFile GSWriter;
    public int numberOfGS = 0;

    public GSNetwork gsNetwork;
    public GSController() throws IOException {

        this.groundStationList = new ArrayList<>();
        this.GSReader = new ReadGSFile();
        this.GSWriter = new WriteGSFile();

        this.groundStationList = GSReader.readStation("src/Data/GS/GS.csv");
    }

    // Méthode pour ajouter une station sol à la liste
    public void addGroundStation(Station groundStation) {
        groundStationList.add(groundStation);
    }
}
