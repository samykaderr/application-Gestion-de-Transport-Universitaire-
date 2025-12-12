package Service;

import DATA.CompteUtilisateurDAO;
import Model.CompteUtilisateur;

public class CompteUtilisateurService {
    private CompteUtilisateurDAO compteUtilisateurDAO;

    public CompteUtilisateurService() {
        this.compteUtilisateurDAO = new CompteUtilisateurDAO();
    }

    public int createCompteUtilisateur(CompteUtilisateur compte) {
        return compteUtilisateurDAO.createCompteUtilisateur(compte);
    }

    public CompteUtilisateur login(String email, String password) {
        return compteUtilisateurDAO.findByEmailAndPassword(email, password);
    }
}
