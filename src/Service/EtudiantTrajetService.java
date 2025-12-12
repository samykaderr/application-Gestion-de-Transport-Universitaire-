package Service;

import DATA.EtudiantTrajetDAO;
import DATA.LoggerUtil;

import java.sql.SQLException;
import java.util.logging.Level;

public class EtudiantTrajetService {

    private final EtudiantTrajetDAO etudiantTrajetDAO;

    public EtudiantTrajetService() {
        this.etudiantTrajetDAO = new EtudiantTrajetDAO();
    }

    /**
     * Vérifie si l'étudiant est déjà inscrit à un trajet
     */
    public boolean etudiantDejaInscrit(int etudiantId) {
        try {
            return etudiantTrajetDAO.etudiantDejaInscrit(etudiantId);
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, "Erreur lors de la vérification d'inscription", e);
            return false;
        }
    }

    /**
     * Récupère l'ID du trajet auquel l'étudiant est inscrit (-1 si aucun)
     */
    public int getTrajetIdPourEtudiant(int etudiantId) {
        try {
            return etudiantTrajetDAO.getTrajetIdPourEtudiant(etudiantId);
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, "Erreur lors de la récupération du trajet de l'étudiant", e);
            return -1;
        }
    }

    public boolean inscrireEtudiantAuTrajet(int etudiantId, int trajetId) {
        try {
            return etudiantTrajetDAO.inscrireEtudiantAuTrajet(etudiantId, trajetId);
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, "Error while enrolling student in a trip", e);
            return false;
        }
    }

    /**
     * Compte le nombre d'étudiants inscrits à un trajet donné
     */
    public int countEtudiantsByTrajet(int trajetId) {
        try {
            return etudiantTrajetDAO.countEtudiantsByTrajet(trajetId);
        } catch (SQLException e) {
            LoggerUtil.log(Level.SEVERE, "Erreur lors du comptage des étudiants du trajet", e);
            return 0;
        }
    }
}
