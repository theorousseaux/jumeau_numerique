package src.App;

import javax.swing.*;
import java.awt.*;

public class NewPerformancePannel extends JPanel {

    public NewPerformancePannel() {

        setLayout(new GridLayout(3, 2));

        // Création des lables
        JLabel stationsSol = new JLabel("Stations to consider");
        JLabel objects = new JLabel("Objects to watch");

        // Création des menus déroulants
        JMenu stationsMenu = new JMenu("Select stations");
        JCheckBoxMenuItem option1 = new JCheckBoxMenuItem("Station 1");
        JCheckBoxMenuItem option2 = new JCheckBoxMenuItem("Station 2");
        stationsMenu.add(option1);
        stationsMenu.add(option2);

        JMenu objectsMenu = new JMenu("Select objects");
        JCheckBoxMenuItem option3 = new JCheckBoxMenuItem("LEO objects");
        JCheckBoxMenuItem option4 = new JCheckBoxMenuItem("GEO  objects");
        objectsMenu.add(option3);
        objectsMenu.add(option4);
        stationsMenu.setVisible(true);

        // Ajout des labels au panneau
        add(stationsSol);
        add(stationsMenu);
        add(objects);
        add(objectsMenu);
    }
}
