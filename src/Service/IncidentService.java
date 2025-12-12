package Service;

import DATA.IncidentDAO;
import Model.Incident;
import Model.IncidentDetails;

import java.util.List;

public class IncidentService {
    private IncidentDAO incidentDAO;

    public IncidentService() {
        this.incidentDAO = new IncidentDAO();
    }

    public void reportIncident(Incident incident) {
        incidentDAO.addIncident(incident);
        // Here you could also change the status of the bus to "EN_PANNE"
    }

    public List<IncidentDetails> getAllIncidentsDetails() {
        return incidentDAO.getAllIncidentsDetails();
    }

    public void deleteIncident(int incidentId) {
        incidentDAO.deleteIncident(incidentId);
    }
}
