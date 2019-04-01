package excepcions;

/**
 * Excepció generada quan hi ha menys dades.
 */
public class MenysDades extends TestException {
    public MenysDades(String message) {
        super(message);
    }

    public MenysDades(String message, Throwable cause) {
        super(message, cause);
    }
}
