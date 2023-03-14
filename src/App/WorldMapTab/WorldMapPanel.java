package src.App.WorldMapTab;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import src.App.MainFrame;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class displays a globe with the ground stations.
 * It will display in different colors selected and unselected stations
 */
public final class WorldMapPanel extends JPanel {
    public MainFrame parent;
    public WorldWindowGLJPanel wwPanel;
    public RenderableLayer GSLayer;
    public RenderableLayer orbitLayer;
    GridBagConstraints gc = new GridBagConstraints ( );

    public WorldMapPanel ( MainFrame parent ) throws IOException {

        super ( new BorderLayout ( ) );

        this.wwPanel = new WorldWindowGLJPanel ( );

        Model m = (Model) WorldWind.createConfigurationComponent
                ( AVKey.MODEL_CLASS_NAME );

        wwPanel.setModel ( m );

        // Display of ground stations
        GSLayer = new RenderableLayer ( );
        for (Station station : parent.gsController.groundStationList) {


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
     * @throws IOException
     */
    public void displayNewGS ( MainFrame parent ) throws IOException {
        this.repaint ( );
        // Display of ground stations

        Model m = (Model) WorldWind.createConfigurationComponent
                ( AVKey.MODEL_CLASS_NAME );

        HashMap<String, String> selectedStations = new HashMap<> ( );
        for (TelescopeAzEl teles : parent.obserController.observerNetwork.getTelescopes ( )) {
            String station = teles.getStation ( ).getName ( );
            selectedStations.put ( station , "" );
        }
        wwPanel.setModel ( m );
        GSLayer = new RenderableLayer ( );
        for (Station station : parent.gsController.groundStationList) {

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
        /*
        Angle lat;
        Angle lon = new Angle();
        orbitLayer = new RenderableLayer ();
        orbitLayer.addRenderable ( new Ellipsoid (new Position ( new Angle() ,,-6300000 ), 8000000, 8000000, 8000000) );
        wwPanel.getModel ().getLayers ().add(orbitLayer);

         */
        wwPanel.getModel ( ).getLayers ( ).add ( GSLayer );

        // Adding WorldWindPanel to the JPanel
        this.add ( this.wwPanel , BorderLayout.CENTER );
    }

    private PointPlacemarkAttributes createPlacemarkAttributes ( String station , HashMap<String, String> selectedstations ) {
        String imageName;
        PointPlacemarkAttributes attrs = new PointPlacemarkAttributes ( );
        boolean selected = selectedstations.containsKey ( station );

        if (selected) {
            imageName = "src/App/img/selected.jpeg";
            attrs.setLabelColor ( "0xFF00FF00" );
            attrs.setScale ( 0.05 );

        } else {
            imageName = "src/App/img/unselected.jpeg";
            attrs.setLabelColor ( "0xFF0000FF" );
            attrs.setScale ( 0.01 );
        }
        attrs.setImageAddress ( imageName );
        return attrs;
    }

}
