package src.App.EstimationTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EstimationPanel extends JPanel {

    public EstimationPanel ( MainFrame parent ) {


        JTextField stdPosField = new JTextField ( 4 );
        JTextField stdVField = new JTextField ( 2 );

        JTextField noisePosField = new JTextField ( 4 );
        JTextField noiseVField = new JTextField ( 2 );

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


        // Adding start date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "Uncertainty on position knowledge: " ) );

        gc.gridx++;
        formPanel.add ( stdPosField );

        // Adding end date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "Uncertainty on velocity knowledge: " ) );

        gc.gridx++;
        formPanel.add ( stdVField );

        // Adding start date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "Kalman position noise: " ) );

        gc.gridx++;
        formPanel.add ( noisePosField );

        // Adding end date
        gc.gridx = 0;
        gc.gridy++;
        formPanel.add ( new JLabel ( "Kalman velocity noise: " ) );

        gc.gridx++;
        formPanel.add ( noiseVField );

        add ( formPanel );

        GridBagConstraints GC = new GridBagConstraints ( );

        // Button to run simulation
        JButton runEstimationButton = new JButton ( "Run Estimation" );

        GC.gridx = 0;
        GC.gridy = 0;

        GC.gridx = 0;
        GC.gridy = 1;
        add ( runEstimationButton , GC );

        DisplayEstPanel disp = new DisplayEstPanel ( parent );

        GC.gridx = 0;
        GC.gridy ++;
        add(new JLabel ( "Estimated orbital parameters" ));
        GC.gridy ++;

        add ( disp , GC );

        DisplayTruePanel truePanel = new DisplayTruePanel ( parent );

        GC.gridx = 0;

        GC.gridy ++;
        add(new JLabel ( "True orbital parameters" ));
        GC.gridy ++;
        add(truePanel,GC);

        // Event manager
        runEstimationButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                //try {
                EstimationController controller = parent.getEstimationController ( );
                controller.model.setStdV ( Double.parseDouble ( stdVField.getText ( ) ) );
                controller.model.setStdPos ( Double.parseDouble ( stdPosField.getText ( ) ) );
                controller.model.setNoiseLevelV ( Double.parseDouble ( noiseVField.getText ( ) ) );
                controller.model.setNoiseLevelPos ( Double.parseDouble ( noisePosField.getText ( ) ) );
                try {
                    controller.loadEstimation ( parent );
                } catch (IOException ex) {
                    throw new RuntimeException ( ex );
                }

                try {
                    controller.runEstimation ( parent );
                } catch (IOException ex) {
                    System.out.println ( ex );
                    throw new RuntimeException ( ex );
                }

                disp.update ( );
                disp.repaint ( );
                disp.revalidate ( );

                truePanel.update ( );
                truePanel.repaint ( );
                truePanel.revalidate ( );
                    /*
                }catch (NullPointerException | IOException npe) {
                    JOptionPane.showMessageDialog(parent, "Run your simulation first", "Error",JOptionPane.ERROR_MESSAGE);
                }

                     */
            }
        } );
    }
}
