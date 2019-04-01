package logica.es.entrada.estrategies;

import java.io.FileFilter;
import java.io.IOException;

/**
 * Interfície que ha de implementar tot constructor d'estratègies de lectura.
 */
public interface ConstructurEstrategiaLectura {
    /**
     * Constructor a partir de la mida del buffer, el nom del fitxer i el separador entre camps.
     * @param midaBuffer Mida del buffer que s'utilitzarà en la lectura.
     * @param fitxerLectura Nom del fitxer de dades font.
     * @param separador Separador entre camps
     * @return Estrategia de lectura
     * @throws IOException Si hi ha algún problema llegint les dades.
     */
    public EstrategiaLectura construir(int midaBuffer, String fitxerLectura, String separador)throws IOException;

    public FileFilter filtre();
}
