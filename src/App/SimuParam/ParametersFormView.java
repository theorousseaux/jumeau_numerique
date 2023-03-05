package src.App.SimuParam;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import src.App.MainFrame;

import java.awt.event.*;
import java.awt.*;

public class ParametersFormView extends JPanel{

    private ParametersController controller = new ParametersController(new Parameters(), this);
    GridBagConstraints gc = new GridBagConstraints();
    MainFrame parent;

    

    public void parametersForm(){
        JLabel paramLabel = new JLabel("Set simulation parameters");
        paramLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        paramLabel.setForeground(Color.BLUE); // Définit la couleur du texte en bleu


        // Création des champs de saisie
        JTextField noiseLevField = new JTextField(10);

        JTextField startYearField = new JTextField(10);
        JTextField startMonthField = new JTextField(10);
        JTextField startDayField = new JTextField(10);

        JTextField endYearField = new JTextField(10);
        JTextField endMonthField = new JTextField(10);
        JTextField endDayField = new JTextField(10);


        // Création du formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Définition de la marge
        gc.insets = new Insets(2, 5, 2, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        formPanel.add(paramLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Noise level: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(noiseLevField, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Start Date (dd-jj-aaa): "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(startDayField, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(startMonthField, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(startYearField, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("End Date (dd-jj-aaa): "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(endDayField, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(endMonthField, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(endYearField, gc);



         // Création du bouton "Add"
         JButton addButton = new JButton("Set Parameters");
         addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des données saisies
                double noiseLevel = Double.parseDouble(noiseLevField.getText());

                int startDay = Integer.parseInt(startDayField.getText());
                int startMonth = Integer.parseInt(startMonthField.getText());
                int startYear = Integer.parseInt(startYearField.getText());

                int endDay = Integer.parseInt(endDayField.getText());
                int endMonth = Integer.parseInt(endMonthField.getText());
                int endYear = Integer.parseInt(endYearField.getText());

                
                // Création du modèle
                System.out.println("Noise level: " + String.valueOf(noiseLevel));
                controller.setNoiseLevel(noiseLevel);

                controller.setStartDate(startYear, startMonth, startDay);

                controller.setEndDate(endYear, endMonth, endDay);

                    // update
                
                System.out.println("Simulation saved");
                System.out.println("Noise level: " + controller.getNoiseLevel());
                System.out.println("Start date: " + controller.getStartDate());
                System.out.println("Start date: " + controller.getEndDate());
            
            }
                
         });

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
   }

  

   public ParametersFormView() {
        parametersForm();
   }
}

