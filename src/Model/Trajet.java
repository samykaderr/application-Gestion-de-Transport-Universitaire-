
package Model;

import java.time.LocalTime;

public class Trajet {
    private int id;
    private String pointDepart;
    private String pointArrivee;
    private LocalTime heureDepart;
    private Bus bus;
    private Chauffeur chauffeur;

    public Trajet(int id, String pointDepart, String pointArrivee, LocalTime heureDepart, Bus bus, Chauffeur chauffeur) {
        this.id = id;
        this.pointDepart = pointDepart;
        this.pointArrivee = pointArrivee;
        this.heureDepart = heureDepart;
        this.bus = bus;
        this.chauffeur = chauffeur;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getPointDepart() {
        return pointDepart;
    }

    public String getPointArrivee() {
        return pointArrivee;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public Bus getBus() {
        return bus;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPointDepart(String pointDepart) {
        this.pointDepart = pointDepart;
    }

    public void setPointArrivee(String pointArrivee) {
        this.pointArrivee = pointArrivee;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }
}

