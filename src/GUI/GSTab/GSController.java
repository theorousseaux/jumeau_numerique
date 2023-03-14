package src.GUI.GSTab;

import src.Data.GS.ReadGSFile;
import src.Data.GS.WriteGSFile;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;

import java.io.IOException;
import java.util.ArrayList;

public class GSController {
    private final GSModel model = new GSModel ( );

    public GSController ( ) throws IOException {

        this.getModel ( ).setGroundStationList ( new ArrayList<> ( ) );
        this.getModel ( ).setGSReader ( new ReadGSFile ( ) );
        this.getModel ( ).setGSWriter ( new WriteGSFile ( ) );

        this.getModel ( ).getGroundStationList ( ).addAll ( this.getModel ( ).getGSReader ( ).readStation ( "src/Data/GS/GS.csv" ) );

        // Ajou d'un télescope standard à chaque station sol
        for (Station station : this.getModel ( ).getGroundStationList ( )) {
            station.addTelescope ( new TelescopeAzEl ( "def" , new double[]{0. , 0.} , new double[]{0.3 * Math.PI / 180 , 0.3 * Math.PI / 180} , 30 * Math.PI / 180 , 119 * Math.PI / 180 , 10 , 10 , station , true ) );
        }

    }

    public GSModel getModel ( ) {
        return model;
    }

    // Méthode pour ajouter une station sol à la liste
    public void addGroundStation ( Station groundStation ) {
        this.getModel ( ).getGroundStationList ( ).add ( groundStation );
    }
}
