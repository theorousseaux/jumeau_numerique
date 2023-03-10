package src.App.Observer;

import src.App.MainFrame;
import src.App.ObserverTab.DisplayNetworkPanel;
import src.App.WorldMapTab.WorldMapPanel;
import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import src.UseCase1_GSNetwork.GSNetwork;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateNetworkPanel extends JPanel {

    MainFrame parent;
    DisplayNetworkPanel displayNetworkPannel;

    private JList list;
    private DefaultListModel listModel;
    Set<String> selectedStations;

    public CreateNetworkPanel(MainFrame parent, DisplayNetworkPanel displayNetworkPannel, WorldMapPanel worldMapPanel) {

        this.parent = parent;
        this.displayNetworkPannel = displayNetworkPannel;
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        // Titre
        JLabel tittle = new JLabel("Choose ground stations to create the network");
        tittle.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        tittle.setForeground(Color.BLUE); // Définit la couleur du texte en bleu

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(tittle, gc);


        listModel = new DefaultListModel ();
        for (TelescopeAzEl tele : parent.obserController.telescopeAzElList) {
            listModel.addElement(tele.getID ());
        }
        list = new JList(listModel);
        list.setLayoutOrientation ( JList.VERTICAL);
        list.setSelectionMode ( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        JScrollPane scrollPane = new JScrollPane (list);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        gc.insets = new Insets ( 15, 10, 15, 10 );
        gc.anchor = GridBagConstraints.WEST;

        gc.gridy = 2;
        this.add(scrollPane,gc);

        JLabel label = new JLabel("Network name : ");
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.SOUTH;
        this.add(label, gc);

        JTextField textField = new JTextField(10);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add(textField, gc);


        JButton button = new JButton("Create network");
        gc.gridx = 1;
        gc.gridy =3;
        gc.anchor = GridBagConstraints.EAST;
        this.add(button, gc);

        // Si le boutton add est cliqué, on crée un réseau avec les stations sélectionnées
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedStations = new HashSet<> (  );
                for (Object name : list.getSelectedValues ()){
                    selectedStations.add(name.toString ());
                }

                try {
                    System.out.println ( selectedStations );
                    parent.gsController.gsNetwork = new GSNetwork(textField.getText(), new ArrayList<> ( selectedStations ));
                    displayNetworkPannel.update();
                    displayNetworkPannel.repaint();
                    displayNetworkPannel.revalidate();

                    worldMapPanel.displayNewGS(parent);
                    worldMapPanel.repaint();
                    worldMapPanel.revalidate();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void displayNewGS(Station groundStation) {
        /*
        GridBagConstraints newGC = new GridBagConstraints();
        newGC.gridx = 0;
        newGC.gridy = parent.gsController.numberOfGS+1;

        JCheckBox checkBox = new JCheckBox(groundStation.getName());
        newGC.gridy ++;
        newGC.anchor = GridBagConstraints.WEST;
        newGC.insets = new Insets(5, 0, 0, 0); // Ajoute 5 pixels de padding en bas
        this.add(checkBox, newGC);


         */

        String name = groundStation.getName ();

        listModel.addElement(name);

    }


}