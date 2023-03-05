package src.App.UpdateSatelliteDBTab;

import src.App.MainFrame;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UpdateSatelliteDBPanel extends JPanel{

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();
    public UpdateSatelliteDBPanel(MainFrame parent) {

        this.parent = parent;
        setLayout(new GridBagLayout());

        // Création des boutons pour l'onglet Satellite
        JButton updateDatabseButton = new JButton("Update database");
        JButton addSatelliteButton = new JButton("Add");

        // Ajout des boutons au panneau Satellite
        gc.gridy = 0;
        gc.gridx = 0;
        add(updateDatabseButton, gc);

        gc.gridx = 1;
        add(addSatelliteButton, gc);

        // Ajout de la barre de progression
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, gc);

        // Gestion des évènements
        updateDatabseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Chemin vers le script shell à exécuter
                    String scriptPath = "/path/to/your/script.sh";

                    // Création du ProcessBuilder pour exécuter le script shell
                    ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
                    processBuilder.redirectErrorStream(true);

                    // Lancement du processus
                    Process process = processBuilder.start();

                    // Attente de la fin de l'exécution du script shell
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        // Le script s'est exécuté avec succès
                        System.out.println("Script executed successfully.");
                    } else {
                        // Le script a rencontré une erreur
                        System.out.println("Script execution failed.");
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
