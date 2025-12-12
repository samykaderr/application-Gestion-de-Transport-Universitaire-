package UI;

import DATA.ChauffeurDAO;
import DATA.EtudiantDAO;
import Model.Chauffeur;
import Model.CompteUtilisateur;
import Model.Etudiant;
import Service.CompteUtilisateurService;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private final JTextField userField;
    private final JPasswordField passField;
    private final CompteUtilisateurService compteUtilisateurService;

    public LoginFrame() {
        this.compteUtilisateurService = new CompteUtilisateurService();
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
        JLabel lblUser = new JLabel("Email");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));

        userField = new JTextField();
        userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: admin@example.com");
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

        // --- LOGIQUE DE CONNEXION ---
        loginButton.addActionListener(e -> performLogin());

        // Permet de se connecter en appuyant sur "Entrée" depuis le champ de mot de passe
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
    }

    private void performLogin() {
        String email = userField.getText();
        String password = new String(passField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CompteUtilisateur user = compteUtilisateurService.login(email, password);

        if (user != null) {
            switch (user.getRole()) {
                case "admin":
                    new AdminDashboard().setVisible(true);
                    dispose();
                    break;
                case "etudiant":
                    EtudiantDAO etudiantDAO = new EtudiantDAO();
                    Etudiant etudiant = etudiantDAO.getEtudiantByAccountId(user.getId());
                    if (etudiant != null) {
                        // Si l'email de l'étudiant est null, utiliser l'email du compte
                        if (etudiant.getEmail() == null || etudiant.getEmail().isEmpty()) {
                            etudiant.setEmail(user.getEmail());
                        }
                        new StudentDashboard(etudiant).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Profil étudiant non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "chauffeur":
                    ChauffeurDAO chauffeurDAO = new ChauffeurDAO();
                    Chauffeur chauffeur = chauffeurDAO.getChauffeurByAccountId(user.getId());
                    if (chauffeur != null) {
                        new ChauffeurDashboard(chauffeur).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Profil chauffeur non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Rôle non reconnu.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Appliquer le look and feel FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}