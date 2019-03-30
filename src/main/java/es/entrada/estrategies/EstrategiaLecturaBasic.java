package es.entrada.estrategies;

import models.Registre;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EstrategiaLecturaBasic implements EstrategiaLectura {

    private final String nomFitxer;
    private final Scanner flux;
    private final String separador;

    public EstrategiaLecturaBasic(String fitxer, String separador) throws IOException{
        this.nomFitxer = fitxer;
        this.flux = new Scanner(new File(fitxer));
        this.separador = separador;
    }

    @Override
    public Registre llegir() {
        if(!flux.hasNextLine()){
            return null;
        }
        String camps[] = flux.nextLine().split(separador);
        return new Registre(camps[0], Float.valueOf(camps[1]));
    }

    @Override
    public void close() throws IOException {
        flux.close();
    }

    @Override
    public String toString(){
        return nomFitxer;
    }
}
