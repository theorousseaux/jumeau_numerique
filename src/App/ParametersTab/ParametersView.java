package src.App.ParametersTab;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.App.MainFrame;

import java.awt.event.*;
import java.awt.*;
import javax.swing.JPanel;

public class ParametersView extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();

    public ParametersView(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());


        SatelliteDataView satDataView = new SatelliteDataView(parent);

        ParametersFormView paramSettingForm = new ParametersFormView(parent);

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(satDataView, gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(paramSettingForm, gc);
        

    }
}

