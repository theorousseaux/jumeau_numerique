package src.App.ParametersTab;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import src.App.MainFrame;

import java.awt.event.*;
import java.awt.*;

/**
 * This class contains the form where the user will enter the setting for the simulation.
 */
public class ParametersFormView extends JPanel{

    // The controller that will use the input information in backend
    private ParametersController controller;

    // The display manager
    GridBagConstraints gc = new GridBagConstraints();

    // The main frame (i.e. the app)
    MainFrame parent;


    /**
     * Constructor
     * @param parent
     */
    public void parametersForm(MainFrame parent){

        // Setting the attributes
        this.parent = parent;
        controller = parent.paramController;

        // Title
        JLabel paramLabel = new JLabel("Set simulation parameters"); // Set text
        paramLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font and size
        paramLabel.setForeground(Color.BLUE); // Set color to blue


        // Input fields creation
        JTextField noiseLevField = new JTextField(10);

        JTextField startYearField = new JTextField(4);
        JTextField startMonthField = new JTextField(2);
        JTextField startDayField = new JTextField(2);

        JTextField endYearField = new JTextField(4);
        JTextField endMonthField = new JTextField(2);
        JTextField endDayField = new JTextField(2);

        // Creation of the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Setting padding
        gc.insets = new Insets(20, 10, 20, 10);
        // Adding title
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;

        formPanel.add(paramLabel, gc);

        // Adding noise selection
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_END;

        formPanel.add(new JLabel("Noise level: "), gc);

        gc.gridx ++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;

        formPanel.add(noiseLevField, gc);

        // Adding start date
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

        // Adding end date
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

         // Add button creation
         JButton addButton = new JButton("Set Parameters");
         addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetching input data
                double noiseLevel = Double.parseDouble(noiseLevField.getText());

                int startDay = Integer.parseInt(startDayField.getText());
                int startMonth = Integer.parseInt(startMonthField.getText());
                int startYear = Integer.parseInt(startYearField.getText());

                int endDay = Integer.parseInt(endDayField.getText());
                int endMonth = Integer.parseInt(endMonthField.getText());
                int endYear = Integer.parseInt(endYearField.getText());

                // Checking their validity

                if (endDay > 31 || endDay < 1 ||
                        startDay > 31 || startDay < 1 ||
                        endMonth > 12 || endMonth < 1 ||
                        startMonth > 12 || startMonth < 1){
                    JOptionPane.showMessageDialog(parent, "Invalid dates", "Error",JOptionPane.ERROR_MESSAGE);
                }else {
                    // Creating the model
                    System.out.println ( "Noise level: " + String.valueOf ( noiseLevel ) );
                    controller.setNoiseLevel ( noiseLevel );

                    controller.setStartDate ( startYear , startMonth , startDay );

                    controller.setEndDate ( endYear , endMonth , endDay );

                    System.out.println ( "Simulation saved" );
                    System.out.println ( "Noise level: " + controller.getNoiseLevel ( ) );
                    System.out.println ( "Start date: " + controller.getStartDate ( ) );
                    System.out.println ( "Start date: " + controller.getEndDate ( ) );
                    System.out.println ( parent.paramController.getStartDate ( ).toString ( ) );
                }
            
            }
                
         });

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
   }

  

   public ParametersFormView(MainFrame parent) {
        parametersForm(parent);
   }
}

