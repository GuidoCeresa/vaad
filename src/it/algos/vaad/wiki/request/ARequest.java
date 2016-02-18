package it.algos.vaad.wiki.request;

import it.algos.vaad.VaadApp;
import it.algos.vaad.wiki.*;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Superclasse astratta per le Request sul Web e su MediaWiki
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 */
public abstract class ARequest {


    //--codifica dei caratteri
    protected static String ENCODE = "UTF-8";


    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";


    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";


    //--richiesta preliminary per ottenere il token per Edit e Move
    protected static String TAG_PRELINARY = "&action=query&meta=tokens";


    //--suffisso per il formato della risposta */
    protected static String API_FORMAT = "format=" + Cost.FORMAT.toString() + "&formatversion=2";

    //--azione API generica */
    protected static String API_ACTION = "&action=";

    //--azione API specifica */
    protected static String API_QUERY = "query";
    protected static String API_ASSERT = "&assert=bot";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_PHP + API_FORMAT;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";
    protected static String CSRF_TOKEN = "csrftoken";

    //--validità generale della request (webUrl esistente e letto)
    protected boolean valida;

    //--validità specifica della request
    protected TipoRisultato risultato = TipoRisultato.erroreGenerico;

    //--indirizzo internet da leggere
    protected String domain;

    //--flag di controllo per la gestione del flusso
    protected boolean needPreliminary;
    protected boolean needAssert;
    protected boolean needContinua;
    protected boolean needPost;
    protected boolean needCookies;
    protected boolean needLogin;
    protected boolean needToken;
    protected boolean needBot;

    //--token per la continuazione della query
    protected String tokenContinua = "";

    //--contenuto testuale completo della risposta (la seconda, se ci sono due request)
    protected String testoResponse;

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

    //--login del collegamento
    protected WikiLogin wikiLogin;

    //--titolo della pagina
    protected String wikiTitle;

    //--pageid della pagina
    protected long wikiPageid;
    // oggetto della modifica in scrittura
    protected String summary;
    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected HashMap cookies;
    // token ottenuto dalla preliminaryRequest ed usato per Edit e Move
    protected String csrfToken;
    // liste di pagine
    protected ArrayList<Long> listaPaginePageids;
    protected ArrayList<String> listaPagineTitles;
    // liste di voci della categoria (namespace=0)
    protected ArrayList<Long> listaVociPageids;
    protected ArrayList<String> listaVociTitles;
    // liste di sottocategorie della categoria (namespace=14)
    protected ArrayList<Long> listaCatPageids;
    protected ArrayList<String> listaCatTitles;
    //--lista di wrapper con pagesid e timestamp
    protected ArrayList<WrapTime> listaWrapTime;
    protected ArrayList<WrapTime> listaWrapTimeMissing;
    //--url del collegamento
    private String preliminatyDomain;
    private String urlDomain;

    /**
     * Costruttore
     */
    public ARequest() {
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public ARequest(long wikiPageid) {
        this.wikiPageid = wikiPageid;
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public ARequest(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Gestione del flusso
     */
    protected void doInit() {
        //--Regola alcuni (eventuali) parametri specifici della sottoclasse
        elaboraParametri();

//        if (!checkLogin()) {
//            valida = false;
//            return;
//        }// end of if cycle

        //--procedura di accesso e registrazione con le API
        try { // prova ad eseguire il codice

            if (needPreliminary) {
                this.preliminaryRequest();
            }// end of if cycle

            this.urlRequest();
            if (needContinua) {
                while (!tokenContinua.equals("")) {
                    this.urlRequest();
                } // fine del blcco while
            }// end of if cycle

            if (needAssert) {
                this.checkValidita();
            }// end of if cycle

        } catch (Exception unErrore) { // intercetta l'errore
            valida = false;
            risultato = TipoRisultato.nonTrovata;
        }// fine del blocco try-catch


//        try { // prova ad eseguire il codice
//            if (needToken) {
//                if (!preliminaryRequest()) {
//                    valida = false;
//                    if (risultato != TipoRisultato.mustbeposted) {
//                        risultato = TipoRisultato.noPreliminaryToken;
//                    }// end of if cycle
//                }// end of if cycle
//                return;
//            }// end of if cycle
//
//            cicloRequest();
//        } catch (Exception unErrore) { // intercetta l'errore
//            valida = false;
//            risultato = TipoRisultato.nonTrovata;
//            String errore = unErrore.getMessage();
//            String errore2 = unErrore.getClass().getSimpleName();
//        }// fine del blocco try-catch
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected void elaboraParametri() {
        needPreliminary = false;
        needAssert = false;
        needContinua = false;
        needPost = false;
        needCookies = false;
        needLogin = false;
        needToken = false;
        needBot = false;
    }// fine del metodo


//    /**
//     * Ciclo delle request
//     */
//    private void cicloRequest() throws Exception {
//        urlRequest();
//        if (needContinua) {
//            while (!tokenContinua.equals("")) {
//                urlRequest();
//            } // fine del blcco while
//        }// end of if cycle
//    } // fine del metodo

    /**
     * Controllo del login
     */
    private boolean checkLogin() {
        boolean status = true;

        if (wikiLogin == null) {
            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if cycle

        if (wikiLogin == null) {
            wikiLogin = VaadApp.WIKI_LOGIN;
        }// end of if cycle

        if (needLogin) {
            if (wikiLogin == null) {
                return false;
            }// end of if cycle
        }// end of if cycle

        return status;
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void preliminaryRequest() throws Exception {

    } // fine del metodo


    /**
     * Request
     * Quella base usa il GET
     * In alcune request (non tutte) è obbligatorio anche il POST
     */
    private void urlRequest() throws Exception {
        URLConnection urlConn;
//        InputStream input;
//        InputStreamReader inputReader;
//        BufferedReader readBuffer;
//        StringBuilder textBuffer = new StringBuilder();
//        String stringa;
        String risposta;

        //--crea la connessione, elaborando il Domain
        urlConn = this.creaUrlConnection();

        //--invia i cookies di supporto, se richiesti
        if (needCookies) {
            this.uploadCookies(urlConn);
        }// end of if cycle

        //--now we send the data POST
        //--crea una connessione di tipo POST, se richiesta
        if (needPost) {
            this.creaPostConnection(urlConn);
        }// end of if cycle

        //--Invia la request (GET oppure POST)
        risposta= sendConnection(urlConn);
//        input = urlConn.getInputStream();
//        inputReader = new InputStreamReader(input, ENCODE);
//
//        // read the request
//        readBuffer = new BufferedReader(inputReader);
//        while ((stringa = readBuffer.readLine()) != null) {
//            textBuffer.append(stringa);
//        }// fine del blocco while
//
//        //--close all
//        readBuffer.close();
//        inputReader.close();
//        input.close();
//
//        // controlla il valore di ritorno della request e regola il risultato
//        risposta = textBuffer.toString();
        elaboraRisposta(risposta);
    } // fine del metodo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY + Cost.CONTENT_ALL;

        if (wikiTitle != null && !wikiTitle.equals("")) {
            domainTmp += TAG_TITOLO + titleEncoded();
        } else {
            domainTmp += TAG_PAGEID + wikiPageid;
        }// end of if/else cycle

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo


    /**
     * Crea la connessione
     * <p>
     * Elabora la stringa di domain per la request
     * Regola i parametri della connessione
     *
     * @return connessione con la request
     */
    private URLConnection creaUrlConnection() throws Exception {
        URLConnection urlConn = null;
        String txtCookies = "";
        String domainTmp = elaboraDomain();

        if (domainTmp != null && !domainTmp.equals("")) {
            urlConn = new URL(domainTmp).openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Accept-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        }// end of if cycle

        // regola le property
        if (wikiLogin != null && urlConn != null) {
            txtCookies = wikiLogin.getStringCookies();
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle

        return urlConn;
    } // fine del metodo


    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione con la request
     */
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

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        return "";
    } // fine del metodo


    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     *
     * @param urlConn connessione con la request
     */
    private void creaPostConnection(URLConnection urlConn) throws Exception {
        PrintWriter out = new PrintWriter(urlConn.getOutputStream());
        out.print(elaboraPost());
        out.close();
    } // fine del metodo

    /**
     * Invia la request (GET oppure POST)
     * Testo della risposta
     *
     * @param urlConn connessione con la request
     * @return valore di ritorno della request
     */
    private String sendConnection(URLConnection urlConn) throws Exception {
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, ENCODE);

        // read the response
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        return textBuffer.toString();
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, Object> mappa = LibWiki.creaMappaQuery(rispostaRequest);
        tokenContinua = "";
        testoResponse = null;

        if (mappa != null) {
            if (mappa.get(PagePar.title.toString()) == null) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
                return;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && (Boolean) mappa.get(PagePar.missing.toString())) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
                return;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && !(Boolean) mappa.get(PagePar.missing.toString())) {
                valida = true;
                risultato = TipoRisultato.esistente;
                if (mappa.get(PagePar.content.toString()) != null) {
                    risultato = TipoRisultato.letta;
                    testoResponse = rispostaRequest;
                }// end of if cycle
                return;
            }// end of if cycle
        }// fine del blocco if

    } // fine del metodo

    /**
     * Controllo finale per verificare le condizioni necessarie a questa request per essere considerata valida
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void checkValidita() {
    } // fine del metodo

    /**
     * Encode del titolo
     */
    protected String titleEncoded() {
        String titolo = "";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getMessage();
        }// fine del blocco try-catch

        return titolo;
    } // fine del metodo

    public boolean isValida() {
        return valida;
    }// end of getter method

    protected void setValida(boolean valida) {
        this.valida = valida;
    }//end of setter method

    public TipoRisultato getRisultato() {
        return risultato;
    }// end of getter method

    protected void setRisultato(TipoRisultato risultato) {
        this.risultato = risultato;
    }//end of setter method

    public String getTestoResponse() {
        return testoResponse;
    }// end of getter method

    public void setTestoResponse(String testoResponse) {
        this.testoResponse = testoResponse;
    }//end of setter method

    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method

    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method

    public ArrayList<Long> getListaCatPageids() {
        return listaCatPageids;
    }// end of getter method

    public ArrayList<String> getListaCatTitles() {
        return listaCatTitles;
    }// end of getter method

    public ArrayList<Long> getListaAllPageids() {
        return LibArray.somma(getListaVociPageids(), getListaCatPageids());
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return LibArray.somma(getListaVociTitles(), getListaCatTitles());
    }// end of getter method

    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }// end of getter method

    public ArrayList<WrapTime> getListaWrapTimeMissing() {
        return listaWrapTimeMissing;
    }// end of getter method

} // fine della classe
