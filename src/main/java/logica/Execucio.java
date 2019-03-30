package logica;

import es.entrada.Lector;
import es.sortida.Escriptor;
import logica.excepcions.ExecucioException;

import java.util.List;

public class Execucio {
    private final List<Lector> lectors;
    private final Escriptor escriptor;
    private Estat estat;

    public Execucio(List<Lector> lectors, Escriptor escriptor){
        this.estat = Estat.NOU;
        this.lectors = lectors;
        this.escriptor = escriptor;
    }

    public void iniciar() throws ExecucioException {
        if(estat != Estat.NOU){
            throw new ExecucioException("L'execució ja s'ha iniciat anteriorment");
        }
        escriptor.start();
        for(Lector lector: lectors){
            lector.start();
        }
        estat = Estat.INICIAT;
    }

    public void parar() throws ExecucioException{
        if(estat == Estat.INICIAT){
            throw new ExecucioException("No s'està executant");
        }
        for(Lector lector: lectors){
            lector.parar();
        }
        escriptor.parar();
        estat = Estat.PARAT;
    }

    private enum Estat{
        NOU,
        INICIAT,
        PARAT;
    }
}
