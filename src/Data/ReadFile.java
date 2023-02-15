package src.Data;
import src.Kalman.Station;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.orekit.propagation.Propagator;

/**
 * Classe de lecture des bases de données
 */
public class ReadFile {

    public List<Station> readStation(String fname) throws NumberFormatException, IOException{
    
        List<Station> GSList = new ArrayList<>(); // list of stations in the network
        BufferedReader br = new BufferedReader(new FileReader(fname));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                GSList.add(new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3])));
            }
        }
        br.close();
        return GSList;
    }

    public List<Station> readStation(String fname, List<String> reqStations) throws NumberFormatException, IOException{
    
        List<Station> GSList = new ArrayList<>(); // list of stations in the network
        BufferedReader br = new BufferedReader(new FileReader(fname));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                if (reqStations.contains(data[0])){
                    GSList.add(new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3])));
                }
            }
        }
        br.close();
        return GSList;
    }

    public List<Propagator> readSat(String fname, int n) throws FileNotFoundException{
        List<Propagator> satList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fname));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null && i<n)
        {
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                if (reqStations.contains(data[0])){
                    GSList.add(new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3])));
                }
            }
            i++;
        }
        return satList;
    }

}