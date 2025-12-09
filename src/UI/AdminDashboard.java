package UI;

import Model.*;
import DATA.*;
import Service.BusService;
import Service.ChauffeurService;
import Service.EtudiantService;
import Service.TrajetService;
import Service.IncidentService;

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
    private final IncidentService incidentService;

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
        incidentService = new IncidentService();

        // Ajout des différentes vues (Panels)
        contentPanel.add(createBusPanel(), "BUS");
        contentPanel.add(createChauffeurPanel(), "CHAUFFEUR");
        contentPanel.add(createEtudiantPanel(), "ETUDIANT");
        contentPanel.add(createTrajetPanel(), "TRAJET");
        contentPanel.add(createIncidentPanel(), "INCIDENT");

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
        sidebar.add(createNavButton("Incidents", "INCIDENT"));

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
        JPasswordField txtPassword = new JPasswordField(10);
        JTextField txtTypePermis = new JTextField(5);
        JButton btnAdd = new JButton("Ajouter Chauffeur");
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Permis:"));
        formPanel.add(txtTypePermis);
        formPanel.add(btnAdd);

        String[] columns = {"ID", "Nom", "Prénom", "Permis"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        loadChauffeurs(model);

        btnAdd.addActionListener(_ -> {
            Chauffeur chauffeur = new Chauffeur(0, txtNom.getText(), txtPrenom.getText(), new String(txtPassword.getPassword()), txtTypePermis.getText());
            chauffeurService.addChauffeur(chauffeur);
            loadChauffeurs(model);
            txtNom.setText("");
            txtPrenom.setText("");
            txtPassword.setText("");
            txtTypePermis.setText("");
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadChauffeurs(DefaultTableModel model) {
        model.setRowCount(0);
        for (Chauffeur chauffeur : chauffeurService.getAllChauffeurs()) {
            model.addRow(new Object[]{chauffeur.getId(), chauffeur.getNom(), chauffeur.getPrenom(), chauffeur.getTypePermis()});
        }
    }

    //model.addRow(new Object[]{chauffeur.getId(), chauffeur.getNom(), chauffeur.getPrenom(), chauffeur.getTypePermis()});
    // Remplace toute la méthode createEtudiantPanel par celle-ci dans AdminDashboard.java

    private JPanel createEtudiantPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Panneau du formulaire ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Étudiants"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Champs de saisie ---
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtNumCarte = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtArretPrincipal = new JTextField();
        JComboBox<String> comboStatut = new JComboBox<>(new String[]{"Non Payé", "Payé"});
        JLabel etudiantIdLabel = new JLabel();

        // --- Ajout des composants au formulaire ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtPrenom, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        formPanel.add(new JLabel("N° Carte:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtNumCarte, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        formPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        formPanel.add(new JLabel("Arrêt Principal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtArretPrincipal, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        formPanel.add(new JLabel("Statut Paiement:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(comboStatut, gbc);

        // --- Panneau pour les boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnAdd = createStyledButton("Ajouter", ACCENT_COLOR);
        JButton btnUpdate = createStyledButton("Modifier", new Color(243, 156, 18));
        JButton btnDelete = createStyledButton("Supprimer", new Color(231, 76, 60));
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        gbc.gridx = 1; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);

        // --- Tableau ---
        String[] columns = {"ID", "Nom", "Prénom", "N° Carte", "Arrêt", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadEtudiants(model);

        // --- Logique de sélection et actions ---
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                Object idValue = model.getValueAt(row, 0);
                Object nomValue = model.getValueAt(row, 1);
                Object prenomValue = model.getValueAt(row, 2);
                Object numCarteValue = model.getValueAt(row, 3);
                Object arretValue = model.getValueAt(row, 4);
                Object statutValue = model.getValueAt(row, 5);

                etudiantIdLabel.setText(idValue != null ? idValue.toString() : "");
                txtNom.setText(nomValue != null ? nomValue.toString() : "");
                txtPrenom.setText(prenomValue != null ? prenomValue.toString() : "");
                txtNumCarte.setText(numCarteValue != null ? numCarteValue.toString() : "");
                txtArretPrincipal.setText(arretValue != null ? arretValue.toString() : "");
                comboStatut.setSelectedItem(statutValue != null ? statutValue.toString() : "Non Payé");

                txtPassword.setText(""); // Ne pas afficher le mot de passe
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                Etudiant etudiant = new Etudiant(0, txtNom.getText(), txtPrenom.getText(), txtNumCarte.getText(), new String(txtPassword.getPassword()), txtArretPrincipal.getText(), comboStatut.getSelectedItem().toString());
                etudiantService.addEtudiant(etudiant);
                loadEtudiants(model);
                clearEtudiantForm(txtNom, txtPrenom, txtNumCarte, txtPassword, txtArretPrincipal, table, btnUpdate, btnDelete);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(etudiantIdLabel.getText());
                String password = new String(txtPassword.getPassword());
                // Si le champ mot de passe est vide, on ne le met pas à jour
                if (password.isEmpty()) {
                    // Il faudrait une méthode pour récupérer l'ancien mot de passe
                    // Pour cet exemple, on va juste ne pas le changer.
                    // Vous devriez implémenter une logique plus sécurisée
                    Etudiant existingEtudiant = etudiantService.getEtudiantById(id); // Méthode à créer
                    password = existingEtudiant.getPassword();
                }
                Etudiant etudiant = new Etudiant(id, txtNom.getText(), txtPrenom.getText(), txtNumCarte.getText(), password, txtArretPrincipal.getText(), comboStatut.getSelectedItem().toString());
                etudiantService.updateEtudiant(etudiant);
                loadEtudiants(model);
                clearEtudiantForm(txtNom, txtPrenom, txtNumCarte, txtPassword, txtArretPrincipal, table, btnUpdate, btnDelete);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet étudiant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(etudiantIdLabel.getText());
                    etudiantService.deleteEtudiant(id);
                    loadEtudiants(model);
                    clearEtudiantForm(txtNom, txtPrenom, txtNumCarte, txtPassword, txtArretPrincipal, table, btnUpdate, btnDelete);
                }
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void clearEtudiantForm(JTextField txtNom, JTextField txtPrenom, JTextField txtNumCarte, JPasswordField txtPassword, JTextField txtArretPrincipal, JTable table, JButton btnUpdate, JButton btnDelete) {
        txtNom.setText("");
        txtPrenom.setText("");
        txtNumCarte.setText("");
        txtPassword.setText("");
        txtArretPrincipal.setText("");
        table.clearSelection();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
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
           // --- VUE 4 : GESTION DES TRAJETS ---
           private JPanel createTrajetPanel() {
               JPanel panel = new JPanel(new BorderLayout(10, 10));
               panel.setBorder(new EmptyBorder(20, 20, 20, 20));

               // --- Panneau du formulaire ---
               JPanel formPanel = new JPanel(new GridBagLayout());
               formPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Trajets"));
               GridBagConstraints gbc = new GridBagConstraints();
               gbc.insets = new Insets(8, 8, 8, 8); // Plus d'espacement
               gbc.anchor = GridBagConstraints.WEST;

               // --- Champs de saisie ---
               JTextField txtDepart = new JTextField();
               JTextField txtArrivee = new JTextField();
               JTextField txtHeure = new JTextField();
               JLabel trajetIdLabel = new JLabel();

               JComboBox<Bus> comboBus = new JComboBox<>(busService.getAllBuses().toArray(new Bus[0]));
               JComboBox<Chauffeur> comboChauffeur = new JComboBox<>(chauffeurService.getAllChauffeurs().toArray(new Chauffeur[0]));

               // --- Ajout des composants au formulaire avec GridBagLayout ---
               gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
               formPanel.add(new JLabel("Départ:"), gbc);
               gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
               formPanel.add(txtDepart, gbc);

               gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
               formPanel.add(new JLabel("Arrivée:"), gbc);
               gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
               formPanel.add(txtArrivee, gbc);

               gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
               formPanel.add(new JLabel("Heure (HH:mm):"), gbc);
               gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
               formPanel.add(txtHeure, gbc);

               gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
               formPanel.add(new JLabel("Bus:"), gbc);
               gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
               formPanel.add(comboBus, gbc);

               gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
               formPanel.add(new JLabel("Chauffeur:"), gbc);
               gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
               formPanel.add(comboChauffeur, gbc);

               // --- Panneau pour les boutons ---
               JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
               JButton btnAdd = createStyledButton("Ajouter", ACCENT_COLOR);
               JButton btnUpdate = createStyledButton("Modifier", new Color(243, 156, 18));
               JButton btnDelete = createStyledButton("Supprimer", new Color(231, 76, 60));
               btnUpdate.setEnabled(false);
               btnDelete.setEnabled(false);

               buttonPanel.add(btnAdd);
               buttonPanel.add(btnUpdate);
               buttonPanel.add(btnDelete);

               gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
               formPanel.add(buttonPanel, gbc);

               // --- Tableau ---
               String[] columns = {"ID", "Départ", "Arrivée", "Heure", "Bus", "Chauffeur"};
               DefaultTableModel model = new DefaultTableModel(columns, 0);
               JTable table = new JTable(model);
               table.setRowHeight(28);
               table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               loadTrajets(model);

               // --- Logique de sélection et actions ---
               table.getSelectionModel().addListSelectionListener(e -> {
                   if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                       int row = table.getSelectedRow();
                       trajetIdLabel.setText(model.getValueAt(row, 0).toString());
                       txtDepart.setText(model.getValueAt(row, 1).toString());
                       txtArrivee.setText(model.getValueAt(row, 2).toString());
                       txtHeure.setText(model.getValueAt(row, 3).toString());
                       comboBus.setSelectedItem(model.getValueAt(row, 4));
                       comboChauffeur.setSelectedItem(model.getValueAt(row, 5));
                       btnUpdate.setEnabled(true);
                       btnDelete.setEnabled(true);
                   }
               });

               btnAdd.addActionListener(e -> {
                   try {
                       Trajet trajet = new Trajet(0, txtDepart.getText(), txtArrivee.getText(), java.time.LocalTime.parse(txtHeure.getText()), (Bus) comboBus.getSelectedItem(), (Chauffeur) comboChauffeur.getSelectedItem());
                       trajetService.addTrajet(trajet);
                       loadTrajets(model);
                       clearTrajetForm(txtDepart, txtArrivee, txtHeure, table, btnUpdate, btnDelete);
                   } catch (Exception ex) {
                       JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                   }
               });

               btnUpdate.addActionListener(e -> {
                   try {
                       int id = Integer.parseInt(trajetIdLabel.getText());
                       Trajet trajet = new Trajet(id, txtDepart.getText(), txtArrivee.getText(), java.time.LocalTime.parse(txtHeure.getText()), (Bus) comboBus.getSelectedItem(), (Chauffeur) comboChauffeur.getSelectedItem());
                       trajetService.updateTrajet(trajet);
                       loadTrajets(model);
                       clearTrajetForm(txtDepart, txtArrivee, txtHeure, table, btnUpdate, btnDelete);
                   } catch (Exception ex) {
                       JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                   }
               });

               btnDelete.addActionListener(e -> {
                   if (table.getSelectedRow() != -1) {
                       int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce trajet ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                       if (confirm == JOptionPane.YES_OPTION) {
                           int id = Integer.parseInt(trajetIdLabel.getText());
                           trajetService.deleteTrajet(id);
                           loadTrajets(model);
                           clearTrajetForm(txtDepart, txtArrivee, txtHeure, table, btnUpdate, btnDelete);
                       }
                   }
               });

               panel.add(formPanel, BorderLayout.NORTH);
               panel.add(new JScrollPane(table), BorderLayout.CENTER);
               return panel;
           }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        return button;
    }

    private void clearTrajetForm(JTextField txtDepart, JTextField txtArrivee, JTextField txtHeure, JTable table, JButton btnUpdate, JButton btnDelete) {
        txtDepart.setText("");
        txtArrivee.setText("");
        txtHeure.setText("");
        table.clearSelection();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void loadTrajets(DefaultTableModel model) {
        model.setRowCount(0);
        for (Trajet trajet : trajetService.getAllTrajets()) {
            model.addRow(new Object[]{trajet.getId(), trajet.getPointDepart(), trajet.getPointArrivee(), trajet.getHeureDepart(), trajet.getBus(), trajet.getChauffeur()});
        }
    }

    // --- VUE 5 : GESTION DES INCIDENTS ---
    private JPanel createIncidentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Liste des Incidents Signalés");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Bus", "Chauffeur", "Description", "Date", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        loadIncidents(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadIncidents(DefaultTableModel model) {
        model.setRowCount(0);
        for (IncidentDetails incident : incidentService.getAllIncidentsDetails()) {
            model.addRow(new Object[]{
                    incident.getIncidentId(),
                    incident.getBusMarque() + " (" + incident.getBusMatricule() + ")",
                    incident.getChauffeurNom() + " " + incident.getChauffeurPrenom(),
                    incident.getDescription(),
                    incident.getDate(),
                    incident.getStatus()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
