package generador;


import generador.configuracio.Configurador;
import log.Log;
import utils.Formats;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe per crear els generadosr de dades, fa de simulació pels sensors.
 */
public class Generador {

    private static final Random RANDOM = new Random(); //<Instància única per generar valors aleatoris (simulacions dels sensors).
    private List<Treballador> treballadors; //<Generadors

    /**
     * Es crea el generador a partir de la configuració.
     * @param conf
     * @throws GeneradorException
     */
    public Generador(Configurador conf) throws GeneradorException {

        File directoriDades = new File(conf.dirDades);
        //El directori on s'han de injectar les dades ha d'existir.
        if(!directoriDades.exists()){
            throw new GeneradorException("El directori de dades no existeix, directori: "+directoriDades.getAbsolutePath());
        }

        List<File> fitxers = new ArrayList<>();
        //S'injectarà dades a tots els fitxers amb extensió .csv
        for(File fitxer: directoriDades.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".csv");
            }
        })){
            fitxers.add(fitxer);
        }

        int nGenerador = 0;
        treballadors = new ArrayList<>(fitxers.size());
        for(File fitxer: fitxers){
            treballadors.add(new Treballador(++nGenerador, conf, fitxer));
        }

        Log.GENERADOR.debug(treballadors.size()+" generadors registrats");
    }

    /**
     * Inicia els generadors.
     */
    public void iniciar(){
        for(Treballador treballador: treballadors){
            treballador.start();
        }
    }

    /**
     * Para els generadors.
     */
    public void parar(){
        for(Treballador treballador: treballadors){
            treballador.parar();
        }
    }

    /**
     * Generador
     */
    private class Treballador extends Thread{
        private boolean parat; //<Estat del generador.
        private Configurador conf; //<Configuració del generador.
        private final File fitxer; //<Fitxer assignat al generador

        /**
         * Crea un nou generador.
         * @param nGenerador Identificador
         * @param conf Configuració
         * @param fitxer Fitxer on ha d'injectar les dades
         */
        public Treballador(int nGenerador, Configurador conf, File fitxer){
            super("Generador-"+nGenerador);
            this.conf = conf;
            this.fitxer = fitxer;
        }

        /**
         * Para el treballador.
         */
        public void parar(){
            parat = true;
        }

        @Override
        public void run() {
            Log.GENERADOR.debug("S'inicia el generador pel fitxer: "+fitxer.getAbsolutePath());
            try{
                while(!parat){
                    //Es generaran les dades en períodes de segonsInterval
                    synchronized (this){
                        Log.GENERADOR.debug("S'espera "+conf.segonsInterval+" segons");
                        wait(conf.segonsInterval*1_000);
                    }
                    //El treballador no ha parat, es generen noves dades.
                    if(!parat){
                        //Nombre de registres a generar.
                        int nRegistres = Generador.RANDOM.nextInt(conf.maxNombreRegistres);
                        if(nRegistres < conf.minNombreRegistres){
                            nRegistres = conf.minNombreRegistres;
                        }
                        Log.GENERADOR.debug("S'escriuran "+nRegistres+" registres nous en el fitxer: "+fitxer.getAbsolutePath());
                        try(PrintWriter pw = new PrintWriter(new FileWriter(fitxer,true))) {
                            //Es generan les dades...
                            for (int i = 0; i < nRegistres; i++) {
                                float velocitat = conf.velocitatBase + ((Generador.RANDOM.nextBoolean() ? 1 : -1) * ((Generador.RANDOM.nextFloat()*Float.MAX_VALUE) % conf.desviacioVelocitat));
                                //No permetem valors negatius en la velocitat.
                                if (velocitat < 0) {
                                    velocitat = 0;
                                }
                                //Escriure i fer públiques les dades.
                                pw.print((System.currentTimeMillis() / 1_000) + conf.separador + Formats.FORMAT_DECIMAL.format(velocitat)+"\n");
                            }
                            pw.flush();
                        } catch (IOException e) {
                            Log.GENERADOR.fatal(e);
                            throw new RuntimeException("Això no pot passar, ja es sap que el fitxer existeix!");
                        }
                    }
                }
                Log.GENERADOR.debug("Parat");
            } catch (InterruptedException ie){
                throw new RuntimeException("S'ha interromput l'espera del generador");
            }
        }
    }
}
