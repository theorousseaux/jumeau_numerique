package App;

//Usually you will require both swing and awt packages
//even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
class gui {
 public static void main(String args[]) {

     //Creating the Frame
     JFrame frame = new JFrame("Space Observation Digital Twin");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(400, 400);

     //Creating the MenuBar and adding components
     JMenuBar mb = new JMenuBar();
     JMenu m1 = new JMenu("Population");
     JMenu m2 = new JMenu("Ground Station");
     mb.add(m1);
     mb.add(m2);
     JMenu m11 = new JMenu("Load");
     JMenuItem m12 = new JMenuItem("Add");
     JMenuItem m13 = new JMenuItem("Display");
     m1.add(m11);
     m1.add(m12);
     m1.add(m13);
     JMenuItem m21 = new JMenuItem("Load");
     JMenuItem m22 = new JMenuItem("Add");
     JMenuItem m23 = new JMenuItem("Display");
     m2.add(m21);
     m2.add(m22);
     m2.add(m23);
     JMenuItem m111 = new JMenuItem("From file");
     JMenuItem m112 = new JMenuItem("From online data");
     m11.add(m111);
     m11.add(m112);

    

     // Text Area at the Center
     JTextArea ta = new JTextArea();

     //Adding Components to the frame.
     frame.getContentPane().add(BorderLayout.NORTH, mb);
     frame.getContentPane().add(BorderLayout.CENTER, ta);
     frame.setVisible(true);
 }
}