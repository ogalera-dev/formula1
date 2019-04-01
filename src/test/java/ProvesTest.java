import adaptadors.AdaptadorEntrada;
import configuracio.Configurador;
import carregadors.Carregador;
import logica.es.entrada.estrategies.ConstructurEstrategiaLectura;
import excepcions.DadesDesiguals;
import excepcions.MenysDades;
import excepcions.MesDades;
import excepcions.TestException;
import adaptadors.AdaptadorSortida;
import logica.es.entrada.ConstructorLecturaBasic;
import logica.es.sortida.Escriptor;
import logica.es.sortida.EstrategiaEscripturaTest;
import logica.Execucio;
import logica.Executador;
import models.Registre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ProvesTest {

    @Test
    public void test1() throws Exception {
        /**
         * PROPOSIT DEL TEST
         *
         * Consisteix en comprovar que els càlculs siguin correctes, per això s'utilitzen unes dades estàtiques (en un
         * fitxer d'entrada i que no varien) d'entrada i sortida. Un cop "ingerides", es prova:
         *
         * 1. Els resultats amb les dades correctes
         * 2. Els resultats amb menys dades
         * 3. Els resultats amb més dades.
         * 4. Els resultats amb dades diferents.
         */
        /*Properties propietats = new Properties();
        propietats.load(Proves.class.getResourceAsStream("t1.properties"));
        ferTest(propietats, construirLector(propietats.getProperty("f1.directori.entrada")) ,new EstrategiaEscripturaTest(), 1000);*/
    }

    @Test
    public void test2() throws Exception {
        /**
         * PROPOSIT DEL TEST
         *
         * Consisteix en comprovar que els càlculs siguin correctes després de dues carregues de dades (t0 i t1),
         * Un cop "ingerides", es prova:
         *
         * 1. Els resultats amb les dades correctes
         * 2. Els resultats amb menys dades
         * 3. Els resultats amb més dades.
         * 4. Els resultats amb dades diferents.
         */
        Properties propietats = new Properties();
        propietats.load(ProvesTest.class.getResourceAsStream("t2.properties"));
        ferTest(propietats, construirLector(propietats.getProperty("f1.directori.entrada")) ,new EstrategiaEscripturaTest(), 3000);
    }


    /**
     * Crea un nou lector amb el contingut de cada un dels sensors.
     * @param nomDirectori On hi ha la informació per cada sensor.
     * @return Instància del lector
     * @throws TestException Si hi ha algún problema amb la lectura.
     */
    private static ConstructorLecturaBasic construirLector(String nomDirectori) throws TestException {
        ConstructorLecturaBasic lector = new ConstructorLecturaBasic();
        for(File directori: new File(nomDirectori).listFiles(lector.filtre())){
            List<List<Registre>> sensors = new LinkedList<>();
            File[] fitxers = directori.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".csv");
                }
            });
            Arrays.sort(fitxers, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for(File fitxer: fitxers){
                sensors.add(Carregador.carregar(fitxer, new AdaptadorEntrada()));
            }
            lector.registrar(directori.getAbsolutePath(), sensors);
        }
        return lector;
    }

    private static void ferTest(Properties propietats, ConstructurEstrategiaLectura estrategiaLectura, EstrategiaEscripturaTest estrategiaEscriptura, int milisEspera) throws Exception{
        Configurador conf = new Configurador(propietats);

        Execucio execucio = Executador.crear(conf, estrategiaLectura, estrategiaEscriptura);
        execucio.iniciar();

        //S'espera 1 segon que s'hagin llegit les dades i es comparen amb el resultat que huaria d'haver.
        Thread.currentThread().sleep(milisEspera);

        //Test 1. Dades iguales.
        //Es comproba amb el resultat correcte.
        try{
            List<Escriptor.RegistreAcumulat> resultat = Carregador.carregar(propietats.getProperty("f1.ruta.fitxer.resultat.correcte"), new AdaptadorSortida());
            estrategiaEscriptura.comparar(resultat);
        }catch(TestException te){

            assertTrue(false, te.getMessage());
        }

        //Test 2. Menys dades.
        //Es comproba amb el resultat incorrecte (hi ha més dades en la lectura actual)
        try{
            List<Escriptor.RegistreAcumulat> resultat = Carregador.carregar(propietats.getProperty("f1.ruta.fitxer.resultat.incorrecte1"), new AdaptadorSortida());
            estrategiaEscriptura.comparar(resultat);
            assertTrue(false, "Hi hauria d'haver més dades en la lectura actual");
        }catch(MesDades te){
            //Ok
        }catch(TestException te){
            assertTrue(false, te.getMessage());
        }

        //Test 3. Més dades.
        //Es comproba amb el resultat incorrecte (hi ha menys dades en la lectura actual)
        try{
            List<Escriptor.RegistreAcumulat> resultat = Carregador.carregar(propietats.getProperty("f1.ruta.fitxer.resultat.incorrecte2"), new AdaptadorSortida());
            estrategiaEscriptura.comparar(resultat);
            assertTrue(false, "Hi hauria d'haver menys dades en la lectura actual");
        }catch(MenysDades md){
            //Ok
        }
        catch(TestException te){
            assertTrue(false, te.getMessage());
        }

        //Test 4. Dades diferents.
        //Es comproba amb el resultat incorrecte (les dades no són iguales)
        try{
            List<Escriptor.RegistreAcumulat> resultat = Carregador.carregar(propietats.getProperty("f1.ruta.fitxer.resultat.incorrecte3"), new AdaptadorSortida());
            estrategiaEscriptura.comparar(resultat);
            assertTrue(false, "Hi hauria d'haver dades que no iguales");
        }catch(DadesDesiguals md){
            //Ok
        }
        catch(TestException te){
            assertTrue(false, te.getMessage());
        }

        execucio.parar();
    }
}
