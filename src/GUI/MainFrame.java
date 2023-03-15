package src.GUI;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import src.GUI.EstimationTab.EstimationController;
import src.GUI.EstimationTab.EstimationView;
import src.GUI.GSTab.GSController;
import src.GUI.GSTab.GSView;
import src.GUI.HomeTab.HomeView;
import src.GUI.ObserverTab.ObserverController;
import src.GUI.ObserverTab.ObserverView;
import src.GUI.ParametersTab.ParametersController;
import src.GUI.ParametersTab.ParametersView;
import src.GUI.SatelliteTab.SatelliteController;
import src.GUI.SatelliteTab.SatelliteView;
import src.GUI.SimulationTab.SimulationController;
import src.GUI.SimulationTab.SimulationView;
import src.GUI.UpdateSatelliteDBTab.UpdateDBController;
import src.GUI.UpdateSatelliteDBTab.UpdateSatelliteDBView;
import src.GUI.WorldMapTab.WorldMapView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private JPanel globePanel;
    private JPanel observerPanel;
    private GSController gsController;
    private UpdateDBController updateDBController;
    private ObserverController obserController;
    private SatelliteController satController;
    private ParametersController paramController;
    private SimulationController simuController;
    private EstimationController estimationController;
    private JTabbedPane tabbedPane;

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
        globePanel = new WorldMapView ( this );
        observerPanel = new ObserverView ( this );
        // Création des onglets
        this.tabbedPane = new JTabbedPane ( );
        JPanel homePanel = new HomeView ( );
        JPanel updateSatelliteDBPanel = new UpdateSatelliteDBView ( this );
        JPanel satPanel = new SatelliteView ( this );
        JPanel groundStationPanel = new GSView ( this );
        JPanel observerPanel = new ObserverView ( this );
        JPanel parametersPanel = new ParametersView ( this );
        JPanel simulationPanel = new SimulationView ( this );
        JPanel estimationPanel = new EstimationView ( this );

        // Build the custom BuildJPanel object - it contains
        // the specified JPanel.  This JPanel will contain the Earth model.

        // Ajout des onglets au panneau d'onglets
        tabbedPane.addTab ( "Home" , homePanel );
        tabbedPane.addTab ( "Update DB" , updateSatelliteDBPanel );

        tabbedPane.addTab ( "Ground Station" , groundStationPanel );
        tabbedPane.addTab ( "Observer" , observerPanel );
        tabbedPane.addTab ( "Globe" , globePanel );
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

    public JPanel getGlobePanel ( ) {
        return globePanel;
    }

    public void setGlobePanel ( JPanel globePanel ) {
        this.globePanel = globePanel;
    }

    public GSController getGsController ( ) {
        return gsController;
    }

    public void setGsController ( GSController gsController ) {
        this.gsController = gsController;
    }

    public UpdateDBController getUpdateDBController ( ) {
        return updateDBController;
    }

    public void setUpdateDBController ( UpdateDBController updateDBController ) {
        this.updateDBController = updateDBController;
    }

    public ObserverController getObserController ( ) {
        return obserController;
    }

    public void setObserController ( ObserverController obserController ) {
        this.obserController = obserController;
    }

    public SatelliteController getSatController ( ) {
        return satController;
    }

    public void setSatController ( SatelliteController satController ) {
        this.satController = satController;
    }

    public ParametersController getParamController ( ) {
        return paramController;
    }

    public void setParamController ( ParametersController paramController ) {
        this.paramController = paramController;
    }

    public SimulationController getSimuController ( ) {
        return simuController;
    }

    public void setSimuController ( SimulationController simuController ) {
        this.simuController = simuController;
    }

    public EstimationController getEstimationController ( ) {
        return estimationController;
    }

    public void setEstimationController ( EstimationController estimationController ) {
        this.estimationController = estimationController;
    }

    public JTabbedPane getTabbedPane ( ) {
        return tabbedPane;
    }

    public void setTabbedPane ( JTabbedPane tabbedPane ) {
        this.tabbedPane = tabbedPane;
    }

    public JPanel getObserverPanel ( ) {
        return observerPanel;
    }

    public void setObserverPanel ( JPanel observerPanel ) {
        this.observerPanel = observerPanel;
    }
}
