package DATA;

import Model.Incident;
import Model.IncidentDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class IncidentDAO {

    public void addIncident(Incident incident) {
        String sql = "INSERT INTO incidents (bus_id, chauffeur_id, description, date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, incident.getBusId());
            pstmt.setInt(2, incident.getChauffeurId());
            pstmt.setString(3, incident.getDescription());
            pstmt.setTimestamp(4, new Timestamp(incident.getDate().getTime()));
            pstmt.setString(5, incident.getStatus());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void deleteIncident(int incidentId) {
        String sql = "DELETE FROM incidents WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incidentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public List<IncidentDetails> getAllIncidentsDetails() {
        List<IncidentDetails> incidents = new ArrayList<>();
        String sql = "SELECT i.id, b.marque, b.matricule, c.nom, c.prenom, i.description, i.date, i.status " +
                     "FROM incidents i " +
                     "JOIN bus b ON i.bus_id = b.id " +
                     "JOIN chauffeur c ON i.chauffeur_id = c.id";

        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                incidents.add(new IncidentDetails(
                        rs.getInt("id"),
                        rs.getString("marque"),
                        rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("description"),
                        rs.getTimestamp("date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return incidents;
    }
}
