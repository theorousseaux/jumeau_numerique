package src.App.EstimationTab;

import org.orekit.estimation.measurements.AngularAzEl;
import org.orekit.estimation.measurements.ObservedMeasurement;
import src.App.MainFrame;
import src.App.SimulationTab.DisplayObsPanel;
import src.App.SimulationTab.SimulationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class EstimationPanel extends JPanel{

    public EstimationPanel ( MainFrame parent){

        GridBagConstraints gc = new GridBagConstraints();

        // Button to run simulation
        JButton runEstimationButton = new JButton("Run Estimation");

        gc.gridx = 0;
        gc.gridy = 0;

        gc.gridx = 0;
        gc.gridy =1;
        add(runEstimationButton, gc);

        DisplayEstPanel disp = new DisplayEstPanel ( parent );

        gc.gridx = 0;
        gc.gridy =2;

        add(disp, gc);
        // Event manager
        runEstimationButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed( ActionEvent e) {
                //try {
                   EstimationController controller = parent.estimationController;
                try {
                    controller.loadEstimation ( parent );
                } catch (IOException ex) {
                    throw new RuntimeException ( ex );
                }
                try {
                    controller.runEstimation( parent );
                } catch (IOException ex) {
                    throw new RuntimeException ( ex );
                }
                disp.update ( );
                    disp.repaint ( );
                    disp.revalidate ( );
                    /*
                }catch (NullPointerException | IOException npe) {
                    JOptionPane.showMessageDialog(parent, "Run your simulation first", "Error",JOptionPane.ERROR_MESSAGE);
                }

                     */
            }
        });
    }
}
