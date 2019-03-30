import configuracio.ConfiguracioException;
import configuracio.Configurador;
import log.Log;
import logica.Execucio;
import logica.Executador;
import logica.excepcions.ExecucioException;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String ... args) throws IOException, ConfiguracioException, ExecucioException {
        Properties propietats = new Properties();
        propietats.load(Main.class.getResourceAsStream("f1.properties"));
        Configurador conf = new Configurador(propietats);
        Log.F1.debug(conf.toString());

        Execucio execucio = Executador.crear(conf);
        execucio.iniciar();

    }
}
