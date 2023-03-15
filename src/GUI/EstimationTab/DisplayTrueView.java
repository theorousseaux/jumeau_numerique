package src.GUI.EstimationTab;

import org.orekit.orbits.KeplerianOrbit;
import src.GUI.MainFrame;
import src.constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Cette classe représente une vue pour afficher les mesures vraies.
 * Elle étend la classe JPanel.
 */
public class DisplayTrueView extends JPanel {

    final MainFrame parent;

    /**
     * Constructeur de la classe DisplayTrueView.
     *
     * @param parent la fenêtre parente
     */
    public DisplayTrueView ( MainFrame parent ) {
        this.parent = parent;
    }

    /**
     * Méthode pour supprimer tous les composants sauf le titre.
     */
    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 0; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    /**
     * Méthode pour mettre à jour l'affichage.
     * Elle supprime d'abord tous les composants sauf le titre,
     * puis affiche les mesures vraies des satellites observés.
     */
    public void update ( ) {
        removeAllExceptTop ( );

        // Affichage des mesures
        GridBagConstraints gc = new GridBagConstraints ( );
        gc.gridx = 0;
        gc.gridy = 0;

        gc.gridy++;
        ArrayList<String> elements2 = new ArrayList<String> ( );
        int j = 0;
        for (String satname : parent.getSimuController ( ).getModel ( ).getSatellitesNames ( )) {
            if (parent.getEstimationController ( ).model.getObservedSat ( ).contains ( satname )) {
                KeplerianOrbit nOrbit = new KeplerianOrbit(parent.getEstimationController ( ).model.getPropagators ( ).get ( j ).getInitialState ( ).getOrbit ( ));
                double[] param = {nOrbit.getA ( ) , nOrbit.getE ( ) , Math.IEEEremainder ( nOrbit.getI ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getRightAscensionOfAscendingNode ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getPerigeeArgument ( ) , 2 * Math.PI ) , Math.IEEEremainder ( nOrbit.getAnomaly ( constants.type ) , 2 * Math.PI )};

                elements2.add ( "Sat " + satname + "    : [" + Arrays.toString ( param ) );
            }
            j++;
        }
        String[] arr2 = {};
        arr2 = elements2.toArray ( arr2 );
        JList<String> liste2 = new JList<> ( arr2 );
        JScrollPane satellites2 = new JScrollPane ( liste2 );
        this.add ( satellites2 , gc );
    }
}

