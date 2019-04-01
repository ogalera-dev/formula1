package generador.configuracio;

/**
 * Excepció generada quan hi ha algún problema amb la configuració del generador.
 */
public class ConfiguradorException extends Exception {
    public ConfiguradorException(String message) {
        super(message);
    }

    public ConfiguradorException(String message, Throwable cause) {
        super(message, cause);
    }
}
