package UI;

import Model.*;
import DATA.*;
import Service.BusService;
import Service.ChauffeurService;
import Service.EtudiantService;
import Service.TrajetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {

    // Couleurs du thème
    private final Color SIDEBAR_COLOR = new Color(52, 73, 94);
    private final Color TEXT_COLOR = Color.DARK_GRAY;
    private final Color ACCENT_COLOR = new Color(46, 204, 113);

    // Composants principaux
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final BusService busService;
    private final ChauffeurService chauffeurService;
    private final EtudiantService etudiantService;
    private final TrajetService trajetService;

    public AdminDashboard() {
        setTitle("Admin - Gestion Transport Scolaire");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Initialisation de la barre latérale (Menu)
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Initialisation du panneau central (Contenu dynamique)
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Initialize Services
        busService = new BusService();
        chauffeurService = new ChauffeurService();
        etudiantService = new EtudiantService();
        trajetService = new TrajetService();

        // Ajout des différentes vues (Panels)
        contentPanel.add(createBusPanel(), "BUS");
        contentPanel.add(createChauffeurPanel(), "CHAUFFEUR");
        contentPanel.add(createEtudiantPanel(), "ETUDIANT");
        contentPanel.add(createTrajetPanel(), "TRAJET");

        add(contentPanel, BorderLayout.CENTER);
    }

    // --- CRÉATION DE LA BARRE LATÉRALE ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 600));
        sidebar.setLayout(new GridLayout(6, 1, 0, 10));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("ADMIN PANEL", SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sidebar.add(titleLabel);

        // Boutons de navigation
        sidebar.add(createNavButton("Gestion des Bus", "BUS"));
        sidebar.add(createNavButton("Gestion Chauffeurs", "CHAUFFEUR"));
        sidebar.add(createNavButton("Gestion Étudiants", "ETUDIANT"));
        sidebar.add(createNavButton("Lignes & Trajets", "TRAJET"));

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(128, 172, 225));
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.addActionListener(_ -> cardLayout.show(contentPanel, cardName));
        return btn;
    }

    // --- VUE 1 : GESTION DES BUS ---
    private JPanel createBusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Formulaire
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un Bus"));

        JTextField txtCapacite = new JTextField(5);
        JTextField txtMarque = new JTextField(10);
        JTextField txtMatricule = new JTextField(10);
        JButton btnAdd = new JButton("Ajouter Bus");
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Capacité:"));
        formPanel.add(txtCapacite);
        formPanel.add(new JLabel("Marque:"));
        formPanel.add(txtMarque);
        formPanel.add(new JLabel("Matricule:"));
        formPanel.add(txtMatricule);
        formPanel.add(btnAdd);

        // Table
        String[] columns = {"ID", "Capacité", "Marque", "Matricule"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        // Load data
        loadBuses(model);

        // Action Bouton
        btnAdd.addActionListener(_ -> {
            Bus bus = new Bus(0, txtMatricule.getText(), txtMarque.getText(), Integer.parseInt(txtCapacite.getText()));
            busService.addBus(bus);
            loadBuses(model); // Refresh table
            txtCapacite.setText("");
            txtMarque.setText("");
            txtMatricule.setText("");
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadBuses(DefaultTableModel model) {
        model.setRowCount(0);
        for (Bus bus : busService.getAllBuses()) {
            model.addRow(new Object[]{bus.getId(), bus.getCapacite(), bus.getMarque(), bus.getMatricule()});
        }
    }

    // --- VUE 2 : GESTION DES CHAUFFEURS ---
    private JPanel createChauffeurPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un Chauffeur"));

        JTextField txtNom = new JTextField(10);
        JTextField txtPrenom = new JTextField(10);
        JTextField txtTypePermis = new JTextField(5);
        JButton btnAdd = new JButton("Ajouter Chauffeur");
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Permis:"));
        formPanel.add(txtTypePermis);
        formPanel.add(btnAdd);

        String[] columns = {"ID", "Nom", "Prénom", "Permis"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        loadChauffeurs(model);

        btnAdd.addActionListener(_ -> {
            Chauffeur chauffeur = new Chauffeur(0, txtNom.getText(), txtPrenom.getText(), txtTypePermis.getText());
            chauffeurService.addChauffeur(chauffeur);
            loadChauffeurs(model);
            txtNom.setText("");
            txtPrenom.setText("");
            txtTypePermis.setText("");
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadChauffeurs(DefaultTableModel model) {
        model.setRowCount(0);
        for (Chauffeur chauffeur : chauffeurService.getAllChauffeurs()) {
            model.addRow(new Object[]{chauffeur.getId(), chauffeur.getNom(), chauffeur.getPrenom()});
        }
    }

            //model.addRow(new Object[]{chauffeur.getId(), chauffeur.getNom(), chauffeur.getPrenom(), chauffeur.getTypePermis()});
    // Remplace toute la méthode createEtudiantPanel par celle-ci dans AdminDashboard.java

    private JPanel createEtudiantPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Zone de formulaire (Haut)
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Grille pour aligner proprement
        formPanel.setBorder(BorderFactory.createTitledBorder("Inscrire un Étudiant"));

        // --- Champs de saisie ---
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtNumCarte = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtArretPrincipal = new JTextField();

        JButton btnAdd = new JButton("Ajouter Étudiant");
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);

        // Ajout des composants au formulaire
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(txtNom);

        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(txtPrenom);

        formPanel.add(new JLabel("N° Carte:"));
        formPanel.add(txtNumCarte);

        formPanel.add(new JLabel("Mot de passe:"));
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Arrêt Principal:"));
        formPanel.add(txtArretPrincipal);

        formPanel.add(new JLabel("")); // Espace vide pour l'alignement
        formPanel.add(btnAdd);

        // --- Tableau (Centre) ---
        // On ajoute "Prénom" et "Statut" aux colonnes
        String[] columns = {"ID", "Nom", "Prénom", "N° Carte", "Arrêt", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        // Chargement initial des données
        loadEtudiants(model);

        // --- Action du bouton Ajouter ---
        btnAdd.addActionListener(e -> {
            try {
                // Vérification basique
                if (txtNom.getText().isEmpty() || txtNumCarte.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom et le numéro de carte sont obligatoires.");
                    return;
                }

                // Création de l'objet Etudiant avec TOUS les champs requis par Etudiant.java
                Etudiant etudiant = new Etudiant(
                        0,                                      // ID (généré auto ou 0)
                        txtNom.getText(),                       // Nom
                        txtPrenom.getText(),                    // Prénom
                        txtNumCarte.getText(),                  // Numéro Carte
                        new String(txtPassword.getPassword()),  // Mot de passe
                        txtArretPrincipal.getText(),            // Arrêt
                        "Non Payé"                              // Statut par défaut
                );

                etudiantService.addEtudiant(etudiant);

                // Rafraîchir le tableau et vider les champs
                loadEtudiants(model);
                txtNom.setText("");
                txtPrenom.setText("");
                txtNumCarte.setText("");
                txtPassword.setText("");
                txtArretPrincipal.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage());
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadEtudiants(DefaultTableModel model) {
        model.setRowCount(0); // Vider le tableau
        for (Etudiant etudiant : etudiantService.getAllEtudiants()) {
            model.addRow(new Object[]{
                    etudiant.getId(),
                    etudiant.getNom(),
                    etudiant.getPrenom(),         // Correction : ajout du prénom
                    etudiant.getNumCarte(),       // Correction : bon getter
                    etudiant.getArretPrincipal(), // Correction : getAddresse() n'existe pas
                    etudiant.getStatutPaiement()
            });
        }
    }


           // model.addRow(new Object[]{etudiant.getId(), etudiant.getNomComplet(), etudiant.getNumCarte(), etudiant.getArretPrincipal()});
    private JPanel createTrajetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Créer un Trajet"));

        JTextField txtDepart = new JTextField(15);
        JTextField txtArrivee = new JTextField(15);
        JTextField txtHeure = new JTextField(8);
        JComboBox<Bus> comboBus = new JComboBox<>(busService.getAllBuses().toArray(new Bus[0]));
        JComboBox<Chauffeur> comboChauffeur = new JComboBox<>(chauffeurService.getAllChauffeurs().toArray(new Chauffeur[0]));
        JButton btnAdd = new JButton("Ajouter Trajet");
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Départ:"));
        formPanel.add(txtDepart);
        formPanel.add(new JLabel("Arrivée:"));
        formPanel.add(txtArrivee);
        formPanel.add(new JLabel("Heure (HH:mm):"));
        formPanel.add(txtHeure);
        formPanel.add(new JLabel("Bus:"));
        formPanel.add(comboBus);
        formPanel.add(new JLabel("Chauffeur:"));
        formPanel.add(comboChauffeur);
        formPanel.add(btnAdd);

        String[] columns = {"ID", "Départ", "Arrivée", "Heure", "Bus", "Chauffeur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        loadTrajets(model);

        btnAdd.addActionListener(_ -> {
            Trajet trajet = new Trajet(0, txtDepart.getText(), txtArrivee.getText(), java.time.LocalTime.parse(txtHeure.getText()), (Bus) comboBus.getSelectedItem(), (Chauffeur) comboChauffeur.getSelectedItem());
            trajetService.addTrajet(trajet);
            loadTrajets(model);
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadTrajets(DefaultTableModel model) {
        model.setRowCount(0);
        for (Trajet trajet : trajetService.getAllTrajets()) {
            model.addRow(new Object[]{trajet.getId(), trajet.getPointDepart(), trajet.getPointArrivee(), trajet.getHeureDepart(), trajet.getBus(), trajet.getChauffeur()});
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
