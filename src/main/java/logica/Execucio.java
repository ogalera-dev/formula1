package logica;

import logica.es.entrada.Lector;
import logica.es.sortida.Escriptor;
import logica.excepcions.ExecucioException;

import java.util.List;

/**
 * Representa l'execució d'una simulació.
 */
public class Execucio {
    private final List<Lector> lectors; //<Lectors creats.
    private final Escriptor escriptor; //<Escriptors creats.
    private Estat estat; //<Estat en que es troba la simulació.

    /**
     * Crea una nova simulació.
     * @param lectors Lectors.
     * @param escriptor Escriptors.
     */
    public Execucio(List<Lector> lectors, Escriptor escriptor){
        this.estat = Estat.NOU;
        this.lectors = lectors;
        this.escriptor = escriptor;
    }

    /**
     * Inicia l'execució.
     * @throws ExecucioException Si hi ha algún problema amb l'execució.
     */
    public void iniciar() throws ExecucioException {
        if(estat != Estat.NOU){
            throw new ExecucioException("L'execució ja s'ha iniciat anteriorment");
        }
        //Primer s'inicien l'escriptor.
        escriptor.start();

        //Llavors s'inicien tots els lectors.
        for(Lector lector: lectors){
            lector.start();
        }

        //Considerem que ja s'està en estat INICIAT.
        estat = Estat.INICIAT;
    }

    /**
     * Para la simulació.
     * @throws ExecucioException Si hi ha algún problema parant la simulació.
     */
    public void parar() throws ExecucioException{
        if(estat != Estat.INICIAT){
            throw new ExecucioException("No s'està executant");
        }
        //Primer es paren tots els lectors.
        for(Lector lector: lectors){
            lector.parar();
        }

        //Llavors es paren tots els escriptors.
        escriptor.parar();

        //Es considera que ja s'està en estat PARAT
        estat = Estat.PARAT;
    }

    /**
     * Possibles estats de la simulació.
     */
    private enum Estat{
        NOU,
        INICIAT,
        PARAT;
    }
}
