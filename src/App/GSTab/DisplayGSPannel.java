package src.App.GSTab;

import java.awt.Image;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hipparchus.util.Precision;

import src.App.MainFrame;
import src.Kalman.Station;

public class DisplayGSPannel extends JPanel{
    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();

    int numberOfGS = 0;

    public DisplayGSPannel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        for (src.Kalman.Station groundStation: parent.groundStationList) {
            // Affichage de la liste des stations sol dans une fenêtre de dialogue
            StringBuilder sb = new StringBuilder();
            sb.append(groundStation.getName());
            sb.append(" : ");
            sb.append("Longitude = ");
            sb.append(Precision.round(groundStation.getLongitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Latitude = ");
            sb.append(Precision.round(groundStation.getLatitude()*180/Math.PI, 2));
            sb.append(", ");
            sb.append("Altitude = ");
            sb.append(groundStation.getAltitude());

            gc.anchor = GridBagConstraints.LINE_START;
            this.add(new JLabel(sb.toString()), gc);
            gc.gridy++;
            numberOfGS++;
        }

        ImageIcon imageIcon = new ImageIcon("src/App/img/station_sol_icon.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back

        // Ajout de l'image
        gc.gridheight = gc.gridy;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        //this.add(new JButton("Oui"), gc);
        this.add(new JLabel(imageIcon), gc);
    }

    public void displayNewStation(Station station) {
        GridBagConstraints newGC = new GridBagConstraints();
        newGC.weightx = 0;
        newGC.gridx = 0;
        newGC.gridy = numberOfGS+1;

        // Affichage de la liste des stations sol dans une fenêtre de dialogue
        StringBuilder sb = new StringBuilder();
        sb.append(station.getName());
        sb.append(" : ");
        sb.append("Longitude = ");
        sb.append(Precision.round(station.getLongitude()*180/Math.PI, 2));
        sb.append(", ");
        sb.append("Latitude = ");
        sb.append(Precision.round(station.getLatitude()*180/Math.PI, 2));
        sb.append(", ");
        sb.append("Altitude = ");
        sb.append(station.getAltitude());

        newGC.anchor = GridBagConstraints.LINE_START;
        this.add(new JLabel(sb.toString()), newGC);
        numberOfGS++;
    }
}
