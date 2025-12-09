package Service;

import DATA.TrajetDAO;
import Model.Trajet;

import java.util.List;

public class TrajetService {

    private final TrajetDAO trajetDAO;

    public TrajetService() {
        this.trajetDAO = new TrajetDAO();
    }

    public List<Trajet> getAllTrajets() {
        return trajetDAO.getAllTrajets();
    }

    public void addTrajet(Trajet trajet) {
        trajetDAO.addTrajet(trajet);
    }

    public void updateTrajet(Trajet trajet) {
        trajetDAO.updateTrajet(trajet);
    }

    public void deleteTrajet(int id) {
        trajetDAO.deleteTrajet(id);
    }
}
