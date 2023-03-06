package src.App.SimulationTab;

import javax.swing.*;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.measurements.AngularAzEl;

import src.Kalman.Station;

import src.App.MainFrame;
import src.UseCase1_GSNetwork.GSNetwork;

import java.util.List;
import java.util.SortedSet;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class DisplayObsPannel extends JPanel{

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();;

    public DisplayObsPannel(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Measurements");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(0, 0, 20, 0); // Ajoute 5 pixels de padding en bas
        this.add(tittle, gc);
    }

    public void removeAllExceptTop() {
        Component[] components = this.getComponents();
        for (int i = 1; i < components.length; i++) {
            this.remove(components[i]);
        }
    }


    public void update() {

        // Suppression des éléments sauf le titre
        removeAllExceptTop();
        // Affichage du réseau
        GridBagConstraints gc = new GridBagConstraints(); // Réinitialisation de gc pour chaque nouvel élément
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.WEST;
        List<SortedSet<ObservedMeasurement<?>>> measurementsList = this.parent.simuController.model.getMeasurementsSetsList();

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String>();
        for (SortedSet<ObservedMeasurement<?>> set : measurementsList){
            for (ObservedMeasurement<?> obs : set){
                elements.add("Date: " + obs.getDate().toString() + "; Station: " + ((AngularAzEl) obs).getStation().getBaseFrame().getName() + "; Satellite: " + ((AngularAzEl) obs).getSatellites().get(0).getPropagatorIndex());
            }
        }
        String[] arr = {};
        arr = elements.toArray(arr); 
        JList<String> liste = new JList<>(arr);
        JScrollPane satellites = new JScrollPane(liste);
        this.add(satellites, gc);


    }

}
    

