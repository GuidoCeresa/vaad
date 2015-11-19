package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.*;

import java.util.HashMap;

/**
 * Superclasse astratta per le Request sul server di Wikipedia
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 */
public abstract class RequestWiki extends Request {


    //--codifica dei caratteri
    protected static String ENCODE = "UTF-8";

    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";

    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";

    /* suffisso per il formato della risposta */
    protected static String API_FORMAT = "format=" + Cost.FORMAT.toString() + "&formatversion=2";

    /* azione API generica */
    protected static String API_ACTION = "&action=";

    /* azione API specifica */
    protected static String API_QUERY = "query";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_PHP + API_FORMAT + API_ACTION;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

    protected boolean needToken;
    protected boolean needPost;


    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void doRequest() {
        preliminaryRequest();

        try { // prova ad eseguire il codice
            urlRequest();
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getClass().getSimpleName();
            valida = false;
        }// fine del blocco try-catch
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void preliminaryRequest() {
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return API_BASE;
    } // fine del metodo



} // fine della classe
