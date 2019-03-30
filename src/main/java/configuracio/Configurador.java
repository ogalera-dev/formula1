package configuracio;

import java.util.Properties;

public final class Configurador {
    public final String directoriEntrada; //<Ruta del directori que conté els fitxers generats pels sensors.
    public final String directoriSortida; //<Ruta del directori on es generaran els fitxers de sortida.
    public final String nomFitxerSortia; //<Nom que han de tenir els fitxers de sortida, a aquest nom es concatenarà el número de fitxer al final
    public final long segonsLectura; //<Nombre de segons que hi ha entre cada lectura dels fitxers d'entrada, 10 per defecte.
    public final String separadorEntrada; //<Caràcter utilitzat com a separador en els fitxers d'entrada.
    public final String separadorSortida; //<Caràcter utilitzat com a separador en els fitxers de sortida.

    public Configurador(Properties propietats) throws ConfiguracioException {
        directoriEntrada = propietats.getProperty("f1.directori.entrada");
        if(directoriEntrada == null || directoriEntrada.length() == 0){
            throw new ConfiguracioException("La propietat f1.directori.entrada especifica el directori que conté els fitxers d'entra i es obligatori");
        }

        directoriSortida = propietats.getProperty("f1.directori.sortida");
        if(directoriSortida == null || directoriSortida.length() == 0){
            throw new ConfiguracioException("La propietat f1.directori.sortida especifica el directori que contindrà els fitxes de sortida i es obligatori");
        }

        nomFitxerSortia = propietats.getProperty("f1.nom.fitxer.sortida");
        if(nomFitxerSortia == null || nomFitxerSortia.length() == 0){
            throw new ConfiguracioException("La propietat f1.nom.fitxer.sortida especifica el nom que han de tenir els fitxers de sortida i es obligatori");
        }

        segonsLectura = Long.parseLong(propietats.getProperty("f1.segons.lectura", "10"));
        if(segonsLectura < 1){
            throw new ConfiguracioException("El temps de refresc mínim és de 1 segón");
        }

        separadorEntrada = propietats.getProperty("f1.separador.entrada", ",");

        separadorSortida = propietats.getProperty("f1.separador.sortida", ",");
    }

    @Override
    public String toString() {
        return "Configurador{" +
                "directoriEntrada='" + directoriEntrada + '\'' +
                ", directoriSortida='" + directoriSortida + '\'' +
                ", nomFitxerSortia='" + nomFitxerSortia + '\'' +
                ", segonsLectura=" + segonsLectura +
                ", separadorEntrada='" + separadorEntrada + '\'' +
                ", separadorSortida='" + separadorSortida + '\'' +
                '}';
    }
}
