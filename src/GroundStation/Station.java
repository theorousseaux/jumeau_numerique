package src.GroundStation;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.TopocentricFrame;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;
import src.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Station représente une station au sol avec une liste de télescopes et une liste de radars.
 * Elle hérite de la classe GroundStation.
 */
public class Station extends GroundStation {

    /** Liste de télescopes Azimuth-Elevation de la station */
    List<TelescopeAzEl> listTelescopes;

    /** Liste de radars de la station */
    List<Radar> listRadars;

    public List<TelescopeAzEl> getListTelescopes() {
        return listTelescopes;
    }

    public void setListTelescopes(List<TelescopeAzEl> listTelescopes) {
        this.listTelescopes = listTelescopes;
    }

    public List<Radar> getListRadars() {
        return listRadars;
    }

    public void setListRadars(List<Radar> listRadars) {
        this.listRadars = listRadars;
    }

    /**
     * Constructeur pour créer une station au sol à une altitude de 0m.
     * @param name Nom de la station.
     * @param latitude Latitude de la station.
     * @param longitude Longitude de la station.
     */
    public Station(String name, Double latitude, Double longitude) {
        super(new TopocentricFrame(constants.EARTH_SHAPE, new GeodeticPoint(latitude, longitude, 0.), name));
        this.listTelescopes = new ArrayList<>();
        this.listRadars = new ArrayList<>();
    }

    /**
     * Constructeur pour créer une station au sol à une altitude donnée.
     * @param name Nom de la station.
     * @param latitude Latitude de la station.
     * @param longitude Longitude de la station.
     * @param altitude Altitude de la station.
     */
    public Station(String name, Double latitude, Double longitude, Double altitude) {
        super(new TopocentricFrame(constants.EARTH_SHAPE, new GeodeticPoint(latitude, longitude, altitude), name));
        this.listTelescopes = new ArrayList<>();
        this.listRadars = new ArrayList<>();
    }

    /**
     * Ajoute un télescope à la liste de télescopes de la station.
     * @param telescope Télescope à ajouter.
     */
    public void addTelescope(TelescopeAzEl telescope) {
        this.listTelescopes.add(telescope);
    }

    /**
     * Retourne la liste de télescopes de la station.
     * @return Liste de télescopes.
     */
    public List<TelescopeAzEl> getListTelescope() {
        return this.listTelescopes;
    }

    /**
     * Ajoute un radar à la liste de radars de la station.
     * @param radar Radar à ajouter.
     */
    public void addRadar(Radar radar) {
        this.listRadars.add(radar);
    }

    /**
     * Retourne la liste de radars de la station.
     * @return Liste de radars.
     */
    public List<Radar> getListRadar() {
        return this.listRadars;
    }

    /**
     * Retourne le nom de la station.
     * @return Nom de la station.
     */
    public String getName() {
        return getBaseFrame().getName();
    }

    /**
     * Retourne la longitude de la station.
     * @return Longitude de la station.
     */
    public Double getLongitude() {
        return getBaseFrame().getPoint().getLongitude();
    }

    /**

     Renvoie la latitude de la station.
     @return la latitude de la station.
     */
    public Double getLatitude() {
        return getBaseFrame().getPoint().getLatitude();
    }

    /**

     Renvoie l'altitude de la station.
     @return l'altitude de la station.
     */
    public Double getAltitude() {
        return getBaseFrame().getPoint().getAltitude();
    }

    /**

     Renvoie une chaîne de caractères représentant la station.
     @return une chaîne de caractères représentant la station.
     */
    public String toString() {
        return "Station: " + getName();
    }
}
