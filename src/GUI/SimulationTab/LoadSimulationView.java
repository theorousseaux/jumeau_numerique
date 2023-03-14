package src.GUI.SimulationTab;

import src.GUI.MainFrame;
import src.GUI.WorldMapTab.WorldMapView;
import src.Data.Simulation.LoadSimu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Panel to load simulation settings
 */
public class LoadSimulationView extends JPanel {

    public LoadSimulationView ( MainFrame parent , DisplaySimView displaySimView ) {

        GridBagConstraints gc = new GridBagConstraints ( );

        // Creation of button for fetching simulation data
        JButton loadSimulationButton = new JButton ( "Load Simulation" );
        JFileChooser fromFile = new JFileChooser ( "./src/Data/Simulation" );
        fromFile.addPropertyChangeListener ( new PropertyChangeListener ( ) {

            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                if (evt.getPropertyName ( ).equals ( "SelectedFileChangedProperty" )) {
                    File file = fromFile.getSelectedFile ( );
                    if (file != null) {
                        LoadSimu saver = new LoadSimu ( file.getPath ( ) );
                        System.out.println ( file.getPath ( ) );
                        try {
                            parent.getSimuController ( ).setModel (saver.load ( parent ));
                        } catch (IOException e) {
                            throw new RuntimeException ( e );
                        }
                        displaySimView.update ( );
                        displaySimView.repaint ( );
                        displaySimView.revalidate ( );
                        WorldMapView worldMapView =(WorldMapView) parent.getGlobePanel ();
                        worldMapView.displayNewGS ( parent, true );
                        worldMapView.repaint ( );
                        worldMapView.revalidate ( );
                    }
                }
            }
        } );

        gc.gridy = 0;
        gc.gridx = 0;
        add ( loadSimulationButton , gc );
        gc.gridx = 1;
        add ( fromFile , gc );
        gc.gridx = 0;

        // Event manager
        loadSimulationButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {

                SimulationController controller = parent.getSimuController ( );
                try {
                    controller.loadSimulation ( parent );

                    displaySimView.update ( );
                    displaySimView.repaint ( );
                    displaySimView.revalidate ( );
                    WorldMapView worldMapView =(WorldMapView) parent.getGlobePanel ();
                    worldMapView.displayNewGS ( parent, true );
                    worldMapView.repaint ( );
                    worldMapView.revalidate ( );
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog ( parent , "Input simulation parameters, ground stations and satellites first." , "Error" , JOptionPane.ERROR_MESSAGE );
                }
            }
        } );
    }
}
