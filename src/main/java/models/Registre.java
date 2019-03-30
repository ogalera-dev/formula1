package models;

public final class Registre {
    public final String moment;
    public final float valor;

    public Registre(String moment, float value){
        this.moment = moment;
        this.valor = value;
    }

    @Override
    public String toString() {
        return "moment: "+moment+", valor: "+ valor;
    }
}
