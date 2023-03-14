package src.App.GSTab;

import src.Data.GS.ReadGSFile;
import src.Data.GS.WriteGSFile;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GSController {
    public List<Station> groundStationList;
    public ReadGSFile GSReader;
    public WriteGSFile GSWriter;
    public int numberOfGS = 0;


    public GSController ( ) throws IOException {

        this.groundStationList = new ArrayList<> ( );
        this.GSReader = new ReadGSFile ( );
        this.GSWriter = new WriteGSFile ( );

        this.groundStationList = GSReader.readStation ( "src/Data/GS/GS.csv" );

        // Ajou d'un télescope standard à chaque station sol
        for (Station station : groundStationList) {
            station.addTelescope ( new TelescopeAzEl ( "def" , new double[]{0. , 0.} , new double[]{0.3 * Math.PI / 180 , 0.3 * Math.PI / 180} , 30 * Math.PI / 180 , 119 * Math.PI / 180 , 10 , 10 , station , true ) );
        }

    }

    // Méthode pour ajouter une station sol à la liste
    public void addGroundStation ( Station groundStation ) {
        groundStationList.add ( groundStation );
    }
}
