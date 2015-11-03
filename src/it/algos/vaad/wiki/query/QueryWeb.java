package it.algos.vaad.wiki.query;

/**
 * Classe concreta per le Request sul Web
 * Legge una pagina internet (qualsiasi)
 * Accetta SOLO un domain (indirizzo) completo
 */
public class QueryWeb extends Query {

    private static String WEB_MISSING = " <html><!--<?xml version=\"1.0\" encoding=\"UTF-8\"?><WISPAccessGatewayParam xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance";

    /**
     * Costruttore utilizzato da una sottoclasse
     * <p>
     * L'istanza della sottoclasse usa un costruttore senza parametri
     * Regola alcune property
     * Regola il domain
     * Invoca il metodo inizializza() della superclasse (questa)
     */
    public QueryWeb() {
    }// fine del metodo costruttore senza parametri

    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il domain già regolato
     * Parte subito il metodo inizializza() che esegue la Request
     */
    public QueryWeb(String domain) {
        this.domain = domain;
        super.doInit();
    }// fine del metodo costruttore completo

    /**
     * Controlla di aver trovato la pagina e di aver letto un contenuto valido
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isLetta() {
        boolean valida = true;
        String contenuto = getContenuto();

        if (contenuto.equals("")) {
            valida = false;
        }// fine del blocco if

        if (contenuto.startsWith(WEB_MISSING)) {
            valida = false;
            contenuto = null;
            errore = "UnknownHostException";
        }// fine del blocco if

        return valida;
    } // end of method

    /**
     * Controlla di aver scritto la pagina
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isScritta() {
        return false;
    } // fine del metodo

} // fine della classe
