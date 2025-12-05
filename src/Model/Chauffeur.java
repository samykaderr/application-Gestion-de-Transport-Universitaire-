package Model;

public class Chauffeur {
    private int id;
    private String nom;
    private String prenom;
    private String typePermis;

    public Chauffeur(int id, String nom, String prenom, String typePermis) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.typePermis = typePermis;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTypePermis() {
        return typePermis;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTypePermis(String typePermis) {
        this.typePermis = typePermis;
    }
}
