package Model;

public class Chauffeur extends user {
    private String typePermis;

    public Chauffeur(int id, String nom, String prenom, String password, String typePermis) {
        super(id, nom, prenom, password);
        this.typePermis = typePermis;
    }

    public String getTypePermis() {
        return typePermis;
    }

    public void setTypePermis(String typePermis) {
        this.typePermis = typePermis;
    }

    @Override
    public String toString() {
        return getPrenom() + " " + getNom();
    }
}
