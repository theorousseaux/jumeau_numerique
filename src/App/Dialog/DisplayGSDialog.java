package src.App.Dialog;

import org.hipparchus.util.Precision;
import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class DisplayGSDialog {

    JFrame parent;
    StringBuilder sb;
    ImageIcon imageIcon;

    public DisplayGSDialog(MainFrame parent) {
        this.parent = parent;

        // Affichage de la liste des stations sol dans une fenêtre de dialogue
        StringBuilder sb = new StringBuilder();
        for (src.Kalman.Station groundStation: parent.groundStationList) {
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
            sb.append("\n");
        }

        ImageIcon imageIcon = new ImageIcon("src/App/img/station_sol_icon.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back

        this.sb = sb;
        this.imageIcon = imageIcon;
    }

    public void display() {
        showMessageDialog(this.parent, this.sb.toString(), "Ground Stations", JOptionPane.INFORMATION_MESSAGE, this.imageIcon);
    }
}
