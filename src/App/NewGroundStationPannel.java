package src.App;

import javax.swing.*;
import java.awt.*;

public class NewGroundStationPannel extends JPanel {

    public NewGroundStationPannel() {

        setLayout(new GridLayout(5, 2)); // 5 lignes, 2 colonnes

        // Création des lables
        JLabel longLabel = new JLabel("Longitude :");
        JLabel latLabel = new JLabel("Latitude :");
        JLabel altLabel = new JLabel("Altitude :");
        JLabel nameLabel = new JLabel("Name :");
        JLabel typeLabel = new JLabel("Type :");

        longLabel.setBounds(10, 10, 80, 25);
        latLabel.setBounds(10, 40, 80, 25);
        altLabel.setBounds(10, 70, 80, 25);
        nameLabel.setBounds(10, 100, 80, 25);
        typeLabel.setBounds(10, 130, 80, 25);

        // Création des champs de texte
        JTextField t1 = new JTextField();
        t1.setBounds(50,100, 200,30);
        JTextField t2 = new JTextField();
        t2.setBounds(50,150, 200,30);

        // Ajout des labels au panneau
        add(longLabel);
        add(new JTextField());
        add(latLabel);
        add(new JTextField());
        add(altLabel);
        add(new JTextField());
        add(nameLabel);
        add(t1);
        add(typeLabel);
        add(t2);
    }
}
