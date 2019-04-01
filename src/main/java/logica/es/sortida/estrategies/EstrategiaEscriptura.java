package logica.es.sortida.estrategies;

import logica.es.sortida.Escriptor;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.util.List;

public interface EstrategiaEscriptura extends Closeable {
    public void escriure(String nom, String separador, List<Escriptor.RegistreAcumulat> dades) throws FileNotFoundException;
}
