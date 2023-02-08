package src.Data;
import src.Kalman.Station;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de lecture des bases de données
 */
public class ReadFile {


    /**
     * Creation de toutes les stations à partir de la base de données
     * @return La liste des {@Station} de la base de donné
     * @throws NumberFormatException
     * @throws IOException
     */
    public List<Station> readStation() throws NumberFormatException, IOException{
    
        List<Station> GSList = new ArrayList<>(); // list of stations in the network
        BufferedReader br = new BufferedReader(new FileReader("Data/GS.csv"));
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


    /**
     * Création d'une liste de stations sélectionnées par l'utilisateur
     * @param reqStations
     * @return la liste des station dans la classe {@Station}
     * @throws NumberFormatException
     * @throws IOException
     */
    public List<Station> readStation(List<String> reqStations) throws NumberFormatException, IOException{
    
        List<Station> GSList = new ArrayList<>(); // list of stations in the network
        List<String> stationsList = new ArrayList<>();  //list of available stations in DB
        BufferedReader br = new BufferedReader(new FileReader("GS.csv"));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                stationsList.add(data[0]);
                GSList.add(new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3])));
            }
        }
        br.close();
        return GSList;
    }

}