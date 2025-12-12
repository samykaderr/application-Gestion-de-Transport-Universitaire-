package UI;

import Model.Bus;
import Model.Chauffeur;
import Model.Trajet;
import Model.Incident;
import Service.TrajetService;
import Service.IncidentService;
import Service.ChauffeurService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ChauffeurDashboard extends JFrame {

    // --- THÈME COULEURS ---
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color SIDEBAR_TEXT_COLOR = new Color(236, 240, 241);
    private final Color NEON_BLUE = new Color(51, 204, 255);
    private final Color ACTIVE_BUTTON_COLOR = new Color(52, 73, 94);

    // Composants principaux
    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Chauffeur chauffeur;
    private final TrajetService trajetService;
    private final IncidentService incidentService;

    public ChauffeurDashboard(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
        if (this.chauffeur == null) {
            throw new IllegalArgumentException("Chauffeur ne peut pas être nul");
        }
        this.trajetService = new TrajetService();
        this.incidentService = new IncidentService();

        setTitle("Espace Chauffeur - Transport Scolaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barre Latérale
        add(createSidebar(), BorderLayout.WEST);

        // 2. En-tête (Header)
        add(createHeader(), BorderLayout.NORTH);

        // 3. Contenu Central
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Color.WHITE);

        // Ajout des Vues
        mainContentPanel.add(createProfilePanel(), "PROFIL");
        mainContentPanel.add(createPlanningPanel(), "PLANNING");
        mainContentPanel.add(createBusInfoPanel(), "BUS");
        mainContentPanel.add(createIncidentPanel(), "INCIDENT");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    // --- BARRE LATÉRALE ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 0, 5));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Titre
        JLabel titleLabel = new JLabel("ESPACE CHAUFFEUR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);
        sidebar.add(new JSeparator());

        // Boutons de navigation
        sidebar.add(createMenuButton("Mon Profil", "PROFIL", "/resources/id-card.png"));
        sidebar.add(createMenuButton("Mon Planning", "PLANNING", "/resources/itinerary.png"));
        sidebar.add(createMenuButton("Signaler un Incident", "INCIDENT", "/resources/bus.png"));

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
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);

        // Action au clic
        btn.addActionListener(_ -> cardLayout.show(mainContentPanel, cardName));

        // Effet de survol
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACTIVE_BUTTON_COLOR);
                btn.setForeground(NEON_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
                btn.setForeground(SIDEBAR_TEXT_COLOR);
            }
        });

        return btn;
    }

    // --- EN-TÊTE ---
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + chauffeur.getPrenom() + " " + chauffeur.getNom());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.putClientProperty("JButton.buttonType", "roundRect");
        logoutBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    // --- VUE 1 : PROFIL CHAUFFEUR ---
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(NEON_BLUE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setOpaque(false);

        // Icône chauffeur
        ImageIcon chauffeurIcon = loadIcon("/resources/chauffeur.png", 80, 80);
        JLabel icon = new JLabel(chauffeurIcon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Fiche Chauffeur");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Données du modèle Chauffeur
        card.add(icon);
        card.add(Box.createVerticalStrut(15));
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("Prénom : " + chauffeur.getPrenom()));
        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("Nom : " + chauffeur.getNom()));
        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("Email : " + chauffeur.getEmail()));

        panel.add(card);
        return panel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // --- VUE 2 : PLANNING (MODIFIÉE EN LISTE DE CARTES) ---
    private JPanel createPlanningPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("  Mes Trajets Assignés");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(15, 10, 15, 10));

        // Container vertical pour les cartes de trajet
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Chargement des données
        List<Trajet> allTrajets = trajetService.getAllTrajets();
        boolean hasTrajet = false;

        for (Trajet t : allTrajets) {
            if (t.getChauffeur() != null && t.getChauffeur().getId() == this.chauffeur.getId()) {
                hasTrajet = true;

                // Création de la carte (JPanel) pour UN trajet
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                        new EmptyBorder(0, 0, 10, 0), // Espace entre les cartes
                        BorderFactory.createLineBorder(new Color(220, 220, 220))
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                // Info Gauche (Heure)
                JLabel timeLabel = new JLabel(" " + t.getHeureDepart().toString() + " ");
                timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                timeLabel.setForeground(new Color(41, 128, 185));
                card.add(timeLabel, BorderLayout.WEST);
                // Info Centre (Route)
                String routeInfo = String.format("<html><div style='padding:5px'><b>Départ :</b> %s<br/><b>Arrivée :</b> %s</div></html>",
                        t.getPointDepart(), t.getPointArrivee());
                JLabel routeLabel = new JLabel(routeInfo);
                routeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                card.add(routeLabel, BorderLayout.CENTER);

                // Info Droite (Bus)
                String busInfo = (t.getBus() != null) ? t.getBus().getMatricule() : "---";
                JLabel busLabel = new JLabel("<html><div style='text-align:right; padding-right:10px'>Bus<br/><b>" + busInfo + "</b></div></html>");
                card.add(busLabel, BorderLayout.EAST);

                cardsContainer.add(card);
            }
        }

        if (!hasTrajet) {
            JLabel emptyLabel = new JLabel("Aucun trajet prévu pour le moment.", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }


    // --- VUE 3 : INFO VÉHICULE ---
    private JPanel createBusInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Logique simple : on prend le premier bus trouvé dans les trajets du jour
        // Dans une vraie app, on pourrait avoir une table "AffectationBus"
        Bus assignedBus = null;
        for(Trajet t : trajetService.getAllTrajets()) {
            if(t.getChauffeur() != null && t.getChauffeur().getId() == chauffeur.getId() && t.getBus() != null) {
                assignedBus = t.getBus();
                break; // On prend le premier trouvé pour l'exemple
            }
        }

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel icon = new JLabel("🚌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setForeground(NEON_BLUE);

        card.add(icon);
        card.add(Box.createVerticalStrut(20));

        if (assignedBus != null) {
            card.add(createInfoLabel("Marque : " + assignedBus.getMarque()));
            card.add(Box.createVerticalStrut(10));
            card.add(createInfoLabel("Matricule : " + assignedBus.getMatricule()));
            card.add(Box.createVerticalStrut(10));
            card.add(createInfoLabel("Capacité : " + assignedBus.getCapacite() + " places"));

            JLabel status = new JLabel("ÉTAT : EN SERVICE");
            status.setFont(new Font("Segoe UI", Font.BOLD, 14));
            status.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(Box.createVerticalStrut(20));
            card.add(status);
        } else {
            JLabel noBus = new JLabel("Aucun bus assigné pour le moment.");
            noBus.setForeground(Color.RED);
            card.add(noBus);
        }

        centerPanel.add(card);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    // --- VUE 4 : SIGNALER UN INCIDENT ---
    private JPanel createIncidentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("Signaler un Incident sur un Véhicule");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Bus (non éditable, auto-rempli)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Bus Concerné:"), gbc);

        Bus assignedBus = getAssignedBus();
        JTextField busField = new JTextField(assignedBus != null ? assignedBus.getMarque() + " - " + assignedBus.getMatricule() : "Aucun bus assigné");
        busField.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(busField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description de l'incident:"), gbc);

        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Bouton de soumission
        JButton submitButton = new JButton("Envoyer le Rapport");
        submitButton.putClientProperty("JButton.buttonType", "roundRect");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(NEON_BLUE);
        submitButton.setForeground(Color.BLACK);
        submitButton.addActionListener(_ -> {
            Bus currentAssignedBus = getAssignedBus();
            if (currentAssignedBus != null) {
                String description = descriptionArea.getText();
                if (description.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez décrire l'incident.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Incident incident = new Incident(currentAssignedBus.getId(), chauffeur.getId(), description, new Date(), "REPORTED");
                incidentService.reportIncident(incident);
                JOptionPane.showMessageDialog(this, "L'incident a été signalé avec succès.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                descriptionArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Vous n'êtes assigné à aucun bus pour le moment.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(submitButton, BorderLayout.SOUTH);

        return panel;
    }

    private Bus getAssignedBus() {
        Bus assignedBus = null;
        for(Trajet t : trajetService.getAllTrajets()) {
            if(t.getChauffeur() != null && t.getChauffeur().getId() == chauffeur.getId() && t.getBus() != null) {
                assignedBus = t.getBus();
                break;
            }
        }
        return assignedBus;
    }

    // Méthode main pour tester l'interface indépendamment
    public static void main(String[] args) {
        // Appliquer le look and feel FlatLaf pour le test
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            int testChauffeurId = 3; // ID fictif pour le test
            ChauffeurService chauffeurService = new ChauffeurService();
            Chauffeur testChauffeur = chauffeurService.getChauffeurById(testChauffeurId);
            if (testChauffeur != null) {
                ChauffeurDashboard dashboard = new ChauffeurDashboard(testChauffeur);
                dashboard.setVisible(true);
            } else {
                System.err.println("Chauffeur non trouvé pour l'ID : " + testChauffeurId);
            }
        });
    }
}
