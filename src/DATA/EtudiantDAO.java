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
        String sql = "SELECT * FROM users WHERE role = 'etudiant'";
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
        String sql = "INSERT INTO users (nom, prenom, num_carte, password, arret_principal, statut_paiement, role) VALUES (?, ?, ?, ?, ?, ?, 'etudiant')";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getNumCarte());
            pstmt.setString(4, etudiant.getPassword());
            pstmt.setString(5, etudiant.getArretPrincipal());
            pstmt.setString(6, etudiant.getStatutPaiement());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEtudiant(Etudiant etudiant) {
        String sql = "UPDATE users SET nom = ?, prenom = ?, num_carte = ?, password = ?, arret_principal = ?, statut_paiement = ? WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getNumCarte());
            pstmt.setString(4, etudiant.getPassword());
            pstmt.setString(5, etudiant.getArretPrincipal());
            pstmt.setString(6, etudiant.getStatutPaiement());
            pstmt.setInt(7, etudiant.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEtudiant(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Etudiant getEtudiantById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Etudiant(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("num_carte"),
                        rs.getString("password"), rs.getString("arret_principal"), rs.getString("statut_paiement"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
