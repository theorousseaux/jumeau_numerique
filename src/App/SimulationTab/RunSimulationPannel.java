package src.App.SimulationTab;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.orekit.estimation.measurements.ObservableSatellite;

import src.App.MainFrame;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;


public class RunSimulationPannel extends JPanel{
    
    public RunSimulationPannel(MainFrame parent, DisplayObsPannel disp){

        GridBagConstraints gc = new GridBagConstraints();

        // Création des boutons pour lancer la simulation
        JButton runSimulationButton = new JButton("Run Simulation");

        // Ajout des boutons au panneau Satellite
        gc.gridx = 0;
        gc.gridy = 0;
        JCheckBox checkBox = new JCheckBox("Display measurements");
        add(checkBox,gc);
        gc.gridx = 0;
        gc.gridy =1;
        add(runSimulationButton, gc);

        

        // Gestion des évènements
        runSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean meas = false;
                if (checkBox.isSelected()){
                    meas = true;
                }
                SimulationController controller = parent.simuController;
                controller.runSimulation(parent);
                disp.update();
                disp.repaint();
                disp.revalidate();
            }
        });
    }
}
