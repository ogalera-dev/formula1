package logica.es.entrada.estrategies;

import models.Registre;

import java.io.Closeable;
import java.util.List;

/**
 * Interficie per les diferents estratègies de lectura.
 */
public interface EstrategiaLectura extends Closeable {
    public List<Registre> llegir();
}