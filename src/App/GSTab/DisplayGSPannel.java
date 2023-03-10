package src.App.GSTab;

import java.awt.Image;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import org.hipparchus.util.Precision;

import org.orekit.estimation.measurements.ObservableSatellite;
import src.App.MainFrame;
import src.Kalman.Station;

public class DisplayGSPannel extends JPanel{
    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();
    JScrollPane stations;
    public DisplayGSPannel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Ground stations list");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.gridwidth = 1;
        ArrayList<String> elements = new ArrayList<String>();

        for (src.Kalman.Station groundStation: parent.gsController.groundStationList) {
            // Affichage de la liste des stations sol dans une fenêtre de dialogue
            StringBuilder sb = new StringBuilder();
            sb.append(groundStation.getName());
            sb.append(" : ");
            sb.append("Latitude = ");
            sb.append(Precision.round(groundStation.getLatitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Longitude = ");
            sb.append(Precision.round(groundStation.getLongitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Altitude = ");
            sb.append(groundStation.getAltitude());

            elements.add(sb.toString());

            parent.gsController.numberOfGS++;
        }
        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        stations = new JScrollPane(liste);
        this.add(stations, gc,1);
    }
    public void removeAllExceptTop() {
        Component[] components = this.getComponents();
        for (int i = 1; i < components.length; i++) {
            this.remove(components[i]);
        }
    }
    public void displayNewStation() {
        removeAllExceptTop();
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Ground stations list");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.gridwidth = 1;
        ArrayList<String> elements = new ArrayList<String>();

        for (src.Kalman.Station groundStation: parent.gsController.groundStationList) {
            // Affichage de la liste des stations sol dans une fenêtre de dialogue
            StringBuilder sb = new StringBuilder();
            sb.append(groundStation.getName());
            sb.append(" : ");
            sb.append("Latitude = ");
            sb.append(Precision.round(groundStation.getLatitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Longitude = ");
            sb.append(Precision.round(groundStation.getLongitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Altitude = ");
            sb.append(groundStation.getAltitude());

            elements.add(sb.toString());

            parent.gsController.numberOfGS++;
        }
        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        stations = new JScrollPane(liste);
        this.add(stations, gc,1);
    }
}
