package src.App.GSTab;

import src.App.MainFrame;
import src.App.ObserverTab.NewObserverPannel;
import src.App.WorldMapTab.WorldMapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NewGSPannel extends JPanel {

    public NewGSPannel ( MainFrame parent , DisplayGSPannel displayGSPannel , WorldMapPanel worldMapPanel ) {

        // Titre
        JLabel addGSLabel = new JLabel ( "Add a new ground station" );
        addGSLabel.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Définit la police d'écriture en Arial, en gras et en taille 18
        addGSLabel.setForeground ( Color.BLUE ); // Définit la couleur du texte en bleu


        // Création des champs de saisie
        JTextField longitudeTextField = new JTextField ( 10 );
        JTextField latitudeTextField = new JTextField ( 10 );
        JTextField altitudeTextField = new JTextField ( 10 );
        JTextField nameTextField = new JTextField ( 10 );

//        String[] typeGS ={"Radar","Laser"};
//        JComboBox<String> cbType = new JComboBox<>(typeGS);


        // Création du formulaire
        JPanel formPanel = new JPanel ( );
        formPanel.setLayout ( new GridBagLayout ( ) );
        GridBagConstraints gc = new GridBagConstraints ( );

        // Définition de la marge
        gc.insets = new Insets ( 2 , 5 , 2 , 5 );

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        formPanel.add ( addGSLabel , gc );

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add ( new JLabel ( "Longitude: " ) , gc );

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( longitudeTextField , gc );

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( new JLabel ( "Latitude: " ) , gc );

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( latitudeTextField , gc );

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( new JLabel ( "Altitude: " ) , gc );

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( altitudeTextField , gc );

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( new JLabel ( "Name: " ) , gc );

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add ( nameTextField , gc );


        // Création du bouton "Add"
        JButton addButton = new JButton ( "Add" );
        addButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // Récupération des données saisies
                double longitude = Double.parseDouble ( longitudeTextField.getText ( ) ) * Math.PI / 180;
                double latitude = Double.parseDouble ( latitudeTextField.getText ( ) ) * Math.PI / 180;
                double altitude = Double.parseDouble ( altitudeTextField.getText ( ) );
                String name = nameTextField.getText ( );

                // Création de l'objet GroundStation avec les données saisies
                src.Kalman.Station groundStation = new src.Kalman.Station ( name , latitude , longitude , altitude );

                // Ajout de la station sol à la liste de la fenêtre principale
                parent.gsController.addGroundStation ( groundStation );

                // update
                try {
                    parent.gsController.GSWriter.writeStation ( groundStation );

                    // Mise à jour du panneau d'affichage des stations sol
                    displayGSPannel.displayNewStation ( );
                    displayGSPannel.repaint ( );
                    displayGSPannel.revalidate ( );

                    // Mise à jour des choix dans l'onglet Observer
                    JPanel tabPanel = (JPanel) parent.tabbedPane.getComponentAt ( 4 );
                    NewObserverPannel newObserverPannel = (NewObserverPannel) tabPanel.getComponent ( 1 );
                    newObserverPannel.updateStationComboBox ( );

                    worldMapPanel.displayNewGS ( parent );
                    worldMapPanel.repaint ( );
                    worldMapPanel.revalidate ( );

                } catch (IOException ex) {
                    throw new RuntimeException ( ex );
                }
            }
        } );

        // Création du bouton "Cancel"
        JButton cancelButton = new JButton ( "Cancel" );
        cancelButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // On efface les données saisies
                longitudeTextField.setText ( "" );
                latitudeTextField.setText ( "" );
                altitudeTextField.setText ( "" );
                nameTextField.setText ( "" );
            }
        } );

        // Création du panel des boutons
        JPanel buttonPanel = new JPanel ( );
        buttonPanel.add ( addButton );
        buttonPanel.add ( cancelButton );

        // Ajout du formulaire et des boutons à la fenêtre
        setLayout ( new BorderLayout ( ) );
        add ( formPanel , BorderLayout.CENTER );
        add ( buttonPanel , BorderLayout.SOUTH );
    }
}