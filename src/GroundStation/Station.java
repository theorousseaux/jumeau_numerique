package src.GroundStation;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.TopocentricFrame;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;
import src.constants;

import java.util.ArrayList;
import java.util.List;

public class Station extends GroundStation {

    List<TelescopeAzEl> listTelescopes;
    List<Radar> listRadars;

    public List<TelescopeAzEl> getListTelescopes ( ) {
        return listTelescopes;
    }

    public void setListTelescopes ( List<TelescopeAzEl> listTelescopes ) {
        this.listTelescopes = listTelescopes;
    }

    public List<Radar> getListRadars ( ) {
        return listRadars;
    }

    public void setListRadars ( List<Radar> listRadars ) {
        this.listRadars = listRadars;
    }

    // altitude a 0m
    public Station ( String name , Double latitude , Double longitude ) {
        super ( new TopocentricFrame ( constants.EARTH_SHAPE , new GeodeticPoint ( latitude , longitude , 0. ) , name ) );
        this.listTelescopes = new ArrayList<> ( );
        this.listRadars = new ArrayList<> ( );
    }

    public Station ( String name , Double latitude , Double longitude , Double altitude ) {
        super ( new TopocentricFrame ( constants.EARTH_SHAPE , new GeodeticPoint ( latitude , longitude , altitude ) , name ) );
        this.listTelescopes = new ArrayList<> ( );
        this.listRadars = new ArrayList<> ( );
    }

    public void addTelescope ( TelescopeAzEl telescope ) {
        this.listTelescopes.add ( telescope );
    }

    public List<TelescopeAzEl> getListTelescope ( ) {
        return this.listTelescopes;
    }

    public void addRadar ( Radar radar ) {
        this.listRadars.add ( radar );
    }

    public List<Radar> getListRadar ( ) {
        return this.listRadars;
    }

    public String getName ( ) {
        return getBaseFrame ( ).getName ( );
    }

    public Double getLongitude ( ) {
        return getBaseFrame ( ).getPoint ( ).getLongitude ( );
    }

    public Double getLatitude ( ) {
        return getBaseFrame ( ).getPoint ( ).getLatitude ( );
    }

    public Double getAltitude ( ) {
        return getBaseFrame ( ).getPoint ( ).getAltitude ( );
    }

    public String toString ( ) {
        return "Station: " + getName ( );
    }
}
