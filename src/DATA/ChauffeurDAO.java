package DATA;

import Model.Chauffeur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurDAO {

    public List<Chauffeur> getAllChauffeurs() {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String sql = "SELECT * FROM chauffeur";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chauffeurs.add(new Chauffeur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("password"), rs.getString("type_permis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chauffeurs;
    }

    public void addChauffeur(Chauffeur chauffeur) {
        String sql = "INSERT INTO chauffeur (nom, prenom, password, type_permis) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chauffeur.getNom());
            pstmt.setString(2, chauffeur.getPrenom());
            pstmt.setString(3, chauffeur.getPassword());
            pstmt.setString(4, chauffeur.getTypePermis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Chauffeur login(String username, String password) {
        String sql = "SELECT * FROM chauffeur WHERE CONCAT(nom, ' ', prenom) = ? AND password = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Chauffeur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("password"), rs.getString("type_permis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Chauffeur getChauffeurById(int id) {
        String sql = "SELECT * FROM chauffeur WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Chauffeur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("password"),
                    rs.getString("type_permis")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
