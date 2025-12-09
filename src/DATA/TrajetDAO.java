package DATA;

import Model.Bus;
import Model.Chauffeur;
import Model.Trajet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {



    public void addTrajet(Trajet trajet) {
        String sql = "INSERT INTO trajets (point_depart, point_arrivee, heure_depart, id_bus, id_chauffeur) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trajet.getPointDepart());
            pstmt.setString(2, trajet.getPointArrivee());
            // Conversion de LocalTime en Time SQL
            pstmt.setTime(3, Time.valueOf(trajet.getHeureDepart()));
            pstmt.setInt(4, trajet.getBus().getId());
            pstmt.setInt(5, trajet.getChauffeur().getId());

            pstmt.executeUpdate();
            System.out.println("Trajet ajouté avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTrajet(Trajet trajet) {
        String sql = "UPDATE trajets SET point_depart = ?, point_arrivee = ?, heure_depart = ?, id_bus = ?, id_chauffeur = ? WHERE id = ?";

        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trajet.getPointDepart());
            pstmt.setString(2, trajet.getPointArrivee());
            pstmt.setTime(3, Time.valueOf(trajet.getHeureDepart()));
            pstmt.setInt(4, trajet.getBus().getId());
            pstmt.setInt(5, trajet.getChauffeur().getId());
            pstmt.setInt(6, trajet.getId());

            pstmt.executeUpdate();
            System.out.println("Trajet modifié avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTrajet(int id) {
        String sql = "DELETE FROM trajets WHERE id = ?";

        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Trajet supprimé avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Trajet> getAllTrajets() {
        List<Trajet> trajets = new ArrayList<>();

        // IMPORTANT : On fait des JOIN pour récupérer les infos du Bus et du Chauffeur
        // On utilise des alias (b.id, c.id) pour éviter les confusions entre les colonnes ID
        String sql = "SELECT t.id, t.point_depart, t.point_arrivee, t.heure_depart, " +
                "b.id AS bus_id, b.matricule, b.marque, b.capacite, " +
                "c.id AS chauffeur_id, c.nom, c.prenom, c.password, c.type_permis " +
                "FROM trajets t " +
                "JOIN bus b ON t.id_bus = b.id " +
                "JOIN chauffeur c ON t.id_chauffeur = c.id";

        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 1. Reconstitution de l'objet Bus
                Bus bus = new Bus(
                        rs.getInt("bus_id"),
                        rs.getString("matricule"),
                        rs.getString("marque"),
                        rs.getInt("capacite")
                );

                // 2. Reconstitution de l'objet Chauffeur
                // C'est ICI que l'erreur se produisait. J'utilise "type_permis" (nom standard SQL).
                // Si votre colonne s'appelle "TypePermis" dans la base, changez la ligne ci-dessous.
                Chauffeur chauffeur = new Chauffeur(
                        rs.getInt("chauffeur_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("password"),
                        rs.getString("type_permis") // <--- CORRECTION ICI
                );

                // 3. Reconstitution de l'objet Trajet
                Trajet trajet = new Trajet(
                        rs.getInt("id"),
                        rs.getString("point_depart"),
                        rs.getString("point_arrivee"),
                        rs.getTime("heure_depart").toLocalTime(),
                        bus,
                        chauffeur
                );

                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL dans getAllTrajets : " + e.getMessage());
            e.printStackTrace();
        }
        return trajets;
    }
}