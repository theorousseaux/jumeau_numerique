package src.GUI.UpdateSatelliteDBTab;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateDBController {

    private DataBase model;

    public DataBase getModel ( ) {
        return model;
    }

    public void setModel ( DataBase model ) {
        this.model = model;
    }

    public void createDB ( String csvFile ) throws ClassNotFoundException, SQLException, IOException {
        model = new DataBase ( csvFile );
        System.out.println ( "Database created" );
    }


}
