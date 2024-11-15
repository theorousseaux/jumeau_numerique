package src.GUI.SatelliteTab;

import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

public class SatelliteView extends JPanel {

    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );

    private final DefaultListModel listModel;

    public SatelliteView ( MainFrame parent ) {

        this.parent = parent;
        setLayout ( new GridBagLayout ( ) );

        listModel = new DefaultListModel ( );
        JList list = new JList ( listModel );
        list.setLayoutOrientation ( JList.VERTICAL );

        // Titre
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        JLabel tittle = new JLabel ( "Choose satellites for simulation" );
        tittle.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground ( Color.BLUE ); // Définit la couleur du texte en bleu
        add ( tittle , gc );

        JLabel chooseLabel = new JLabel ( "Choose satellites for simulation" );
        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        add ( chooseLabel , gc );

        // Choix entre LEO et GEO
        String[] satellites = {"LEO" , "GEO"};
        JComboBox<String> satellitesList = new JComboBox<> ( satellites );
        gc.gridy = 1;
        gc.gridx = 1;
        add ( satellitesList , gc );

        // Choix du nombre de satellites
        JLabel numberLabel = new JLabel ( "Number of satellites" );
        gc.gridy = 2;
        gc.gridx = 0;
        add ( numberLabel , gc );

        JTextField numberSat = new JTextField ( 5 );
        gc.gridy = 2;
        gc.gridx = 1;
        add ( numberSat , gc );

        // Bouton de validation
        JButton validate = new JButton ( "Validate" );
        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        add ( validate , gc );

        // Affichage des satellites sélectionnés
        JLabel selectedSat = new JLabel ( "Selected satellites" );
        gc.gridy = 4;
        gc.gridx = 0;
        gc.gridwidth = 1;
        add ( selectedSat , gc );

        JScrollPane selectedSatList = new JScrollPane ( list );
        gc.gridy = 5;
        gc.gridx = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        add ( selectedSatList , gc );

        // gestion des évènements
        validate.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                try {
                    int nbSat = Integer.parseInt ( numberSat.getText ( ) );
                    if (Objects.equals ( satellitesList.getSelectedItem ( ) , "LEO" )) {
                        try {
                            parent.getSatController ( ).setSatellitesList ( "LEO" , nbSat , parent );
                        } catch (NullPointerException | SQLException npe) {
                            JOptionPane.showMessageDialog ( parent , "Load or connect your database first and select simulation dates" , "Error" , JOptionPane.ERROR_MESSAGE );
                        }
                    } else if (Objects.equals ( satellitesList.getSelectedItem ( ) , "GEO" )) {
                        try {
                            parent.getSatController ( ).setSatellitesList ( "GEO" , nbSat , parent );
                        } catch (NullPointerException | SQLException npe) {
                            JOptionPane.showMessageDialog ( parent , "Load or connect your database first and select simulation dates" , "Error" , JOptionPane.ERROR_MESSAGE );
                        }
                    }
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog ( parent , "Invalid number of satellites" , "Error" , JOptionPane.ERROR_MESSAGE );

                }
                // on vide la liste avant de la remplir
                listModel.removeAllElements ( );
                for (String idSat : parent.getSatController ( ).getModel ( ).getIdSatellitesList ( )) {
                    listModel.addElement ( idSat );
                }
            }
        } );
    }
}
