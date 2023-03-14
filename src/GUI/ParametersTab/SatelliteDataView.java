package src.GUI.ParametersTab;

import src.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SatelliteDataView extends JPanel {
    final GridBagConstraints gc = new GridBagConstraints ( );
    MainFrame parent;

    public SatelliteDataView ( MainFrame parent ) {
        // A compléter
        satellitesInformation ( "ISS" , "PLEIADE" , "20-08-2000" , "30-05-2002" );
    }

    public void satellitesInformation ( String startName , String endName , String startDate , String endDate ) {
        setLayout ( new GridBagLayout ( ) );

        // Title
        JLabel title = new JLabel ( "Current satellites limit dates" );
        title.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) );
        title.setForeground ( Color.BLUE );

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets ( 20 , 10 , 20 , 10 );
        this.add ( title , gc );

        // Display of satellites data
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0;
        gc.gridwidth = 1;
        this.add ( new JLabel ( "First satellite date: " + startDate + " (" + startName + ")" ) , gc );
        gc.gridy++;
        this.add ( new JLabel ( "Last satellite date: " + endDate + " (" + endName + ")" ) , gc );
    }
}

