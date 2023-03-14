package src.Data.Observer;

import src.Observer.Radar;
import src.GroundStation.Station;
import src.Observer.TelescopeAzEl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadObserverFile {

    public List<TelescopeAzEl> readTelescopesFromCSV ( String fileName , List<Station> stationList ) {
        List<TelescopeAzEl> telescopes = new ArrayList<> ( );
        try (BufferedReader br = new BufferedReader ( new FileReader ( fileName ) )) {
            String line;
            // Ignore first line
            br.readLine ( );
            while ((line = br.readLine ( )) != null) {
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
                for (Station groundStation : stationList) {
                    if (groundStation.getName ( ).equals ( station )) {
                        TelescopeAzEl telescope = new TelescopeAzEl ( values[0] , mean , angularIncertitude , elevationLimit , angularFoV , stepMeasure , breakTime , groundStation , geo );
                        telescopes.add ( telescope );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        return telescopes;
    }

    public List<Radar> readRadarsFromCSV ( String fileName , List<Station> stationList ) {
        List<Radar> radars = new ArrayList<> ( );
        try (BufferedReader br = new BufferedReader ( new FileReader ( fileName ) )) {
            String line;
            // Ignore first line
            br.readLine ( );
            while ((line = br.readLine ( )) != null) {
                String[] values = line.split ( "," );

                String[] idValues = values[0].split ( ":" );
                String station = idValues[0];
                String type = idValues[1];

                int id = Integer.parseInt ( idValues[2].trim ( ) );

                // On vérifie qu'il s'agit bien d'un radar
                if (!Objects.equals ( type , "radar" )) {
                    continue;
                }

                double mean1 = Double.parseDouble ( values[1].trim ( ) );
                double mean2 = Double.parseDouble ( values[2].trim ( ) );
                double[] mean = {mean1 , mean2};

                double angularIncertitude1 = Double.parseDouble ( values[3].trim ( ) );
                double angularIncertitude2 = Double.parseDouble ( values[4].trim ( ) );
                double[] angularIncertitude = {angularIncertitude1 , angularIncertitude2};

                double angularFoV = Double.parseDouble ( values[5].trim ( ) );
                double stepMeasure = Double.parseDouble ( values[6].trim ( ) );

                // On associe le TelescopeAzEl à un GroundStation
                for (Station groundStation : stationList) {
                    if (groundStation.getName ( ).equals ( station )) {
                        Radar newRadar = new Radar ( values[0] , mean , angularIncertitude , angularFoV , stepMeasure , groundStation );
                        radars.add ( newRadar );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        return radars;
    }
}
