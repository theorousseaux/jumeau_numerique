package src.App;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import src.App.EstimationTab.EstimationController;
import src.App.EstimationTab.EstimationPanel;
import src.App.GSTab.GSController;
import src.App.GSTab.GSpannel;
import src.App.HomeTab.HomePannel;
import src.App.ObserverTab.ObserverController;
import src.App.ObserverTab.ObserverPannel;
import src.App.ParametersTab.ParametersController;
import src.App.ParametersTab.ParametersView;
import src.App.SatelliteTab.SatelliteController;
import src.App.SatelliteTab.SatellitePanel;
import src.App.SimulationTab.SimulationController;
import src.App.SimulationTab.SimulationView;
import src.App.UpdateSatelliteDBTab.UpdateDBController;
import src.App.UpdateSatelliteDBTab.UpdateSatelliteDBPanel;
import src.App.WorldMapTab.WorldMapPanel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    public JPanel globePanel;
    public GSController gsController;
    public UpdateDBController updateDBController;

    public ObserverController obserController;

    public SatelliteController satController;

    public ParametersController paramController;

    public SimulationController simuController;

    public EstimationController estimationController;
    public JTabbedPane tabbedPane;

    public MainFrame ( ) throws NumberFormatException, IOException, SQLException, ClassNotFoundException {

        // Initialisation du controller des stations sol
        gsController = new GSController ( );
        obserController = new ObserverController ( gsController );
        paramController = new ParametersController ( );
        estimationController = new EstimationController ( );
        simuController = new SimulationController ( );
        updateDBController = new UpdateDBController ( );
        satController = new SatelliteController ( updateDBController );

        JFrame frame = new JFrame ( "Space Observation Digital Twin" );
        globePanel = new WorldMapPanel ( this );

        // Création des onglets
        this.tabbedPane = new JTabbedPane ( );
        JPanel homePanel = new HomePannel ( );
        JPanel updateSatelliteDBPanel = new UpdateSatelliteDBPanel ( this );
        JPanel satPanel = new SatellitePanel ( this );
        JPanel groundStationPanel = new GSpannel ( this );
        JPanel observerPanel = new ObserverPannel ( this );
        // JPanel analysisPanel = new AnalysisPannel();
        JPanel parametersPanel = new ParametersView ( this );
        JPanel simulationPanel = new SimulationView ( this );
        JPanel estimationPanel = new EstimationPanel ( this );

        // Build the custom BuildJPanel object - it contains
        // the specified JPanel.  This JPanel will contain the Earth model.

        // Ajout des onglets au panneau d'onglets
        tabbedPane.addTab ( "Home" , homePanel );
        tabbedPane.addTab ( "Update DB" , updateSatelliteDBPanel );

        tabbedPane.addTab ( "Ground Station" , groundStationPanel );
        tabbedPane.addTab ( "Observer" , observerPanel );
        tabbedPane.addTab ( "Globe" , globePanel );
        //tabbedPane.addTab("Station visualization", worldMapPanel);
        // tabbedPane.addTab("Analysis", analysisPanel);
        tabbedPane.addTab ( "Simulation parameters" , parametersPanel );
        tabbedPane.addTab ( "Satellites" , satPanel );
        tabbedPane.addTab ( "Run Simulation" , simulationPanel );
        tabbedPane.addTab ( "OD" , estimationPanel );

        // Configuration de la fenêtre
        frame.add ( tabbedPane );
        frame.setTitle ( "Digital twin" );
        frame.setSize ( 1200 , 800 );
        frame.setLocationRelativeTo ( null );
        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        frame.setVisible ( true );
    }

    public static void main ( String[] args ) throws NumberFormatException, IOException, SQLException, ClassNotFoundException {
        //importation des donnees de base (toujour mettre ça en début de programme)
        File orekitData = new File ( "lib/orekit-data-master" );
        DataProvidersManager manager = DataContext.getDefault ( ).getDataProvidersManager ( );
        manager.addProvider ( new DirectoryCrawler ( orekitData ) );

        MainFrame frame = new MainFrame ( );
    }
}
