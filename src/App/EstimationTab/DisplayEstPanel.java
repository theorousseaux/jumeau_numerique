package src.App.EstimationTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DisplayEstPanel extends JPanel {

    MainFrame parent;

    public DisplayEstPanel ( MainFrame parent ) {
        this.parent = parent;

    }

    public void removeAllExceptTop ( ) {
        Component[] components = this.getComponents ( );
        for (int i = 0; i < components.length; i++) {
            this.remove ( components[i] );
        }
    }

    public void update ( ) {

        // Deleting object except title
        removeAllExceptTop ( );
        // Display of the measurements
        GridBagConstraints gc = new GridBagConstraints ( );
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;

        List<String> measurementsList = this.parent.estimationController.model.estimationsList;

        gc.anchor = GridBagConstraints.PAGE_START;
        ArrayList<String> elements = new ArrayList<String> ( );
        Set<String> observedsat = new HashSet<> (  );
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
