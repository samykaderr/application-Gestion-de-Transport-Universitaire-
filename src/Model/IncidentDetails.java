package Model;

import java.util.Date;

public class IncidentDetails {
    private int incidentId;
    private String busMarque;
    private String busMatricule;
    private String chauffeurNom;
    private String chauffeurPrenom;
    private String description;
    private Date date;
    private String status;

    public IncidentDetails(int incidentId, String busMarque, String busMatricule, String chauffeurNom, String chauffeurPrenom, String description, Date date, String status) {
        this.incidentId = incidentId;
        this.busMarque = busMarque;
        this.busMatricule = busMatricule;
        this.chauffeurNom = chauffeurNom;
        this.chauffeurPrenom = chauffeurPrenom;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public String getBusMarque() {
        return busMarque;
    }

    public String getBusMatricule() {
        return busMatricule;
    }

    public String getChauffeurNom() {
        return chauffeurNom;
    }

    public String getChauffeurPrenom() {
        return chauffeurPrenom;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}

