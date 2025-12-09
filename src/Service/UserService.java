package Service;

import DATA.UserDAO;
import Model.Admin;
import Model.Etudiant;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public Object login(String username, String password) {
        Object user = userDAO.login(username, password);
        if (user == null) {
            DATA.ChauffeurDAO chauffeurDAO = new DATA.ChauffeurDAO();
            return chauffeurDAO.login(username, password);
        }
        return user;
    }
}

