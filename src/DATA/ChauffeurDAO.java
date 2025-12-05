package DATA;

import Model.Chauffeur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurDAO {

    public List<Chauffeur> getAllChauffeurs() {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String sql = "SELECT * FROM chauffeurs";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chauffeurs.add(new Chauffeur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("permis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chauffeurs;
    }

    public void addChauffeur(Chauffeur chauffeur) {
        String sql = "INSERT INTO chauffeurs (nom, prenom, permis) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chauffeur.getNom());
            pstmt.setString(2, chauffeur.getPrenom());
            pstmt.setString(3, chauffeur.getTypePermis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

