package Model;

import java.util.Date;

public class Incident {
    private int id;
    private int busId;
    private int chauffeurId;
    private String description;
    private Date date;
    private String status; // ex: "REPORTED", "RESOLVED"

    public Incident(int id, int busId, int chauffeurId, String description, Date date, String status) {
        this.id = id;
        this.busId = busId;
        this.chauffeurId = chauffeurId;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public Incident(int busId, int chauffeurId, String description, Date date, String status) {
        this.busId = busId;
        this.chauffeurId = chauffeurId;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(int chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

