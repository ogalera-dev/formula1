package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Log {
    F1("Test practic");

    private final Logger log;

    private Log(String nom){
        log = LogManager.getLogger(nom);
    }

    public void info(String msg){
        log.info(msg);
    }

    public void debug(String msg){
        log.debug(msg);
    }

    public void warn(String msg){
        log.warn(msg);
    }

    public void error(String msg){
        log.error(msg);
    }

    public void fatal(String msg){
        log.fatal(msg);
    }

    public void fatal(String msg, Throwable t){
        log.fatal(msg, t);
    }
}
