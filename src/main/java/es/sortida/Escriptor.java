package es.sortida;

import javafx.print.Printer;
import log.Log;
import models.Registre;
import utils.Formats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Escriptor{
    private final int nLectors;
    private int nLectorsFets;
    private final Treballador treballador;

    public Escriptor(String fitxer, int nLectors) throws FileNotFoundException {
        this.nLectors = nLectors;
        this.treballador = new Treballador(fitxer, nLectors);
        this.nLectorsFets = 0;
    }

    public synchronized void buidar(List<Registre> dades){
        synchronized (treballador.sincronitzador){
            treballador.buidar(dades);
        }
        nLectorsFets++;
        if(nLectorsFets == nLectors){
            synchronized (treballador.sincronitzador){
                treballador.sincronitzador.notifyAll();
            }
            nLectorsFets = 0;
        }
    }

    public void parar(){
        treballador.parat = true;
        synchronized (treballador.sincronitzador){
            treballador.sincronitzador.notifyAll();
        }
    }

    private static class Treballador extends Thread{
        private final String fitxer;
        private final List<List<Registre>> feines;
        public boolean parat;
        public final Object sincronitzador;

        public Treballador(String fitxer, int nLectors) throws FileNotFoundException {
            this.fitxer = fitxer;
            this.feines = new ArrayList<>(nLectors);
            this.sincronitzador = new Object();
        }

        public void buidar(List<Registre> dades){
            feines.add(dades);
        }

        public void parar(){
            parat = false;
        }

        @Override
        public void run(){
            try{
                new File(fitxer).createNewFile();
                try(PrintStream escriptor = new PrintStream(fitxer)){
                    while(!parat){
                        synchronized (sincronitzador){
                            if(!parat){
                                List<RegistreAcumulat> calculat = calcular();
                                Collections.sort(calculat);
                                escriure(escriptor, calculat);
                                try{
                                    sincronitzador.wait();
                                } catch (InterruptedException e) {
                                    Log.F1.fatal("Hi ha hagut una excepció en la sincronització, el treballador mor");
                                    throw new RuntimeException();
                                }
                            }
                        }
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
                        registres.add(valor);
                        acumulats.put(registreFeina.moment, valor);
                    }
                    valor.valor += registreFeina.value;
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
            final String moment;

            public RegistreAcumulat(String moment) {
                this.moment = moment;
            }

            float valor;
            int n = 0;

            public String calcular(){
                return Formats.FORMAT_DECIMAL.format(valor/n);
            }

            @Override
            public int compareTo(RegistreAcumulat altre) {
                return n - altre.n;
            }
        }
    }

    @Override public String toString(){
        return treballador.toString();
    }
}
