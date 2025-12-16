
package UI;

import Model.*;
import Service.*;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminDashboard extends JFrame {

    // --- COULEURS ET STYLE ---
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);        // Bleu Nuit
    private final Color SIDEBAR_TEXT_COLOR = new Color(236, 240, 241);// Blanc Cassé
    private final Color ACTIVE_BUTTON_COLOR = new Color(52, 73, 94);  // Gris Bleu (Sélection)
    private final Color NEON_BLUE = new Color(51, 204, 255);          // Bleu Néon (Accent)
    private final Color BACKGROUND_COLOR = new Color(248, 250, 252);  // Gris très clair pour le fond
    private final Color CARD_HOVER_COLOR = new Color(225, 240, 255);  // Couleur au survol

    // Services
    private final BusService busService;
    private final ChauffeurService chauffeurService;
    private final EtudiantService etudiantService;
    private final TrajetService trajetService;
    private final IncidentService incidentService;
    private final EtudiantTrajetService etudiantTrajetService;

    // Composants UI
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public AdminDashboard() {
        // Initialisation des Services (Assurez-vous que vos constructeurs sont vides ou adaptés)
        this.busService = new BusService();
        this.chauffeurService = new ChauffeurService();
        this.etudiantService = new EtudiantService();
        this.trajetService = new TrajetService();
        this.incidentService = new IncidentService();
        this.etudiantTrajetService = new EtudiantTrajetService();

        initUI();
    }

    private void initUI() {
        setTitle("Admin - Gestion Transport Scolaire");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // 2. Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Ajout des vues (Chaque méthode retourne un JPanel complet)
        contentPanel.add(createBusPanel(), "BUS");
        contentPanel.add(createChauffeurPanel(), "CHAUFFEUR");
        contentPanel.add(createEtudiantPanel(), "ETUDIANT");
        contentPanel.add(createTrajetPanel(), "TRAJET");
        contentPanel.add(createIncidentPanel(), "INCIDENT");

        add(contentPanel, BorderLayout.CENTER);
    }

    // ====================================================================================
    //                                  SIDEBAR
    // ====================================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 0, 8)); // Espacement vertical
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Titre
        JLabel titleLabel = new JLabel("ADMIN PORTAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);

        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setBackground(new Color(255, 255, 255, 50));
        sep.setForeground(new Color(255, 255, 255, 50));
        sidebar.add(sep);

        // Menu Items
        sidebar.add(createMenuButton("Gestion Bus", "BUS", "/resources/bus.png"));
        sidebar.add(createMenuButton("Gestion Chauffeurs", "CHAUFFEUR", "/resources/chauffeur.png"));
        sidebar.add(createMenuButton("Gestion Étudiants", "ETUDIANT", "/resources/education.png"));
        sidebar.add(createMenuButton("Trajets & Lignes", "TRAJET", "/resources/itinerary.png"));
        sidebar.add(createMenuButton("Incidents", "INCIDENT", "/resources/alert.png"));

        // Bouton Déconnexion
        JButton btnLogout = createMenuButton("Déconnexion", "", null);
        btnLogout.setForeground(new Color(231, 76, 60)); // Rouge pour déconnexion
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
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

        // Propriété FlatLaf pour boutons arrondis (si supporté par le thème)
        btn.putClientProperty("JButton.buttonType", "roundRect");

        btn.addActionListener(_ -> cardLayout.show(contentPanel, cardName));

        // Hover Effect
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
    //                        OUTILS GÉNÉRIQUES (LISTE & ANIMATION)
    // ====================================================================================

    /**
     * Crée une ligne "Carte" pour une liste avec des coins arrondis et boutons d'action.
     */
    private JPanel createItemRow(String textInfo, ActionListener onEdit, ActionListener onDelete) {
        // Utilisation d'un panneau personnalisé pour l'arrondi (RoundedPanel)
        JPanel row = new RoundedPanel(15, Color.WHITE);
        row.setLayout(new BorderLayout(10, 10));
        row.setBorder(new EmptyBorder(10, 15, 10, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); // Hauteur fixe

        // Label Info (HTML pour le formatage multilingne)
        JLabel infoLabel = new JLabel(textInfo);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        row.add(infoLabel, BorderLayout.CENTER);

        // Panel Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        if (onEdit != null) {
            JButton btnEdit = createActionButton("✏", new Color(241, 196, 15), onEdit);
            btnPanel.add(btnEdit);
        }
        if (onDelete != null) {
            JButton btnDelete = createActionButton("🗑", new Color(231, 76, 60), onDelete);
            btnPanel.add(btnDelete);
        }

        row.add(btnPanel, BorderLayout.EAST);

        // Animation de survol fluide
        addSmoothHoverEffect(row, CARD_HOVER_COLOR);

        return row;
    }

    private JButton createActionButton(String icon, Color bg, ActionListener action) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(40, 35));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        // Arrondi FlatLaf
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.addActionListener(action);
        return btn;
    }

    // Animation de couleur fluide (Interpolation)
    private void addSmoothHoverEffect(JPanel panel, Color hoverColor) {
        panel.addMouseListener(new MouseAdapter() {
            private Timer timer;
            private final int DURATION = 200;

            @Override
            public void mouseEntered(MouseEvent e) {
                animate(panel, hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animate(panel, Color.WHITE);
            }

            private void animate(JComponent component, Color targetColor) {
                if (timer != null && timer.isRunning()) timer.stop();
                Color startColor = component.getBackground();
                long startTime = System.currentTimeMillis();
                int DELAY = 10;

                timer = new Timer(DELAY, evt -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    float progress = Math.min(1.0f, (float) elapsed / DURATION);
                    int r = (int) (startColor.getRed() + (targetColor.getRed() - startColor.getRed()) * progress);
                    int g = (int) (startColor.getGreen() + (targetColor.getGreen() - startColor.getGreen()) * progress);
                    int b = (int) (startColor.getBlue() + (targetColor.getBlue() - startColor.getBlue()) * progress);
                    component.setBackground(new Color(r, g, b));
                    component.repaint();
                    if (progress >= 1.0f) ((Timer) evt.getSource()).stop();
                });
                timer.start();
            }
        });
    }

    // ====================================================================================
    //                                  PANELS DE GESTION
    // ====================================================================================

    // --- 1. GESTION BUS ---
    private JPanel createBusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Formulaire
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Nouveau / Modifier Bus"));
        // Arrondi du panel formulaire
        formPanel.putClientProperty("Component.arc", 20);

        JTextField txtMatricule = new JTextField(10);
        JTextField txtMarque = new JTextField(10);
        JTextField txtCapacite = new JTextField(5);
        JTextField txtId = new JTextField(); txtId.setVisible(false); // ID caché

        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler"); btnCancel.setEnabled(false);

        formPanel.add(new JLabel("Marque:")); formPanel.add(txtMarque);
        formPanel.add(new JLabel("Matricule:")); formPanel.add(txtMatricule);
        formPanel.add(new JLabel("Capacité:")); formPanel.add(txtCapacite);
        formPanel.add(btnSave); formPanel.add(btnCancel);

        // Liste Container
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Logique Refresh
        final Runnable[] refreshBusList = new Runnable[1];
        refreshBusList[0] = () -> {
            listContainer.removeAll();
            listContainer.add(Box.createVerticalStrut(10)); // Marge top
            for (Bus b : busService.getAllBuses()) {
                String info = String.format("<html><b>%s</b> <span style='color:gray'>(%s)</span> - Capacité: %d</html>",
                        b.getMarque(), b.getMatricule(), b.getCapacite());

                ActionListener onEdit = _ -> {
                    txtId.setText(String.valueOf(b.getId()));
                    txtMarque.setText(b.getMarque());
                    txtMatricule.setText(b.getMatricule());
                    txtCapacite.setText(String.valueOf(b.getCapacite()));
                    btnSave.setText("Modifier");
                    btnCancel.setEnabled(true);
                };

                ActionListener onDelete = _ -> {
                    if(JOptionPane.showConfirmDialog(this, "Supprimer ce bus ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        busService.deleteBus(b.getId());
                        refreshBusList[0].run();
                    }
                };

                JPanel row = createItemRow(info, onEdit, onDelete);
                listContainer.add(row);
                listContainer.add(Box.createVerticalStrut(10)); // Espace entre éléments
            }
            listContainer.revalidate();
            listContainer.repaint();
        };

        // Bouton Save Action
        btnSave.addActionListener(_ -> {
            try {
                int cap = Integer.parseInt(txtCapacite.getText());
                Bus b = new Bus(txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText()),
                        txtMatricule.getText(), txtMarque.getText(), cap);

                if (btnSave.getText().equals("Modifier")) {
                    busService.updateBus(b);
                } else {
                    busService.addBus(b);
                }
                refreshBusList[0].run();
                // Reset
                txtId.setText(""); txtMatricule.setText(""); txtMarque.setText(""); txtCapacite.setText("");
                btnSave.setText("Enregistrer"); btnCancel.setEnabled(false);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage()); }
        });

        btnCancel.addActionListener(_ -> {
            txtId.setText(""); txtMatricule.setText(""); txtMarque.setText(""); txtCapacite.setText("");
            btnSave.setText("Enregistrer"); btnCancel.setEnabled(false);
        });

        refreshBusList[0].run(); // Init load
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- 2. GESTION CHAUFFEURS ---
    private JPanel createChauffeurPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Formulaire Simplifié
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Gestion Chauffeur"));
        formPanel.putClientProperty("Component.arc", 20);

        JTextField txtNom = new JTextField(8);
        JTextField txtPrenom = new JTextField(8);
        JTextField txtEmail = new JTextField(10);
        JTextField txtTypePermis = new JTextField(5);
        JButton btnSave = new JButton("Ajouter");

        formPanel.add(new JLabel("Nom:")); formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:")); formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);
        formPanel.add(new JLabel("Permis:")); formPanel.add(txtTypePermis);
        formPanel.add(btnSave);

        // Liste
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        final Runnable[] refreshChauffeurList = new Runnable[1];
        refreshChauffeurList[0] = () -> {
            listContainer.removeAll();
            listContainer.add(Box.createVerticalStrut(10));
            for (Chauffeur c : chauffeurService.getAllChauffeurs()) {
                String info = String.format("<html><b>%s %s</b><br/><span style='font-size:10px'>Permis: %s</span></html>",
                        c.getNom().toUpperCase(), c.getPrenom(), c.getTypePermis());

                JPanel row = createItemRow(info,
                        _ -> openChauffeurDialog(c, refreshChauffeurList[0]),
                        _ -> {
                            if (JOptionPane.showConfirmDialog(this, "Supprimer ce chauffeur ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                chauffeurService.deleteChauffeur(c.getId());
                                refreshChauffeurList[0].run();
                            }
                        }
                );
                listContainer.add(row);
                listContainer.add(Box.createVerticalStrut(10));
            }
            listContainer.revalidate(); listContainer.repaint();
        };

        btnSave.addActionListener(_ -> openChauffeurDialog(null, refreshChauffeurList[0]));

        refreshChauffeurList[0].run();
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- 3. GESTION ETUDIANTS ---
    private JPanel createEtudiantPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Étudiant"));
        formPanel.putClientProperty("Component.arc", 20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        JTextField txtNom = new JTextField(10);
        JTextField txtPrenom = new JTextField(10);
        JTextField txtEmail = new JTextField(10);
        JPasswordField txtPassword = new JPasswordField(10);
        JTextField txtCarte = new JTextField(10);
        // Limiter la saisie à 12 chiffres uniquement
        txtCarte.setToolTipText("Entrez exactement 12 chiffres");
        ((javax.swing.text.AbstractDocument) txtCarte.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                String filtered = string.replaceAll("[^0-9]", "");
                if ((fb.getDocument().getLength() + filtered.length()) <= 12) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                String filtered = text.replaceAll("[^0-9]", "");
                if ((fb.getDocument().getLength() - length + filtered.length()) <= 12) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        });
        JTextField txtArret = new JTextField(10);
        JComboBox<String> cbStatut = new JComboBox<>(new String[]{"Payé", "Non Payé"});
        JTextField txtId = new JTextField(); txtId.setVisible(false);

        // Ligne 1: Nom et Prénom
        gbc.gridx=0; gbc.gridy=0; formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx=1; formPanel.add(txtNom, gbc);
        gbc.gridx=2; formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx=3; formPanel.add(txtPrenom, gbc);

        // Ligne 2: Email et Mot de passe
        gbc.gridx=0; gbc.gridy=1; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx=1; formPanel.add(txtEmail, gbc);
        gbc.gridx=2; formPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx=3; formPanel.add(txtPassword, gbc);

        // Ligne 3: Carte et Arrêt
        gbc.gridx=0; gbc.gridy=2; formPanel.add(new JLabel("Carte:"), gbc);
        gbc.gridx=1; formPanel.add(txtCarte, gbc);
        gbc.gridx=2; formPanel.add(new JLabel("Arrêt:"), gbc);
        gbc.gridx=3; formPanel.add(txtArret, gbc);

        // Ligne 4: Statut
        gbc.gridx=0; gbc.gridy=3; formPanel.add(new JLabel("Statut:"), gbc);
        gbc.gridx=1; formPanel.add(cbStatut, gbc);

        // Ligne 5: Bouton
        JButton btnAdd = new JButton("Ajouter/Modifier");
        gbc.gridx=3; gbc.gridy=3; gbc.anchor = GridBagConstraints.EAST; formPanel.add(btnAdd, gbc);
        formPanel.add(txtId);

        // Liste
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        final Runnable[] refreshEtudiantList = new Runnable[1];
        refreshEtudiantList[0] = () -> {
            listContainer.removeAll();
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            for (Etudiant etudiant : etudiants) {
                String html = String.format("<html><div style='width:350px'><b>%s %s</b> (ID: %d)<br/>" +
                                "<span style='color:#555'>Carte: %s | Arrêt: %s | Statut: <span style='color:%s'>%s</span></span></div></html>",
                        etudiant.getPrenom(), etudiant.getNom(), etudiant.getId(),
                        etudiant.getNumCarte(), etudiant.getArretPrincipal(),
                        "Payé".equals(etudiant.getStatutPaiement()) ? "green" : "red", etudiant.getStatutPaiement());

                JPanel row = createItemRow(html,
                        // Edit Action
                        e -> {
                            txtId.setText(String.valueOf(etudiant.getId()));
                            txtNom.setText(etudiant.getNom());
                            txtPrenom.setText(etudiant.getPrenom());
                            txtEmail.setText(etudiant.getEmail());
                            txtCarte.setText(etudiant.getNumCarte());
                            txtArret.setText(etudiant.getArretPrincipal());
                            cbStatut.setSelectedItem(etudiant.getStatutPaiement());
                            // Récupérer l'ancien mot de passe
                            CompteUtilisateurService compteService = new CompteUtilisateurService();
                            CompteUtilisateur compte = compteService.getCompteById(etudiant.getIdCompte());
                            if (compte != null) {
                                txtPassword.setText(compte.getMotDePasse());
                            }
                        },
                        // Delete Action
                        e -> {
                            if (JOptionPane.showConfirmDialog(this, "Supprimer cet étudiant ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                etudiantService.deleteEtudiant(etudiant.getId());
                                refreshEtudiantList[0].run();
                            }
                        }
                );
                listContainer.add(row);
                listContainer.add(Box.createVerticalStrut(10));
            }
            listContainer.revalidate(); listContainer.repaint();
        };

        btnAdd.addActionListener(_ -> {
            try {
                // Validation du numéro de carte : doit contenir exactement 12 chiffres
                String numCarte = txtCarte.getText().trim();
                if (!numCarte.matches("\\d{12}")) {
                    JOptionPane.showMessageDialog(this, "Le numéro de carte doit contenir exactement 12 chiffres.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
                Etudiant etudiant;
                String password = new String(txtPassword.getPassword()).trim();
                if (id == 0) { // Ajout
                    // Validation du mot de passe
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Veuillez saisir un mot de passe.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // La création de compte pour l'étudiant avec email et mot de passe saisis
                    CompteUtilisateur newCompte = new CompteUtilisateur(0, txtEmail.getText().trim(), password, "etudiant");
                    int idCompte = new CompteUtilisateurService().createCompteUtilisateur(newCompte);
                    if (idCompte == -1) {
                        JOptionPane.showMessageDialog(this, "Erreur création compte étudiant", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    etudiant = new Etudiant(0, txtNom.getText(), txtPrenom.getText(), txtEmail.getText(), numCarte, txtArret.getText(), (String) cbStatut.getSelectedItem(), idCompte);
                    etudiantService.addEtudiant(etudiant);
                } else { // Modification
                    Etudiant existing = etudiantService.getEtudiantById(id);
                    etudiant = new Etudiant(id, txtNom.getText(), txtPrenom.getText(), txtEmail.getText(), numCarte, txtArret.getText(), (String) cbStatut.getSelectedItem(), existing.getIdCompte());
                    etudiantService.updateEtudiant(etudiant);
                    // Mise à jour du mot de passe si modifié
                    if (!password.isEmpty()) {
                        CompteUtilisateurService compteService = new CompteUtilisateurService();
                        CompteUtilisateur compte = compteService.getCompteById(existing.getIdCompte());
                        if (compte != null) {
                            compte.setMotDePasse(password);
                            compteService.updateCompteUtilisateur(compte);
                        }
                    }
                }
                refreshEtudiantList[0].run();
                // Reset form
                txtId.setText(""); txtNom.setText(""); txtPrenom.setText(""); txtEmail.setText(""); txtPassword.setText(""); txtCarte.setText(""); txtArret.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        refreshEtudiantList[0].run();

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- 4. GESTION TRAJETS ---
    private JPanel createTrajetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);
        listContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

        final Runnable[] refreshTrajetList = new Runnable[1];

        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.setBackground(BACKGROUND_COLOR);
        JButton btnAdd = new JButton("Nouveau Trajet");
        btnAdd.addActionListener(_ -> openTrajetDialog(null, refreshTrajetList[0]));
        header.add(btnAdd);

        refreshTrajetList[0] = () -> {
            listContainer.removeAll();
            listContainer.add(Box.createVerticalStrut(10));
            for (Trajet t : trajetService.getAllTrajets()) {
                int nbEtudiants = etudiantTrajetService.countEtudiantsByTrajet(t.getId());
                String info = String.format("<html><b>Départ: %s ➝ Arrivée: %s</b><br/>Heure: %s | Bus: %s | <span style='color:#3498db'><b>%d étudiant(s) inscrit(s)</b></span></html>",
                        t.getPointDepart(), t.getPointArrivee(), t.getHeureDepart(),
                        (t.getBus()!=null ? t.getBus().getMatricule() : "?"), nbEtudiants);

                JPanel row = createItemRow(info,
                        _ -> openTrajetDialog(t, refreshTrajetList[0]),
                        _ -> {
                            if (JOptionPane.showConfirmDialog(this, "Supprimer ce trajet ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                trajetService.deleteTrajet(t.getId());
                                refreshTrajetList[0].run();
                            }
                        });
                listContainer.add(row);
                listContainer.add(Box.createVerticalStrut(10));
            }
            listContainer.revalidate();
            listContainer.repaint();
        };

        refreshTrajetList[0].run();
        panel.add(header, BorderLayout.NORTH);
        panel.add(new JScrollPane(listContainer), BorderLayout.CENTER);
        return panel;
    }

    // --- 5. GESTION INCIDENTS ---
    private JPanel createIncidentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);
        listContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

        final Runnable[] refreshIncidentList = new Runnable[1];
        refreshIncidentList[0] = () -> {
            listContainer.removeAll();
            listContainer.add(Box.createVerticalStrut(10));
            // Note: Adapter selon votre modèle IncidentDetails ou Incident
            List<IncidentDetails> incidents = incidentService.getAllIncidentsDetails();
            for (IncidentDetails inc : incidents) {
                String info = String.format("<html><b>%s</b> (%s)<br/>%s - <font color='red'>%s</font></html>",
                        inc.getBusMatricule(), inc.getDate(), inc.getDescription(), inc.getStatus());

                // Pas de bouton Modifier pour incident, juste Supprimer ou Traiter
                JPanel row = createItemRow(info, null, _ -> {
                    if (JOptionPane.showConfirmDialog(this, "Supprimer cet incident ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        incidentService.deleteIncident(inc.getIncidentId());
                        refreshIncidentList[0].run();
                    }
                });
                listContainer.add(row);
                listContainer.add(Box.createVerticalStrut(10));
            }
            listContainer.revalidate();
            listContainer.repaint();
        };

        refreshIncidentList[0].run();
        panel.add(new JLabel("   Liste des Incidents Signalés"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listContainer), BorderLayout.CENTER);
        return panel;
    }

    private void openChauffeurDialog(Chauffeur chauffeur, Runnable refreshCallback) {
        JDialog dialog = new JDialog(this, "Gestion Chauffeur", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNom = new JTextField(15);
        JTextField txtPrenom = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        JTextField txtTypePermis = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtPrenom, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Permis:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(txtTypePermis, gbc);

        if (chauffeur != null) {
            txtNom.setText(chauffeur.getNom());
            txtPrenom.setText(chauffeur.getPrenom());
            txtTypePermis.setText(chauffeur.getTypePermis());
            Chauffeur fullChauffeur = chauffeurService.getChauffeurById(chauffeur.getId());
            if (fullChauffeur != null) {
                txtEmail.setText(fullChauffeur.getEmail());
            }
            // Récupérer l'ancien mot de passe
            CompteUtilisateurService compteService = new CompteUtilisateurService();
            CompteUtilisateur compte = compteService.getCompteById(chauffeur.getIdCompte());
            if (compte != null) {
                txtPassword.setText(compte.getMotDePasse());
            }
            txtEmail.setEditable(false); // L'email est la clé de connexion, ne pas le modifier ici
        }

        JButton btnSave = new JButton("Enregistrer");
        btnSave.addActionListener(_ -> {
            try {
                String password = new String(txtPassword.getPassword()).trim();
                if (chauffeur == null) { // Ajout
                    if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() || txtTypePermis.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Veuillez saisir un mot de passe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    CompteUtilisateur newCompte = new CompteUtilisateur(0, txtEmail.getText(), password, "chauffeur");
                    int idCompte = new CompteUtilisateurService().createCompteUtilisateur(newCompte);

                    if (idCompte != -1) {
                        Chauffeur newChauffeur = new Chauffeur(0, txtNom.getText(), txtPrenom.getText(), txtTypePermis.getText(), idCompte);
                        chauffeurService.addChauffeur(newChauffeur);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Erreur lors de la création du compte utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else { // Modification
                    Chauffeur updatedChauffeur = new Chauffeur(chauffeur.getId(), txtNom.getText(), txtPrenom.getText(), txtTypePermis.getText(), chauffeur.getIdCompte());
                    chauffeurService.updateChauffeur(updatedChauffeur);
                    // Mise à jour du mot de passe si modifié
                    if (!password.isEmpty()) {
                        CompteUtilisateurService compteService = new CompteUtilisateurService();
                        CompteUtilisateur compte = compteService.getCompteById(chauffeur.getIdCompte());
                        if (compte != null) {
                            compte.setMotDePasse(password);
                            compteService.updateCompteUtilisateur(compte);
                        }
                    }
                }
                refreshCallback.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur de format ou données invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void openTrajetDialog(Trajet trajet, Runnable refreshCallback) {
        JDialog dialog = new JDialog(this, "Gestion Trajet", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtDepart = new JTextField(15);
        JTextField txtArrivee = new JTextField(15);
        JTextField txtHeure = new JTextField(15);
        JComboBox<Bus> comboBus = new JComboBox<>(busService.getAllBuses().toArray(new Bus[0]));
        JComboBox<Chauffeur> comboChauffeur = new JComboBox<>(chauffeurService.getAllChauffeurs().toArray(new Chauffeur[0]));

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Départ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtDepart, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Arrivée:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtArrivee, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Heure (HH:mm):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtHeure, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Bus:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(comboBus, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Chauffeur:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(comboChauffeur, gbc);

        if (trajet != null) {
            txtDepart.setText(trajet.getPointDepart());
            txtArrivee.setText(trajet.getPointArrivee());
            txtHeure.setText(trajet.getHeureDepart().toString());
            comboBus.setSelectedItem(trajet.getBus());
            comboChauffeur.setSelectedItem(trajet.getChauffeur());
        }

        JButton btnSave = new JButton("Enregistrer");
        btnSave.addActionListener(_ -> {
            try {
                Trajet newTrajet = new Trajet(
                        trajet == null ? 0 : trajet.getId(),
                        txtDepart.getText(),
                        txtArrivee.getText(),
                        java.time.LocalTime.parse(txtHeure.getText()),
                        (Bus) comboBus.getSelectedItem(),
                        (Chauffeur) comboChauffeur.getSelectedItem()
                );

                if (trajet == null) {
                    trajetService.addTrajet(newTrajet);
                } else {
                    trajetService.updateTrajet(newTrajet);
                }
                refreshCallback.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur de format de l'heure ou données invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ====================================================================================
    //                         CLASSE INTERNE : ROUNDED PANEL
    // ====================================================================================
    // Panel personnalisé qui dessine un fond arrondi
    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Important pour que paintComponent dessine le fond custom
        }

        @Override
        public void setBackground(Color bg) {
            this.backgroundColor = bg;
            super.setBackground(bg); // Pour compatibilité
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessin du fond arrondi
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            // Optionnel : Bordure fine
            g2.setColor(new Color(230, 230, 230));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        }
    }

    // ====================================================================================
    //                                      MAIN
    // ====================================================================================
    public static void main(String[] args) {
        // Setup FlatLaf pour le look moderne
        try {
            FlatLightLaf.setup();
            // Personnalisation globale des arrondis via propriétés FlatLaf
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception ex) {
            System.err.println("FlatLaf not loaded");
        }

        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}

