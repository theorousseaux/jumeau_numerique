package src.Data.Observer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import src.Kalman.TelescopeAzEl;

/**
 * Cette classe permet d'enregisterer les changements faits par l'utilisateur dans la base de données du site
 */
public class WriteObserverFile {

    /**
     * Methode pour ajouter un nouveau telescope à la base de donnée globale de l'application
     *
     * @param telescopeAzEl le telescope que l'on souhaite ajouter à la base de données
     */
    public void writeObserverTelescope(TelescopeAzEl telescopeAzEl) throws NumberFormatException {

        try {
            FileWriter fw = new FileWriter("src/Data/Observer/Observer.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(telescopeAzEl.getID() + ',' + telescopeAzEl.getMean()[0] + ',' + telescopeAzEl.getMean()[1] + ',' + telescopeAzEl.getAngularIncertitude()[0] + ',' + telescopeAzEl.getAngularIncertitude()[1] + ',' + telescopeAzEl.getElevationLimit() + ',' + telescopeAzEl.getAngularFoV() + ',' + telescopeAzEl.getStepMeasure() + ',' + telescopeAzEl.getBreakTime() + ',' + telescopeAzEl.getGEO ());
            bw.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
