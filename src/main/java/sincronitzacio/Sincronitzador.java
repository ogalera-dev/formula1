package sincronitzacio;

import sincronitzacio.excepcions.SincronitzacioException;

/**
 * Interfície que ha d'implementar qualsevol de les sincronitzacions.
 */
public interface Sincronitzador {
    /**
     * S'ha d'indicar quan finalitza la lectura, es bloqueja fins que toca tornar a llegir.
     * @throws SincronitzacioException Si hi ha algun problema amb la sincronització.
     */
    public void sincLectura() throws SincronitzacioException;

    /**
     * S'ha d'indicar quan finalitza l'escriptura, es bloqueja fins que toca tornar a escriure.
     * @throws SincronitzacioException Si hi ha algun problema amb la sincronització.
     */
    public void sincEscriptura() throws SincronitzacioException;

    /**
     * S'ha d'indicar quan finalitza la tasca d'escriptura, ha de servir per iniciar el temps d'espera dels lectors.
     * @throws SincronitzacioException Si hi ha algun problema amb la sincronització.
     */
    public void fiEscriptura() throws SincronitzacioException;
}
