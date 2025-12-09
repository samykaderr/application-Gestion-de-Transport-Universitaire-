package Model;

public class Bus {
    private int id;
    private String matricule; // Renommé de numeroPlaque à matricule
    private String marque;
    private int capacite;

    public Bus(int id, String matricule, String marque, int capacite) {
        this.id = id;
        this.matricule = matricule;
        this.marque = marque;
        this.capacite = capacite;
    }

    // Getters
    public int getId() { return id; }
    public String getMatricule() { return matricule; }
    public String getMarque() { return marque; }
    public int getCapacite() { return capacite; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    @Override
    public String toString() {
        return marque + " (" + matricule + ")";
    }
}