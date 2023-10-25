package domain;

public class Proba extends Entity<Long> {

    private int varstamin;
    private int varstamax;
    private String proba;


    public Proba(int varstamin, int varstamax, String proba) {
        this.varstamin = varstamin;
        this.varstamax = varstamax;
        this.proba = proba;
    }

    public Proba(){}

    public int getVarstamin() {
        return this.varstamin;
    }

    public void setVarstamin(int vartamin) {
        this.varstamin = vartamin;
    }

    public  int getVarstamax() {
        return this.varstamax;
    }

    public void setVarstamax(int varstamax) {
        this.varstamax = varstamax;
    }

    public  String getProba() {
        return this.proba;
    }

    public void setProba(String proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return "Proba{" +
                "id=" + getId() +
                "varstamin=" + varstamin +
                ", varstamax=" + varstamax +
                ", proba='" + proba + '\'' +
                '}';
    }
}