package es.sortida;

import log.Log;
import models.Registre;
import sincronitzacio.Sincronitzador;
import sincronitzacio.excepcions.SincronitzacioException;
import utils.Formats;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Escriptor extends Thread{

    private static AtomicInteger nTreballador = new AtomicInteger(0);
    private final String fitxer;
    private final List<List<Registre>> feines;
    public boolean parat;
    private int nFitxer;
    private final Sincronitzador sincronitzador;

    public Escriptor(String fitxer, Sincronitzador sincronitzador){
        super("Escriptor-"+nTreballador.addAndGet(1));
        this.fitxer = fitxer;
        this.feines = new LinkedList<>();
        this.sincronitzador = sincronitzador;
        this.nFitxer = 0;
    }

    public synchronized void buidar(List<Registre> dades){
        feines.add(dades);
    }

    public void parar(){
        parat = false;
    }

    @Override
    public void run(){
        Log.F1.debug("Escriptor iniciat");
        try{
            while(!parat){
                try{
                    sincronitzador.sincEscriptura();
                } catch (SincronitzacioException e) {
                    Log.F1.fatal("Hi ha hagut una excepció en la sincronització, el treballador mor");
                    throw new RuntimeException();
                }
                if(!parat){
                    List<RegistreAcumulat> calculat;
                    synchronized (this){
                        calculat = calcular();
                        feines.clear();
                    }
                    Collections.sort(calculat);
                    try(PrintStream escriptor = new PrintStream(fitxer+nFitxer+".csv")) {
                        escriure(escriptor, calculat);
                    }
                    try{
                        sincronitzador.fiEscriptura();
                    }catch(SincronitzacioException se){
                        Log.F1.fatal("Hi ha hagut una excepció mentre es sincronitzava el final d'escriptura", se);
                    }
                    nFitxer++;
                }
            }
        }catch(IOException ioe){
            Log.F1.fatal("Error al crear el fitxer de sortida", ioe);
        }
    }

    private List<RegistreAcumulat> calcular(){
        Map<String, RegistreAcumulat> acumulats = new HashMap<>(feines.size());
        List<RegistreAcumulat> registres = new ArrayList<>(feines.size());
        for(List<Registre> feina: feines){
            for(Registre registreFeina: feina){
                RegistreAcumulat valor = acumulats.get(registreFeina.moment);
                if(valor == null){
                    valor = new RegistreAcumulat(registreFeina.moment);
                    valor.valor = registreFeina.valor;
                    valor.n = 1;
                    registres.add(valor);
                    acumulats.put(registreFeina.moment, valor);
                }
                valor.valor += registreFeina.valor;
                valor.n++;
            }
        }
        return registres;
    }

    private void escriure(PrintStream escriptor, List<RegistreAcumulat> calcular){
        for(RegistreAcumulat registre: calcular){
            escriptor.print(registre.moment+","+registre.calcular()+"\n");
        }
    }

    @Override public String toString(){
        return fitxer;
    }

    private class RegistreAcumulat implements Comparable<RegistreAcumulat>{
        final long moment;

        public RegistreAcumulat(String moment) {
            this.moment = Long.valueOf(moment);
        }

        float valor;
        int n = 0;

        public String calcular(){
            return Formats.FORMAT_DECIMAL.format(valor/n);
        }

        @Override
        public int compareTo(RegistreAcumulat altre) {
            return (int) (moment - altre.moment);
        }
    }
}
