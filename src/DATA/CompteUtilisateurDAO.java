package DATA;

import Model.CompteUtilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class CompteUtilisateurDAO {

    public CompteUtilisateurDAO() {
    }

    public int createCompteUtilisateur(CompteUtilisateur compte) {
        String sql = "INSERT INTO comptes_utilisateurs (email, mot_de_passe, role) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, compte.getEmail());
            pstmt.setString(2, compte.getMotDePasse());
            pstmt.setString(3, compte.getRole());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return -1; // Return -1 on failure
    }

    public CompteUtilisateur findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM comptes_utilisateurs WHERE email = ? AND mot_de_passe = ?";
        try (Connection connexion = ConnexionBDD.getConnexion();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new CompteUtilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("mot_de_passe"),
                        resultSet.getString("role")
                );
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
