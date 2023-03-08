package src.App.WorldMapTab;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.tools.javadoc.Main;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.Model;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.view.orbit.OrbitView;
import src.App.MainFrame;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.render.PointPlacemark;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import gov.nasa.worldwind.layers.RenderableLayer;
import src.Kalman.Station;

public final class WorldMapPanel extends JPanel{
    public void setParent(MainFrame parent) {
        this.parent = parent;
    }

    public MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();
    public WorldWindowGLJPanel wwPanel;

    public RenderableLayer GSLayer;
    public WorldMapPanel(MainFrame parent) throws IOException {

        super(new BorderLayout());
        // Création d'une instance de WorldWindPanel
        this.wwPanel = new WorldWindowGLJPanel();

        Model m = (Model)WorldWind.createConfigurationComponent
                (AVKey.MODEL_CLASS_NAME);

        wwPanel.setModel(m);

        // Display of ground stations
        GSLayer = new RenderableLayer();
        for (Station station : parent.gsController.groundStationList){


            double latitude = station.getLatitude();
            double longitude = station.getLongitude();
            double altitude = station.getAltitude();
            Position position = Position.fromRadians(latitude, longitude, altitude);
            PointPlacemark placemark = new PointPlacemark(position);
            //PointPlacemarkAttributes attrs = (PointPlacemarkAttributes) placemark.getDefaultAttributes();

            placemark.setLabelText(station.getName());
            //attrs.setScale(0.02);
            //attrs.setImage(image);
            placemark.setAttributes(createPlacemarkAttributes("src/App/img/station_sol_icon.png", false));
            GSLayer.addRenderable(placemark);

        }
        wwPanel.getModel().getLayers().add(GSLayer);

        // Ajout de l'instance de WorldWindPanel à ce JPanel
        this.add(wwPanel, BorderLayout.CENTER);
    }

    public void displayNewGS(MainFrame parent) throws IOException {
        this.repaint();
        // Display of ground stations

        Model m = (Model)WorldWind.createConfigurationComponent
                (AVKey.MODEL_CLASS_NAME);

        wwPanel.setModel(m);
        GSLayer = new RenderableLayer();
        BufferedImage selectimage = ImageIO.read(new File("src/App/img/requin.jpeg"));
        BufferedImage image = ImageIO.read(new File("src/App/img/station_sol_icon.png"));
        for (Station station : parent.gsController.groundStationList){

            double latitude = station.getLatitude();
            double longitude = station.getLongitude();
            double altitude = station.getAltitude();
            Position position = Position.fromRadians(latitude, longitude, altitude);
            PointPlacemark placemark = new PointPlacemark(position);
            try{
                if (parent.gsController.gsNetwork.getNames().contains(station.getName())){

                    placemark.setAttributes(createPlacemarkAttributes("src/App/img/requin.jpeg",true));

                }else{
                    placemark.setAttributes(createPlacemarkAttributes("src/App/img/station_sol_icon.png",false));

                }
            }catch(Exception e){

            }

            //PointPlacemarkAttributes attrs = (PointPlacemarkAttributes) placemark.getDefaultAttributes();

            placemark.setLabelText(station.getName());
            //attrs.setScale(0.02);
            //attrs.setImage(image);
            GSLayer.addRenderable(placemark);


        }
        wwPanel.getModel().getLayers().add(GSLayer);

        // Ajout de l'instance de WorldWindPanel à ce JPanel
        this.add(this.wwPanel, BorderLayout.CENTER);
    }

    private PointPlacemarkAttributes createPlacemarkAttributes(String imageName, boolean selected) {
        PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
        attrs.setImageAddress(imageName);
        if (selected){
            attrs.setLabelColor("0xFF0000FF");
        }
        attrs.setScale(0.01);
        return attrs;
    }

}
