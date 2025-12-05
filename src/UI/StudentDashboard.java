package UI;
import Model.*;
import Service.TrajetService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentDashboard extends JFrame {

    // ... (garder les couleurs et constantes)
    private final Color SIDEBAR_COLOR = new Color(52, 73, 94);
    private final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private final Color ACTIVE_BUTTON_COLOR = new Color(44, 62, 80);
    private final Color HEADER_COLOR = new Color(236, 240, 241);

    // Une couleur d'accentuation spécifique pour différencier l'espace étudiant (Bleu clair)
    private final Color STATUS_OK_COLOR = new Color(46, 204, 113); // Vert
    private final Color STATUS_WARNING_COLOR = new Color(231, 76, 60); // Rouge

    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Etudiant etudiant;
    private final TrajetService trajetService;

    public StudentDashboard(Etudiant etudiant) {
        this.etudiant = etudiant;
        this.trajetService = new TrajetService();
        setTitle("Espace Étudiant - Transport Universitaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Color.WHITE);

        mainContentPanel.add(createHomePanel(), "HOME");
        mainContentPanel.add(createTrajetsPanel(), "TRAJETS");
        mainContentPanel.add(createSubscriptionPanel(), "ABONNEMENT");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    // ... (garder createSidebar et createMenuButton)
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 0, 5));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Titre
        JLabel titleLabel = new JLabel("ESPACE ÉTUDIANT", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sidebar.add(titleLabel);
        sidebar.add(new JSeparator());

        // Boutons
        sidebar.add(createMenuButton("Mon Profil", "HOME"));
        sidebar.add(createMenuButton("Consulter Trajets", "TRAJETS"));
        sidebar.add(createMenuButton("Abonnement & Paiement", "ABONNEMENT"));

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

        btn.addActionListener(_ -> cardLayout.show(mainContentPanel, cardName));

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

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel welcomeLabel = new JLabel("Bonjour, " + etudiant.getNom() + " (ID: " + etudiant.getId() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.DARK_GRAY);

        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.setBackground(Color.GRAY);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(_ -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        return header;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(new Color(248, 249, 250));

        JLabel title = new JLabel("Ma Carte d'Étudiant");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel("Nom: " + etudiant.getNom());
        name.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prenom= new JLabel("Prenom: " + etudiant.getPrenom());
        prenom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        prenom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cardNum = new JLabel("N° Carte: " + etudiant.getNumCarte());
        cardNum.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cardNum.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel address = new JLabel("Arrêt Principal: " + etudiant.getArretPrincipal());
        address.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        address.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(5));
        card.add(cardNum);
        card.add(Box.createVerticalStrut(5));
        card.add(address);

        panel.add(card);
        return panel;
    }

    private JPanel createTrajetsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("  Horaires et Lignes Disponibles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(15, 10, 15, 10));

        String[] columns = {"ID", "Départ", "Arrivée", "Heure", "Bus", "Chauffeur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(230, 230, 230));

        // Charger les données depuis la base de données
        loadTrajets(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadTrajets(DefaultTableModel model) {
        model.setRowCount(0); // Vider la table
        for (Trajet trajet : trajetService.getAllTrajets()) {
            model.addRow(new Object[]{
                trajet.getId(),
                trajet.getPointDepart(),
                trajet.getPointArrivee(),
                trajet.getHeureDepart(),
                trajet.getBus() != null ? trajet.getBus().getMarque() : "N/A",
                trajet.getChauffeur() != null ? trajet.getChauffeur().getNom() : "N/A"
            });
        }
    }

    private JPanel createSubscriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Partie Haute: Indicateur de Statut Visuel
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        // Carte Statut Actuel
        JPanel statusCard = new JPanel(new BorderLayout());
        boolean isPaid = "Payé".equalsIgnoreCase(etudiant.getStatutPaiement());
        statusCard.setBorder(new LineBorder(isPaid ? STATUS_OK_COLOR : STATUS_WARNING_COLOR, 2));
        statusCard.setBackground(isPaid ? new Color(230, 255, 230) : new Color(255, 230, 230));

        JLabel statusTitle = new JLabel("STATUT ABONNEMENT", SwingConstants.CENTER);
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusTitle.setBorder(new EmptyBorder(10,0,0,0));

        JLabel statusValue = new JLabel(isPaid ? "ACTIF" : "INACTIF", SwingConstants.CENTER);
        statusValue.setFont(new Font("Arial", Font.BOLD, 24));
        statusValue.setForeground(isPaid ? STATUS_OK_COLOR : STATUS_WARNING_COLOR);

        JLabel statusDate = new JLabel(isPaid ? "Valide" : "Paiement requis", SwingConstants.CENTER);
        statusDate.setBorder(new EmptyBorder(0,0,10,0));

        statusCard.add(statusTitle, BorderLayout.NORTH);
        statusCard.add(statusValue, BorderLayout.CENTER);
        statusCard.add(statusDate, BorderLayout.SOUTH);

        // Carte Reste à Payer
        JPanel paymentCard = new JPanel(new BorderLayout());
        paymentCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        paymentCard.setBackground(new Color(245, 245, 245));

        JLabel payTitle = new JLabel("RESTE À PAYER", SwingConstants.CENTER);
        payTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        payTitle.setBorder(new EmptyBorder(10,0,0,0));

        JLabel payValue = new JLabel("0.00 DA", SwingConstants.CENTER);
        payValue.setFont(new Font("Arial", Font.BOLD, 24));
        payValue.setForeground(Color.DARK_GRAY);

        paymentCard.add(payTitle, BorderLayout.NORTH);
        paymentCard.add(payValue, BorderLayout.CENTER);

        // Ajout aux panneaux
        statusPanel.add(statusCard);
        statusPanel.add(paymentCard);
        statusPanel.add(new JLabel("")); // Spacer vide pour alignement

        // Partie Basse: Historique
        String[] columns = {"Date de Paiement", "Montant", "Type", "Reçu N°"};
        Object[][] data = {
                {"01/09/2023", "1500 DA", "Abonnement Annuel", "REC-9988"},
                {"01/09/2022", "1500 DA", "Abonnement Annuel", "REC-5544"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable historyTable = new JTable(model);
        JScrollPane scrollHistory = new JScrollPane(historyTable);
        scrollHistory.setBorder(BorderFactory.createTitledBorder("Historique des Paiements"));

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(scrollHistory, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        try {
            // Mise à jour pour correspondre au constructeur (id, nom, prenom, carte, pass, arret, statut)
            Etudiant etudiantTest = new Etudiant(1, "Moussaoui", "Ryad", "123456", "pass123", "Cité Universitaire 1", "Payé");

            // On lance l'interface de test
            SwingUtilities.invokeLater(() -> {
                StudentDashboard frame = new StudentDashboard(etudiantTest);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
