package src.App.UpdateSatelliteDBTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateSatelliteDBPanel extends JPanel{

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();
    public UpdateSatelliteDBPanel(MainFrame parent) {

        this.parent = parent;
        setLayout(new GridBagLayout());

        // Création des boutons pour l'onglet Satellite
        JButton updateDatabseButton = new JButton("Update database");

        // Ajout des boutons au panneau Satellite
        gc.gridy = 0;
        gc.gridx = 0;
        add(updateDatabseButton, gc);

        // Création du JTextArea et d'un JScrollPane pour pouvoir faire défiler le texte
        JTextArea outputTextArea = new JTextArea(30, 40);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        // Ajout du JScrollPane au panneau
        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 2;
        add(scrollPane, gc);

        // Ajout d'un ActionListener pour le bouton "Update database"
        updateDatabseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Désactiver le bouton pendant l'exécution du script
                updateDatabseButton.setEnabled(false);

                // Exécuter le script dans un Thread séparé pour ne pas bloquer l'interface graphique
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            // Exécution du script sh
                            Process p = Runtime.getRuntime().exec("sh src/TLE/updatedb.sh");

                            // Redirection de la sortie d'erreur du processus vers le JTextArea en temps réel
                            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                            String errorLine = "";
                            while ((errorLine = errorReader.readLine()) != null) {
                                String finalErrorLine = errorLine;
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        outputTextArea.append(finalErrorLine + "\n");
                                    }
                                });
                            }

                            // Attendre que le script sh soit terminé
                            int exitCode = p.waitFor();

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    outputTextArea.append("Script sh terminé avec le code de sortie " + exitCode + "\n");
                                    // Réactiver les boutons lorsque le script est terminé
                                    updateDatabseButton.setEnabled(true);
                                }
                            });
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}

