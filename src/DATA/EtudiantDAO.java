package DATA;

import Model.Etudiant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EtudiantDAO {

    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiant";
        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                etudiants.add(new Etudiant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_carte"),
                        rs.getString("arret_principal"),
                        rs.getString("statut_paiement"),
                        rs.getInt("id_compte")
                ));
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return etudiants;
    }

    public void addEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO etudiant (nom, prenom, email, num_carte, arret_principal, statut_paiement, id_compte) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getEmail());
            pstmt.setString(4, etudiant.getNumCarte());
            pstmt.setString(5, etudiant.getArretPrincipal());
            pstmt.setString(6, etudiant.getStatutPaiement());
            pstmt.setInt(7, etudiant.getIdCompte());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void updateEtudiant(Etudiant etudiant) {
        String sql = "UPDATE etudiant SET nom = ?, prenom = ?, email = ?, num_carte = ?, arret_principal = ?, statut_paiement = ? WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getEmail());
            pstmt.setString(4, etudiant.getNumCarte());
            pstmt.setString(5, etudiant.getArretPrincipal());
            pstmt.setString(6, etudiant.getStatutPaiement());
            pstmt.setInt(7, etudiant.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void deleteEtudiant(int id) {
        String sql = "DELETE FROM etudiant WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Etudiant getEtudiantById(int id) {
        String sql = "SELECT * FROM etudiant WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Etudiant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_carte"),
                        rs.getString("arret_principal"),
                        rs.getString("statut_paiement"),
                        rs.getInt("id_compte")
                );
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public Etudiant getEtudiantByAccountId(int accountId) {
        String sql = "SELECT * FROM etudiant WHERE id_compte = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Etudiant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_carte"),
                        rs.getString("arret_principal"),
                        rs.getString("statut_paiement"),
                        rs.getInt("id_compte")
                );
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
