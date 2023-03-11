package src.App.ObserverTab;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

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
        ArrayList<String> elements = new ArrayList<String>();

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
            sb.append(", ");
            sb.append("GEO :");
            sb.append(telescopeAzEl.getGEO());
            elements.add(sb.toString ());
        }

        for (src.Kalman.Radar radar: parent.obserController.radarList) {
            // Affichage de la liste des telescopes
            StringBuilder sb = new StringBuilder();
            sb.append(radar.getID());
            sb.append(" : ");
            sb.append("Mean :");
            sb.append(Arrays.toString(radar.getMean()));
            sb.append(", ");
            sb.append("Angular incertitude :");
            sb.append(Arrays.toString(radar.getAngularIncertitude()));
            sb.append(", ");
            sb.append("angularFoV :");
            sb.append(radar.getAngularFoV());
            sb.append(", ");
            sb.append("stepMeasure :");
            sb.append(radar.getStepMeasure());
            elements.add(sb.toString ());
        }

        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        JScrollPane scrollPane = new JScrollPane(liste);
        this.add(scrollPane, gc,1);
    }

    public void removeAllExceptTop() {
        Component[] components = this.getComponents();
        for (int i = 1; i < components.length; i++) {
            this.remove(components[i]);
        }
    }

    public void displayNewObserver() {
        removeAllExceptTop();
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
        ArrayList<String> elements = new ArrayList<String>();

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
            sb.append(", ");
            sb.append("GEO :");
            sb.append(telescopeAzEl.getGEO());
            elements.add(sb.toString ());
        }

        for (src.Kalman.Radar radar: parent.obserController.radarList) {
            // Affichage de la liste des telescopes
            StringBuilder sb = new StringBuilder();
            sb.append(radar.getID());
            sb.append(" : ");
            sb.append("Mean :");
            sb.append(Arrays.toString(radar.getMean()));
            sb.append(", ");
            sb.append("Angular incertitude :");
            sb.append(Arrays.toString(radar.getAngularIncertitude()));
            sb.append(", ");
            sb.append("angularFoV :");
            sb.append(radar.getAngularFoV());
            sb.append(", ");
            sb.append("stepMeasure :");
            sb.append(radar.getStepMeasure());
            elements.add(sb.toString ());
        }

        String[] arr = {};
        arr = elements.toArray(arr);
        JList<String> liste = new JList<>(arr);
        JScrollPane scrollPane = new JScrollPane(liste);
        this.add(scrollPane, gc,1);
    }
}
