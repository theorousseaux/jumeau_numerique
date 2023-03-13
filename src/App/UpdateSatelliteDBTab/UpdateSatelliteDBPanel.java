package src.App.UpdateSatelliteDBTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class UpdateSatelliteDBPanel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints ( );

    public UpdateSatelliteDBPanel ( MainFrame parent ) throws SQLException, IOException, ClassNotFoundException {

        this.parent = parent;
        setLayout ( new GridBagLayout ( ) );

        // Tittle
        JLabel title = new JLabel ( "Update Satellite Database" );
        title.setFont ( new Font ( "Arial" , Font.BOLD , 20 ) );
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = 2;
        add ( title , gc );

        // Création des boutons pour l'onglet Satellite
        JButton updateDatabseButton = new JButton ( "Update CSV" );

        // Ajout des boutons au panneau Satellite
        gc.gridy = 1;
        gc.gridx = 0;
        add ( updateDatabseButton , gc );

        // Création du JTextArea et d'un JScrollPane pour pouvoir faire défiler le texte
        JTextArea outputTextArea = new JTextArea ( 30 , 40 );
        JScrollPane scrollPane = new JScrollPane ( outputTextArea );

        // Ajout du JScrollPane au panneau
        gc.gridy = 2;
        gc.gridx = 0;
        gc.gridwidth = 2;
        add ( scrollPane , gc );

        // Création du bouton pour créer la base de données
        JButton createDBButton = new JButton ( "Create/Connect DB" );
        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = 1;
        add ( createDBButton , gc );

        // Ajout d'un ActionListener pour le bouton "Update database"
        updateDatabseButton.addActionListener ( new ActionListener ( ) {
            public void actionPerformed ( ActionEvent e ) {
                // Désactiver le bouton pendant l'exécution du script
                updateDatabseButton.setEnabled ( false );

                // Exécuter le script dans un Thread séparé pour ne pas bloquer l'interface graphique
                Thread thread = new Thread ( new Runnable ( ) {
                    public void run ( ) {
                        try {
                            // Exécution du script sh
                            Process p = Runtime.getRuntime ( ).exec ( "sh src/TLE/TLE_recov_kep.sh" );

                            // Redirection de la sortie d'erreur du processus vers le JTextArea en temps réel
                            BufferedReader errorReader = new BufferedReader ( new InputStreamReader ( p.getErrorStream ( ) ) );
                            String errorLine = "";
                            while ((errorLine = errorReader.readLine ( )) != null) {
                                String finalErrorLine = errorLine;
                                SwingUtilities.invokeLater ( new Runnable ( ) {
                                    public void run ( ) {
                                        outputTextArea.append ( finalErrorLine + "\n" );
                                    }
                                } );
                            }

                            // Attendre que le script sh soit terminé
                            int exitCode = p.waitFor ( );

                            SwingUtilities.invokeLater ( new Runnable ( ) {
                                public void run ( ) {
                                    outputTextArea.append ( "Script sh terminé avec le code de sortie " + exitCode + "\n" );
                                    // Réactiver les boutons lorsque le script est terminé
                                    updateDatabseButton.setEnabled ( true );
                                }
                            } );
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace ( );
                        }
                    }
                } );
                thread.start ( );
            }
        } );

        // Ajout d'un ActionListener pour le bouton "Create DB"
        createDBButton.addActionListener ( new ActionListener ( ) {
            public void actionPerformed ( ActionEvent e ) {
                try {
                    parent.updateDBController.createDB ( "src/TLE/tle.csv" );
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    throw new RuntimeException ( ex );
                }
            }
        } );
    }
}

