package src.Data.Simulation;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import src.App.MainFrame;
import src.App.ParametersTab.Parameters;
import src.App.SimulationTab.SimulationModel;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;
import src.constants;
import src.Observer.ObserverNetwork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class LoadSimu {

    String sourceFile; // the file with simulation parameters


    public LoadSimu ( String sourceFile ) {
        this.sourceFile = sourceFile;
    }

    public SimulationModel load ( MainFrame parent ) throws IOException {
        SimulationModel model = new SimulationModel ( );

        try {
            FileReader fr = new FileReader ( this.sourceFile );
            BufferedReader br = new BufferedReader ( fr );
            String line;

            // Reading network
            br.readLine ( );
            String netName = br.readLine ( ).split ( ": " )[1];
            List<String> teleNames = new ArrayList<> ( );
            List<TelescopeAzEl> teleList = new ArrayList<> ( );
            while (!(line = br.readLine ( )).equals ( "PARAMETERS" )) {
                teleNames.add ( line.split ( "," )[0] );

                String[] values = line.split ( "," );
                String[] idValues = values[0].split ( ":" );
                String station = idValues[0];
                String type = idValues[1];
                int id = Integer.parseInt ( idValues[2].trim ( ) );

                // On vérifie qu'il s'agit bien d'un télescope
                if (!Objects.equals ( type , "telescope" )) {
                    continue;
                }


                double mean1 = Double.parseDouble ( values[1].trim ( ) );
                double mean2 = Double.parseDouble ( values[2].trim ( ) );
                double[] mean = {mean1 , mean2};

                double angularIncertitude1 = Double.parseDouble ( values[3].trim ( ) );
                double angularIncertitude2 = Double.parseDouble ( values[4].trim ( ) );
                double[] angularIncertitude = {angularIncertitude1 , angularIncertitude2};

                double elevationLimit = Double.parseDouble ( values[5].trim ( ) );
                double angularFoV = Double.parseDouble ( values[6].trim ( ) );
                double stepMeasure = Double.parseDouble ( values[7].trim ( ) );
                double breakTime = Double.parseDouble ( values[8].trim ( ) );
                boolean geo = Boolean.parseBoolean ( values[9].trim ( ) );

                // On associe le TelescopeAzEl à un GroundStation
                List<Station> stationList = parent.gsController.groundStationList;
                for (Station groundStation : stationList) {
                    if (groundStation.getName ( ).equals ( station )) {
                        TelescopeAzEl telescope = new TelescopeAzEl ( values[0] , mean , angularIncertitude , elevationLimit , angularFoV , stepMeasure , breakTime , groundStation , geo );
                        teleList.add ( telescope );
                    }
                }
            }
            model.setObserverNetwork ( new ObserverNetwork ( netName , teleNames , teleList ) );

            // Reading parameters
            line = br.readLine ( );
            String[] dateStr = line.split ( "T" )[0].split ( ": " )[1].split ( "-" );
            AbsoluteDate initialDate = new AbsoluteDate ( parseInt ( dateStr[0] ) , parseInt ( dateStr[1] ) , parseInt ( dateStr[2] ) , TimeScalesFactory.getUTC ( ) );
            line = br.readLine ( );
            dateStr = line.split ( "T" )[0].split ( ": " )[1].split ( "-" );
            AbsoluteDate finalDate = new AbsoluteDate ( parseInt ( dateStr[0] ) , parseInt ( dateStr[1] ) , parseInt ( dateStr[2] ) , TimeScalesFactory.getUTC ( ) );

            model.setSimulationParameters ( new Parameters ( initialDate , finalDate ) );

            // Reading satellites
            List<Propagator> satList = new ArrayList<> ( );
            List<ObservableSatellite> obsSat = new ArrayList<> ( );
            List<String> satnames = new ArrayList<> (  );
            line = br.readLine ( );
            int i = 0;
            while ((line = br.readLine ( )) != null) {
                String[] data = line.split ( "," );
                if (!(data[0].equals ( "a" ))) {
                    KeplerianOrbit trueOrbit = new KeplerianOrbit (Float.parseFloat ( data[1] ) , Float.parseFloat ( data[2] ) , Float.parseFloat ( data[3] ) , Float.parseFloat ( data[4] ) , Float.parseFloat ( data[5] ),Float.parseFloat ( data[6] ) , constants.type , constants.GCRF , initialDate , Constants.EGM96_EARTH_MU );
                    KeplerianPropagator truePropagator = new KeplerianPropagator ( trueOrbit );
                    satList.add ( truePropagator );
                    satnames.add ( data[0] );
                    obsSat.add ( new ObservableSatellite ( i ) );
                    i++;
                }
            }
            model.setSatellitesNames ( satnames );
            model.setSatellites ( satList );
            model.setObservableSatellites ( obsSat );

        } catch (FileNotFoundException e) {
            throw new RuntimeException ( e );
        }


        return model;
    }


    public void save ( String fileName , SimulationModel simulationModel ) {
        this.sourceFile = fileName;
        try {
            FileWriter fw = new FileWriter ( "src/Data/Simulation/" + fileName + ".csv" , false );
            BufferedWriter bw = new BufferedWriter ( fw );

            // Saving the network
            bw.write ( "NETWORK" );
            ObserverNetwork network = simulationModel.getObserverNetwork ( );
            bw.newLine ( );
            bw.write ( "Name: " + network.getName ( ) );
            for (TelescopeAzEl telescopeAzEl : network.getTelescopes ( )) {
                bw.newLine ( );
                bw.write ( telescopeAzEl.getID ( ) + ',' + telescopeAzEl.getMean ( )[0] + ',' + telescopeAzEl.getMean ( )[1] + ',' + telescopeAzEl.getAngularIncertitude ( )[0] + ',' + telescopeAzEl.getAngularIncertitude ( )[1] + ',' + telescopeAzEl.getElevationLimit ( ) + ',' + telescopeAzEl.getAngularFoV ( ) + ',' + telescopeAzEl.getStepMeasure ( ) + ',' + telescopeAzEl.getBreakTime ( ) + ',' + telescopeAzEl.getGEO ( ) );
            }

            // Saving the parameters
            bw.newLine ( );
            bw.write ( "PARAMETERS" );
            Parameters parameters = simulationModel.getSimulationParameters ( );
            bw.newLine ( );
            bw.write ( "Start date: " + parameters.getStartDate ( ).toString ( ) );
            bw.newLine ( );
            bw.write ( "End date: " + parameters.getEndDate ( ).toString ( ) );

            // Saving the satellites
            bw.newLine ( );
            bw.write ( "SATELLITES" );
            List<Propagator> satellites = simulationModel.getSatellites ( );
            List<String> satellitesNames = simulationModel.getSatellitesNames ( );
            int i = 0;
            for (Propagator satellite : satellites) {
                bw.newLine ( );
                KeplerianOrbit state = new KeplerianOrbit ( satellite.getInitialState ( ).getOrbit ( ) );
                bw.write ( satellitesNames.get ( i ) + "," + state.getA ( ) + "," + state.getE ( ) + "," + state.getI ( ) + "," + state.getPerigeeArgument ( ) + "," + state.getRightAscensionOfAscendingNode ( ) + "," + state.getMeanAnomaly ( ) );
                i++;
            }

            bw.close ( );
            System.out.println ( "Simulation saved on file" + "src/Data/OSimulation/" + fileName + ".csv" );

        } catch (IOException e) {
            throw new RuntimeException ( e );
        }


    }
}
