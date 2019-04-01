package excepcions;

/**
 * Excepció generada quan hi ha algún problema amb el test.
 */
public class TestException extends Exception {
    public TestException(String message) {
        super(message);
    }

    public TestException(String message, Throwable cause) {
        super(message, cause);
    }
}
