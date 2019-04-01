package excepcions;

/**
 * Excepció generada quan hi ha més dades.
 */
public class MesDades extends TestException {
    public MesDades(String message) {
        super(message);
    }

    public MesDades(String message, Throwable cause) {
        super(message, cause);
    }
}
