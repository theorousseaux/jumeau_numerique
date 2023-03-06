package src.App.ObserverTab;

import src.App.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewObserverPannel extends JPanel {

    private JComboBox<String> cbType;
    private JPanel formPanel;

    public NewObserverPannel(MainFrame parent) {

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;

        // Titre
        JLabel addGSLabel = new JLabel("Add a new observer");
        addGSLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Définit la police d'écriture en Arial, en gras et en taille 18
        addGSLabel.setForeground(Color.BLUE); // Définit la couleur du texte en bleu
        add(addGSLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(10, 0, 0, 0);
        JLabel typeLabel = new JLabel("Type: ");
        add(typeLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(10, 0, 0, 0);
        String[] typeGS ={"Telescope", "Radar"};
        cbType = new JComboBox<>(typeGS);
        cbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFormPanel();
            }
        });
        add(cbType, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(10, 0, 0, 0);

        formPanel = new JPanel();
        updateFormPanel();
        add(formPanel, gc);
    }

    private void updateFormPanel() {
        String selectedType = (String) cbType.getSelectedItem();
        assert selectedType != null;
        if (selectedType.equals("Telescope")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(2, 2));

            JLabel telescopeLabel = new JLabel("Telescope:");
            JTextField telescopeField = new JTextField();
            JLabel apertureLabel = new JLabel("Aperture:");
            JTextField apertureField = new JTextField();

            formPanel.add(telescopeLabel);
            formPanel.add(telescopeField);
            formPanel.add(apertureLabel);
            formPanel.add(apertureField);

            formPanel.revalidate();
            formPanel.repaint();
        } else if (selectedType.equals("Radar")) {
            formPanel.removeAll();
            formPanel.setLayout(new GridLayout(2, 2));

            JLabel radarLabel = new JLabel("Radar:");
            JTextField radarField = new JTextField();
            JLabel frequencyLabel = new JLabel("Frequency:");
            JTextField frequencyField = new JTextField();

            formPanel.add(radarLabel);
            formPanel.add(radarField);
            formPanel.add(frequencyLabel);
            formPanel.add(frequencyField);

            formPanel.revalidate();
            formPanel.repaint();
        }
    }
}
