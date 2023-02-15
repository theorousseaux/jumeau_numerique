package src.App.SatelliteTab;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SatellitePannel extends JPanel{
    public SatellitePannel() {
        // Création des boutons pour l'onglet Satellite
        JButton updateDatabseButton = new JButton("Update databse");
        JButton addSatelliteButton = new JButton("Add");

        // Ajout des boutons au panneau Satellite
        this.add(updateDatabseButton);
        this.add(addSatelliteButton);
    }
}
