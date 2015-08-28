package it.algos.vaad;

import it.algos.vaad.wiki.VersioneBootStrap;
import it.algos.webbase.web.ABootStrap;
import it.algos.webbase.web.entity.EM;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Executed on container startup
 * <p>
 * Setup non-UI logic here <br>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO creare la sottoclasse per regolare il valore della persistence unit che crea l'EntityManager <br>
 */
public class VaadBootStrap extends ABootStrap {

    /**
     * Valore standard suggerito per ogni progetto
     * Questo singolo progetto può modificarlo nel metodo setPersistenceEntity()
     */
    private static final String DEFAULT_PERSISTENCE_UNIT = "MySqlUnit";

    /**
     * Regola il valore della persistence unit per crearae l'EntityManager <br>
     * DEVE essere sovrascritto (obbligatorio) nella sottoclasse del progetto <br>
     */
    @Override
    public void setPersistenceEntity() {
        EM.PERSISTENCE_UNIT = "MySqlUnit";
    }// end of method


    /**
     * Executed on container startup
     * <p>
     * Setup non-UI logic here
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        ServletContext svltCtx = ABootStrap.getServletContext();

        // registra il servlet context non appena è disponibile
        VaadApp.setServletContext(svltCtx);

        // Controllo, aggiunta, esecuzione di pacth e versioni (principalmente dei dati)
        VersioneBootStrap.init(svltCtx);

        // Do server stuff here

    }// end of method


    /**
     * Tear down logic here<br>
     * Sovrascritto
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        super.contextDestroyed(sce);
    }// end of method

}// end of boot class
