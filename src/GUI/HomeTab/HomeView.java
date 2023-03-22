package src.GUI.HomeTab;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {
    public HomeView ( ) {

        // Title of the application
        JLabel title = new JLabel ( "Space Observation Digital Twin" );
        title.setFont ( new java.awt.Font ( "Arial" , Font.BOLD , 20 ) );
        title.setHorizontalAlignment ( SwingConstants.CENTER );
        title.setVerticalAlignment ( SwingConstants.CENTER );
        title.setForeground ( Color.BLUE );
        title.setBounds ( 0 , 0 , 800 , 50 );

        // How to use the application
        JLabel text2 = new JLabel ( "How to use the application" );
        text2.setFont ( new java.awt.Font ( "Arial" , Font.BOLD , 15 ) );
        text2.setHorizontalAlignment ( SwingConstants.CENTER );
        text2.setVerticalAlignment ( SwingConstants.CENTER );
        text2.setBounds ( 0 , 50 , 800 , 50 );

        JLabel text3 = new JLabel ( "1. Update the satellite database" );
        text3.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text3.setHorizontalAlignment ( SwingConstants.CENTER );
        text3.setVerticalAlignment ( SwingConstants.CENTER );
        text3.setBounds ( 0 , 100 , 800 , 50 );

        JLabel text4 = new JLabel ( "2. Add a new ground station" );
        text4.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text4.setHorizontalAlignment ( SwingConstants.CENTER );
        text4.setVerticalAlignment ( SwingConstants.CENTER );
        text4.setBounds ( 0 , 150 , 800 , 50 );

        JLabel text5 = new JLabel ( "3. Add a new observer to a ground station, and create a network of observers" );
        text5.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text5.setHorizontalAlignment ( SwingConstants.CENTER );
        text5.setVerticalAlignment ( SwingConstants.CENTER );
        text5.setBounds ( 0 , 200 , 800 , 50 );

        JLabel text6 = new JLabel ( "4. Select the parameters of the simulation" );
        text6.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text6.setHorizontalAlignment ( SwingConstants.CENTER );
        text6.setVerticalAlignment ( SwingConstants.CENTER );
        text6.setBounds ( 0 , 250 , 800 , 50 );

        JLabel text7 = new JLabel ( "5. Select satellites to observe" );
        text7.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text7.setHorizontalAlignment ( SwingConstants.CENTER );
        text7.setVerticalAlignment ( SwingConstants.CENTER );
        text7.setBounds ( 0 , 300 , 800 , 50 );

        JLabel text8 = new JLabel ( "6. Run the simulation" );
        text8.setFont ( new java.awt.Font ( "Arial" , 1 , 15 ) );
        text8.setHorizontalAlignment ( SwingConstants.CENTER );
        text8.setVerticalAlignment ( SwingConstants.CENTER );
        text8.setBounds ( 0 , 350 , 800 , 50 );

        this.setLayout ( null );
        this.add ( title );
        this.add ( text2 );
        this.add ( text3 );
        this.add ( text4 );
        this.add ( text5 );
        this.add ( text6 );
        this.add ( text7 );
        this.add ( text8 );

    }
}
