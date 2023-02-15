package src.App;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import src.App.AnalysisTab.AnalysisPannel;
import src.App.GSTab.GSpannel;
import src.App.HomeTab.HomePannel;
import src.App.SatelliteTab.SatellitePannel;
import src.Data.ReadFile;
import src.Data.WriteFile;
import src.Kalman.Station;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    public List<Station> groundStationList;
    public ReadFile fileReader;
    public WriteFile fileWriter;

    public MainFrame() throws NumberFormatException, IOException {

        this.groundStationList = new ArrayList<>();
        this.fileReader = new ReadFile();
        this.fileWriter = new WriteFile();
        this.groundStationList = fileReader.readStation("src/Data/GS.csv");

        JFrame frame = new JFrame("Space Observation Digital Twin");

        // Création des onglets
        JTabbedPane tabbedPane=new JTabbedPane(); 
        JPanel homePanel = new HomePannel();
        JPanel satellitePanel = new SatellitePannel();
        JPanel groundStationPanel = new GSpannel(this);
        JPanel analysisPanel = new AnalysisPannel();

        // Ajout des onglets au panneau d'onglets
        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Satellite", satellitePanel);
        tabbedPane.addTab("Ground Station", groundStationPanel);
        tabbedPane.addTab("Analysis", analysisPanel);


        // Configuration de la fenêtre
        frame.add(tabbedPane);
        frame.setTitle("Main Window");
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Méthode pour ajouter une station sol à la liste
    public void addGroundStation(Station groundStation) {
        groundStationList.add(groundStation);
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        //importation des donnees de base (toujour mettre ça en début de programme)
        File orekitData = new File("lib/orekit-data-master");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        new MainFrame();
    }
}
