package src.App.SimulationTab;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import src.App.MainFrame;

public class SimulationView extends JPanel{

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();
    SimulationController controller;

    public SimulationView(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Création du bouton pour récupérer les données pour la simulation
        JButton loadSimulationButton = new JButton("Load Simulation");

        // Ajout des boutons au panneau Satellite
        gc.gridy = 0;
        gc.gridx = 0;
        add(loadSimulationButton, gc);

    


        // Gestion des évènements
        loadSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller = parent.simuController;
                try {
                    controller.loadSimulation(parent);
                } catch (IllegalArgumentException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        // Création des boutons pour lancer la simulation
        JButton runSimulationButton = new JButton("Run Simulation");

        // Ajout des boutons au panneau Satellite
        gc.gridy ++;
        add(runSimulationButton, gc);

        

        // Ajout de la barre de progression
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        gc.gridy ++;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, gc);

        // Gestion des évènements
        runSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller = parent.simuController;

                controller.runSimulation(parent);
            }
        });
    }
    
}
