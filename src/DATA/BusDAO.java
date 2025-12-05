package DATA;

import Model.Bus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM bus";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                buses.add(new Bus(rs.getInt("id"), rs.getString("matricule"), rs.getString("marque"), rs.getInt("capacite")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;
    }

    public void addBus(Bus bus) {
        String sql = "INSERT INTO bus (matricule, marque, capacite) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bus.getMatricule());
            pstmt.setString(2, bus.getMarque());
            pstmt.setInt(3, bus.getCapacite());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

