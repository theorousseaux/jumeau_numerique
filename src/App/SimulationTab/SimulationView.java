package src.App.SimulationTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints ( );
    SimulationController controller;

    public SimulationView ( MainFrame parent ) {

        this.parent = parent;
        DisplaySimPanel displaySetup = new DisplaySimPanel ( parent );
        LoadSimulationPannel loadSimulation = new LoadSimulationPannel ( parent , displaySetup );
        DisplayObsPanel displayObs = new DisplayObsPanel ( parent );
        RunSimulationPanel runSimulation = new RunSimulationPanel ( parent , displayObs );

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
