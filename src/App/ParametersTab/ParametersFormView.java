package src.App.ParametersTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class contains the form where the user will enter the setting for the simulation.
 */
public class ParametersFormView extends JPanel {

    // The display manager
    GridBagConstraints gc = new GridBagConstraints ( );
    // The main frame (i.e. the app)
    MainFrame parent;
    // The controller that will use the input information in backend
    private ParametersController controller;


    public ParametersFormView ( MainFrame parent ) {
        parametersForm ( parent );
    }

    /**
     * Constructor
     *
     * @param parent
     */
    public void parametersForm ( MainFrame parent ) {

        // Setting the attributes
        this.parent = parent;
        controller = parent.getParamController ( );

        // Title
        JLabel paramLabel = new JLabel ( "Set simulation parameters" ); // Set text
        paramLabel.setFont ( new Font ( "Arial" , Font.BOLD , 18 ) ); // Set font and size
        paramLabel.setForeground ( Color.BLUE ); // Set color to blue


        // Input fields creation
        JTextField noiseLevField = new JTextField ( 10 );

        JTextField startYearField = new JTextField ( 4 );
        JTextField startMonthField = new JTextField ( 2 );
        JTextField startDayField = new JTextField ( 2 );

        JTextField endYearField = new JTextField ( 4 );
        JTextField endMonthField = new JTextField ( 2 );
        JTextField endDayField = new JTextField ( 2 );

        // Creation of the form
        JPanel formPanel = new JPanel ( );
        formPanel.setLayout ( new GridLayout ( 2 , 4 ) );

        GridBagConstraints gc = new GridBagConstraints ( );

        // Setting padding
        gc.insets = new Insets ( 20 , 10 , 20 , 10 );
        // Adding title
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;

        add ( paramLabel , gc );

        // Adding start date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "Start Date (jj-mm-aaa): " ) );

        gc.gridx++;
        formPanel.add ( startDayField );

        gc.gridx++;
        formPanel.add ( startMonthField );

        gc.gridx++;
        formPanel.add ( startYearField );

        // Adding end date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "End Date (jj-mm-aaa): " ) );

        gc.gridx++;
        formPanel.add ( endDayField );

        gc.gridx++;
        formPanel.add ( endMonthField );

        gc.gridx++;
        formPanel.add ( endYearField );

        // Add button creation
        JButton addButton = new JButton ( "Set Parameters" );
        addButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // Fetching input data
                try {
                    int startDay = Integer.parseInt ( startDayField.getText ( ) );
                    int startMonth = Integer.parseInt ( startMonthField.getText ( ) );
                    int startYear = Integer.parseInt ( startYearField.getText ( ) );

                    int endDay = Integer.parseInt ( endDayField.getText ( ) );
                    int endMonth = Integer.parseInt ( endMonthField.getText ( ) );
                    int endYear = Integer.parseInt ( endYearField.getText ( ) );

                    // Checking their validity

                    if (endDay > 31 || endDay < 1 ||
                            startDay > 31 || startDay < 1 ||
                            endMonth > 12 || endMonth < 1 ||
                            startMonth > 12 || startMonth < 1) {
                        JOptionPane.showMessageDialog ( parent , "Invalid dates" , "Error" , JOptionPane.ERROR_MESSAGE );
                    } else {
                        // Creating the model


                        controller.setStartDate ( startYear , startMonth , startDay );

                        controller.setEndDate ( endYear , endMonth , endDay );

                        System.out.println ( "Simulation saved" );
                        System.out.println ( "Start date: " + controller.getStartDate ( ) );
                        System.out.println ( "Start date: " + controller.getEndDate ( ) );
                        System.out.println ( parent.getParamController ( ).getStartDate ( ).toString ( ) );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog ( parent , "Invalid dates" , "Error" , JOptionPane.ERROR_MESSAGE );
                }

            }

        } );

        setLayout ( new BorderLayout ( ) );
        add ( formPanel , BorderLayout.CENTER );


        JPanel buttonPanel = new JPanel ( );
        buttonPanel.add ( addButton );

        add ( buttonPanel , BorderLayout.SOUTH );
    }
}

