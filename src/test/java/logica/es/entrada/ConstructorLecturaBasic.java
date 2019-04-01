package logica.es.entrada;

import logica.es.entrada.estrategies.ConstructurEstrategiaLectura;
import logica.es.entrada.estrategies.EstrategiaLectura;
import models.Registre;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constructur de lectors amb només una font de dades.
 */
public class ConstructorLecturaBasic implements ConstructurEstrategiaLectura {
    private Map<String, List<List<Registre>>> dades;

    public ConstructorLecturaBasic(){
        this.dades = new HashMap<>();
    }

    public void registrar(String fitxerLectura, List<List<Registre>> dada){
        dades.put(fitxerLectura, dada);
    }

    @Override
    public EstrategiaLectura construir(int midaBuffer, String fitxerLectura, String separador) throws IOException {
        EstrategiaLecturaTest estrategia = new EstrategiaLecturaTest(dades.get(fitxerLectura));
        return estrategia;
    }

    @Override
    public FileFilter filtre() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
    }
}
