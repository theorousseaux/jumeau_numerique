package src.App.AnalysisTab;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AnalysisPannel extends JPanel{
    public AnalysisPannel() {
        // Création des boutons pour l'onglet Analysis
        JButton performanceButton = new JButton("Performance");
        JButton riskButton = new JButton("Risk");

        // Ajout des boutons au panneau Analysis
        this.add(performanceButton);
        this.add(riskButton);
    }
}
