package logica.es.entrada.estrategies;

import log.Log;
import models.Registre;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Estratègia simple de lectura, s'obre el fitxer al inici i s'ha de tancar de forma explícita (close).
 */
public class EstrategiaLecturaBasic implements EstrategiaLectura {

    /**
     * Nom del fitxer a llegir
     */
    private final String nomFitxer;

    /**
     * Separador entre camps
     */
    private final String separador;

    /**
     * Canal de lectura
     */
    private final FileChannel channel;

    /**
     * Buffer de bytes per llegir del canal
     */
    private final ByteBuffer buffer;

    /**
     * Crea una nova estratègia simple de lectura
     * @param midaBuffer Mida del buffer (&gt; 0)
     * @param fitxer Ruta del fitxer a llegir (ha d'existir)
     * @param separador Cadena de caràcters que fan de separador
     * @throws IOException En cas que no existeixi el fitxer que es vol llegir
     */
    public EstrategiaLecturaBasic(int midaBuffer, String fitxer, String separador) throws IOException{
        this.nomFitxer = fitxer;
        this.channel = new FileInputStream(new File(fitxer)).getChannel();
        this.buffer = ByteBuffer.allocate(midaBuffer);
        this.separador = separador;
    }

    @Override
    public List<Registre> llegir() {
        buffer.clear();
        List<Registre> registres = new LinkedList<>();
        try(ByteArrayOutputStream output = new ByteArrayOutputStream()){
            int nLlegits = 0;

            //Es llegeix el contingut del fitxer i es va guardant en el buffer out.
            while((nLlegits = channel.read(buffer)) > 0){
                output.write(buffer.array(), 0, nLlegits);
            }

            //En cas de que s'hagi llegit...
            byte[] bytes = output.toByteArray();
            if(bytes.length > 0){
                String linies[] = new String(bytes).split("\n");
                for(int i = 0; i < linies.length; i++){
                    String camps[] = linies[i].split(separador);
                    if(camps.length > 1){
                        registres.add(new Registre(camps[0], Float.valueOf(camps[1])));
                    }
                }
            }
        } catch (IOException e) {
            Log.F1.warn("Error tancant el buffer, es continua...", e);
        }
        return registres;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public String toString(){
        return nomFitxer;
    }
}
