package Model;

import java.util.ArrayList;
import java.util.List;

public class Admin extends user {
    private List<Bus> busList;
    private List<Chauffeur> chauffeurList;
    private List<Etudiant> etudiantList;
    private List<Trajet> trajetList;

    public Admin(int id, String nom, String prenom, String password) {
        super(id, nom , prenom, password);
        this.busList = new ArrayList<>();
        this.chauffeurList = new ArrayList<>();
        this.etudiantList = new ArrayList<>();
        this.trajetList = new ArrayList<>();
    }

    // Methods for managing Bus
    public void addBus(Bus bus) {
        busList.add(bus);
    }

    public List<Bus> getBusList() {
        return busList;
    }

    // Methods for managing Chauffeur
    public void addChauffeur(Chauffeur chauffeur) {
        chauffeurList.add(chauffeur);
    }

    public List<Chauffeur> getChauffeurList() {
        return chauffeurList;
    }

    // Methods for managing Etudiant
    public void addEtudiant(Etudiant etudiant) {
        etudiantList.add(etudiant);
    }

    public List<Etudiant> getEtudiantList() {
        return etudiantList;
    }

    // Methods for managing Trajet
    public void createTrajet(Trajet trajet) {
        trajetList.add(trajet);
    }



    public List<Trajet> getTrajetList() {
        return trajetList;
    }

}
