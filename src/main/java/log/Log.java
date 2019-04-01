package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enumeració amb tots els canals de log disponibles, s'agrupa així per facilitar la redirecció dels canals.
 */
public enum Log {
    F1("F1"), //<Log principal de l'aplicació.
    GENERADOR("GENERADOR"), //<Log pels generadors.
    TEST("TEST"); //Log pels testos

    private final Logger log;

    private Log(String nom){
        log = LogManager.getLogger(nom);
    }

    public void info(String msg){
        log.info(msg);
    }

    public void info(String msg, Throwable t){
        log.info(msg, t);
    }

    public void debug(String msg){
        log.debug(msg);
    }

    public void debug(String msg, Throwable t){
        log.debug(msg, t);
    }

    public void warn(String msg){
        log.warn(msg);
    }

    public void warn(String msg, Throwable t){
        log.warn(msg, t);
    }

    public void error(String msg){
        log.error(msg);
    }

    public void error(String msg, Throwable t){
        log.error(msg, t);
    }

    public void fatal(String msg){
        log.fatal(msg);
    }

    public void fatal(String msg, Throwable t){
        log.fatal(msg, t);
    }

    public void fatal(Throwable t){
        log.fatal(t);
    }
}
