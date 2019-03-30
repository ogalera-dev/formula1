package logica;

import configuracio.Configurador;
import es.entrada.Lector;
import es.entrada.estrategies.EstrategiaLectura;
import es.entrada.estrategies.EstrategiaLecturaBasic;
import es.sortida.Escriptor;
import log.Log;
import logica.excepcions.ExecucioException;
import sincronitzacio.Sincronitzador;
import sincronitzacio.SincronitzadorBasic;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Executador {
    public static Execucio crear(Configurador conf) throws ExecucioException {
        //Directori d'entrada
        File dirEntrada = new File(conf.directoriEntrada);
        if(!dirEntrada.exists() || !dirEntrada.isDirectory()){
            throw new ExecucioException("El directori d'entrada no existeix o no és valid ["+dirEntrada.getAbsolutePath()+"]");
        }
        Log.F1.debug("El directori d'entrada és: "+dirEntrada.getAbsolutePath());

        //Directori de sortida
        File dirSortida = new File(conf.directoriSortida);
        if(!dirSortida.exists()){
            Log.F1.debug("El directori de sortida no existeix, es crea...");
            dirSortida.mkdirs();
        }
        if(!dirSortida.isDirectory()){
            throw new ExecucioException("El directori de sortida no és valid ["+dirSortida.getAbsolutePath()+"]");
        }
        Log.F1.debug("El directori de sortida és: "+dirSortida.getAbsolutePath());

        //Fitxers a llegir
        List<String> fitxersLectura = carregarFitxersLlegir(dirEntrada);

        //Creació del sincronitzador
        Sincronitzador sincronitzador = new SincronitzadorBasic(fitxersLectura.size(), conf.segonsLectura);

        //Creació de l'escriptor
        String fitxerSortida = dirSortida.getAbsolutePath()+File.separator+conf.nomFitxerSortia;
        Escriptor escriptor = new Escriptor(fitxerSortida, sincronitzador);
        Log.F1.debug("S'ha creat l'escriptor");

        //Creació dels lectors
        List<Lector> lectors = new LinkedList<>();


        for(String fitxerLectura: fitxersLectura){
            try{
                EstrategiaLectura estrategia = new EstrategiaLecturaBasic(fitxerLectura, conf.separadorEntrada);
                lectors.add(new Lector(escriptor, estrategia, sincronitzador));
            }catch(IOException ioe){
                //Shhht! no pot haver-hi una excepció d'aquest tipus perquè el fitxer l'hem llistat...
            }
        }
        Log.F1.debug("S'han creat "+lectors.size()+" lectors");

        //Creació de l'execució
        Execucio execucio = new Execucio(lectors, escriptor);
        Log.F1.debug("S'ha creat l'execució");

        return execucio;
    }

    private static List<String> carregarFitxersLlegir(File dirEntrada){
        List<String> fitxersLectura = new LinkedList<>();
        //Es llisten tots els fitxers del directori d'entrada filtrant tots aquells que no tenen extensió .csv
        for(File fitxer: dirEntrada.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".csv");
            }
        })){
            fitxersLectura.add(fitxer.getAbsolutePath());
        }
        return fitxersLectura;
    }

}
