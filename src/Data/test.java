package src.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;

public class test {

    public static void main(String[] args) throws NumberFormatException, IOException {

        File orekitData = new File("lib/orekit-data-master");
    	DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	manager.addProvider(new DirectoryCrawler(orekitData));	
        ReadGSFile reader = new ReadGSFile();
        //reader.readStation();

        Station station = new Station("Tournan-en-Brie", 3., 20., 310.);

        WriteGSFile writer = new WriteGSFile();
        writer.writeStation(station);
    }
    
}
