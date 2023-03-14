package src.Data.Observer;

import src.GroundStation.Station;
import src.Observer.Radar;
import src.Observer.TelescopeAzEl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cette classe est utilisée pour lire les fichiers CSV contenant les informations des observateurs.
 */
public class ReadObserverFile {

    /**
     * Cette méthode lit les télescopes à partir d'un fichier CSV donné et renvoie une liste de télescopes.
     *
     * @param fileName    le nom du fichier CSV à lire
     * @param stationList la liste des stations auquel les télescopes sont associés
     * @return une liste de télescopes
     */
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

    /**
     * Lit les données des radars à partir d'un fichier CSV et les associe à des stations terrestres.
     *
     * @param fileName    le nom du fichier CSV à lire
     * @param stationList une liste de stations terrestres associées aux radars
     * @return une liste d'objets Radar associés à des stations terrestres
     */
    public List<Radar> readRadarsFromCSV ( String fileName , List<Station> stationList ) {
        List<Radar> radars = new ArrayList<> ( );

        try (BufferedReader br = new BufferedReader ( new FileReader ( fileName ) )) {
            String line;

            // Ignore la première ligne (entête)
            br.readLine ( );

            // Lit les données ligne par ligne
            while ((line = br.readLine ( )) != null) {
                String[] values = line.split ( "," );

                // Extrait l'ID de la station, le type d'objet et l'ID de l'objet à partir de la première colonne
                String[] idValues = values[0].split ( ":" );
                String station = idValues[0];
                String type = idValues[1];
                int id = Integer.parseInt ( idValues[2].trim ( ) );

                // Vérifie que l'objet est un radar
                if (!Objects.equals ( type , "radar" )) {
                    continue;
                }

                // Récupère les valeurs des moyennes et des incertitudes angulaires
                double mean1 = Double.parseDouble ( values[1].trim ( ) );
                double mean2 = Double.parseDouble ( values[2].trim ( ) );
                double[] mean = {mean1 , mean2};

                double angularIncertitude1 = Double.parseDouble ( values[3].trim ( ) );
                double angularIncertitude2 = Double.parseDouble ( values[4].trim ( ) );
                double[] angularIncertitude = {angularIncertitude1 , angularIncertitude2};

                // Récupère les valeurs de l'angle de champ et du pas de mesure
                double angularFoV = Double.parseDouble ( values[5].trim ( ) );
                double stepMeasure = Double.parseDouble ( values[6].trim ( ) );

                // Associe le radar à une station terrestre
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
