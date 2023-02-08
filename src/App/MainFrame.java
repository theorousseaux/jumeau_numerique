package src.App;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import src.Kalman.Station;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    public static List<Station> groundStationList = new ArrayList<>();

    public MainFrame() {

        JFrame frame = new JFrame("Space Observation Digital Twin");

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Création des menus
        JMenu homeMenu = new JMenu("Home");
        JMenu satelliteMenu = new JMenu("Satellite");
        JMenu groundStationMenu = new JMenu("Ground Station");
        JMenu analysisMenu = new JMenu("Analysis");

        // Création des items de menu pour le menu Satellite
        JMenuItem updateDatabse = new JMenuItem("Update databse");
        JMenuItem addSatelliteItem = new JMenuItem("Add");

        // Ajout des items de menu au menu Satellite
        satelliteMenu.add(updateDatabse);
        satelliteMenu.add(addSatelliteItem);

        // Création des items de menu pour le menu Ground Station
        JMenuItem addGSMenuItem = new JMenuItem("Add");
        JMenuItem displayGSMenuItem = new JMenuItem("Display");

        // Ajout des items de menu au menu Ground Station
        groundStationMenu.add(addGSMenuItem);
        groundStationMenu.add(displayGSMenuItem);

        // Création des items de menu pour le menu Analysis
        JMenuItem performanceMenuItem = new JMenuItem("Performance");
        JMenuItem riskMenuItem = new JMenuItem("Risk");

        // Ajout des items de menu au menu Analysis
        analysisMenu.add(performanceMenuItem);
        analysisMenu.add(riskMenuItem);


        // Ajout des menus à la barre de menu
        menuBar.add(homeMenu);
        menuBar.add(satelliteMenu);
        menuBar.add(groundStationMenu);
        menuBar.add(analysisMenu);

        // Ajout de la barre de menu à la fenêtre
        frame.setJMenuBar(menuBar);

        // Création du panneau d'onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        //Gestion de l'événnement lorsque l'item "Add" du menu "Ground Station" est cliqué
        addGSMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Création du panneau
                NewGroundStationDialog newGroundStationDialog = new NewGroundStationDialog(MainFrame.this);
                newGroundStationDialog.setVisible(true);
            }
        });

        // Ajout d'un listener pour l'élément de menu "Display" de "Ground Station"
        displayGSMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Affichage de la liste des stations sol dans une fenêtre de dialogue
                StringBuilder sb = new StringBuilder();
                for (src.Kalman.Station groundStation : groundStationList) {
                    sb.append(groundStation.getName());
                    sb.append(groundStation.getLongitude());
                    sb.append(", ");
                    sb.append(groundStation.getLatitude());
                    sb.append(", ");
                    sb.append(groundStation.getAltitude());
                    sb.append("\n");
                }

                ImageIcon imageIcon = new ImageIcon("src/App/img/station_sol_icon.png"); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back

                JOptionPane.showMessageDialog(MainFrame.this, sb.toString(), "Ground Stations", JOptionPane.INFORMATION_MESSAGE, imageIcon);
            }
        });


        // Configuration de la fenêtre
        frame.setTitle("Main Window");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Méthode pour ajouter une station sol à la liste
    public void addGroundStation(Station groundStation) {
        groundStationList.add(groundStation);
    }

    public static void main(String[] args) {
        //importation des donnees de base (toujour mettre ça en début de programme)
        File orekitData = new File("lib/orekit-data-master");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        new MainFrame();
    }
}
