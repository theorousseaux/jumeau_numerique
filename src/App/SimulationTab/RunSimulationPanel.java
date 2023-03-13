package src.App.SimulationTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel to run the simulation
 */
public class RunSimulationPanel extends JPanel {

    public RunSimulationPanel ( MainFrame parent , DisplayObsPanel disp ) {

        GridBagConstraints gc = new GridBagConstraints ( );

        // Button to run simulation
        JButton runSimulationButton = new JButton ( "Run Simulation" );

        gc.gridx = 0;
        gc.gridy = 0;
        // Checkbox to choose to display measurements
        JCheckBox checkBox = new JCheckBox ( "Display measurements" );
        add ( checkBox , gc );
        gc.gridx = 0;
        gc.gridy = 1;
        add ( runSimulationButton , gc );

        // Event manager
        runSimulationButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                boolean meas = false;
                try {
                    if (checkBox.isSelected ( )) {
                        meas = true;
                    }
                    SimulationController controller = parent.simuController;
                    controller.runSimulation ( parent );
                    disp.update ( );
                    disp.repaint ( );
                    disp.revalidate ( );

                } catch (NullPointerException npe) {
                    JOptionPane.showMessageDialog ( parent , "Load your simulation first" , "Error" , JOptionPane.ERROR_MESSAGE );
                }


            }
        } );
    }
}
