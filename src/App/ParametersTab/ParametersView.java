package src.App.ParametersTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * This class groups all the information for the parameter setting tab
 */
public class ParametersView extends JPanel {

    // The parent window (i.e. the app)
    MainFrame parent;

    // The display manager
    GridBagConstraints gc = new GridBagConstraints ( );

    public ParametersView ( MainFrame parent ) {

        this.parent = parent;

        setLayout ( new GridBagLayout ( ) );

        SatelliteDataView satDataView = new SatelliteDataView ( parent );

        ParametersFormView paramSettingForm = new ParametersFormView ( parent );

        // Adding satellite data display
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( satDataView , gc );

        // Adding parameters setting form
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add ( paramSettingForm , gc );
    }
}

