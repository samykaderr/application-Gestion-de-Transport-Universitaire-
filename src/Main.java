import com.formdev.flatlaf.FlatLightLaf;
import UI.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;
import javax.swing.*;
import UI.*;
public class Main {
    public static void main(String[] args) {

            // Pour l'exemple, on lance directement l'AdminUI
            // Dans le projet complet, vous mettriez ici la fenêtre de Login
        
            SwingUtilities.invokeLater(() -> {
                try {
                    // Configuration du Look and Feel FlatLaf
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                    // En cas d'échec, retour au look and feel par défaut
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                new LoginFrame().setVisible(true);
            });

    }
}