package generador.configuracio;

import configuracio.excepcions.ConfiguracioException;

import java.util.Properties;

/**
 * Configurador pel generador de dades.
 */
public class Configurador {
    public final String dirDades; //<Ruta del directori amb els fitxers que s'han d'injectar dades.
    public final int maxNombreRegistres; //<Nombre màxim de registres escrits en un fitxer.
    public final int minNombreRegistres; //<Nombre mínim de registres escrits en un fitxer.
    public final int segonsInterval; //<Nombre de segons entre cada escriptura.
    public final String separador; //<Separador entre registres.
    public final float velocitatBase; //<Velocitat bàse per les generacions en km/h
    public final float desviacioVelocitat; //<Velocitat de desviació per les generacions en km/h

    public Configurador(Properties propietats) throws ConfiguracioException{
        dirDades = propietats.getProperty("generador.dir.dades");
        if(dirDades == null){
            throw new ConfiguracioException("No hi ha directori de dades [generador.dir.dades]");
        }

        minNombreRegistres = Integer.parseInt(propietats.getProperty("generador.nombre.registres.min", "25"));
        if(minNombreRegistres < 0){
            throw new ConfiguracioException("El minim nombre de registres (generador.nombre.registres.min) ha de ser un valor positiu");
        }

        maxNombreRegistres = Integer.parseInt(propietats.getProperty("generador.nombre.registres.max", "100"));
        if(maxNombreRegistres < 0){
            throw new ConfiguracioException("El maxim nombre de registres (generador.nombre.registres.max) ha de ser un valor positiu");
        }

        if(maxNombreRegistres < minNombreRegistres){
            throw new ConfiguracioException("El màxim nombre de registres generats (generador.nombre.registres.max) ha de ser superior o igual al mínim nombre de registres generats (generador.nombre.registres.min)");
        }

        segonsInterval = Integer.parseInt(propietats.getProperty("generador.interval.segons", "2"));
        if(segonsInterval < 0){
            throw new ConfiguracioException("El nombre de segons per l'interval (generador.interval.segons) no pot ser negatiu");
        }

        separador = propietats.getProperty("generador.separador", ",");

        velocitatBase = Float.parseFloat(propietats.getProperty("generador.velocitat.base", "270"));
        if(velocitatBase < 0){
            throw new ConfiguracioException("La velocitat base no pot ser negativa [generador.velocitat.base]");
        }

        desviacioVelocitat = Float.parseFloat(propietats.getProperty("generador.velocitat.variacio", "50"));
        if(desviacioVelocitat < 0){
            throw new ConfiguracioException("La velocitat desviació en la velocitat no pot ser negativa [generador.velocitat.variacio]");
        }
    }

    @Override
    public String toString() {
        return "Configurador{" +
                "dirDades='" + dirDades + '\'' +
                ", maxNombreRegistres=" + maxNombreRegistres +
                ", minNombreRegistres=" + minNombreRegistres +
                ", segonsInterval=" + segonsInterval +
                ", separador='" + separador + '\'' +
                ", velocitatBase=" + velocitatBase +
                ", desviacioVelocitat=" + desviacioVelocitat +
                '}';
    }
}
