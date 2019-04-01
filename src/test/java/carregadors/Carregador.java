package carregadors;

import adaptadors.Adaptador;
import excepcions.TestException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe d'utilitat amb la lògica per carregar les dades.
 */
public abstract class Carregador {
    /**
     * Carrega les dades del fitxer en una llista d'objectes en base a l'adaptador.
     * @param nomFitxer Fitxer amb les dades.
     * @param adaptador Nom de l'adaptador
     * @param <T> Tipus del registre
     * @return Llista amb els objectes llegits
     * @throws TestException Si hi ha algun problema llegint les dades.
     */
    public static <T> List<T> carregar(File nomFitxer, Adaptador<T> adaptador) throws TestException {
        List<T> registres = null;
        try(BufferedReader br = new BufferedReader(new FileReader(nomFitxer))){
            registres = new LinkedList<>();
            String linia = null;
            while((linia = br.readLine())!=null){
                registres.add(adaptador.adaptar(linia));
            }
        }catch(Throwable t){
            throw new TestException("Error carregant les dades, ", t);
        }
        return registres;
    }

    /**
     * Carrega les dades del fitxer en una llista d'objectes en base a l'adaptador.
     * @param nomFitxer Nom del fitxer amb les dades.
     * @param adaptador Nom de l'adaptador
     * @param <T> Tipus del registre
     * @return Llista amb els objectes llegits
     * @throws TestException Si hi ha algun problema llegint les dades.
     */
    public static <T> List<T> carregar(String nomFitxer, Adaptador<T> adaptador) throws TestException {
        return carregar(new File(nomFitxer), adaptador);
    }

}
