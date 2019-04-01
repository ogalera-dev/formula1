package adaptadors;

import models.Registre;

/**
 * Adaptador per generar les dades d'entrada, converteix un registre llegit en un objecte de tipus Registre
 */
public class AdaptadorEntrada implements Adaptador<Registre> {
    @Override
    public Registre adaptar(String registre) {
        String[] camps = registre.split(",");
        return new Registre(camps[0], Float.valueOf(camps[1]));
    }
}
