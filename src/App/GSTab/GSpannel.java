package src.App.GSTab;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.App.MainFrame;

import java.awt.event.*;
import java.awt.*;

public class GSpannel extends JPanel {

    MainFrame parent;
    GridBagConstraints gc = new GridBagConstraints();;

    public GSpannel(MainFrame parent) {

        this.parent = parent;

        setLayout(new GridBagLayout());

        // Création du texte d'ajout
        JLabel addGSLabel = new JLabel("Add a new ground station");
        
        // Création du bouton d'affichage
        JButton displayGSButton = new JButton("Display");

        // Affichage des stations sol
        DisplayGSPannel displayGSPannel = new DisplayGSPannel(parent);

        // Affichage du formulaire d'ajout d'une station sol
        NewGSPannel newGSPannel = new NewGSPannel(parent, displayGSPannel);

        // Ajout des éléments au panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(newGSPannel, gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        this.add(displayGSPannel, gc);


        // Ajout d'un listener pour l'élément de menu "Display" de "Ground Station"
        displayGSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Affichage de la liste des stations sol dans une fenêtre de dialogue
                DisplayGSDialog displayGSDialog = new DisplayGSDialog(parent);
                displayGSDialog.display();
            }
        });

    }
}