package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.webbase.web.lib.LibSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    protected static String API_ASSERT = "&assert=bot";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_PHP + API_FORMAT;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

    protected boolean needLogin;
    protected boolean needToken;
    protected boolean needBot;

    //--login del collegamento
    protected WikiLogin wikiLogin;

    //--titolo della pagina
    protected String wikiTitle;

    //--pageid della pagina
    protected long wikiPageid;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected HashMap cookies;

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
            try { // prova ad eseguire il codice
                if (preliminaryRequest()) {
                    super.doRequest();
                } else {
                    valida = false;
                    if (risultato != TipoRisultato.mustbeposted) {
                        risultato = TipoRisultato.noToken;
                    }// end of if cycle
                }// end of if/else cycle
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getMessage();
            }// fine del blocco try-catch
        } else {
            super.doRequest();
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean preliminaryRequest() throws Exception {
        URLConnection urlConn;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        //--connessione
        urlConn = creaConnessionePreliminary();

        //--POST
        this.creaPostPreliminary(urlConn);

        //--GET
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--cookies
        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        if (needCookies) {
            this.downlodCookies(urlConn);
        }// end of if cycle

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        return elaboraRispostaPreliminary(textBuffer.toString());
    } // fine del metodo

    /**
     * Crea la connessione preliminary
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected URLConnection creaConnessionePreliminary() throws Exception {
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
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     *
     * @param urlConn connessione
     */
    private void downlodCookies(URLConnection urlConn) {
        LinkedHashMap mappa = new LinkedHashMap();
        String headerName;
        String cookie;
        String name;
        String value;

        if (urlConn != null) {
            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = urlConn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    name = cookie.substring(0, cookie.indexOf("="));
                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    mappa.put(name, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        this.cookies = mappa;
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
     * Crea il POST della request preliminary
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void creaPostPreliminary(URLConnection urlConn) throws Exception {
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean isLoggato() {
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
        String domain = API_BASE;

        if (needBot) {
            domain += API_ASSERT;
        }// end of if cycle
        domain += API_ACTION;

        return domain;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean elaboraRispostaPreliminary(String rispostaRequest) {
        return false;
    } // end of getter method

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    @Override
    protected void uploadCookies(URLConnection urlConn) {
        HashMap cookies = this.cookies;
        Object[] keyArray;
        Object[] valArray;
        Object sessionObj = null;
        String sesionTxt = "";
        String sep = "=";
        Object valObj = null;
        String valTxt = "";

        // controllo di congruità
        if (urlConn != null) {
            if (cookies != null && cookies.size() > 0) {

                keyArray = cookies.keySet().toArray();
                if (keyArray.length > 0) {
                    sessionObj = keyArray[0];
                }// fine del blocco if
                if (sessionObj != null && sessionObj instanceof String) {
                    sesionTxt = (String) sessionObj;
                }// fine del blocco if

                valArray = cookies.values().toArray();
                if (valArray.length > 0) {
                    valObj = valArray[0];
                }// fine del blocco if
                if (valObj != null && valObj instanceof String) {
                    valTxt = (String) valObj;
                }// fine del blocco if

                urlConn.setRequestProperty("Cookie", sesionTxt + sep + valTxt);
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

} // fine della classe
