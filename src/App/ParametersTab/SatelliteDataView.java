package src.App.ParametersTab;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import src.App.MainFrame;

import java.awt.event.*;
import java.awt.*;

public class SatelliteDataView extends JPanel{
    GridBagConstraints gc = new GridBagConstraints();
    MainFrame parent;

    public void satellitesInformation(String startName, String endName, String startDate, String endDate){
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Current satellites limit dates");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.gridwidth = 1;
        this.add(new JLabel("First satellite date: " + startDate + " ("+startName+")"), gc);
        gc.gridy++;
        this.add(new JLabel("Last satellite date: " + endDate + " ("+endName+")"), gc);
    }


   public SatelliteDataView(MainFrame parent) {
        satellitesInformation("ISS", "PLEIADE", "20-08-2000", "30-05-2002");
   }
}

