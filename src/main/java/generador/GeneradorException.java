package generador;

/**
 * Excepció generada quan hi ha algún problema amb el generador.
 */
public class GeneradorException extends Exception {
    public GeneradorException(String message) {
        super(message);
    }

    public GeneradorException(String message, Throwable cause) {
        super(message, cause);
    }
}
