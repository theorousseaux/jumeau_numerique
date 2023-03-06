package src.App.SimulationTab;
import javax.swing.*;

import org.orekit.estimation.measurements.ObservableSatellite;
import src.Kalman.Station;

import src.App.MainFrame;
import src.UseCase1_GSNetwork.GSNetwork;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;


public class DisplaySimPannel extends JPanel{
    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();;

    public DisplaySimPannel(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Simulation set up");
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
        JLabel satellitesLabel = new JLabel("Satellites:");
        this.add(satellitesLabel, gc);

        

        gc.gridx = 1;

        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        List<ObservableSatellite> satelliteList = this.parent.simuController.model.getSatellitesNames();
        ArrayList<String> elements = new ArrayList<String>();
        for (ObservableSatellite sat : satelliteList){
            elements.add(String.valueOf(sat.getPropagatorIndex()));
        }
        String[] arr = {};
        arr = elements.toArray(arr); 
        JList<String> liste = new JList<>(arr);
        JScrollPane satellites = new JScrollPane(liste);
        this.add(satellites, gc);

        gc.gridx = 0;
        gc.gridy ++;
        gc.anchor = GridBagConstraints.WEST;
        JLabel networkLabel = new JLabel("Network stations:");
        this.add(networkLabel, gc);

        gc.gridx ++;
        GSNetwork stationsList = this.parent.simuController.model.getGroundStationNetwork();
        ArrayList<String> stations = new ArrayList<String>();
        for (Station station : stationsList.getNetwork()){
            stations.add(String.valueOf(station.getName()));
        }
        String[] arrStations = {};
        arrStations = stations.toArray(arrStations); 
        JList<String> listestations = new JList<>(arrStations);
        JScrollPane affichagetations = new JScrollPane(listestations);
        this.add(affichagetations, gc);


        gc.gridx = 0;
        gc.gridy ++;
        gc.anchor = GridBagConstraints.WEST;
        JLabel datesLabel = new JLabel("Simulation dates:");
        this.add(datesLabel, gc);
        gc.gridx = 1;
        gc.gridy ++;
        JLabel startDate = new JLabel("Start date: " + this.parent.simuController.model.getSimulationParameters().getStartDate().toString());
        JLabel endDate = new JLabel("End date: " + this.parent.simuController.model.getSimulationParameters().getEndDate().toString());
        this.add(startDate, gc);
        gc.gridy ++;
        this.add(endDate, gc);

    }

}
