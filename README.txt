1. IDEA, creazione di un nuovo progetto che usa Vaadin col plugin Vaad

2. File -> New -> Project...

3. Selezionare Java Enterprise a sinistra (la seconda opzione)

4. Controllare i valori (potrebbero differire):
    . Project SDK           = 1.8 (java version 1.8.0_25)
    . Java EE Version       = Java EE 7
    . Application Server    = Tomcat 8.0.15

5. NON abilitare nessuna Additional Libraries and Frameworks. Confermare -> Next

6. Selezionare Create project from template. Confermare -> Next

7. Controllare il nome del progetto (minuscolo) e la directory d'installazione. Confermare -> Finish

8. Controllare che il progetto (minimale) funzioni.
    - è stata creata una configurazione col server Tomcat;
    - è stato creato il file web->WEB_INF->index.jsp, dove si può inserire quello che appare a video
    - senza necessità di ulteriori interventi, selezionando Run il programma funziona

9. In Project Settings -> Libraries
    - aggiungere New Project Library (tipo java), selezionando ~/Documents/IdeaProjects/vaad/out/artifacts/vaad_jar
    - selezionando la CARTELLA, a destra appariranno due path: uno per i Classes ed uno per i Sources
    - se in Project Setting appare in basso a sinistra la scritte Problems, cliccare su Fix e selezionare Add webbase_jar to the artifact

10. Aprire il progetto Vaad e lanciare (in Ant) lo script templates.script.pack:
    - nel primo dialogo, inserire (obbligatorio) il nome (Maiuscolo) del nuovo progetto
    - nel secondo dialogo, inserire (facoltativo, default 'MySqlUnit') il parametro di collegamento persistence-entity
    - nel terzo dialogo, inserire (facoltativo, default 'test') il nome del database mysql
    - nel quarto dialogo, inserire (facoltativo, default '') il nome del primo modulo

11. È stato creato il package minimo:
    - crea la directory base del progetto -> it.algos.nomeProgetto
    - crea la classe xxxBootStrap Prima classe in esecuzione del programma.
    - crea la classe xxxApp Repository di costanti generali del programma
    - crea la classe xxxServlet Punto di partenza della sessione nel server.
    - crea la classe xxxUI Punto di partenza quando si accede dal browser
    - crea il file persistence.xml Parametri di regolazione del database. Elenco di Entity
    - sostituisce il file web.WEB-INF.web.xml
    - se selezionato primo modulo, crea la directory -> it.algos.nomeProgetto.nomeModulo
    - se selezionato primo modulo, crea la domain class -> it.algos.nomeProgetto.nomeModulo.nomeModulo
    - se selezionato primo modulo, crea la entity class -> it.algos.nomeProgetto.nomeModulo.nomeModulo_
    - se selezionato primo modulo, crea la classe modulo -> it.algos.nomeProgetto.nomeModulo.nomeModuloMod
    - se selezionato primo modulo, aggiunge il nome della domain class nel file persistence.xml
