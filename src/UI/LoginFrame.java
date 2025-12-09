package UI;

import Model.Admin;
import Model.Chauffeur;
import Model.Etudiant;
import Service.UserService;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private final JTextField userField;
    private final JPasswordField passField;

    public LoginFrame() {
        setTitle("Connexion - Transport Scolaire");
        setSize(400, 450); // Un peu plus haut pour l'aération
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panneau principal avec un padding (marge intérieure)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Les composants prennent toute la largeur
        gbc.insets = new Insets(5, 0, 10, 0); // Espacement vertical
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. TITRE
        JLabel titleLabel = new JLabel("Bienvenue", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Connectez-vous à votre compte", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        // 2. CHAMP UTILISATEUR
        JLabel lblUser = new JLabel("Nom d'utilisateur");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));

        userField = new JTextField();
        userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: admin ou matricule");
        userField.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10"); // Arrondi et marge interne
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 3. CHAMP MOT DE PASSE
        JLabel lblPass = new JLabel("Mot de passe");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));

        passField = new JPasswordField();
        passField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Entrez votre mot de passe");
        passField.putClientProperty(FlatClientProperties.STYLE, "arc: 10; showRevealButton: true; margin: 5,10,5,10"); // showRevealButton ajoute l'icône "œil"
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 4. BOUTON DE CONNEXION
        JButton loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(46, 204, 113)); // Le vert de votre AdminDashboard
        loginButton.setForeground(Color.WHITE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15; borderWidth: 0"); // Bouton bien arrondi

        // --- AJOUT DES COMPOSANTS AU PANEL ---

        // Titres (plus d'espace en bas)
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(subtitleLabel, gbc);

        // Utilisateur
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblUser, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(userField, gbc);

        // Mot de passe
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblPass, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 25, 0);
        panel.add(passField, gbc);

        // Bouton (plus haut)
        gbc.gridy = 6; gbc.ipady = 10; // Hauteur interne du bouton
        panel.add(loginButton, gbc);

        add(panel);

        // --- LOGIQUE ---

        // Permettre de valider avec la touche "Entrée"
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(_ -> performLogin());
    }

    private void performLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UserService userService = new UserService();
            Object user = userService.login(username, password);

            if (user != null) {
                if (user instanceof Admin) {
                    new AdminDashboard().setVisible(true);
                    dispose();
                } else if (user instanceof Etudiant) {
                    new StudentDashboard((Etudiant) user).setVisible(true);
                    dispose();
                } else if (user instanceof Chauffeur) {
                    new ChauffeurDashboard(((Chauffeur) user).getId()).setVisible(true);
                    dispose();
                }
            } else {
                // Animation simple : secouer le champ mot de passe (optionnel, juste change la couleur)
                passField.putClientProperty("JComponent.outline", "error");
                JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion base de données: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Configuration FlatLaf
        try {
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 10);
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Erreur FlatLaf");
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}