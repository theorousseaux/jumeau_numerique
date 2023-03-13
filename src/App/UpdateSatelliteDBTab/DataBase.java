package src.App.UpdateSatelliteDBTab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.util.ArrayList;
import java.util.List;

import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import src.Kalman.*;
import src.Kalman.Object;

public class DataBase {
    
    private Connection connection; 
    
    public DataBase(String csvFile) throws ClassNotFoundException, IOException, SQLException {
    	// importation des donnees de base (toujour mettre ça en début de programme)
        // File orekitData = new File("/Users/eliott/Documents/jumeau_numerique/lib/orekit-data-master");
        // DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        // manager.addProvider(new DirectoryCrawler(orekitData));
        
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:myDB;create=true";
        this.connection = DriverManager.getConnection(url);
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE tleDB (id_sat CHAR(10), date CHAR(40), a DOUBLE, eccentricity DOUBLE, inclination DOUBLE, raan DOUBLE, argp DOUBLE, ma DOUBLE, SM DOUBLE)");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Table already exists");
        }
        
        //String csvFile = "/Users/eliott/Documents/jumeau_numerique/src/PIE_SQL/src/tle.csv";
        String filename = csvFile;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;

        while ((line = reader.readLine()) != null) {
            // parse TLE data
            String[] parts = line.split(",");
            // String epoch = parts1[3];contient la date
            String id_sat = parts[0];
            String a = parts[1];
            String e = parts[2];
            String i = parts[3];
            String raan = parts[4];
            String argp = parts[5];
            String ma = parts[6];
            // String date = parts[7];
            String SM = parts[8];

            // Process on the date
            TimeScale utc = TimeScalesFactory.getUTC();
            double dateDouble = Double.parseDouble(parts[7]);
            int year = (int) Math.floor(dateDouble / 1000);
            AbsoluteDate yearDate;
            if (year < 20) {
                yearDate = new AbsoluteDate(1900 + year, 1, 1, utc);
            } else {
                yearDate = new AbsoluteDate(2000 + year, 1, 1, utc);
            }
            double elapseDuration = (dateDouble - year * 1000) * 86400;
            AbsoluteDate abs_date_ = new AbsoluteDate(yearDate, elapseDuration);
            //
            String date = abs_date_.toString();

            // System.out.println(satelliteNum);

            // add data into db
            // "INSERT INTO maTable (id, nom) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM
            // maTable WHERE id = ?)"
            // WHERE NOT EXISTS (SELECT 1 FROM tleDB WHERE satellite_num = ?)
            String sql_add = "INSERT INTO tleDB (id_sat, date, a, eccentricity, inclination, raan, argp, ma, SM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(sql_add);
            stmt2.setString(1, id_sat);
            stmt2.setString(2, date);
            stmt2.setString(3, a);
            stmt2.setString(4, e);
            stmt2.setString(5, i);
            stmt2.setString(6, raan);
            stmt2.setString(7, argp);
            stmt2.setString(8, ma);
            stmt2.setString(9, SM);
            stmt2.executeUpdate();
        }
        reader.close();
        /*
        Statement stmt3 = connection.createStatement();
        ResultSet rs = stmt3.executeQuery("SELECT * FROM tleDB");
        while (rs.next()) {
            String id_sat = rs.getString("id_sat");
            String date = rs.getString("date");
            String a = rs.getString("a");
            String e = rs.getString("eccentricity");
            String i = rs.getString("inclination");
            String raan = rs.getString("raan");
            String argp = rs.getString("argp");
            String ma = rs.getString("ma");
            String SM = rs.getString("SM");

            System.out.println("id_sat: " + id_sat + "date: " + date + " a: " + a + " e: " + e
                        + " i: " + i + " raan: " + raan + " argp: " + argp + " ma: " + ma + " SM: " + SM);
        }
        */
    }
    
    public List<Object> selectSatellites(String type, Integer nb) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.setMaxRows(nb);
        String query = selectSatellitesQuery(type);
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        List<Object> objectList = new ArrayList<>();
        while (rs.next()) {
            String id_sat = rs.getString("id_sat");
            String date = rs.getString("date");
            String a = rs.getString("a");
            String e = rs.getString("eccentricity");
            String i = rs.getString("inclination");
            String raan = rs.getString("raan");
            String argp = rs.getString("argp");
            String ma = rs.getString("ma");
            String SM = rs.getString("SM");

            String[] date_parts = date.split("-");
            int year = Integer.parseInt(date_parts[0]);
            int month = Integer.parseInt(date_parts[1]);
            String[] day_parts = date_parts[2].split("T");
            int day = Integer.parseInt(day_parts[0]);
            String[] hours_parts = day_parts[1].split(":");
            int hour = Integer.parseInt(hours_parts[0]);
            int minute = Integer.parseInt(hours_parts[1]);
            String[] seconds_parts = hours_parts[2].split("Z");
            Double second = Double.parseDouble(seconds_parts[0]);

            AbsoluteDate date2 = new AbsoluteDate(year, month, day, hour, minute, second,TimeScalesFactory.getUTC());
            double prop_min_step = 0.001;
            double prop_max_step = 300.0;
            double prop_position_error = 10.0;

            KeplerianOrbit orbit = new KeplerianOrbit(Double.parseDouble(a), Double.parseDouble(e),
                    Double.parseDouble(i), Double.parseDouble(argp), Double.parseDouble(raan),
                    Double.parseDouble(ma), constants.type, constants.gcrf, date2,
                    constants.mu);
            double[][] tolerances = NumericalPropagator.tolerances(prop_position_error, orbit, OrbitType.CARTESIAN);
            DormandPrince853Integrator integrator = new DormandPrince853Integrator(prop_min_step, prop_max_step, tolerances[0], tolerances[1]);
            NumericalPropagator propagator = new NumericalPropagator(integrator);
            CartesianOrbit cartOrbit = new CartesianOrbit(orbit);
            SpacecraftState initialState = new SpacecraftState(cartOrbit);
            propagator.setInitialState(initialState);
            propagator.setMu(constants.mu);
            OrbitType orbitType = OrbitType.CARTESIAN;
            propagator.setOrbitType(orbitType);
            propagator.setPositionAngleType(constants.type);
            Object object = new Object(propagator, id_sat, Double.parseDouble(SM));
            objectList.add(object);
        }
        return objectList;
    }

    public String selectSatellitesQuery(String type) {
    	
    	String select = "";
    	String from = "";
    	String where = "";
    	
        if (type == "GEO") {
            select = "*";
            from = "tleDB";
            where = "a BETWEEN 41500000 AND 42500000 AND inclination BETWEEN -0.1 AND 0.1 AND eccentricity < 0.1";
        }
        if (type == "LEO") {
            select = "*";
            from = "tleDB";
            where = "a BETWEEN 6600000 AND 6900000 AND inclination BETWEEN -0.1 AND 0.1 AND eccentricity < 0.1";
        }
        String query = "SELECT " + select + " FROM " + from + " WHERE " + where;
        
        return query;
    }
}
