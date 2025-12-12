package Model;

public class Chauffeur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String typePermis;
    private int idCompte;

    public Chauffeur(int id, String nom, String prenom, String typePermis, int idCompte) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.typePermis = typePermis;
        this.idCompte = idCompte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTypePermis() {
        return typePermis;
    }

    public void setTypePermis(String typePermis) {
        this.typePermis = typePermis;
    }

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    @Override
    public String toString() {
        return getPrenom() + " " + getNom();
    }
}
