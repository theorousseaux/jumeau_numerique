package src.App.UpdateSatelliteDBTab;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateDBController {

    public DataBase db;


    public void createDB ( String csvFile ) throws ClassNotFoundException, SQLException, IOException {
        db = new DataBase ( csvFile );
        System.out.println ( "Database created" );
    }


}
