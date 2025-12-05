package Model;

public class user {
    protected int id;
    protected String nom;
    protected String password;
    protected  String prenom;

    public user(int id, String nom, String prenom ,String password) {
        this.id = id;
        this.nom = nom;
        this.password = password;
        this.prenom = prenom;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getPrenom() {
        return prenom;
    }
}
