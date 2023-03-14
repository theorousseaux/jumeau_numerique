package src.GUI.SimulationTab;

import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends JPanel {

    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );
    SimulationController controller;

    public SimulationView ( MainFrame parent ) {

        this.parent = parent;
        DisplaySimView displaySetup = new DisplaySimView ( parent );
        LoadSimulationView loadSimulation = new LoadSimulationView ( parent , displaySetup );
        DisplayObsView displayObs = new DisplayObsView ( parent );
        RunSimulationView runSimulation = new RunSimulationView ( parent , displayObs );

        setLayout ( new GridBagLayout ( ) );

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.PAGE_START;

        add ( loadSimulation , gc );

        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;

        add ( displaySetup , gc );

        gc.gridx = 1;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.PAGE_START;

        add ( runSimulation , gc );

        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;

        add ( displayObs , gc );


    }

}
