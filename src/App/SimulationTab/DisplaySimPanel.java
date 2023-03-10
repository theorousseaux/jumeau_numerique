package src.App.SimulationTab;
import javax.swing.*;

import org.orekit.estimation.measurements.ObservableSatellite;
import src.Kalman.Station;

import src.App.MainFrame;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.GSNetwork;

import java.util.List;

import java.awt.*;
import java.util.ArrayList;


/**
 * This class display a reminder of the current simulation set up
 */
public class DisplaySimPanel extends JPanel{

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();;

    /**
     * Default constructor
     * @param parent the main frame
     */
    public DisplaySimPanel ( MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Title
        JLabel title = new JLabel("Simulation set up");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.BLUE);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(20, 10, 20, 10);
        this.add(title, gc);

    }

    public void removeAllExceptTop() {
        Component[] components = this.getComponents();
        for (int i = 1; i < components.length; i++) {
            this.remove(components[i]);
        }
    }


    /**
     * Updates the display
     */
    public void update() {

        removeAllExceptTop();

        GridBagConstraints gc = new GridBagConstraints();

        // Selected satellites display
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.WEST;
        JLabel satellitesLabel = new JLabel("Satellites:");
        this.add(satellitesLabel, gc);


        // Scroll through selected satellites
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.WEST;
        List<ObservableSatellite> satelliteList = this.parent.simuController.model.getSatellitesNames();
        ArrayList<String> elements = new ArrayList<String>();
        for (ObservableSatellite sat : satelliteList){
            elements.add(String.valueOf(sat.getPropagatorIndex()));
        }
        String[] arr = {};
        arr = elements.toArray(arr); 
        JList<String> liste = new JList<>(arr);
        JScrollPane satellites = new JScrollPane(liste);
        this.add(satellites, gc);


        // Network stations display
        gc.gridx = 0;
        gc.gridy ++;
        gc.anchor = GridBagConstraints.WEST;
        JLabel networkLabel = new JLabel("Network stations:");
        this.add(networkLabel, gc);

        // Scroll through selected stations
        gc.gridx  = 1 ;
        /*
        GSNetwork stationsList = this.parent.simuController.model.getGroundStationNetwork();
        ArrayList<String> stations = new ArrayList<String>();
        for (Station station : stationsList.getNetwork()){
            stations.add(String.valueOf(station.getName()));
        }
        String[] arrStations = {};
        arrStations = stations.toArray(arrStations); 
        JList<String> listestations = new JList<>(arrStations);
        JScrollPane affichagetations = new JScrollPane(listestations);
        this.add(affichagetations, gc);


         */


        List<TelescopeAzEl> observersList = this.parent.simuController.model.getObserverNetwork ( ).getTelescopes ( );
        ArrayList<String> stations = new ArrayList<String>();
        for (TelescopeAzEl telescope : observersList){
            stations.add(String.valueOf(telescope.getID ()));
        }
        String[] arrStations = {};
        arrStations = stations.toArray(arrStations);
        JList<String> listestations = new JList<>(arrStations);
        JScrollPane affichagetations = new JScrollPane(listestations);
        this.add(affichagetations, gc);

        // Simulation dates display

        gc.gridx = 0;
        gc.gridy ++;
        gc.anchor = GridBagConstraints.WEST;

        JLabel datesLabel = new JLabel("Simulation dates:");
        this.add(datesLabel, gc);

        gc.gridx = 1;
        JLabel startDate = new JLabel("Start date: " + this.parent.simuController.model.getSimulationParameters().getStartDate().toString());
        JLabel endDate = new JLabel("End date: " + this.parent.simuController.model.getSimulationParameters().getEndDate().toString());
        this.add(startDate, gc);
        gc.gridy ++;
        this.add(endDate, gc);
    }

}
