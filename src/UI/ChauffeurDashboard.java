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
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
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
    private final IncidentService incidentService;

    public ChauffeurDashboard(int idChauffeur) {
        ChauffeurService chauffeurService = new ChauffeurService();
        this.chauffeur = chauffeurService.getChauffeurById(idChauffeur);
        if (this.chauffeur == null) {
            throw new IllegalArgumentException("Chauffeur not found for ID: " + idChauffeur);
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
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sidebar.add(titleLabel);
        sidebar.add(new JSeparator());

        // Boutons de navigation
        sidebar.add(createMenuButton("Mon Profil", "PROFIL"));
        sidebar.add(createMenuButton("Mon Planning", "PLANNING"));
        sidebar.add(createMenuButton("Info Véhicule", "BUS"));
        sidebar.add(createMenuButton("Signaler un Incident", "INCIDENT"));

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

    // --- VUE 4 : SIGNALER UN INCIDENT ---
    private JPanel createIncidentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Signaler un Incident sur un Véhicule");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
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
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Bouton de soumission
        JButton submitButton = new JButton("Envoyer le Rapport");
        submitButton.setBackground(ACCENT_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.addActionListener(e -> {
            if (assignedBus != null) {
                String description = descriptionArea.getText();
                if (description.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez décrire l'incident.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Incident incident = new Incident(assignedBus.getId(), chauffeur.getId(), description, new Date(), "REPORTED");
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
        SwingUtilities.invokeLater(() -> {
            int testChauffeurId = 12; // ID fictif pour le test
            ChauffeurDashboard dashboard = new ChauffeurDashboard(testChauffeurId);
            dashboard.setVisible(true);
        });
    }
}

