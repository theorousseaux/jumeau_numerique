package src.GUI.GSTab;

import src.Data.GS.ReadGSFile;
import src.Data.GS.WriteGSFile;
import src.GroundStation.Station;

import java.util.List;

public class GSModel {

    private List<Station> groundStationList;
    private ReadGSFile GSReader;
    private WriteGSFile GSWriter;

    public List<Station> getGroundStationList ( ) {
        return groundStationList;
    }

    public void setGroundStationList ( List<Station> groundStationList ) {
        this.groundStationList = groundStationList;
    }

    public ReadGSFile getGSReader ( ) {
        return GSReader;
    }

    public void setGSReader ( ReadGSFile GSReader ) {
        this.GSReader = GSReader;
    }

    public WriteGSFile getGSWriter ( ) {
        return GSWriter;
    }

    public void setGSWriter ( WriteGSFile GSWriter ) {
        this.GSWriter = GSWriter;
    }

}
