# Sistema de monitoratge *Formula 1*

##Descripció del sistema
Sistema de seguiment per les dades generades a través de diferents **sensors de velocitat** per cotxes de **formula 1**.

El sistema ha de ser capaç de monitorar diferents fonts de dades (fitxers per defecte) on diversos sensors aniran registrant informació periòdicament (cada 10 segons s'agafarà una mostra) i asíncronament en  **format [timestamp,velocitat]** . Un cop llegides les dades i per cada cicle, s'agruparan les dades en base a l'instant de temps que s'han recollit (nombre de segons des del 1 de Gener de 1970) i es calcularà la mitja de la velocitat del cotxe.

##Arquitectura
###Lectors
Es crea un **fil d'execució per a cada un dels fitxers** que hi ha a la carpeta on els sensors generen les dades, cada fil s'encarrega de monitorar el contingut d'un dels sensors. La lectura s'efectua durant l'etapa de lectura i per cada cicle (la separació temporal entre cicles és de 10 segons per defecte).

###Escriptors
Es crea **un fil d'execució** responsable de rebre les dades llegides per cada lector, agrupar-les fer els càlculs i efectuar l'escriptura en un fitxer nou per cada fase d'escriptura de cada cicle.

###Sincronitzador
És el responsable de coordinar les diferents fases del programa (només hi ha un sincronitzador). Cada cicle està compost per les fases de: `Lectura`, `Escriptura` i `Espera` 

Seqüencia:
>  `Lectura` -> `Escriptura` -> `Espera` -> `Lectura ` -> `...`

###Generadors de dades [EXTRA]
Per tal de poder fer **proves realistes**, també s'ha desenvolupat un **sistema generador de dades**, on es **crea un fil d'execució per cada fitxer on es reben les dades** (lector) i s'injecten dades pseudoaleatòries (cada segon per defecte). Això permet simular de forma realista l'impacte que tindria l'ús de sensors reals en l'aplicació.

###Configuració
Per configurar l'aplicació es poden utilitzar dos fitxers de propietats diferents:

- `f1.properties`, amb:
    - *f1.directori.entrada*: Directori d'on s'ingeriran les dades (**dades/in** per defecte).
    - *f1.directori.sortida*: Directori on es generaran les dades de sortida (**dades/out** per defecte).
    - *f1.nom.fitxer.sortida*: Nom del fitxer de sortida, a cada cicle es crearà un fitxer amb nomFitxer# (**AverageSpeed_** per defecte).
    - *f1.segons.lectura*: Nombre de segons entre cada cicle (**10** per defecte).
    - *f1.separador.entrada*: Separador entre camps d'entrada (**','** per defecte).
    - *f1.separador.sortida*: Separador entre els camps de sortida (**'#'** per defecte).
    - *f1.generador*: Ruta del fitxer de configuració pel generador (**generador.properties** per defecte). Si no s'especifica aquest camp, no hi ha fase de generació de dades.
    
- `generador.properties*`, amb:
    - *generador.dir.dades*: Directori on es farà la injecció de dades (**dades/in** per defecte).
    - *generador.nombre.registres.min*: Nombre mínim de mostres que agafarà cada sensor en cada etapa (**1** per defecte).
    -  *generador.nombre.registres.max*: Nombre màxim de mostres que agafarà cada sensor en cada etapa (**1** per defecte).
    -  *generador.velocitat.base*: Velocitat bàse de les mostres (**270km/h** per defecte).
    - *generador.velocitat.variacio*: Desviació en les mostres (**50km/h** per defecte).
    - *generador.interval.segons*: Nombre de segons entre cada generació (**1** per defecte)

###Notes de disseny
S'ha dissenyat i desenvolupat l'aplicació tenint en compte els següents punts:

- **Eficiència** general del sistema.
- **Facilitat per configurar** el comportament durant d'execució
- **Facilitat per incorporar nous generadors / consumidors de dades** (sockets...)

##Com executar el programa?
És tan senzill com descarregar el projecte (`$git clone https://github.com/ogalera-dev/userzoom.git`) i executar l'script `$compila.sh`. Això generarà la carpeta `compilat/` amb el fitxer `compilat.jar` (aquest *.jar* ja conté totes les dependències) i només cal accedir al directori `compilat` i iniciar l'execució amb la comanda 

```
$java -jar compilat.jar
``` 

**Nota:** Això iniciarà l'execució amb tots els paràmetres per defecte i per finalitzar-la cal matar el procés (Ctrl+C)

###Paràmetres
Es poden passar diferents paràmetres per configurar l'execució del programa, aquests paràmetres són:
```
 -h: Mostra l'ajuda.
 -c: Fitxer de configuració de l'aplicació (veure secció configuració)
 -l: Fitxer de configuració del log (log4j2.xml)
```

Exemples
```
java -jar compilat.jar -h
```

```
java -jar compilat.jar -c nova_configuracio.properties
```

```
java -jar compilat.jar -c nova_configuracio.properties -l configuracio_log.xml
```

##Com executar les proves?
Per això només cal executar la comanda `$mvn clean test` en el directori arrel del projecte.