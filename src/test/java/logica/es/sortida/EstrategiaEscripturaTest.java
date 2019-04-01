package logica.es.sortida;

import logica.es.sortida.estrategies.EstrategiaEscriptura;
import excepcions.DadesDesiguals;
import excepcions.MenysDades;
import excepcions.MesDades;
import excepcions.TestException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementació de l'estratègia d'escriptura.
 */
public class EstrategiaEscripturaTest implements EstrategiaEscriptura {
    private List<Escriptor.RegistreAcumulat> dades; //<Dades a escriure

    public EstrategiaEscripturaTest(){
        dades = new LinkedList<>();
    }

    @Override
    public void escriure(String nom, String separador, List<Escriptor.RegistreAcumulat> dades) throws FileNotFoundException {
        this.dades.addAll(dades);
    }

    /**
     * Comparació entre les dades calculades i un altre llista de registres.
     * @param altres Llista de registres contra la que es farà la comparació.
     * @throws TestException Si les dades no coincideixen.
     */
    public void comparar(List<Escriptor.RegistreAcumulat> altres) throws TestException{
        Iterator<Escriptor.RegistreAcumulat> iAltre = altres.iterator();
        Iterator<Escriptor.RegistreAcumulat> iJo = dades.iterator();
        int i = 1;
        while(iJo.hasNext() && iAltre.hasNext()){
            Escriptor.RegistreAcumulat meu = iJo.next().calculat();
            Escriptor.RegistreAcumulat altre = iAltre.next();

            //La comparació es fa literalment per evitar problemes amb els decimals...
            if(!meu.toString().equals(altre.toString())){
                throw new DadesDesiguals("Les dúes col·leccions no són iguales en l'element de la posició: "+i+", actual: "+meu+", altre: "+altre);
            }
            i++;
        }
        //Les dades actuals tenen més registres.
        if(iJo.hasNext() && !iAltre.hasNext()){
            throw new MesDades("La col·lecció actual té mes dades, actual: "+dades.size()+", altre: "+altres.size());
        }
        //Les dades actuals tenen menys registres.
        if(iAltre.hasNext() && !iJo.hasNext()){
            throw new MenysDades("La col·lecció actual té menys dades, actual: "+dades.size()+", altre: "+altres.size());
        }
    }

    @Override
    public void close() throws IOException {
        //No s'ha obert res, no cal tancar res...
    }
}
