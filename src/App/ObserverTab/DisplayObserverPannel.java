package src.App.ObserverTab;

import java.awt.*;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hipparchus.util.Precision;

import src.App.MainFrame;
import src.Kalman.Station;

public class DisplayObserverPannel extends JPanel{
    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();

    public DisplayObserverPannel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Observers list");
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
        for (src.Kalman.TelescopeAzEl telescopeAzEl: parent.obserController.telescopeAzElList) {
            // Affichage de la liste des telescopes
            StringBuilder sb = new StringBuilder();
            sb.append(telescopeAzEl.getID());
            sb.append(" : ");
            sb.append("Mean :");
            sb.append(Arrays.toString(telescopeAzEl.getMean()));
            sb.append(", ");
            sb.append("Angular incertitude :");
            sb.append(Arrays.toString(telescopeAzEl.getAngularIncertitude()));
            sb.append(", ");
            sb.append("eleVationLimit :");
            sb.append(telescopeAzEl.getElevationLimit());
            sb.append(", ");
            sb.append("angularFoV :");
            sb.append(telescopeAzEl.getAngularFoV());
            sb.append(", ");
            sb.append("stepMeasure :");
            sb.append(telescopeAzEl.getStepMeasure());
            sb.append(", ");
            sb.append("breakTime :");
            sb.append(telescopeAzEl.getBreakTime());

            gc.anchor = GridBagConstraints.LINE_START;
            this.add(new JLabel(sb.toString()), gc);
            gc.gridy++;
            parent.gsController.numberOfGS++;
        }
    }

    public void displayNewStation(Station station) {
        GridBagConstraints newGC = new GridBagConstraints();
        newGC.weightx = 0;
        newGC.gridx = 0;
        newGC.gridy = parent.gsController.numberOfGS+1;

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
        parent.gsController.numberOfGS++;
    }
}
