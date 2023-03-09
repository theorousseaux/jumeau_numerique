package src.App;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import src.App.AnalysisTab.AnalysisPannel;
import src.App.EstimationTab.EstimationController;
import src.App.EstimationTab.EstimationPanel;
import src.App.GSTab.GSController;
import src.App.GSTab.GSpannel;
import src.App.HomeTab.HomePannel;
import src.App.ObserverTab.ObserverController;
import src.App.ObserverTab.ObserverPannel;
import src.App.ParametersTab.ParametersController;
import src.App.ParametersTab.ParametersView;
import src.App.UpdateSatelliteDBTab.UpdateSatelliteDBPanel;
import src.App.WorldMapTab.WorldMapPanel;
import src.App.SimulationTab.SimulationController;
import src.App.SimulationTab.SimulationView;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    public JPanel globePanel;
    public GSController gsController;

    public ObserverController obserController;

    public ParametersController paramController;

    public SimulationController simuController;

    public EstimationController estimationController;

    public MainFrame() throws NumberFormatException, IOException {

        // Initialisation du controller des stations sol
        gsController = new GSController();

        obserController = new ObserverController(gsController);

        paramController = new ParametersController();

        simuController = new SimulationController();

        JFrame frame = new JFrame("Space Observation Digital Twin");
        globePanel = new WorldMapPanel(this);

        // Création des onglets
        JTabbedPane tabbedPane=new JTabbedPane(); 
        JPanel homePanel = new HomePannel();
        JPanel updateSatelliteDBPanel = new UpdateSatelliteDBPanel(this);
        JPanel groundStationPanel = new GSpannel(this);
        JPanel observerPanel = new ObserverPannel(this);
        // JPanel analysisPanel = new AnalysisPannel();
        JPanel parametersPanel = new ParametersView(this);
        JPanel simulationPanel = new SimulationView(this);
        JPanel estimationPanel = new EstimationPanel ( this );

        // Build the custom BuildJPanel object - it contains
        // the specified JPanel.  This JPanel will contain the Earth model.

        // Ajout des onglets au panneau d'onglets
        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Update DB", updateSatelliteDBPanel);
        tabbedPane.addTab("Ground Station", groundStationPanel);
        tabbedPane.addTab("Observer", observerPanel);
        tabbedPane.addTab("globe",globePanel);
        //tabbedPane.addTab("Station visualization", worldMapPanel);
        // tabbedPane.addTab("Analysis", analysisPanel);
        tabbedPane.addTab("Simulation parameters", parametersPanel);
        tabbedPane.addTab("Run Simulation", simulationPanel);
        tabbedPane.addTab("OD", estimationPanel);

        // Configuration de la fenêtre
        frame.add(tabbedPane);
        frame.setTitle("Digital twin");
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        //importation des donnees de base (toujour mettre ça en début de programme)
        File orekitData = new File("lib/orekit-data-master");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        new MainFrame();
    }
}
