package Service;

import DATA.ChauffeurDAO;
import Model.Chauffeur;

import java.util.List;

public class ChauffeurService {

    private final ChauffeurDAO chauffeurDAO;

    public ChauffeurService() {
        this.chauffeurDAO = new ChauffeurDAO();
    }

    public List<Chauffeur> getAllChauffeurs() {
        return chauffeurDAO.getAllChauffeurs();
    }

    public void addChauffeur(Chauffeur chauffeur) {
        chauffeurDAO.addChauffeur(chauffeur);
    }

    public void deleteChauffeur(int id) {
        chauffeurDAO.deleteChauffeur(id);
    }

    public void updateChauffeur(Chauffeur chauffeur) {
        chauffeurDAO.updateChauffeur(chauffeur);
    }

    public Chauffeur getChauffeurById(int id) {
        return chauffeurDAO.getChauffeurById(id);
    }
}
