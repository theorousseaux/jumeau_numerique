package src.App;

import javax.swing.*;
import java.awt.event.*;

public class TestApp {

    int numberGroundStations = 0;

    public TestApp() {

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
        JMenuItem addMenuItem = new JMenuItem("Add");
        JMenuItem displayMenuItem = new JMenuItem("Display");

        // Ajout des items de menu au menu Ground Station
        groundStationMenu.add(addMenuItem);
        groundStationMenu.add(displayMenuItem);

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

        // Gestion de l'événement lorsque l'item "Add" est cliqué
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                numberGroundStations++;
                // Création d'un nouveau panneau
                JPanel newPanel = new NewGroundStationPannel();

                // Ajout du panneau à l'onglet
                tabbedPane.addTab(String.format("Ground station number %d", numberGroundStations), newPanel);
            }
        });

        // Ajout du panneau d'onglets à la fenêtre
        frame.add(tabbedPane);

        // Configuration de la fenêtre
        frame.setTitle("Main Window");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TestApp();
    }
}
