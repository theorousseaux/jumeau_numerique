package src.Data.GS;

import src.Kalman.Station;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Cette classe permet d'enregisterer les changements faits par l'utilisateur dans la base de données du site
 */
public class WriteGSFile {

    /**
     * Methode pour ajouter une nouvelle station à la base de donnée globale de l'application
     *
     * @param station la station que l'on souhaite ajouter à la base de données
     * @throws NumberFormatException
     * @throws IOException
     */
    public void writeStation ( Station station ) throws NumberFormatException, IOException {

        try {
            FileWriter fw = new FileWriter ( "src/Data/GS/GS.csv" , true );
            BufferedWriter bw = new BufferedWriter ( fw );
            bw.newLine ( );
            bw.write ( station.getName ( ) + ", " + station.getLatitude ( ) * 180 / Math.PI + ", " + station.getLongitude ( ) * 180 / Math.PI + ", " + station.getAltitude ( ) );
            bw.close ( );
            System.out.println ( "Successfully wrote to the file." );
        } catch (IOException e) {
            System.out.println ( "An error occurred." );
            e.printStackTrace ( );
        }
    }
}
