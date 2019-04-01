package models;

/**
 * Registre del fitxer de dades.
 */
public final class Registre {
    public final String moment;
    public final float valor;

    /**
     * Crea un nou registre immutable.
     * @param moment Moment en que s'ha capturat la dada (segons des de Gener 1970)
     * @param value Velocitat del F1
     */
    public Registre(final String moment, final float value){
        this.moment = moment;
        this.valor = value;
    }

    @Override
    public String toString() {
        return "moment: "+moment+", valor: "+ valor;
    }
}
