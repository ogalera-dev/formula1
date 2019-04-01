import configuracio.excepcions.ConfiguracioException;
import configuracio.Configurador;
import generador.Generador;
import generador.GeneradorException;
import log.Log;
import logica.Execucio;
import logica.Executador;
import logica.excepcions.ExecucioException;
import utils.Parametres;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void ajuda(){
        System.out.println("Ajuda");
        System.out.println("java -jar programa [-h] [-c pathConfiguracio] [-l pathConfiguracioLog]");
        System.out.println("On:");
        System.out.println("\t-c: Especifica la ruta del fitxer de configuració (f1.properties per defecte)");
        System.out.println("\t-l: Especifica la ruta del fitxer de configuració del log (log4j2.xml per defecte)");
        System.out.println("\t-h: Mostra l'ajuda");
        System.out.println("Per més informació, consultar: https://github.com/ogalera-dev/userzoom");
    }

    public static void main(String ... args) throws IOException, ConfiguracioException, ExecucioException, GeneradorException {
        Parametres parametres = new Parametres(args);
        if(parametres.ajuda){
            ajuda();
            return;
        }

        System.setProperty("log4j.configurationFile", parametres.pathLog);
        Log.F1.debug("Com a fitxer de configuració de log s'ha agafat: "+parametres.pathLog);
        Log.F1.debug("Com a fitxer de configuració de l'aplicació s'agafa "+parametres.pathConf);
        Properties propietats = new Properties();
        propietats.load(Main.class.getResourceAsStream(parametres.pathConf));
        Configurador conf = new Configurador(propietats);

        Log.F1.debug("Paràmetres de configuració");
        Log.F1.debug(conf.toString());

        Generador gen = null;
        if(conf.configuracioGenerador != null){
            Log.F1.debug("Hi ha fase de generació de dades");
            gen = ferGenerador(conf.configuracioGenerador);
            gen.iniciar();
        }

        Execucio execucio = Executador.crear(conf);
        execucio.iniciar();
    }

    private static Generador ferGenerador(String rutaConfiguracio) throws IOException, ConfiguracioException, GeneradorException {
        Properties propietats = new Properties();
        propietats.load(Main.class.getResourceAsStream(rutaConfiguracio));
        generador.configuracio.Configurador conf = new generador.configuracio.Configurador(propietats);
        Log.F1.debug("Paràmetres de configuració");
        Log.F1.debug(conf.toString());
        return new Generador(conf);
    }
}
