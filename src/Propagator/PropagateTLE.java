package src.Propagator;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.AbsolutePVCoordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
Useful class to read TLE from a CSV File and propagate their orbit with a keplerian propagator
 */
public class PropagateTLE {

    List<String[]> absCoordinatesStringList;
    List<AbsolutePVCoordinates> absCoordinatesList;
    List<KeplerianPropagator> propagatorsList;
    
    PropagateTLE() throws Exception {
        this.absCoordinatesStringList = PropagateTLE.readPvCSV();
        this.absCoordinatesList = this.createAbsCoordinatesList();
        this.propagatorsList = this.createKeplerianPropagatorList();
    }

    /**
     * Reads TLE from a CSV file
     * @return a list of strings representing the TlEs
     * @throws Exception jcp
     */
    public static List<String[]> readPvCSV() throws Exception {
        List<String[]> tleList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("src/TLE/pv.csv"));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            tleList.add(data);
        }
        br.close();

        return tleList;
    }

    /**
     * Transforms a TLE String in an AbsolutePVCoordinates TLE, the string must first be in a AbsolutePVCoordinates format (position, velocity, date)
     * @param absCoordinatesString a TLE String representing the absolute coordinates of a TLE
     * @return AbsolutePVCoordinates of a TLE
     */
    public AbsolutePVCoordinates absoluteCoordinatesFromStringPV(String[] absCoordinatesString){
        double x = Double.parseDouble(absCoordinatesString[0]);
        double y = Double.parseDouble(absCoordinatesString[1]);
        double z = Double.parseDouble(absCoordinatesString[2]);

        double vx = Double.parseDouble(absCoordinatesString[3]);
        double vy = Double.parseDouble(absCoordinatesString[4]);
        double vz = Double.parseDouble(absCoordinatesString[5]);

        Vector3D position = new Vector3D(x, y, z);
        Vector3D velocity = new Vector3D(vx, vy, vz);

        Frame inertialFrame = FramesFactory.getEME2000();
        TimeScale utc = TimeScalesFactory.getUTC();

        double dateDouble = Double.parseDouble(absCoordinatesString[6]);
        int year = (int)Math.floor(dateDouble/1000);
        AbsoluteDate yearDate;

        if (year < 20){
            yearDate = new AbsoluteDate(1900 + year, 1, 1, utc);
        } else {
            yearDate = new AbsoluteDate(2000 + year, 1, 1, utc);
        }

        double elapseDuration = (dateDouble - year * 1000) * 86400;
        AbsoluteDate date = new AbsoluteDate(yearDate, elapseDuration);

        return new AbsolutePVCoordinates(inertialFrame, date, position, velocity);
    }

    /**
     * Reads an AbsolutePVCoordinates TLE and return a propagator corresponding
     * @param absPVCoordinates the Absolute coordinates of a TLE
     * @return a propagator corresponding to the previous TLE
     */
    public KeplerianPropagator keplerianPropagatorFromAbsPVC(AbsolutePVCoordinates absPVCoordinates){
        double mu =  3.986004415e+14;
        KeplerianOrbit initialOrbit = new KeplerianOrbit(absPVCoordinates.getPVCoordinates(), absPVCoordinates.getFrame(), mu);
        return new KeplerianPropagator(initialOrbit);
    }

    /**
     * Transforms a TLE String into a propagator TLE, the string must first be in a AbsolutePVCoordinates format (position, velocity, date)
     * @param absCoordinatesString the Absolute coordinates of a TLE
     * @return a propagator corresponding to the previous TLE
     */
    public KeplerianPropagator keplerianPropagatorFromStringAbsPVC(String[] absCoordinatesString){
        return this.keplerianPropagatorFromAbsPVC(this.absoluteCoordinatesFromStringPV(absCoordinatesString));
    }

    /**
     * Reads a list of TLEs, in a string AbsolutePVCoordinates format, and transforms it into a list of AbsolutePVCoordinates
     * @return a list of Keplerian propagators
     */
    public List<AbsolutePVCoordinates> createAbsCoordinatesList() {
        List<AbsolutePVCoordinates> absCoordinatesList = new ArrayList<>();
        for (String[] absPVCString : this.absCoordinatesStringList) {
            absCoordinatesList.add(this.absoluteCoordinatesFromStringPV(absPVCString));
        }

        return absCoordinatesList;
    }

    /**
     * Reads a list of TLEs, in a string AbsolutePVCoordinates format, and transforms it into a list of propagators
     * @return a list of Keplerian propagators
     */
    public List<KeplerianPropagator> createKeplerianPropagatorList(){
        List<KeplerianPropagator> propagatorList = new ArrayList<>();
        for (String[] absPVCString : this.absCoordinatesStringList) {
            propagatorList.add(this.keplerianPropagatorFromStringAbsPVC(absPVCString));
        }
        
        return propagatorList;
    }

    public static void main(String[] args) throws Exception
    {
        File orekitData = new File("lib/orekit-data-master/");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        PropagateTLE propagateTLE = new PropagateTLE();

        AbsolutePVCoordinates initialAbsPV = propagateTLE.absoluteCoordinatesFromStringPV(propagateTLE.absCoordinatesStringList.get(0));
        AbsoluteDate initialDate = initialAbsPV.getDate();

        System.out.println(propagateTLE.absCoordinatesStringList.size());
        System.out.println(initialAbsPV);

        KeplerianPropagator kepler1 = propagateTLE.keplerianPropagatorFromAbsPVC(initialAbsPV);

        double duration = 600.;
        AbsoluteDate finalDate = initialDate.shiftedBy(duration);
        double stepT = 60.;
        int cpt = 1;
        for (AbsoluteDate extrapDate = initialDate; extrapDate.compareTo(finalDate) <= 0; extrapDate = extrapDate.shiftedBy(stepT)) {
            SpacecraftState currentState = kepler1.propagate(extrapDate);
            System.out.format(Locale.US, "step %2d %s %s%n", cpt++, currentState.getDate(), currentState.getOrbit());
        }

        KeplerianPropagator kepler2 = propagateTLE.propagatorsList.get(0);
        AbsoluteDate initialDate2 = propagateTLE.absCoordinatesList.get(0).getDate();
        AbsoluteDate finalDate2 = initialDate2.shiftedBy(duration);
        cpt = 1;
        for (AbsoluteDate extrapDate = initialDate2; extrapDate.compareTo(finalDate2) <= 0; extrapDate = extrapDate.shiftedBy(stepT)) {
            SpacecraftState currentState = kepler2.propagate(extrapDate);
            System.out.format(Locale.US, "step %2d %s %s%n", cpt++, currentState.getDate(), currentState.getOrbit());
        }



    }
}