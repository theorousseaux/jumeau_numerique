package src.GUI.ObserverTab;

import src.GUI.MainFrame;
import src.GUI.WorldMapTab.WorldMapView;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;
import src.Observer.ObserverNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreateNetworkView extends JPanel {

    final MainFrame parent;
    final DisplayNetworkView displayNetworkPannel;
    Set<String> selectedStations;
    private final JList<String> list;
    private final DefaultListModel<String> listModel;

    public CreateNetworkView ( MainFrame parent , DisplayNetworkView displayNetworkPannel , WorldMapView worldMapView ) {

        this.parent = parent;
        this.displayNetworkPannel = displayNetworkPannel;
        GridBagConstraints gc = new GridBagConstraints ( );
        setLayout ( new GridBagLayout ( ) );

        // Titre
        JLabel tittle = new JLabel ( "Choose observers to create the network" );
        tittle.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground ( Color.BLUE ); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add ( tittle , gc );


        listModel = new DefaultListModel<String> ( );
        for (TelescopeAzEl tele : parent.getObserController ( ).getModel ( ).getTelescopeAzElList ( )) {
            listModel.addElement ( tele.getID ( ) );
        }
        for (Radar radar : parent.getObserController ( ).getModel ( ).getRadarList ( )) {
            listModel.addElement ( radar.getID ( ) );
        }
        list = new JList<String> ( listModel );
        list.setLayoutOrientation ( JList.VERTICAL );
        list.setSelectionMode ( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        JScrollPane scrollPane = new JScrollPane ( list );
        scrollPane.setPreferredSize ( new Dimension ( 200 , 200 ) );
        gc.insets = new Insets ( 15 , 10 , 15 , 10 );
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = GridBagConstraints.REMAINDER;
        this.add ( scrollPane , gc );

        JLabel label = new JLabel ( "Network name : " );
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.SOUTH;
        this.add ( label , gc );

        JTextField textField = new JTextField ( 10 );
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( textField , gc );


        JButton button = new JButton ( "Create network" );
        gc.gridx = 1;
        gc.gridy = 3;
        gc.anchor = GridBagConstraints.EAST;
        this.add ( button , gc );

        // Si le boutton add est cliqué, on crée un réseau avec les stations sélectionnées
        button.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {

                selectedStations = new HashSet<> ( );
                for (Object name : list.getSelectedValues ( )) {
                    selectedStations.add ( name.toString ( ) );
                }


                System.out.println ( selectedStations );
                parent.getObserController ( ).getModel ().setObserverNetwork ( new ObserverNetwork ( textField.getText ( ) , new ArrayList<> ( selectedStations ) , parent.getObserController ( ).getModel ( ).getTelescopeAzElList ( ) ));
                displayNetworkPannel.update ( );
                displayNetworkPannel.repaint ( );
                displayNetworkPannel.revalidate ( );

                worldMapView.displayNewGS ( parent, false );
                worldMapView.repaint ( );
                worldMapView.revalidate ( );


            }
        } );


    }

    public void displayNewTelescope ( TelescopeAzEl teles ) {

        String name = teles.getID ( );

        listModel.addElement ( name );

    }

    public void update ( ) {
        listModel.clear ( );
        for (TelescopeAzEl tele : parent.getObserController ( ).getModel ( ).getTelescopeAzElList ( )) {
            listModel.addElement ( tele.getID ( ) );
        }
        for (Radar radar : parent.getObserController ( ).getModel ( ).getRadarList ( )) {
            listModel.addElement ( radar.getID ( ) );
        }
    }
}