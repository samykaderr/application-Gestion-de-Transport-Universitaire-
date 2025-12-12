package Model;

public class Etudiant {

    private int id;
    private String nom;
    private String prenom;
    private String email; // Ajout du champ email
    private String numCarte;
    private String arretPrincipal;
    private String statutPaiement; // Correction: minuscule 's' pour respecter les conventions
    private int idCompte;

    public Etudiant(int id, String nom, String prenom, String email, String numCarte, String arretPrincipal, String statutPaiement, int idCompte) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numCarte = numCarte;
        this.arretPrincipal = arretPrincipal;
        this.statutPaiement = statutPaiement;
        this.idCompte = idCompte;
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

    public String getEmail() {
        return email;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public String getArretPrincipal() {
        return arretPrincipal;
    }

    public int getIdCompte() {
        return idCompte;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    public void setArretPrincipal(String arretPrincipal) {
        this.arretPrincipal = arretPrincipal;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }
}