package logica.excepcions;

/**
 * Excepció generada durant l'execució de l'aplicació.
 */
public class ExecucioException extends Exception{
    public ExecucioException(String message) {
        super(message);
    }

    public ExecucioException(String message, Throwable cause) {
        super(message, cause);
    }
}
