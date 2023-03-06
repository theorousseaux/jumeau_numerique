package src.App.SimulationTab;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.orekit.estimation.measurements.ObservableSatellite;

import src.App.MainFrame;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class LoadSimulationPannel extends JPanel{


    public LoadSimulationPannel(MainFrame parent, DisplaySimPannel displaySimPannel){
        GridBagConstraints gc = new GridBagConstraints();

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
                
                SimulationController controller = parent.simuController;
                try {
                    controller.loadSimulation(parent);
                    displaySimPannel.update();
                    displaySimPannel.repaint();
                    displaySimPannel.revalidate();
                    }

                catch (IllegalArgumentException | IOException e1) {
                    // TODO Auto-generated catch block
                    ((Throwable) e1).printStackTrace();
                }
            }
        });
    }
}
