/**
 * Cette classe permet de lire un fichier contenant les informations sur les stations au sol d'un réseau de communication satellite.
 * Elle renvoie une liste d'objets Station.
 */
package src.Data.GS;

import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;
import src.GroundStation.Station;
import src.constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadGSFile {

    /**
     * Cette méthode permet de lire un fichier contenant les informations sur les stations au sol d'un réseau de communication satellite.
     *
     * @param fname le nom du fichier à lire.
     * @return une liste d'objets Station contenant les informations sur les stations au sol du réseau.
     * @throws NumberFormatException si le format des nombres dans le fichier est incorrect.
     * @throws IOException si une erreur se produit lors de la lecture du fichier.
     */
    public List<Station> readStation(String fname) throws NumberFormatException, IOException {

        List<Station> GSList = new ArrayList<>(); // list of stations in the network
        BufferedReader br = new BufferedReader(new FileReader(fname));
        String line;
        while ((line = br.readLine()) != null) {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")) {
                GSList.add(new Station(data[0], Double.parseDouble(data[1]) * Math.PI / 180, Double.parseDouble(data[2]) * Math.PI / 180, Double.parseDouble(data[3])));
            }
        }
        br.close();
        return GSList;
    }
}
