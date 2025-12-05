package UI;

import Model.Bus;
import Model.Chauffeur;
import Model.Trajet;
import Service.TrajetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChauffeurDashboard extends JFrame {

    // --- THÈME (Similaire DU DASHBOARD ÉTUDIANT/ADMIN) ---
    private final Color SIDEBAR_COLOR = new Color(52, 73, 94);
    private final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private final Color ACTIVE_BUTTON_COLOR = new Color(44, 62, 80);
    private final Color HEADER_COLOR = new Color(236, 240, 241);
    private final Color ACCENT_COLOR = new Color(46, 204, 113); // Vert

    // Composants principaux
    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Chauffeur chauffeur;
    private final TrajetService trajetService;

    public ChauffeurDashboard(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
        this.trajetService = new TrajetService();

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
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sidebar.add(titleLabel);
        sidebar.add(new JSeparator());

        // Boutons de navigation
        sidebar.add(createMenuButton("Mon Profil", "PROFIL"));
        sidebar.add(createMenuButton("Mon Planning", "PLANNING"));
        sidebar.add(createMenuButton("Info Véhicule", "BUS"));

        return sidebar;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // Action au clic
        btn.addActionListener(e -> cardLayout.show(mainContentPanel, cardName));

        // Effet de survol
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACTIVE_BUTTON_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });

        return btn;
    }

    // --- EN-TÊTE (HEADER) ---
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + chauffeur.getPrenom() + " " + chauffeur.getNom());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.DARK_GRAY);

        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.setBackground(Color.GRAY);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        return header;
    }

    // --- VUE 1 : PROFIL CHAUFFEUR ---
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        card.setBackground(new Color(248, 249, 250));

        JLabel icon = new JLabel("🪪");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Fiche Chauffeur");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Données du modèle Chauffeur
        card.add(icon);
        card.add(Box.createVerticalStrut(15));
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        card.add(createInfoLabel("Nom : " + chauffeur.getNom()));
        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("Prénom : " + chauffeur.getPrenom()));
        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("ID Chauffeur : " + chauffeur.getId()));
        card.add(Box.createVerticalStrut(10));
        card.add(createInfoLabel("Type de Permis : " + chauffeur.getTypePermis()));

        panel.add(card);
        return panel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // --- VUE 2 : PLANNING (TRAJETS) ---
    private JPanel createPlanningPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("  Mes Trajets Assignés");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(15, 10, 15, 10));

        String[] columns = {"ID Trajet", "Départ", "Arrivée", "Heure", "Bus Matricule"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Chargement des données filtrées pour CE chauffeur
        loadDriverTrajets(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadDriverTrajets(DefaultTableModel model) {
        model.setRowCount(0);
        List<Trajet> allTrajets = trajetService.getAllTrajets();

        for (Trajet t : allTrajets) {
            // FILTRE : On n'affiche que si l'ID du chauffeur du trajet correspond à l'ID du chauffeur connecté
            if (t.getChauffeur() != null && t.getChauffeur().getId() == this.chauffeur.getId()) {
                model.addRow(new Object[]{
                        t.getId(),
                        t.getPointDepart(),
                        t.getPointArrivee(),
                        t.getHeureDepart(),
                        (t.getBus() != null) ? t.getBus().getMatricule() : "Non assigné"
                });
            }
        }
    }

    // --- VUE 3 : INFO VÉHICULE ---
    private JPanel createBusInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

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
        centerPanel.setBackground(Color.WHITE);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new LineBorder(SIDEBAR_COLOR, 2));
        card.setBackground(new Color(240, 248, 255)); // Bleu très clair
        card.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel icon = new JLabel("🚌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

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
            status.setForeground(ACCENT_COLOR);
            status.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(Box.createVerticalStrut(20));
            card.add(status);
        } else {
            JLabel noBus = new JLabel("Aucun bus assigné pour le moment.");
            noBus.setForeground(Color.RED);
            noBus.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(noBus);
        }

        centerPanel.add(card);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Méthode main pour tester l'interface indépendamment
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Création d'un chauffeur fictif pour le test
            Chauffeur testChauffeur = new Chauffeur(1, "Brahimi", "Karim", "Permis D");
            ChauffeurDashboard dashboard = new ChauffeurDashboard(testChauffeur);
            dashboard.setVisible(true);
        });
    }
}