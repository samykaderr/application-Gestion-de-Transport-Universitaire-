package Model;

public class Etudiant extends user {

    private String numCarte;
    private String arretPrincipal;
    private String statutPaiement; // Correction: minuscule 's' pour respecter les conventions

    public Etudiant(int id, String nom , String prenom , String numCarte, String password, String arretPrincipal, String statutPaiement) {
        super(id, nom, prenom, password);
        this.statutPaiement = statutPaiement;
        this.numCarte = numCarte;
        this.arretPrincipal = arretPrincipal;
    }

    // Getters
    public String getStatutPaiement() {
        return statutPaiement;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public String getArretPrincipal() {
        return arretPrincipal;
    }

    // Setters
    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    public void setArretPrincipal(String arretPrincipal) {
        this.arretPrincipal = arretPrincipal;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
}