package Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Kalman.Station;

/**
 * Cette classe permet d'enregisterer les changements faits par l'utilisateur dans la base de données du site
 */
public class WriteFile {
    
    /**
     * Methode pour ajouter une nouvelle station à la base de donnée globale de l'application
     * 
     * @param station la station que l'on souhaite ajouter à la base de données
     * @throws NumberFormatException
     * @throws IOException
     */
    public void writeStation(Station station) throws NumberFormatException, IOException{
    
        try {
            FileWriter fw = new FileWriter("Data/GS.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(station.getName() + ", " + station.getLatitude() + ", " + station.getLongitude() + ", " + station.getAltitude());
            bw.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
