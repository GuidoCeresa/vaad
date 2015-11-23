package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.TipoRisultato;

/**
 * Classe concreta per le Request sul Web
 * Fornisce le funzionalità di base per una request standard sul web
 * Usa solo il metodo GET
 * Legge una pagina internet (qualsiasi)
 * Accetta SOLO un webUrl (indirizzo) completo
 */
public class RequestWeb extends Request {

    private final static String WEB_MISSING = "UnknownHostException";

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano direttamente il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param webUrl indirizzo webUrl completo
     */
    public RequestWeb(String webUrl) {
        super.webUrl = webUrl;
        super.doInit();
    }// fine del metodo costruttore completo


    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void doRequest() {
        try { // prova ad eseguire il codice
            urlRequest();
            risultato = TipoRisultato.letta;
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getClass().getSimpleName();
            if (errore.equals(WEB_MISSING)) {
                risultato = TipoRisultato.nonTrovata;
            }// end of if cycle
            valida = false;
        }// fine del blocco try-catch
    } // fine del metodo


} // fine della classe
