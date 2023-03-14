package src.GUI.SimulationTab;

import src.GUI.MainFrame;
import src.Data.Simulation.LoadSimu;
import src.Observer.TelescopeAzEl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * This class display a reminder of the current simulation set up
 */
public class DisplaySimView extends JPanel {

    final MainFrame parent;
    final GridBagConstraints gc = new GridBagConstraints ( );

    /**
     * Default constructor
     *
     * @param parent the main frame
     */
    public DisplaySimView ( MainFrame parent ) {

        this.parent = parent;

        setLayout ( new GridBagLayout ( ) );

        // Title
        JLabel title = new JLabel ( "Simulation set up" );
        title.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) );
        title.setForeground ( Color.BLUE );

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets ( 20 , 10 , 20 , 10 );
        this.add ( title , gc );

    }

    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 1; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }


    /**
     * Updates the display
     */
    public void update ( ) {

        removeAllExceptTop ( );

        GridBagConstraints gc = new GridBagConstraints ( );

        // Selected satellites display
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.WEST;
        JLabel satellitesLabel = new JLabel ( "Satellites:" );
        this.add ( satellitesLabel , gc );


        // Scroll through selected satellites
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.WEST;
        List<String> satelliteList = this.parent.getSimuController ( ).getModel ().getSatellitesNames ( );
        ArrayList<String> elements = new ArrayList<String> ( );
        for (String sat : satelliteList) {
            elements.add ( sat );
        }
        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        JScrollPane satellites = new JScrollPane ( liste );
        this.add ( satellites , gc );


        // Network stations display
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.WEST;
        JLabel networkLabel = new JLabel ( "Network stations:" );
        this.add ( networkLabel , gc );

        // Scroll through selected stations
        gc.gridx = 1;


        List<TelescopeAzEl> observersList = this.parent.getSimuController ( ).getModel ().getObserverNetwork ( ).getTelescopes ( );
        ArrayList<String> stations = new ArrayList<String> ( );
        for (TelescopeAzEl telescope : observersList) {
            stations.add ( String.valueOf ( telescope.getID ( ) ) );
        }
        String[] arrStations = {};
        arrStations = stations.toArray ( arrStations );
        JList<String> listestations = new JList<> ( arrStations );
        JScrollPane affichagetations = new JScrollPane ( listestations );
        this.add ( affichagetations , gc );

        // Simulation dates display

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.WEST;

        JLabel datesLabel = new JLabel ( "Simulation dates:" );
        this.add ( datesLabel , gc );

        gc.gridx = 1;
        JLabel startDate = new JLabel ( "Start date: " + this.parent.getSimuController ( ).getModel ().getSimulationParameters ( ).getStartDate ( ).toString ( ) );
        JLabel endDate = new JLabel ( "End date: " + this.parent.getSimuController ( ).getModel ().getSimulationParameters ( ).getEndDate ( ).toString ( ) );
        this.add ( startDate , gc );
        gc.gridy++;
        this.add ( endDate , gc );

        JLabel fileName = new JLabel ( "File to save simulation: " );
        JTextField fileNameInput = new JTextField ( "filename" , 10 );
        JButton saveSimulationButton = new JButton ( "Save simulation set up" );

        gc.gridx = 0;
        gc.gridy++;
        this.add ( fileName , gc );

        gc.gridx = 1;
        this.add ( fileNameInput , gc );

        gc.gridy++;
        this.add ( saveSimulationButton , gc );
        saveSimulationButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {


                LoadSimu saver = new LoadSimu ( fileNameInput.getText ( ) );
                saver.save ( fileNameInput.getText ( ) , parent.getSimuController ( ).getModel () );

            }
        } );
    }

}
