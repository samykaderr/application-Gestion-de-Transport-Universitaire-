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
        String sql = "SELECT * FROM users WHERE (nom = ? AND role = 'admin' OR num_carte = ?) AND password = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String dbPassword = rs.getString("password");
                String role = rs.getString("role");

                if ("admin".equals(role)) {
                    return new Admin(id, nom, prenom, dbPassword);
                } else if ("etudiant".equals(role)) {
                    String numCarte = rs.getString("num_carte");
                    String arretPrincipal = rs.getString("arret_principal");
                    String statutPaiement = rs.getString("statut_paiement");
                    return new Etudiant(id, nom, prenom, numCarte, dbPassword, arretPrincipal, statutPaiement);
                }
                // This part might not be strictly necessary if all users are either admin or etudiant
                return new user(id, nom, prenom, dbPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}