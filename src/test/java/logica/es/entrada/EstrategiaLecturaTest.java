package logica.es.entrada;

import logica.es.entrada.estrategies.EstrategiaLectura;
import models.Registre;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementació d'una estratègia de lectura on es guarden les dades en una llista de registres.
 */
public class EstrategiaLecturaTest implements EstrategiaLectura {
    private Iterator<List<Registre>> registres;//<Llista de registres.
    private int n;

    public EstrategiaLecturaTest(List<List<Registre>> registres){
        this.registres = registres.iterator();
    }

    @Override
    public List<Registre> llegir() {
        if(registres.hasNext()){
            return registres.next();
        }
        //Opps! no queden dades...
        return new LinkedList<>();
    }

    @Override
    public void close() throws IOException {
        //No s'obre res, no cal tancar res...
    }
}
