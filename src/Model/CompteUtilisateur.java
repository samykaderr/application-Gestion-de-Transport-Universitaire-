package Model;

public class CompteUtilisateur {
    private int id;
    private String email;
    private String motDePasse;
    private String role;

    public CompteUtilisateur(int id, String email, String motDePasse, String role) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

