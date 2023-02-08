package Data;
import Kalman.Station;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    

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
