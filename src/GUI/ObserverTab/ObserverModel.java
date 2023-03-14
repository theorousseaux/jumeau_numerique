package src.GUI.ObserverTab;

import src.Data.Observer.ReadObserverFile;
import src.Data.Observer.WriteObserverFile;
import src.Observer.ObserverNetwork;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;

import java.util.List;

public class ObserverModel {


    private List<TelescopeAzEl> telescopeAzElList;
    private List<Radar> radarList;
    private ReadObserverFile readObserverFile;
    private WriteObserverFile writeObserverFile;
    private int numberOfTelescope = 0;
    private int numberOfRadar = 0;

    private ObserverNetwork observerNetwork;

    public List<TelescopeAzEl> getTelescopeAzElList ( ) {
        return telescopeAzElList;
    }

    public void setTelescopeAzElList ( List<TelescopeAzEl> telescopeAzElList ) {
        this.telescopeAzElList = telescopeAzElList;
    }

    public List<Radar> getRadarList ( ) {
        return radarList;
    }

    public void setRadarList ( List<Radar> radarList ) {
        this.radarList = radarList;
    }

    public ReadObserverFile getReadObserverFile ( ) {
        return readObserverFile;
    }

    public void setReadObserverFile ( ReadObserverFile readObserverFile ) {
        this.readObserverFile = readObserverFile;
    }

    public WriteObserverFile getWriteObserverFile ( ) {
        return writeObserverFile;
    }

    public void setWriteObserverFile ( WriteObserverFile writeObserverFile ) {
        this.writeObserverFile = writeObserverFile;
    }

    public int getNumberOfTelescope ( ) {
        return numberOfTelescope;
    }

    public void setNumberOfTelescope ( int numberOfTelescope ) {
        this.numberOfTelescope = numberOfTelescope;
    }

    public int getNumberOfRadar ( ) {
        return numberOfRadar;
    }

    public void setNumberOfRadar ( int numberOfRadar ) {
        this.numberOfRadar = numberOfRadar;
    }

    public ObserverNetwork getObserverNetwork ( ) {
        return observerNetwork;
    }

    public void setObserverNetwork ( ObserverNetwork observerNetwork ) {
        this.observerNetwork = observerNetwork;
    }
}
