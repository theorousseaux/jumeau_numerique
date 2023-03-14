package src.GUI.EstimationTab;

import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisplayEstView extends JPanel {

    final MainFrame parent;

    /**
     * Constructeur de la vue d'affichage des estimations pour l'est.
     *
     * @param parent La fenêtre principale de l'application.
     */
    public DisplayEstView ( MainFrame parent ) {
        this.parent = parent;
    }

    /**
     * Supprime tous les composants sauf le titre de la vue.
     */
    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 0; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    /**
     * Met à jour la vue avec les nouvelles estimations.
     */
    public void update ( ) {
        // Suppression des anciens éléments
        removeAllExceptTop ( );

        // Affichage des nouvelles mesures
        GridBagConstraints gc = new GridBagConstraints ( );
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;

        List<String> measurementsList = this.parent.getEstimationController ( ).model.estimationsList;

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String> ( );
        Set<String> observedsat = new HashSet<> ( );
        for (String line : measurementsList) {
            elements.add ( line );
        }
        String[] arr = {};
        arr = elements.toArray ( arr );
        JList<String> liste = new JList<> ( arr );
        JScrollPane satellites = new JScrollPane ( liste );
        this.add ( satellites , gc );
    }
}

