package DATA;

import Model.Chauffeur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ChauffeurDAO {

    public List<Chauffeur> getAllChauffeurs() {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String sql = "SELECT * FROM chauffeur";
        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chauffeurs.add(new Chauffeur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("type_permis"), rs.getInt("id_compte")));
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return chauffeurs;
    }

    public void addChauffeur(Chauffeur chauffeur) {
        String sql = "INSERT INTO chauffeur (nom, prenom, type_permis, id_compte) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chauffeur.getNom());
            pstmt.setString(2, chauffeur.getPrenom());
            pstmt.setString(3, chauffeur.getTypePermis());
            pstmt.setInt(4, chauffeur.getIdCompte());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void updateChauffeur(Chauffeur chauffeur) {
        String sql = "UPDATE chauffeur SET nom = ?, prenom = ?, type_permis = ? WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chauffeur.getNom());
            pstmt.setString(2, chauffeur.getPrenom());
            pstmt.setString(3, chauffeur.getTypePermis());
            pstmt.setInt(4, chauffeur.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void deleteChauffeur(int id) {
        String sql = "DELETE FROM chauffeur WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Chauffeur getChauffeurById(int id) {
        String sql = "SELECT c.*, cu.email FROM chauffeur c JOIN comptes_utilisateurs cu ON c.id_compte = cu.id WHERE c.id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Chauffeur chauffeur = new Chauffeur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("type_permis"),
                    rs.getInt("id_compte")
                );
                chauffeur.setEmail(rs.getString("email"));
                return chauffeur;
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public Chauffeur getChauffeurByAccountId(int accountId) {
        String sql = "SELECT * FROM chauffeur WHERE id_compte = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Chauffeur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("type_permis"),
                        rs.getInt("id_compte")
                );
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
