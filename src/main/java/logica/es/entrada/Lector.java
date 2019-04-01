package logica.es.entrada;

import logica.es.entrada.estrategies.EstrategiaLectura;
import logica.es.sortida.Escriptor;
import log.Log;
import models.Registre;
import sincronitzacio.Sincronitzador;
import sincronitzacio.excepcions.SincronitzacioException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lector per llegir les dades que provenen dels fitxers "font". El fitxer s'obra en la creació i s'ha de tancar explicitament
 * amb el mètode parar
 */
public class Lector extends Thread{
    private final static AtomicInteger comptador = new AtomicInteger(); //<Per generar els identificadors dels threads.
    private boolean parat; //<Indica si el treballador està o no parat.
    private final Escriptor escriptor; //<Escriptor que anirà recollint les dades llegides.
    private final EstrategiaLectura lector; //<Estratègia per llegir les dades.
    private final Sincronitzador sincronitzador; //<Sincronitzador per saber quan s'ha de llegir

    public Lector(Escriptor escriptor, EstrategiaLectura lector, Sincronitzador sincronitzador){
        super("Lector-"+comptador.addAndGet(1));
        this.escriptor = escriptor;
        this.lector = lector;
        this.sincronitzador = sincronitzador;
    }

    public void parar(){
        parat = true;
    }

    @Override
    public void run(){
        Log.F1.debug("Lector iniciat ["+lector.toString()+"]");
        try{
            while(!parat){
                List<Registre> contingut = new LinkedList<>();
                for(Registre registre: lector.llegir()){
                    Log.F1.debug("Llegit "+registre.toString());
                    contingut.add(registre);
                }
                //Fi de fitxer
                Log.F1.debug("Fi lectura");
                escriptor.buidar(contingut);
                sincronitzador.sincLectura();
            }
            Log.F1.debug("El thread "+Thread.currentThread().getName()+" ha acabat");
        }catch(SincronitzacioException se){
            Log.F1.fatal("Hi ha hagut un error al sinconritzar el lector, el thread "+Thread.currentThread().getName()+", mor");
            throw new RuntimeException();
        }finally{
            try{
                //Hem acabat, cal tancar el fitxer de lectura.
                lector.close();
            }catch(IOException ioe){
                Log.F1.error("Error tancant l'stream del fitxer: "+lector.toString());
            }
        }
    }
}
