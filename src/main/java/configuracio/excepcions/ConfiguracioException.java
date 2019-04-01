package configuracio.excepcions;

/**
 * Excepció generada quan hi ha un error en el fitxer de configuració principal de l'aplicació.
 */
public class ConfiguracioException extends Exception {
    public ConfiguracioException(String message) {
        super(message);
    }

    public ConfiguracioException(String message, Throwable cause) {
        super(message, cause);
    }
}
