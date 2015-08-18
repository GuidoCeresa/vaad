package it.algos.vaad;

import it.algos.webbase.web.AlgosApp;


/**
 * Contenitore di costanti della applicazione
 */
public class VaadApp extends AlgosApp {

    /**
     * Eventuali modifiche dei parametri standard
     */
    public static void flag() {
        AlgosApp.USE_SECURITY = false;
        AlgosApp.USE_COMPANY = false;
        AlgosApp.IS_DEBUG = false;
    }// end of static method

}// end of class
