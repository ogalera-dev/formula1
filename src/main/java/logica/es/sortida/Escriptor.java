package logica.es.sortida;

import logica.es.sortida.estrategies.EstrategiaEscriptura;
import log.Log;
import models.Registre;
import sincronitzacio.Sincronitzador;
import sincronitzacio.excepcions.SincronitzacioException;
import utils.Formats;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread que escriu les dades recollides dels diferents fitxers en el fitxer resultat. Cada vegada que efectua una
 * escriptura (de totes les dades recollides dels lectors) crea un nou fitxer.
 */
public class Escriptor extends Thread{
    private EstrategiaEscriptura estrategia; //<Estratègia que s'utilitzarà per escriure les dades en el fitxer de sortida.
    private static AtomicInteger nTreballador = new AtomicInteger(0); //<Per marcar cada treballador
    private final String fitxer; //<Nom del fitxer que es generarà.
    private final List<List<Registre>> dades; //<Dades recollides i que s'han de escriure.
    public boolean parat; //<Indica si el thread està o no parat.
    private int nFitxer; //<Número del fitxer escrit.
    private final Sincronitzador sincronitzador; //<Sincronitzador per saber quan s'ha d'escriure.
    private final String separador; //<Separador entre camps del registre.

    /**
     * Crea un nou escriptor.
     * @param fitxer Nom del fitxer que s'utilitzarà, es concatenarà el número del fitxer al final (fitxer_#)
     * @param sincronitzador Sincronitzador per l'escriptor.
     */
    public Escriptor(EstrategiaEscriptura estrategia, String fitxer, String separador, Sincronitzador sincronitzador){
        super("Escriptor-"+nTreballador.addAndGet(1));
        this.estrategia = estrategia;
        this.fitxer = fitxer;
        this.dades = new LinkedList<>();
        this.sincronitzador = sincronitzador;
        this.nFitxer = 1;
        this.separador = separador;
    }

    /**
     * Afegeix noves dades pendents a escriure.
     * @param dades Dades que s'escriuran.
     */
    public synchronized void buidar(List<Registre> dades){
        this.dades.add(dades);
    }

    /**
     * Para el thread.
     */
    public void parar(){
        parat = false;
    }

    @Override
    public void run(){
        Log.F1.debug("Escriptor iniciat");
        try{
            while(!parat){
                try{
                    //Es sincronitza l'escriptor.
                    sincronitzador.sincEscriptura();
                } catch (SincronitzacioException e) {
                    //Això no ha de passar mai!
                    Log.F1.fatal("Hi ha hagut una excepció en la sincronització, el treballador mor");
                    throw new RuntimeException();
                }
                if(!parat){
                    List<RegistreAcumulat> calculat;
                    synchronized (this){
                        calculat = agrupar();
                        dades.clear();
                    }

                    //Ordenem les dades de forma ascendent.
                    Collections.sort(calculat);

                    //Es crea el nou fitxer.
                    estrategia.escriure(fitxer+nFitxer+".csv", separador, calculat);

                    try{
                        //Es marca el fi de lectura.
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

    /**
     * Agrupa les dades pel camp moment.
     * @return Dades agrupades.
     */
    private List<RegistreAcumulat> agrupar(){
        /**
         * S'acumulen les dades amb ajuda d'un hashMap on la clau és el moment en que s'ha recollit la dada
         * i el valor un agregat del registre i el nombre de vegades que s'ha trobat aquella clau.
         */
        Map<String, RegistreAcumulat> acumulats = new HashMap<>(dades.size());
        List<RegistreAcumulat> registres = new ArrayList<>(dades.size());
        for(List<Registre> feina: dades){
            for(Registre registreFeina: feina){
                RegistreAcumulat valor = acumulats.get(registreFeina.moment);
                if(valor == null){
                    valor = new RegistreAcumulat(Long.valueOf(registreFeina.moment), 0.0f, 0);
                    registres.add(valor);
                    acumulats.put(registreFeina.moment, valor);
                }
                valor.valor += registreFeina.valor;
                valor.n++;
            }
        }
        return registres;
    }

    @Override public String toString(){
        return fitxer;
    }

    /**
     * Classe que conté les dades en format agrupat pel moment. Es comparable pel moment.
     */
    public static class RegistreAcumulat implements Comparable<RegistreAcumulat>{
        public final long moment;
        private float valor;
        private int n;

        public RegistreAcumulat(long moment, float valor, int n){
            this.moment = moment;
            this.valor = valor;
            this.n = n;
        }

        public String calcular(){
            return Formats.FORMAT_DECIMAL.format(valor/n);
        }

        public RegistreAcumulat calculat(){
            RegistreAcumulat calculat = new RegistreAcumulat(moment, valor,n);
            calculat.valor = calculat.valor / calculat.n;
            return calculat;
        }

        @Override
        public int compareTo(RegistreAcumulat altre) {
            return (int) (moment - altre.moment);
        }

        public boolean igual(RegistreAcumulat altre){
            return moment == altre.moment && valor == altre.valor && n == altre.n;
        }

        @Override
        public String toString(){
            return moment+";"+Formats.FORMAT_DECIMAL.format(valor)+";"+n;
        }
    }
}
