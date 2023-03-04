package src.App.SimuParam;
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

    public ParametersView() {

        this.parent = parent;

        setLayout(new GridBagLayout());


        // Affichage des stations sol
        SateliteDataView displayGSPannel = new SateliteDataView();

        // Affichage du formulaire d'ajout d'une station sol
        ParameterFormView newGSPannel = new ParameterFormView();

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(displayGSPannel, gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(newGSPannel, gc);
        

    }
}

