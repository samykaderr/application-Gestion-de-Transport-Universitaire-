package DATA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class EtudiantTrajetDAO {

    /**
     * Vérifie si l'étudiant est déjà inscrit à un trajet (n'importe lequel)
     */
    public boolean etudiantDejaInscrit(int etudiantId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM etudiant_trajet WHERE id_etudiant = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, etudiantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Récupère l'ID du trajet auquel l'étudiant est inscrit (ou -1 si aucun)
     */
    public int getTrajetIdPourEtudiant(int etudiantId) throws SQLException {
        String sql = "SELECT id_trajet FROM etudiant_trajet WHERE id_etudiant = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, etudiantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_trajet");
                }
            }
        }
        return -1;
    }

    public boolean inscrireEtudiantAuTrajet(int etudiantId, int trajetId) throws SQLException {
        String sql = "INSERT INTO etudiant_trajet (id_etudiant, id_trajet) VALUES (?, ?)";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, etudiantId);
            pstmt.setInt(2, trajetId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Gérer le cas où l'étudiant est déjà inscrit (doublon de clé primaire)
            if (e.getErrorCode() == 1062) { // Code d'erreur pour entrée dupliquée dans MySQL
                LoggerUtil.log(Level.WARNING, "L'étudiant est déjà inscrit à ce trajet.", e);
                return false;
            }
            throw e; // Re-throw other SQL exceptions
        }
    }

    /**
     * Compte le nombre d'étudiants inscrits à un trajet donné
     */
    public int countEtudiantsByTrajet(int trajetId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM etudiant_trajet WHERE id_trajet = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trajetId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
