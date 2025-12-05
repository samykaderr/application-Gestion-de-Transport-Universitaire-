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
        return userDAO.login(username, password);
    }
}

