package DATA;

import Model.Bus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BusDAO {

    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM bus";
        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                buses.add(new Bus(rs.getInt("id"), rs.getString("matricule"), rs.getString("marque"), rs.getInt("capacite")));
            }
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
        return buses;
    }

    public void addBus(Bus bus) {
        String sql = "INSERT INTO bus (matricule, marque, capacite) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bus.getMatricule());
            pstmt.setString(2, bus.getMarque());
            pstmt.setInt(3, bus.getCapacite());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void updateBus(Bus bus) {
        String sql = "UPDATE bus SET matricule = ?, marque = ?, capacite = ? WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bus.getMatricule());
            pstmt.setString(2, bus.getMarque());
            pstmt.setInt(3, bus.getCapacite());
            pstmt.setInt(4, bus.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void deleteBus(int id) {
        String sql = "DELETE FROM bus WHERE id = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
