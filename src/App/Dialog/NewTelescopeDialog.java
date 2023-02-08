package src.App.Dialog;

import javax.swing.*;
import java.awt.*;

public class NewTelescopeDialog extends JDialog {
    public NewTelescopeDialog(JFrame parent) {
        super(parent, "Add a Telescope", true);



        // Ajout du formulaire et des boutons à la fenêtre de dialogue
        setLayout(new BorderLayout());

        // Configuration de la fenêtre de dialogue
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
}
