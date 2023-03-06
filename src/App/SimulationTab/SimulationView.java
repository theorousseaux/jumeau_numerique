package src.App.SimulationTab;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.orekit.estimation.measurements.ObservableSatellite;

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
        DisplaySimPannel displaySetup = new DisplaySimPannel(parent);
        LoadSimulationPannel loadSimulation = new LoadSimulationPannel(parent, displaySetup);
        DisplayObsPannel displayObs = new DisplayObsPannel(parent);
        RunSimulationPannel runSimulation = new RunSimulationPannel(parent, displayObs);

        setLayout(new GridBagLayout());

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.PAGE_START;

        add(loadSimulation,gc);

        gc.gridy ++;
        gc.anchor = GridBagConstraints.PAGE_START;

        add(displaySetup, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.PAGE_START;

        add(runSimulation,gc);

        gc.gridy ++;
        gc.anchor = GridBagConstraints.PAGE_START;

        add(displayObs, gc);

        
    }
    
}
