package sincronitzacio.excepcions;

/**
 * Excepció generada quan hi ha algún problema amb la sincronització.
 */
public class SincronitzacioException extends Exception {

    public SincronitzacioException(String message) {
        super(message);
    }

    public SincronitzacioException(String message, Throwable t) {
        super(message, t);
    }
}
