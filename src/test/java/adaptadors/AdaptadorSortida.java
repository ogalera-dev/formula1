package adaptadors;

import logica.es.sortida.Escriptor;

/**
 * Adaptador de sortida, converteix un registre llegit en un objecte de tipus RegistreAcumulat.
 */
public class AdaptadorSortida implements Adaptador<Escriptor.RegistreAcumulat>{
    @Override
    public Escriptor.RegistreAcumulat adaptar(String registre) {
        String[] camps = registre.split(";");
        long moment = Long.valueOf(camps[0]);
        float valor =  Float.valueOf(camps[1]);
        int n = Integer.valueOf(camps[2]);
        return new Escriptor.RegistreAcumulat(moment, valor, n);
    }
}
