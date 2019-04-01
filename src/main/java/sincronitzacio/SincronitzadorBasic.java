package sincronitzacio;

import log.Log;
import sincronitzacio.excepcions.SincronitzacioException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementació bàsica d'un sincronitzador.
 */
public class SincronitzadorBasic implements Sincronitzador {
    private final Object sincLectura; //<Objecte que serveix per sincronitzar els lectors.
    private final Object sincEscriptura; //<Objecte que serveix per sincronitzar els escriptors.
    private int nLectures; //<Nombre de lectors que han llegit.
    private final int nLectors; //<Nombre de lectors total.
    private final long segonsEspera; //<Nombre de segons que hi ha d'haver per cada cicle lectura-escriptura.

    /**
     * Es crea un sincronitzador a partir del nombre de lectors i el temps d'espera.
     * @param nLectors Nombre de lectors
     * @param segonsEspera Segons d'espera entre cicle lectura-escriptura.
     */
    public SincronitzadorBasic(int nLectors, long segonsEspera){
        this.segonsEspera = segonsEspera;
        this.nLectors = nLectors;
        this.sincEscriptura = new Object();
        this.sincLectura = new Object();
        this.nLectures = 0;
    }

    @Override
    public void sincLectura() throws SincronitzacioException {
        //Nova lectura
        synchronized (sincEscriptura){
            nLectures++;
            Log.F1.debug("Lectors: "+nLectures+"/"+nLectors);
            //Si s'han fet totes les lectures, cal avisar a l'escriptor.
            if(nLectures == nLectors){
                sincEscriptura.notifyAll();
                Log.F1.debug("Lector avisa a escriptor");
                nLectures = 0;
            }
        }
        try{
            //El lector espera.
            Log.F1.debug("Lector dormint");
            synchronized (sincLectura){
                sincLectura.wait();
            }
            Log.F1.debug("Lector despertat");
        }catch(InterruptedException ie){
            throw new SincronitzacioException("Hi ha hagut una interrupció mentre el thread de lectura s'esperava...", ie);
        }
    }

    @Override
    public void sincEscriptura() throws SincronitzacioException {
        try{
            Log.F1.debug("Escriptor espera");
            synchronized (sincEscriptura){
                sincEscriptura.wait();
            }
            Log.F1.debug("Escriptor despertat");
        }catch(InterruptedException ie){
            throw new SincronitzacioException("Hi ha hagut una interrupció mentre el thread d'escriptura s'esperava...", ie);
        }
    }

    @Override
    public void fiEscriptura() throws SincronitzacioException{
        Log.F1.debug("Fi d'escriptura, hi haurà una espera de "+segonsEspera+" segons fins el pròxim cicle lectura-escriptura");
        synchronized (sincLectura){
            try{
                synchronized (this){
                    this.wait(segonsEspera*1_000);
                    Log.F1.debug("Fi d'espera, s'avisa als lectors que poden tornar a llegir");
                }
            }catch(InterruptedException ie){
                throw new SincronitzacioException("Hi ha hagut una interrupció mentre s'esperava a la següent lectura", ie);
            }
            //Com que ja es té el monitor, no cal tornar a sincronitzar...
            sincLectura.notifyAll();
        }
    }
}
