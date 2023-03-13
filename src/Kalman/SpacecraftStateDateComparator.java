package src.Kalman;
import java.util.Comparator;
import org.orekit.propagation.SpacecraftState;


public class SpacecraftStateDateComparator implements Comparator<SpacecraftState> {

    @Override
    public int compare(SpacecraftState o1, SpacecraftState o2) {
        // Compare les dates de SpacecraftState o1 et o2
        return o1.getDate().compareTo(o2.getDate());
    }
}
