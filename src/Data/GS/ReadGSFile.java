package src.Data.GS;
import src.Kalman.Station;
import src.Kalman.constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;

public class ReadGSFile {

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

    public List<Propagator> readSat(String fname, AbsoluteDate initialDate, int n) throws NumberFormatException, IllegalArgumentException, IOException{
        List<Propagator> satList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fname));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null && i<n)
        {
            String[] data = line.split(",");
            if(!(data[0].equals("a"))){
                KeplerianOrbit trueOrbit = new KeplerianOrbit(Float.parseFloat(data[0]),Float.parseFloat(data[1]),Float.parseFloat(data[2]),Float.parseFloat(data[3]),Float.parseFloat(data[4]),Float.parseFloat(data[5]), constants.type, constants.gcrf, initialDate, Constants.EGM96_EARTH_MU);
                KeplerianPropagator truePropagator = new KeplerianPropagator(trueOrbit);
                satList.add(truePropagator);
                i++;
            }
        }
        br.close();
        return satList;
    }

}