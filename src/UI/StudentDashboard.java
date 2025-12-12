package UI;

import Model.Etudiant;
import Model.Trajet;
import Service.EtudiantService;
import Service.EtudiantTrajetService;
import Service.TrajetService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboard extends JFrame {

    // --- COULEURS & STYLE (Même charte que Admin) ---
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);        // Bleu Nuit
    private final Color SIDEBAR_TEXT_COLOR = new Color(236, 240, 241);// Blanc Cassé
    private final Color ACTIVE_BUTTON_COLOR = new Color(52, 73, 94);  // Gris Bleu (Sélection)
    private final Color NEON_BLUE = new Color(51, 204, 255);          // Bleu Néon (Accent)
    private final Color BACKGROUND_COLOR = new Color(248, 250, 252);  // Gris très clair fond
    private final Color CARD_HOVER_COLOR = new Color(225, 240, 255);  // Survol

    // Couleurs Spécifiques Étudiant
    private final Color STATUS_OK = new Color(46, 204, 113);      // Vert
    private final Color STATUS_WARN = new Color(231, 76, 60);     // Rouge

    // Services & Data
    private final Etudiant etudiant;
    private final TrajetService trajetService;
    private final EtudiantTrajetService etudiantTrajetService;


    // Composants UI
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel trajetsResultsContainer;
    private JButton trajetsSearchButton;

    public StudentDashboard(Etudiant etudiant) {
        this.etudiant = etudiant;
        this.trajetService = new TrajetService();
        this.etudiantTrajetService = new EtudiantTrajetService();

        initUI();
    }

    private void initUI() {
        setTitle("Espace Étudiant - " + etudiant.getPrenom() + " " + etudiant.getNom());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // 2. Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Ajout des vues
        contentPanel.add(createHomePanel(), "HOME");
        contentPanel.add(createTrajetsPanel(), "TRAJETS");
        contentPanel.add(createAbonnementPanel(), "ABONNEMENT");

        add(contentPanel, BorderLayout.CENTER);
    }

    // ====================================================================================
    //                                  SIDEBAR (Style Unifié)
    // ====================================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 0, 8));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Info Étudiant (En haut du menu)
        JLabel titleLabel = new JLabel("ESPACE ÉTUDIANT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);

        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setBackground(new Color(255, 255, 255, 50));
        sep.setForeground(new Color(255, 255, 255, 50));
        sidebar.add(sep);

        // Menu Items
        sidebar.add(createMenuButton("Mon Profil", "HOME", "/resources/id-card.png"));
        sidebar.add(createMenuButton("Rechercher Trajet", "TRAJETS", "/resources/school-bus.png"));
        sidebar.add(createMenuButton("Mon Abonnement", "ABONNEMENT", "/resources/subscription-model.png"));

        // Espace vide
        sidebar.add(Box.createGlue());

        // Bouton Déconnexion (Optionnel)
        JButton btnLogout = createMenuButton("Déconnexion", "", null);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Ferme la fenêtre actuelle
                new LoginFrame().setVisible(true); // Ouvre la fenêtre de login
            }
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private JButton createMenuButton(String text, String cardName, String iconPath) {
        JButton btn = new JButton(text);
        if (iconPath != null) {
            ImageIcon icon = loadIcon(iconPath, 20, 20);
            if (icon != null) {
                btn.setIcon(icon);
            }
        }
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setIconTextGap(10);
        btn.putClientProperty("JButton.buttonType", "roundRect");

        if(!cardName.isEmpty()) {
            btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ACTIVE_BUTTON_COLOR);
                btn.setForeground(NEON_BLUE);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_COLOR);
                btn.setForeground(SIDEBAR_TEXT_COLOR);
            }
        });
        return btn;
    }

    // ====================================================================================
    //                                  PANELS (Vues)
    // ====================================================================================

    // --- 1. ACCUEIL / PROFIL ---
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Carte Profil Centrale
        RoundedPanel profileCard = new RoundedPanel(25, Color.WHITE);
        profileCard.setLayout(new BorderLayout(20, 20));
        profileCard.setPreferredSize(new Dimension(500, 300));
        profileCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        // En-tête (Nom + Prénom)
        JLabel lblName = new JLabel(etudiant.getPrenom().toUpperCase() + " " + etudiant.getNom().toUpperCase(), SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblName.setForeground(SIDEBAR_COLOR);

        // Détails avec icônes
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        detailsPanel.setOpaque(false);

        // Email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        emailPanel.setOpaque(false);
        ImageIcon emailIcon = loadIcon("/resources/email.png", 20, 20);
        emailPanel.add(new JLabel(emailIcon));
        JLabel lblEmailText = new JLabel(etudiant.getEmail());
        lblEmailText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailPanel.add(lblEmailText);

        // Numéro de carte
        JPanel cartePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cartePanel.setOpaque(false);
        ImageIcon idIcon = loadIcon("/resources/id.png", 20, 20);
        cartePanel.add(new JLabel(idIcon));
        JLabel lblCarteText = new JLabel("Carte N°: " + etudiant.getNumCarte());
        lblCarteText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cartePanel.add(lblCarteText);

        // Arrêt principal
        JPanel arretPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        arretPanel.setOpaque(false);
        ImageIcon arretIcon = loadIcon("/resources/placeholder.png", 20, 20);
        arretPanel.add(new JLabel(arretIcon));
        JLabel lblArretText = new JLabel("Arrêt Principal: " + etudiant.getArretPrincipal());
        lblArretText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        arretPanel.add(lblArretText);

        detailsPanel.add(emailPanel);
        detailsPanel.add(cartePanel);
        detailsPanel.add(arretPanel);

        // Statut de paiement (Bas de carte)
        boolean isPaid = "Payé".equalsIgnoreCase(etudiant.getStatutPaiement());
        JLabel lblStatus = new JLabel(isPaid ? "ABONNEMENT ACTIF" : "ABONNEMENT INACTIF", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(isPaid ? STATUS_OK : STATUS_WARN);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(new EmptyBorder(10, 0, 10, 0));

        profileCard.add(lblName, BorderLayout.NORTH);
        profileCard.add(detailsPanel, BorderLayout.CENTER);
        profileCard.add(lblStatus, BorderLayout.SOUTH);

        // Animation sur la carte
        addSmoothHoverEffect(profileCard, Color.WHITE, new Color(250, 250, 255));

        panel.add(profileCard);
        return panel;
    }

    // --- 2. RECHERCHE DE TRAJETS ---
    private JPanel createTrajetsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Barre de Recherche (Haut)
        JPanel searchBar = new RoundedPanel(20, Color.WHITE);
        searchBar.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JTextField txtDepart = new JTextField(12);
        JTextField txtArrivee = new JTextField(12);
        // Placeholder style
        txtDepart.putClientProperty("JTextField.placeholderText", "Départ...");
        txtArrivee.putClientProperty("JTextField.placeholderText", "Arrivée...");

        trajetsSearchButton = new JButton("Trouver un bus");
        trajetsSearchButton.setBackground(SIDEBAR_COLOR);
        trajetsSearchButton.setForeground(Color.WHITE);
        trajetsSearchButton.putClientProperty("JButton.buttonType", "roundRect");

        searchBar.add(new JLabel("De:")); searchBar.add(txtDepart);
        searchBar.add(new JLabel("À:")); searchBar.add(txtArrivee);
        searchBar.add(trajetsSearchButton);

        // Conteneur Résultats
        trajetsResultsContainer = new JPanel();
        trajetsResultsContainer.setLayout(new BoxLayout(trajetsResultsContainer, BoxLayout.Y_AXIS));
        trajetsResultsContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(trajetsResultsContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Action Recherche
        trajetsSearchButton.addActionListener(e -> {
            trajetsResultsContainer.removeAll();
            String dep = txtDepart.getText().trim().toLowerCase();
            String arr = txtArrivee.getText().trim().toLowerCase();

            List<Trajet> all = trajetService.getAllTrajets();

            // Filtrage (Simple contains pour l'exemple)
            List<Trajet> results = all.stream()
                    .filter(t -> (dep.isEmpty() || t.getPointDepart().toLowerCase().contains(dep)) &&
                            (arr.isEmpty() || t.getPointArrivee().toLowerCase().contains(arr)))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                JLabel empty = new JLabel("Aucun trajet trouvé.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                trajetsResultsContainer.add(empty);
            } else {
                for (Trajet t : results) {
                    JPanel card = createTrajetCardWithButton(t);
                    trajetsResultsContainer.add(card);
                    trajetsResultsContainer.add(Box.createVerticalStrut(10));
                }
            }
            trajetsResultsContainer.revalidate();
            trajetsResultsContainer.repaint();
        });

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Charger tous les trajets au démarrage (optionnel)
        trajetsSearchButton.doClick();

        return panel;
    }

    /**
     * Rafraîchit le panel des trajets après une inscription
     */
    private void refreshTrajetsPanel() {
        if (trajetsSearchButton != null) {
            trajetsSearchButton.doClick();
        }
    }

    private JPanel createTrajetCardWithButton(Trajet trajet) {
        Color cardColor = new Color(240, 248, 255);
        JPanel row = new RoundedPanel(15, cardColor);
        row.setLayout(new BorderLayout(10, 10));
        row.setBorder(new EmptyBorder(15, 15, 15, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90)); // Increased height for button

        String busInfo = (trajet.getBus() != null) ? "Bus " + trajet.getBus().getMatricule() : "Bus non assigné";
        String html = String.format("<html><div style='width:300px'><b>%s ➝ %s</b><br/>" +
                        "<span style='color:#555'>Départ: %s</span><br/>" +
                        "<span style='color:#2980b9'>%s</span></div></html>",
                trajet.getPointDepart(), trajet.getPointArrivee(), trajet.getHeureDepart(), busInfo);

        JLabel info = new JLabel(html);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        row.add(info, BorderLayout.CENTER);

        // Vérifier si l'étudiant est déjà inscrit à un trajet
        int trajetInscrit = etudiantTrajetService.getTrajetIdPourEtudiant(etudiant.getId());
        boolean dejaInscritACeTrajet = (trajetInscrit == trajet.getId());
        boolean dejaInscritAutreTrajet = (trajetInscrit > 0 && trajetInscrit != trajet.getId());

        JButton btnInscrire = new JButton();

        if (dejaInscritACeTrajet) {
            // L'étudiant est inscrit à CE trajet
            btnInscrire.setText("✓ Inscrit");
            btnInscrire.setBackground(new Color(52, 152, 219)); // Bleu
            btnInscrire.setForeground(Color.WHITE);
            btnInscrire.setEnabled(false);
        } else if (dejaInscritAutreTrajet) {
            // L'étudiant est inscrit à un AUTRE trajet
            btnInscrire.setText("S'inscrire");
            btnInscrire.setBackground(new Color(189, 195, 199)); // Gris
            btnInscrire.setForeground(Color.WHITE);
            btnInscrire.setEnabled(false);
            btnInscrire.setToolTipText("Vous êtes déjà inscrit à un autre trajet");
        } else {
            // L'étudiant n'est inscrit à aucun trajet
            btnInscrire.setText("S'inscrire");
            btnInscrire.setBackground(STATUS_OK);
            btnInscrire.setForeground(Color.WHITE);
            btnInscrire.setEnabled(true);
        }

        btnInscrire.putClientProperty("JButton.buttonType", "roundRect");
        btnInscrire.addActionListener(e -> {
            // Revérifier avant inscription (au cas où)
            if (etudiantTrajetService.etudiantDejaInscrit(etudiant.getId())) {
                JOptionPane.showMessageDialog(this,
                    "Vous êtes déjà inscrit à un trajet.\nUn seul trajet par étudiant est autorisé pour garantir une place.",
                    "Inscription impossible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = etudiantTrajetService.inscrireEtudiantAuTrajet(etudiant.getId(), trajet.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Inscription au trajet réussie !\nVotre place est garantie.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                // Rafraîchir la vue des trajets
                cardLayout.show(contentPanel, "TRAJETS");
                refreshTrajetsPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription au trajet.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnInscrire, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);


        // Animation
        addSmoothHoverEffect(row, cardColor, CARD_HOVER_COLOR);
        return row;
    }

    // --- 3. ABONNEMENT (HISTORIQUE) ---
    private JPanel createAbonnementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Historique des paiements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BACKGROUND_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Simulation de données (À remplacer par etudiantTrajetService.getHistorique...)
        // Comme le modèle exact d'historique n'est pas fourni, je simule des entrées basées sur l'étudiant

        if ("Payé".equalsIgnoreCase(etudiant.getStatutPaiement())) {
            // Carte Paiement Récent
            String html = "<html><b>Abonnement Annuel (En cours)</b><br/>Date: 01/09/2024 - 31/08/2025<br/>Montant: 1500 DA</html>";
            JPanel card = createInfoCard(html, new Color(230, 255, 230)); // Vert très clair
            listContainer.add(card);

            // Anciens
            listContainer.add(Box.createVerticalStrut(10));
            listContainer.add(createInfoCard("<html><b>Abonnement Annuel</b><br/>Date: 01/09/2023 - 31/08/2024<br/>Montant: 1500 DA</html>", Color.WHITE));
        } else {
            JLabel empty = new JLabel("Aucun historique de paiement actif.", SwingConstants.CENTER);
            listContainer.add(empty);
        }

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ====================================================================================
    //                        OUTILS GRAPHIQUES
    // ====================================================================================

    // Crée une carte d'information simple sans boutons d'action (Pour liste lecture seule)
    private JPanel createInfoCard(String htmlContent, Color bgColor) {
        JPanel row = new RoundedPanel(15, bgColor);
        row.setLayout(new BorderLayout(10, 10));
        row.setBorder(new EmptyBorder(15, 15, 15, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel info = new JLabel(htmlContent);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        row.add(info, BorderLayout.CENTER);

        // Animation
        addSmoothHoverEffect(row, bgColor, CARD_HOVER_COLOR);
        return row;
    }

    // Animation Smooth Hover
    private void addSmoothHoverEffect(JPanel panel, Color defaultColor, Color hoverColor) {
        panel.addMouseListener(new MouseAdapter() {
            private Timer timer;
            private final int DURATION = 200;
            @Override
            public void mouseEntered(MouseEvent e) { animate(panel, hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { animate(panel, defaultColor); }

            private void animate(JComponent c, Color target) {
                if (timer != null && timer.isRunning()) timer.stop();
                Color start = c.getBackground();
                long startTime = System.currentTimeMillis();
                timer = new Timer(10, evt -> {
                    float progress = Math.min(1f, (System.currentTimeMillis() - startTime) / (float) DURATION);
                    int r = (int) (start.getRed() + (target.getRed() - start.getRed()) * progress);
                    int g = (int) (start.getGreen() + (target.getGreen() - start.getGreen()) * progress);
                    int b = (int) (start.getBlue() + (target.getBlue() - start.getBlue()) * progress);
                    c.setBackground(new Color(r, g, b));
                    c.repaint();
                    if (progress >= 1f) ((Timer) evt.getSource()).stop();
                });
                timer.start();
            }
        });
    }

    // Classe Panel Arrondi
    class RoundedPanel extends JPanel {
        private int r;
        private Color bg;
        public RoundedPanel(int r, Color bg) { this.r = r; this.bg = bg; setOpaque(false); }
        @Override public void setBackground(Color c) { this.bg = c; super.setBackground(c); repaint(); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.setColor(new Color(230, 230, 230)); // Bordure fine
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, r, r);
        }
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception ex) { System.err.println("FlatLaf init error"); }

        // On récupère un étudiant depuis la base de données
        EtudiantService etudiantService = new EtudiantService();
        // Note: Assurez-vous qu'un étudiant avec l'ID 1 existe dans votre base de données.
        Etudiant etudiant = etudiantService.getEtudiantById(10);

        if (etudiant == null) {
            JOptionPane.showMessageDialog(null, "Aucun étudiant trouvé avec l'ID 1. Veuillez vérifier la base de données.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final Etudiant finalEtudiant = etudiant;
        SwingUtilities.invokeLater(() -> {
            new StudentDashboard(finalEtudiant).setVisible(true);
        });
    }
}
