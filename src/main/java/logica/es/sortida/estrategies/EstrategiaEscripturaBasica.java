package logica.es.sortida.estrategies;

import logica.es.sortida.Escriptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class EstrategiaEscripturaBasica implements EstrategiaEscriptura{

    @Override
    public void escriure(String nom, String separador, List<Escriptor.RegistreAcumulat> calculat) throws FileNotFoundException {
        try(PrintStream escriptor = new PrintStream(nom)) {
            escriure(escriptor, separador, calculat);
        }
    }

    /**
     * Escriu les dades en l'escriptor.
     * @param escriptor Escriptor.
     * @param dades Dades
     */
    private void escriure(PrintStream escriptor, String separador, List<Escriptor.RegistreAcumulat> dades){
        for(Escriptor.RegistreAcumulat dada: dades){
            escriptor.println(dada.moment+separador+dada.calcular());
        }
    }

    @Override
    public void close() throws IOException {

    }
}
