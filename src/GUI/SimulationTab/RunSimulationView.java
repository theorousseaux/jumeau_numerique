package src.GUI.SimulationTab;

import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel to run the simulation
 */
public class RunSimulationView extends JPanel {

    public RunSimulationView ( MainFrame parent , DisplayObsView disp ) {

        GridBagConstraints gc = new GridBagConstraints ( );

        // Button to run simulation
        JButton runSimulationButton = new JButton ( "Run Simulation" );

        gc.gridx = 0;
        gc.gridy = 0;
        add ( runSimulationButton , gc );

        // Event manager
        runSimulationButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                boolean meas = false;
                try {
                    SimulationController controller = parent.getSimuController ( );
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
