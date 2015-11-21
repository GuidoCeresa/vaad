package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.webbase.web.lib.LibSession;

import java.net.URLConnection;

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

    protected boolean needLogin;
    protected boolean needToken;

    //--login del collegamento
    protected WikiLogin wikiLogin;

    //--titolo della pagina
    protected String wikiTitle;

    //--pageid della pagina
    protected long wikiPageid;

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit() {
        if (needLogin) {
            if (isLoggato()) {
                super.doInit();
            } else {
                valida = false;
                risultato = TipoRisultato.noLogin;
            }// end of if/else cycle
        } else {
            super.doInit();
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void doRequest() {
        if (wikiLogin == null) {
            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if cycle

        if (needLogin) {
            if (wikiLogin == null) {
                return;
            }// end of if cycle
        }// end of if cycle

        if (needToken) {
            if (preliminaryRequest()) {
                super.doRequest();
            } else {
                valida = false;
                risultato = TipoRisultato.noToken;
            }// end of if/else cycle
        }// end of if cycle

        super.doRequest();
    } // fine del metodo

    /**
     * Crea la connessione
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn = super.creaConnessione();
        String txtCookies = "";


        // regola le property
        if (wikiLogin != null) {
            txtCookies = wikiLogin.getStringCookies();
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle

        return urlConn;
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean isLoggato() {
        return true;
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean preliminaryRequest() {
        return true;
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
