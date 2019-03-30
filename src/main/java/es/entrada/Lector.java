package es.entrada;

import es.entrada.estrategies.EstrategiaLectura;
import es.sortida.Escriptor;
import log.Log;
import models.Registre;
import sincronitzacio.Sincronitzador;
import sincronitzacio.excepcions.SincronitzacioException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Lector extends Thread{
    private final static AtomicInteger comptador = new AtomicInteger();
    private List<Registre> contingut;
    private boolean parat;
    private final Escriptor escriptor;
    private final EstrategiaLectura lector;
    private final Sincronitzador sincronitzador;

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
        try{
            while(!parat){
                contingut = new LinkedList<>();
                Registre registre;
                while((registre = lector.llegir()) != null){
                    contingut.add(registre);
                }
                //Fi de fitxer
                escriptor.buidar(contingut);
                sincronitzador.sincronitzar();
            }
            Log.F1.debug("El thread "+Thread.currentThread().getName()+" ha acabat");
        }catch(SincronitzacioException se){
            Log.F1.fatal("Hi ha hagut un error al sinconritzar el lector, el thread "+Thread.currentThread().getName()+", mor");
            throw new RuntimeException();
        }finally{
            try{
                lector.close();
            }catch(IOException ioe){
                Log.F1.error("Error tancant l'stream del fitxer: "+lector.toString());
            }
        }
    }
}
