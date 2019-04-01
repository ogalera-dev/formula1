package utils;

import log.Log;

/**
 * Parametres d'execució.
 */
public class Parametres {
    public String pathConf; //<Path del fitxer de configuració, f1.properties per defecte.
    public String pathLog; //<Path del fitxer de configuració de log.
    public boolean ajuda; //<Si hi ha flag d'ajuda.

    public Parametres(String ... args){
        pathConf = "f1.properties";
        pathLog = "log4j2.xml";
        ajuda = false;
        int i = 0;
        while(i < args.length){
            switch(args[i]){
                case "-h":
                    ajuda = true;
                break;
                case "-l":
                    i++;
                    if(i < args.length){
                        pathLog = args[i];
                    }else{
                        Log.F1.warn("Hi ha l'opció -l però no hi ha més paràmetres, s'ingora");
                    }
                    break;
                case "-c":
                    i++;
                    if(i < args.length){
                        pathConf = args[i];
                    }else{
                        Log.F1.warn("Hi ha l'opció -c però no hi ha més paràmetres, s'ignora");
                    }
                    break;
                default:
                    Log.F1.debug("Opció "+args[i]+" desconeguda");
                    break;
            }
            i++;
        }
    }
}
