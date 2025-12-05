package DATA;

import Model.Admin;
import Model.Etudiant;
import Model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public Object login(String username, String password) {
        String sql = "SELECT * FROM users WHERE nom = ? AND password = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom"); // Assure-toi que cette colonne existe dans 'users'
                String dbPassword = rs.getString("password");
                String role = rs.getString("role"); // Assure-toi que cette colonne existe

                if ("admin".equals(role)) {
                    return new Admin(id, nom, prenom, dbPassword);
                } else if ("student".equals(role)) {
                    // On passe le mot de passe récupéré ici pour construire l'étudiant complet
                    return getEtudiantDetails(id, nom, prenom, dbPassword);
                }
                return new user(id, nom, prenom, dbPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // J'ai modifié la signature pour passer les infos déjà récupérées dans la table users
    private Etudiant getEtudiantDetails(int userId, String nom, String prenom, String password) {
        // Attention: Assure-toi que la table s'appelle bien 'etudiants' et contient ces colonnes
        String sql = "SELECT * FROM etudiants WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // On utilise le constructeur complet de Etudiant existant
                // Note: 'statut_paiement' doit correspondre au nom de ta colonne en BDD
                return new Etudiant(
                        userId,
                        nom,
                        prenom,
                        rs.getString("num_carte"),
                        password,
                        rs.getString("arret_principal"),
                        rs.getString("statut_paiement") // ou null si tu n'as pas cette info ici
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}