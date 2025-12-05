package DATA;

import Model.Etudiant;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class EtudiantDAO {

    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                etudiants.add(new Etudiant(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("num_carte"),
                        rs.getString("password"), rs.getString("arret_principal"), rs.getString("statut_paiement")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etudiants;
    }

    public void addEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO etudiants (nom, prenom, num_carte, password, arret_principal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getNumCarte());
            pstmt.setString(4, etudiant.getPassword());
            pstmt.setString(5, etudiant.getArretPrincipal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
