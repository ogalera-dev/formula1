package excepcions;

/**
 * Excepció generada quan hi ha dades diferents.
 */
public class DadesDesiguals extends TestException {
    public DadesDesiguals(String message) {
        super(message);
    }

    public DadesDesiguals(String message, Throwable cause) {
        super(message, cause);
    }
}
