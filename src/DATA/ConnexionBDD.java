package DATA;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    // Modifier ces valeurs selon votre configuration (WAMP/XAMPP défaut : root / vide)
    private static final String URL = "jdbc:mysql://localhost:3306/transport_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection con = null;


    public static Connection getConnexion() throws SQLException {
        if (con == null || con.isClosed()) {
            try {
                // Chargement du driver (optionnel pour les versions récentes de Java mais recommandé)
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
                e.printStackTrace();
                throw new SQLException("Impossible d'établir la connexion à la base de données.", e);
            }
        }
        return con;
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnexion();
            if (conn != null) {
                System.out.println("Connexion à la base de données réussie !");
                conn.close();
            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}