package logica;

import configuracio.Configurador;
import logica.es.entrada.Lector;
import logica.es.entrada.estrategies.ConstructorLectura;
import logica.es.entrada.estrategies.ConstructurEstrategiaLectura;
import logica.es.sortida.Escriptor;
import logica.es.sortida.estrategies.EstrategiaEscriptura;
import logica.es.sortida.estrategies.EstrategiaEscripturaBasica;
import log.Log;
import logica.excepcions.ExecucioException;
import sincronitzacio.Sincronitzador;
import sincronitzacio.SincronitzadorBasic;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe per crear una nova simulació.
 */
public class Executador {

    /**
     * Es crea una nova simulació a partir de la configuració.
     * @param conf Configuració a utilitzar.
     * @return Una nova simulació.
     * @throws ExecucioException Si algún dels paràmetres de la configuració no és valid.
     */
    public static Execucio crear(Configurador conf) throws ExecucioException {
        return crear(conf, new ConstructorLectura(), new EstrategiaEscripturaBasica());
    }

    public static Execucio crear(Configurador conf, ConstructurEstrategiaLectura constructorLectura, EstrategiaEscriptura estrategiaEscriptura) throws ExecucioException{
        //Directori d'entrada, ha d'existir.
        File dirEntrada = new File(conf.directoriEntrada);
        if(!dirEntrada.exists() || !dirEntrada.isDirectory()){
            throw new ExecucioException("El directori d'entrada no existeix o no és valid ["+dirEntrada.getAbsolutePath()+"]");
        }
        Log.F1.debug("El directori d'entrada és: "+dirEntrada.getAbsolutePath());

        //Directori de sortida, ha d'existir.
        File dirSortida = new File(conf.directoriSortida);
        if(!dirSortida.exists()){
            Log.F1.debug("El directori de sortida no existeix, es crea...");
            dirSortida.mkdirs();
        }

        //No es pot especificar un fitxer que existeixi i no sigui un directori...
        if(!dirSortida.isDirectory()){
            throw new ExecucioException("El directori de sortida no és valid ["+dirSortida.getAbsolutePath()+"]");
        }
        Log.F1.debug("El directori de sortida és: "+dirSortida.getAbsolutePath());

        //Fitxers a llegir
        List<String> fitxersLectura = carregarFitxersLlegir(dirEntrada, constructorLectura.filtre());

        //Creació del sincronitzador
        Sincronitzador sincronitzador = new SincronitzadorBasic(fitxersLectura.size(), conf.segonsLectura);

        //Creació de l'escriptor
        String fitxerSortida = dirSortida.getAbsolutePath()+File.separator+conf.nomFitxerSortia;
        Escriptor escriptor = new Escriptor(estrategiaEscriptura, fitxerSortida, conf.separadorSortida, sincronitzador);
        Log.F1.debug("S'ha creat l'escriptor");

        //Creació dels lectors
        List<Lector> lectors = new LinkedList<>();


        for(String fitxerLectura: fitxersLectura){
            try{
                lectors.add(new Lector(escriptor, constructorLectura.construir(1024, fitxerLectura, conf.separadorEntrada), sincronitzador));
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

    private static List<String> carregarFitxersLlegir(File dirEntrada, FileFilter filtre){
        List<String> fitxersLectura = new LinkedList<>();
        //Es llisten tots els fitxers del directori d'entrada filtrant tots aquells que no tenen extensió .csv
        for(File fitxer: dirEntrada.listFiles(filtre)){
            fitxersLectura.add(fitxer.getAbsolutePath());
        }
        return fitxersLectura;
    }

}
