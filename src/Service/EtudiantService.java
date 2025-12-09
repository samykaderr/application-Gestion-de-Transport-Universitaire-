package Service;

import DATA.EtudiantDAO;
import Model.Etudiant;

import java.util.List;

public class EtudiantService {

    private final EtudiantDAO etudiantDAO;

    public EtudiantService() {
        this.etudiantDAO = new EtudiantDAO();
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantDAO.getAllEtudiants();
    }

    public void addEtudiant(Etudiant etudiant) {
        etudiantDAO.addEtudiant(etudiant);
    }

    public void updateEtudiant(Etudiant etudiant) {
        etudiantDAO.updateEtudiant(etudiant);
    }

    public void deleteEtudiant(int id) {
        etudiantDAO.deleteEtudiant(id);
    }

    public Etudiant getEtudiantById(int id) {
        return etudiantDAO.getEtudiantById(id);
    }
}
