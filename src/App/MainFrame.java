package src.App;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import src.App.AnalysisTab.AnalysisPannel;
import src.App.GSTab.GSController;
import src.App.GSTab.GSpannel;
import src.App.HomeTab.HomePannel;
import src.App.SatelliteTab.SatellitePannel;
import src.App.SimuParam.ParametersView;
import src.Data.ReadGSFile;
import src.Data.WriteGSFile;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.GSNetwork;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    public GSController gsController;

    public MainFrame() throws NumberFormatException, IOException {

        // Initialisation du controller des stations sol
        gsController = new GSController();

        JFrame frame = new JFrame("Space Observation Digital Twin");

        // Création des onglets
        JTabbedPane tabbedPane=new JTabbedPane(); 
        JPanel homePanel = new HomePannel();
        JPanel satellitePanel = new SatellitePannel();
        JPanel groundStationPanel = new GSpannel(this);
        JPanel analysisPanel = new AnalysisPannel();
        JPanel parametersPanel = new ParametersView();


        // Ajout des onglets au panneau d'onglets
        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Satellite", satellitePanel);
        tabbedPane.addTab("Ground Station", groundStationPanel);
        tabbedPane.addTab("Analysis", analysisPanel);
        tabbedPane.addTab("Simulation parameters", parametersPanel);



        // Configuration de la fenêtre
        frame.add(tabbedPane);
        frame.setTitle("Main Window");
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
