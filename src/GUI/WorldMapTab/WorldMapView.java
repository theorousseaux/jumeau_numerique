package src.GUI.WorldMapTab;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import src.GUI.MainFrame;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class displays a globe with the ground stations.
 * It will display in different colors selected and unselected stations
 */
public final class WorldMapView extends JPanel {
    public MainFrame parent;
    public final WorldWindowGLJPanel wwPanel;
    public RenderableLayer GSLayer;
    public RenderableLayer orbitLayer;
    GridBagConstraints gc = new GridBagConstraints ( );

    public WorldMapView ( MainFrame parent ) {

        super ( new BorderLayout ( ) );

        this.wwPanel = new WorldWindowGLJPanel ( );

        Model m = (Model) WorldWind.createConfigurationComponent
                ( AVKey.MODEL_CLASS_NAME );

        wwPanel.setModel ( m );

        // Display of ground stations
        GSLayer = new RenderableLayer ( );
        for (Station station : parent.getGsController ( ).getModel ().getGroundStationList()) {


            double latitude = station.getLatitude ( );
            double longitude = station.getLongitude ( );
            double altitude = station.getAltitude ( );
            Position position = Position.fromRadians ( latitude , longitude , altitude );
            PointPlacemark placemark = new PointPlacemark ( position );

            placemark.setLabelText ( station.getName ( ) );

            placemark.setAttributes ( createPlacemarkAttributes ( station.getName ( ) , new HashMap<String, String> ( ) ) );
            GSLayer.addRenderable ( placemark );

        }
        wwPanel.getModel ( ).getLayers ( ).add ( GSLayer );

        this.add ( wwPanel , BorderLayout.CENTER );
    }

    public void setParent ( MainFrame parent ) {
        this.parent = parent;
    }

    /**
     * Display a new ground station when its created
     *
     * @param parent
     */
    public void displayNewGS ( MainFrame parent, boolean loaded) {
        this.repaint ( );
        // Display of ground stations

        Model m = (Model) WorldWind.createConfigurationComponent
                ( AVKey.MODEL_CLASS_NAME );


        wwPanel.setModel ( m );
        GSLayer = new RenderableLayer ( );
        ArrayList<Station> stations = new ArrayList<> (  );
        HashMap<String, String> selectedStations = new HashMap<> ( );
        stations.addAll ( parent.getGsController ().getModel ().getGroundStationList () );
        if (loaded){


            for (TelescopeAzEl teles : parent.getSimuController ().getModel ().getObserverNetwork ().getTelescopes ()) {
                Station station = teles.getStation ( );
                selectedStations.put ( station.getName ( ) , "" );
            }
        }else{
            for (TelescopeAzEl teles : parent.getObserController ().getModel ().getTelescopeAzElList ()) {
                Station station = teles.getStation ( );
                selectedStations.put ( station.getName ( ) , "" );
            }        }
        for (Station station : stations) {

            double latitude = station.getLatitude ( );
            double longitude = station.getLongitude ( );
            double altitude = station.getAltitude ( );
            Position position = Position.fromRadians ( latitude , longitude , altitude );
            PointPlacemark placemark = new PointPlacemark ( position );
            try {
                placemark.setAttributes ( createPlacemarkAttributes ( station.getName ( ) , selectedStations ) );
            } catch (Exception e) {

            }
            if (selectedStations.containsKey ( station )) {
                placemark.setLabelText ( station.getName ( ) + ": " + selectedStations.get ( station.getName ( ) ) );
            } else {
                placemark.setLabelText ( station.getName ( ) );
            }
            GSLayer.addRenderable ( placemark );
        }

        wwPanel.getModel ( ).getLayers ( ).add ( GSLayer );

        // Adding WorldWindPanel to the JPanel
        this.add ( this.wwPanel , BorderLayout.CENTER );
    }

    private PointPlacemarkAttributes createPlacemarkAttributes ( String station , HashMap<String, String> selectedstations ) {
        String imageName;
        PointPlacemarkAttributes attrs = new PointPlacemarkAttributes ( );
        boolean selected = selectedstations.containsKey ( station );

        if (selected) {
            imageName = "src/GUI/img/selected.jpeg";
            attrs.setLabelColor ( "0xFF00FF00" );
            attrs.setScale ( 0.05 );

        } else {
            imageName = "src/GUI/img/unselected.jpeg";
            attrs.setLabelColor ( "0xFF0000FF" );
            attrs.setScale ( 0.01 );
        }
        attrs.setImageAddress ( imageName );
        return attrs;
    }

}
