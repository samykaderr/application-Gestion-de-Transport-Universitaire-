package UI;

import Model.Etudiant;
import Model.IncidentDetails;
import Model.Trajet;
import Service.EtudiantService;
import Service.EtudiantTrajetService;
import Service.IncidentService;
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
    private final IncidentService incidentService;


    // Composants UI
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel trajetsResultsContainer;
    private JButton trajetsSearchButton;

    public StudentDashboard(Etudiant etudiant) {
        this.etudiant = etudiant;
        this.trajetService = new TrajetService();
        this.etudiantTrajetService = new EtudiantTrajetService();
        this.incidentService = new IncidentService();

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
        contentPanel.add(createIncidentsPanel(), "INCIDENTS");

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
        sidebar.add(createMenuButton("Incidents Bus", "INCIDENTS", "/resources/alert.png"));

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

    // --- 3. ABONNEMENT (HISTORIQUE + PAIEMENT) ---
    private JPanel createAbonnementPanel() {
        // Panel principal avec scroll pour tout voir
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Section Paiement (En haut) ---
        RoundedPanel paiementCard = new RoundedPanel(20, Color.WHITE);
        paiementCard.setLayout(new BorderLayout(15, 15));
        paiementCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        paiementCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        paiementCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Titre section paiement avec icône credit-card
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        ImageIcon creditCardIcon = loadIcon("/resources/credit-card.png", 24, 24);
        if (creditCardIcon != null) {
            titlePanel.add(new JLabel(creditCardIcon));
        } else {
            JLabel fallbackIcon = new JLabel("💳");
            fallbackIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            titlePanel.add(fallbackIcon);
        }
        JLabel titlePaiement = new JLabel("Paiement des Frais de Transport", SwingConstants.LEFT);
        titlePaiement.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlePaiement.setForeground(SIDEBAR_COLOR);
        titlePanel.add(titlePaiement);

        // Info statut actuel
        boolean isPaid = "Payé".equalsIgnoreCase(etudiant.getStatutPaiement());
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statusPanel.setOpaque(false);

        JLabel lblStatutLabel = new JLabel("Statut actuel:");
        lblStatutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblStatutValue = new JLabel(isPaid ? "✓ PAYÉ" : "✗ NON PAYÉ");
        lblStatutValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatutValue.setForeground(isPaid ? STATUS_OK : STATUS_WARN);

        statusPanel.add(lblStatutLabel);
        statusPanel.add(lblStatutValue);

        // Panel info tarif
        JPanel tarifPanel = new JPanel();
        tarifPanel.setLayout(new BoxLayout(tarifPanel, BoxLayout.Y_AXIS));
        tarifPanel.setOpaque(false);

        // Tarif avec icône money-bag
        JPanel tarifRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tarifRow.setOpaque(false);
        ImageIcon moneyBagIcon = loadIcon("/resources/money-bag.png", 20, 20);
        if (moneyBagIcon != null) {
            tarifRow.add(new JLabel(moneyBagIcon));
        } else {
            JLabel fallbackIcon = new JLabel("💰");
            fallbackIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            tarifRow.add(fallbackIcon);
        }
        JLabel lblTarif = new JLabel("Tarif Abonnement Annuel: 1500 DA");
        lblTarif.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarifRow.add(lblTarif);

        // Période avec icône schedule
        JPanel periodeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        periodeRow.setOpaque(false);
        ImageIcon scheduleIcon = loadIcon("/resources/schedule.png", 20, 20);
        if (scheduleIcon != null) {
            periodeRow.add(new JLabel(scheduleIcon));
        } else {
            JLabel fallbackIcon = new JLabel("📅");
            fallbackIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            periodeRow.add(fallbackIcon);
        }
        JLabel lblPeriode = new JLabel("Période: Septembre 2024 - Août 2025");
        lblPeriode.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPeriode.setForeground(new Color(80, 80, 80));
        periodeRow.add(lblPeriode);

        tarifPanel.add(statusPanel);
        tarifPanel.add(tarifRow);
        tarifPanel.add(periodeRow);

        // Panel bouton paiement
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton btnPayer = new JButton();
        btnPayer.setText(isPaid ? "✓ Déjà Payé" : "Payer Maintenant");
        btnPayer.setBackground(isPaid ? new Color(189, 195, 199) : STATUS_OK);
        btnPayer.setEnabled(!isPaid); // Désactiver si le paiement est déjà effectué
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPayer.setPreferredSize(new Dimension(180, 45));
        btnPayer.putClientProperty("JButton.buttonType", "roundRect");

        btnPayer.addActionListener(e -> {
            // Dialog de confirmation de paiement
            JPanel paiementFormPanel = new JPanel(new GridBagLayout());
            paiementFormPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Titre
            JLabel lblTitre = new JLabel("💳 Paiement par Carte Dahabia");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitre.setForeground(SIDEBAR_COLOR);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            paiementFormPanel.add(lblTitre, gbc);

            // Séparateur
            gbc.gridy = 1;
            paiementFormPanel.add(new JSeparator(), gbc);

            // Numéro de carte Dahabia (16 chiffres)
            gbc.gridwidth = 1;
            gbc.gridx = 0; gbc.gridy = 2;
            JLabel lblNumCarte = new JLabel("N° Carte Dahabia:");
            lblNumCarte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            paiementFormPanel.add(lblNumCarte, gbc);

            gbc.gridx = 1;
            JTextField txtNumCarte = new JTextField(20);
            txtNumCarte.setToolTipText("Entrez les 16 chiffres de votre carte Dahabia");
            // Limiter à 16 chiffres uniquement
            ((javax.swing.text.AbstractDocument) txtNumCarte.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                    String filtered = string.replaceAll("[^0-9]", "");
                    if ((fb.getDocument().getLength() + filtered.length()) <= 16) {
                        super.insertString(fb, offset, filtered, attr);
                    }
                }
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                    String filtered = text.replaceAll("[^0-9]", "");
                    if ((fb.getDocument().getLength() - length + filtered.length()) <= 16) {
                        super.replace(fb, offset, length, filtered, attrs);
                    }
                }
            });
            paiementFormPanel.add(txtNumCarte, gbc);

            // Code de confirmation (CVV - 3 chiffres)
            gbc.gridx = 0; gbc.gridy = 3;
            JLabel lblCodeCVV = new JLabel("Code de confirmation:");
            lblCodeCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            paiementFormPanel.add(lblCodeCVV, gbc);

            gbc.gridx = 1;
            JPasswordField txtCodeCVV = new JPasswordField(5);
            txtCodeCVV.setToolTipText("Entrez les 3 chiffres au dos de votre carte");
            // Limiter à 3 chiffres uniquement
            ((javax.swing.text.AbstractDocument) txtCodeCVV.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                    String filtered = string.replaceAll("[^0-9]", "");
                    if ((fb.getDocument().getLength() + filtered.length()) <= 3) {
                        super.insertString(fb, offset, filtered, attr);
                    }
                }
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                    String filtered = text.replaceAll("[^0-9]", "");
                    if ((fb.getDocument().getLength() - length + filtered.length()) <= 3) {
                        super.replace(fb, offset, length, filtered, attrs);
                    }
                }
            });
            paiementFormPanel.add(txtCodeCVV, gbc);

            // Montant
            gbc.gridx = 0; gbc.gridy = 4;
            JLabel lblMontant = new JLabel("Montant à payer:");
            lblMontant.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            paiementFormPanel.add(lblMontant, gbc);

            gbc.gridx = 1;
            JLabel lblMontantValue = new JLabel("1500 DA");
            lblMontantValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblMontantValue.setForeground(new Color(39, 174, 96));
            paiementFormPanel.add(lblMontantValue, gbc);

            // Note d'info
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            JLabel lblNote = new JLabel("<html><i style='color:gray;font-size:10px'>🔒 Paiement sécurisé - Vos données sont protégées</i></html>");
            paiementFormPanel.add(lblNote, gbc);

            int result = JOptionPane.showConfirmDialog(this, paiementFormPanel,
                "Paiement des Frais de Transport", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Validation du numéro de carte (16 chiffres)
                String numCarte = txtNumCarte.getText().trim();
                if (!numCarte.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un numéro de carte Dahabia valide (16 chiffres).",
                        "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validation du code CVV (3 chiffres)
                String codeCVV = new String(txtCodeCVV.getPassword()).trim();
                if (!codeCVV.matches("\\d{3}")) {
                    JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un code de confirmation valide (3 chiffres).",
                        "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mettre à jour le statut de paiement
                etudiant.setStatutPaiement("Payé");
                EtudiantService etudiantService = new EtudiantService();
                etudiantService.updateEtudiant(etudiant);

                // Afficher les 4 derniers chiffres de la carte
                String carteMasquee = "**** **** **** " + numCarte.substring(12);

                JOptionPane.showMessageDialog(this,
                    "✅ Paiement effectué avec succès!\n\n" +
                    "Carte Dahabia: " + carteMasquee + "\n" +
                    "Montant: 1500 DA\n\n" +
                    "Votre abonnement est maintenant actif.",
                    "Paiement Confirmé", JOptionPane.INFORMATION_MESSAGE);

                // Rafraîchir l'interface
                refreshAbonnementPanel();
                refreshHomePanel();
            }
        });

        btnPanel.add(btnPayer);

        paiementCard.add(titlePanel, BorderLayout.NORTH);
        paiementCard.add(tarifPanel, BorderLayout.CENTER);
        paiementCard.add(btnPanel, BorderLayout.SOUTH);

        // --- Section Historique (En bas) ---
        JPanel historiquePanel = new JPanel(new BorderLayout());
        historiquePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleHistorique = new JLabel("📋 Historique des Paiements");
        titleHistorique.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleHistorique.setBorder(new EmptyBorder(10, 0, 15, 0));

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);


        if ("Payé".equalsIgnoreCase(etudiant.getStatutPaiement())) {
            // Carte Paiement Récent
            String html = "<html><b>✓ Abonnement Annuel (En cours)</b><br/>Période: 01/09/2024 - 31/08/2025<br/>Montant: 1500 DA<br/><span style='color:green'>Statut: Payé</span></html>";
            JPanel card = createInfoCard(html, new Color(230, 255, 230));
            listContainer.add(card);

            listContainer.add(Box.createVerticalStrut(15));
            listContainer.add(createInfoCard("<html><b>Abonnement Annuel</b><br/>Période: 01/09/2023 - 31/08/2024<br/>Montant: 1500 DA<br/><span style='color:green'>Statut: Payé</span></html>", Color.WHITE));
        } else {
            JPanel emptyCard = new RoundedPanel(15, new Color(255, 245, 245));
            emptyCard.setLayout(new BorderLayout());
            emptyCard.setBorder(new EmptyBorder(20, 20, 20, 20));
            JLabel emptyLabel = new JLabel("<html><center>⚠️ Aucun paiement enregistré.<br/>Veuillez effectuer le paiement ci-dessus.</center></html>", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(STATUS_WARN);
            emptyCard.add(emptyLabel, BorderLayout.CENTER);
            listContainer.add(emptyCard);
        }

        historiquePanel.add(titleHistorique, BorderLayout.NORTH);
        historiquePanel.add(listContainer, BorderLayout.CENTER);
        historiquePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ajouter les sections au container principal
        mainContainer.add(paiementCard);
        mainContainer.add(Box.createVerticalStrut(20));
        mainContainer.add(historiquePanel);

        // Scroll principal pour tout le contenu
        JScrollPane mainScroll = new JScrollPane(mainContainer);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(BACKGROUND_COLOR);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Panel wrapper avec BorderLayout
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.add(mainScroll, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * Rafraîchit le panel d'abonnement après un paiement
     */
    private void refreshAbonnementPanel() {
        contentPanel.remove(contentPanel.getComponent(2)); // Supprimer l'ancien panel
        contentPanel.add(createAbonnementPanel(), "ABONNEMENT", 2);
        cardLayout.show(contentPanel, "ABONNEMENT");
    }

    /**
     * Rafraîchit le panel d'accueil (profil) après un paiement
     */
    private void refreshHomePanel() {
        contentPanel.remove(contentPanel.getComponent(0)); // Supprimer l'ancien panel
        contentPanel.add(createHomePanel(), "HOME", 0);
    }

    // --- 4. INCIDENTS BUS ---
    private JPanel createIncidentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // En-tête avec titre et icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleRow.setOpaque(false);
        ImageIcon alertIcon = loadIcon("/resources/alert.png", 28, 28);
        if (alertIcon != null) {
            titleRow.add(new JLabel(alertIcon));
        }
        JLabel title = new JLabel("Incidents et Retards des Bus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_COLOR);
        titleRow.add(title);

        JLabel subtitle = new JLabel("Consultez les incidents signalés pour anticiper les retards");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBorder(new EmptyBorder(5, 38, 0, 0));

        headerPanel.add(titleRow, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        // Bouton Rafraîchir
        JButton btnRefresh = new JButton("🔄 Actualiser");
        btnRefresh.setBackground(SIDEBAR_COLOR);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.putClientProperty("JButton.buttonType", "roundRect");

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setOpaque(false);
        refreshPanel.add(btnRefresh);
        headerPanel.add(refreshPanel, BorderLayout.EAST);

        // Liste des incidents
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Charger les incidents
        Runnable loadIncidents = () -> {
            listContainer.removeAll();
            List<IncidentDetails> incidents = incidentService.getAllIncidentsDetails();

            if (incidents.isEmpty()) {
                JPanel emptyCard = new RoundedPanel(15, new Color(230, 255, 230));
                emptyCard.setLayout(new BorderLayout());
                emptyCard.setBorder(new EmptyBorder(30, 30, 30, 30));
                emptyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                JLabel emptyLabel = new JLabel("✅ Aucun incident signalé actuellement. Tous les bus circulent normalement.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                emptyLabel.setForeground(STATUS_OK);
                emptyCard.add(emptyLabel, BorderLayout.CENTER);
                listContainer.add(emptyCard);
            } else {
                for (IncidentDetails incident : incidents) {
                    JPanel card = createIncidentCard(incident);
                    listContainer.add(card);
                    listContainer.add(Box.createVerticalStrut(10));
                }
            }
            listContainer.revalidate();
            listContainer.repaint();
        };

        // Charger au démarrage
        loadIncidents.run();

        // Action du bouton Rafraîchir
        btnRefresh.addActionListener(e -> loadIncidents.run());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée une carte d'incident pour l'affichage
     */
    private JPanel createIncidentCard(IncidentDetails incident) {
        // Couleur selon le statut
        boolean isResolved = "RESOLVED".equalsIgnoreCase(incident.getStatus());
        Color cardColor = isResolved ? new Color(240, 255, 240) : new Color(255, 245, 238);
        Color statusColor = isResolved ? STATUS_OK : STATUS_WARN;
        String statusText = isResolved ? "RÉSOLU" : "EN COURS";
        String statusIcon = isResolved ? "✅" : "⚠️";

        RoundedPanel card = new RoundedPanel(15, cardColor);
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setPreferredSize(new Dimension(0, 140));

        // Icône de statut à gauche
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(50, 50));

        JLabel iconLabel = new JLabel(statusIcon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        // Infos centrales
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Bus info
        JPanel busRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        busRow.setOpaque(false);
        ImageIcon busIcon = loadIcon("/resources/bus.png", 18, 18);
        if (busIcon != null) {
            busRow.add(new JLabel(busIcon));
        }
        JLabel lblBus = new JLabel("Bus: " + incident.getBusMarque() + " - " + incident.getBusMatricule());
        lblBus.setFont(new Font("Segoe UI", Font.BOLD, 15));
        busRow.add(lblBus);

        // Chauffeur
        JPanel chauffeurRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chauffeurRow.setOpaque(false);
        ImageIcon chauffeurIcon = loadIcon("/resources/chauffeur.png", 18, 18);
        if (chauffeurIcon != null) {
            chauffeurRow.add(new JLabel(chauffeurIcon));
        }
        JLabel lblChauffeur = new JLabel("Chauffeur: " + incident.getChauffeurPrenom() + " " + incident.getChauffeurNom());
        lblChauffeur.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblChauffeur.setForeground(new Color(80, 80, 80));
        chauffeurRow.add(lblChauffeur);

        // Description
        JLabel lblDescription = new JLabel("📝 " + incident.getDescription());
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescription.setBorder(new EmptyBorder(5, 0, 0, 0));

        infoPanel.add(busRow);
        infoPanel.add(chauffeurRow);
        infoPanel.add(lblDescription);

        // Panel droit: date et statut
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(150, 100));

        // Date
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = incident.getDate() != null ? sdf.format(incident.getDate()) : "N/A";
        JLabel lblDate = new JLabel("📅 " + dateStr);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(new Color(100, 100, 100));
        lblDate.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Statut
        JLabel lblStatus = new JLabel(statusIcon + " " + statusText);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(statusColor);
        lblStatus.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(10, 0, 0, 0));

        rightPanel.add(lblDate);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(lblStatus);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        // Animation hover
        addSmoothHoverEffect(card, cardColor, CARD_HOVER_COLOR);

        return card;
    }

    // ====================================================================================
    //                        OUTILS GRAPHIQUES
    // ====================================================================================

    // Crée une carte d'information simple sans boutons d'action (Pour liste lecture seule)
    private JPanel createInfoCard(String htmlContent, Color bgColor) {
        JPanel row = new RoundedPanel(15, bgColor);
        row.setLayout(new BorderLayout(10, 10));
        row.setBorder(new EmptyBorder(20, 20, 20, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        row.setPreferredSize(new Dimension(0, 120));

        JLabel info = new JLabel(htmlContent);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
