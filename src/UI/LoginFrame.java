package UI;

import Model.*;
import DATA.*;
import Service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField userField;
    private final JPasswordField passField;

    public LoginFrame() {
        setTitle("Connexion - Gestion Transport Scolaire");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Titre
        JLabel titleLabel = new JLabel("Authentification");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Champ Utilisateur
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Utilisateur:"), gbc);

        gbc.gridx = 1;
        userField = new JTextField(15);
        panel.add(userField, gbc);

        // Champ Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        passField = new JPasswordField(15);
        panel.add(passField, gbc);

        // Bouton de connexion
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton, gbc);

        add(panel);

        loginButton.addActionListener(_ -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            UserService userService = new UserService();
            Object user = userService.login(username, password);

            if (user != null) {
                if (user instanceof Admin) {
                    // Ouvrir le dashboard admin
                    SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
                    dispose(); // Fermer la fenêtre de login
                } else if (user instanceof Etudiant) {
                    // Ouvrir le dashboard étudiant
                    SwingUtilities.invokeLater(() -> new StudentDashboard((Etudiant) user).setVisible(true));
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Nom d'utilisateur ou mot de passe incorrect.",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

}
