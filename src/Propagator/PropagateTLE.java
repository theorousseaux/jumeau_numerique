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

public class PropagateTLE {

    List<String[]> pvCoordinatesStringList;

    PropagateTLE() throws Exception {
        this.pvCoordinatesStringList = PropagateTLE.readPvCSV();
    }

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

    public AbsolutePVCoordinates absoluteCoordinatesFromStringPV(String[] pvCoordinatesString){
        double x = Double.parseDouble(pvCoordinatesString[0]);
        double y = Double.parseDouble(pvCoordinatesString[1]);
        double z = Double.parseDouble(pvCoordinatesString[2]);

        double vx = Double.parseDouble(pvCoordinatesString[3]);
        double vy = Double.parseDouble(pvCoordinatesString[4]);
        double vz = Double.parseDouble(pvCoordinatesString[5]);

        Vector3D position = new Vector3D(x, y, z);
        Vector3D velocity = new Vector3D(vx, vy, vz);

        Frame inertialFrame = FramesFactory.getEME2000();
        TimeScale utc = TimeScalesFactory.getUTC();

        double dateDouble = Double.parseDouble(pvCoordinatesString[6]);
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

    public KeplerianPropagator createKeplerianPropagatorFromAbsPVC(AbsolutePVCoordinates absPVCoordinates){
        double mu =  3.986004415e+14;
        KeplerianOrbit initialOrbit = new KeplerianOrbit(absPVCoordinates.getPVCoordinates(), absPVCoordinates.getFrame(), mu);
        return new KeplerianPropagator(initialOrbit);
    }

    public KeplerianPropagator keplerianPropagatorFromStringAbsPVC(String[] pvCoordinatesString){
        return this.createKeplerianPropagatorFromAbsPVC(this.absoluteCoordinatesFromStringPV(pvCoordinatesString));
    }

    public static void main(String[] args) throws Exception
    {
        File orekitData = new File("lib/orekit-data-master/");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        PropagateTLE propagator = new PropagateTLE();

        AbsolutePVCoordinates initialAbsPV = propagator.absoluteCoordinatesFromStringPV(propagator.pvCoordinatesStringList.get(0));
        AbsoluteDate initialDate = initialAbsPV.getDate();

        System.out.println(propagator.pvCoordinatesStringList.size());
        System.out.println(initialAbsPV);

        KeplerianPropagator kepler = propagator.createKeplerianPropagatorFromAbsPVC(initialAbsPV);

        double duration = 600.;
        AbsoluteDate finalDate = initialDate.shiftedBy(duration);
        double stepT = 60.;
        int cpt = 1;
        for (AbsoluteDate extrapDate = initialDate; extrapDate.compareTo(finalDate) <= 0; extrapDate = extrapDate.shiftedBy(stepT)) {
            SpacecraftState currentState = kepler.propagate(extrapDate);
            System.out.format(Locale.US, "step %2d %s %s%n", cpt++, currentState.getDate(), currentState.getOrbit());
        }

    }
}