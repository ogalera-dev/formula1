package sincronitzacio;

import log.Log;
import sincronitzacio.excepcions.SincronitzacioException;

import java.util.concurrent.atomic.AtomicInteger;

public class SincronitzadorBasic implements Sincronitzador {
    private final Object sincLectura;
    private final Object sincEscriptura;
    private AtomicInteger nLectures;
    private final int nLectors;
    private final long segonsEspera;

    public SincronitzadorBasic(int nLectors, long segonsEspera){
        this.segonsEspera = segonsEspera;
        this.nLectors = nLectors;
        this.sincEscriptura = new Object();
        this.sincLectura = new Object();
        this.nLectures = new AtomicInteger(0);
    }

    @Override
    public void sincLectura() throws SincronitzacioException {
        int nEfectuades = nLectures.addAndGet(1);
        if(nEfectuades == nLectors){
            synchronized (sincEscriptura){
                sincEscriptura.notifyAll();
            }
            nLectures.set(0);
        }
        try{
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
            synchronized (sincEscriptura){
                sincEscriptura.wait();
            }
        }catch(InterruptedException ie){
            throw new SincronitzacioException("Hi ha hagut una interrupció mentre el thread d'escriptura s'esperava...", ie);
        }
    }

    @Override
    public void fiEscriptura() throws SincronitzacioException{
        Log.F1.debug("Fi d'escriptura, hi haurà una espera de "+segonsEspera+" segons fins la pròxima lectura");
        synchronized (sincLectura){
            try{
                synchronized (this){
                    this.wait(segonsEspera*1_000);
                    Log.F1.debug("Ha finalitzat l'espera");
                }
            }catch(InterruptedException ie){
                throw new SincronitzacioException("Hi ha hagut una interrupció mentre s'esperava a la següent lectura", ie);
            }
            Log.F1.debug("Es desperten els lectors");
            sincLectura.notifyAll();
        }
    }
}
