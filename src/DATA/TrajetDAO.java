package DATA;
import java.sql.Connection;
import Model.Bus;
import Model.Chauffeur;
import Model.Trajet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {

    public List<Trajet> getAllTrajets() {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT t.id, t.point_depart, t.point_arrivee, t.heure_depart, " +
                     "b.id as bus_id, b.capacite, b.marque, b.matricule, " +
                     "c.id as chauffeur_id, c.nom as chauffeur_nom, c.prenom as chauffeur_prenom, c.permis " +
                     "FROM trajets t " +
                     "LEFT JOIN bus b ON t.id_bus = b.id " +
                     "LEFT JOIN chauffeurs c ON t.id_chauffeur = c.id";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bus bus = null;
                if (rs.getObject("bus_id") != null) {
                    bus = new Bus(rs.getInt("bus_id"),rs.getString("matricule"), rs.getString("marque"), rs.getInt("capacite"));
                }

                Chauffeur chauffeur = null;
                if (rs.getObject("chauffeur_id") != null) {
                    chauffeur = new Chauffeur(rs.getInt("chauffeur_id"), rs.getString("chauffeur_nom"),
                            rs.getString("chauffeur_prenom"), rs.getString("permis"));
                }

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
            e.printStackTrace();
        }
        return trajets;
    }

    public void addTrajet(Trajet trajet) {
        String sql = "INSERT INTO trajets (point_depart, point_arrivee, heure_depart, id_bus, id_chauffeur) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trajet.getPointDepart());
            pstmt.setString(2, trajet.getPointArrivee());
            pstmt.setTime(3, Time.valueOf(trajet.getHeureDepart()));
            pstmt.setInt(4, trajet.getBus().getId());
            pstmt.setInt(5, trajet.getChauffeur().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

