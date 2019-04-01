package adaptadors;

/**
 * Interfície que ha de implementar tot adaptador de dades.
 * @param <T> Tipus de la dada.
 */
public interface Adaptador<T> {
    T adaptar(String registre);
}
