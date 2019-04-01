package logica.es.entrada.estrategies;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class ConstructorLectura implements ConstructurEstrategiaLectura{

    @Override
    public EstrategiaLectura construir(int midaBuffer, String fitxerLectura, String separador) throws IOException {
        return new EstrategiaLecturaBasic(midaBuffer, fitxerLectura, separador);
    }

    @Override
    public FileFilter filtre() {
        return new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".csv");
            }
        };
    }
}
