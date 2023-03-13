package src.App.UpdateSatelliteDBTab;

public class UpdateDBController {
	
	DataBase db;


    public void createDB(String csvFile) throws SQLException, IOException, ClassNotFoundException {
    	db = new DataBase(csvFile);
        System.out.println("Database created");
    }
}
