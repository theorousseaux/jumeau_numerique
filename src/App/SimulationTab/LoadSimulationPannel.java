package src.App.SimulationTab;

import javax.swing.*;

import src.App.MainFrame;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * Panel to load simulation settings
 */
public class LoadSimulationPannel extends JPanel{

    public LoadSimulationPannel(MainFrame parent, DisplaySimPanel displaySimPanel ){

        GridBagConstraints gc = new GridBagConstraints();

        // Creation of button for fetching simulation data
        JButton loadSimulationButton = new JButton("Load Simulation");

        gc.gridy = 0;
        gc.gridx = 0;
        add(loadSimulationButton, gc);

        // Event manager
        loadSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                SimulationController controller = parent.simuController;
                try {
                    controller.loadSimulation(parent);
                    displaySimPanel.update();
                    displaySimPanel.repaint();
                    displaySimPanel.revalidate();
                    }

                catch (Exception e2) {
                    JOptionPane.showMessageDialog(parent, "Input simulation parameters, ground stations and satellites first.", "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
