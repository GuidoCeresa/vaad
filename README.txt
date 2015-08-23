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
    - è stato creato (vuoto) il file web.WEB-INF.web.xml, che verrà successivamente sovrascritto
    - è stato creato il file web.index.jsp, dove si può inserire quello che appare a video
    - senza necessità di ulteriori interventi, selezionando Run il programma funziona

9. In Project Settings -> Libraries
    - aggiungere New Project Library (tipo java), selezionando ~/Documents/IdeaProjects/vaad/out/artifacts/vaad_jar
    - selezionando la CARTELLA, a destra apparirà il path per i Classes
    - se in Project Setting appare in basso a sinistra la scritte Problems, cliccare su Fix e selezionare Add webbase_jar to the artifact

10. Aprire il plugin Vaad e lanciare (in Ant) lo script templates.script.pack:
    - nel primo dialogo, inserire (obbligatorio) il nome (Maiuscolo) del nuovo progetto
    - nel secondo dialogo, inserire (facoltativo, default 'MySqlUnit') il parametro di collegamento persistence-entity
    - nel terzo dialogo, inserire (facoltativo, default 'test') il nome del database mysql

11. È stato creato il package minimo:
    - creata (sotto src) la directory base del progetto -> it.algos.nomeProgetto
    - creato il file (sotto src) META-INF.persistence.xml: parametri di regolazione del database. Elenco di Entity
    - creata la classe xxxApp: repository di costanti generali del programma
    - creata la classe xxxBootStrap: prima classe in esecuzione del programma.
    - creata la classe xxxServlet: punto di partenza della sessione nel server.
    - creata la classe xxxUI: punto di partenza quando si accede dal browser
    - sostituisce il file web.WEB-INF.web.xml
    - senza necessità di ulteriori interventi, selezionando Run il programma funziona con già installi i 3 moduli ereditati
        dal plugin Vaad: Versione, Preferenze e Logo

12. Aprire il plugin Vaad e lanciare (in Ant) lo script templates.script.modulo:
    - nel primo dialogo, inserire (obbligatorio) il nome (Maiuscolo) del progetto di riferimento
    - nel secondo dialogo, inserire (obbligatorio) il nome (Maiuscolo) del nuovo package da creare
