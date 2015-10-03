package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.WikiLogin;
import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.webbase.web.lib.LibSession;

import java.net.URLConnection;

/**
 * Superclasse per le Request al server MediaWiki
 * Fornisce le funzionalità di base
 * Necessita di Login per la sottoclasse QueryMultiId
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 */
public abstract class QueryWiki extends Query {

    // codifica dei caratteri
    protected static String ENCODE = "UTF-8";

    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";

    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_QUERY + Cost.API_FORMAT;

    protected String title;
    protected String pageid;
    protected String stringaPageIds;

//    // risultato della pagina
//    // risultato grezzo della query nel formato prescelto
//    protected String contenuto;

    //--token per la continuazione della query
    protected String continua = "";

    //--tipo di ricerca della pagina
    //--di default il titolo
    private TipoRicerca tipoRicerca = TipoRicerca.title;

    //--tipo di request - solo una per leggere - due per scrivere
    //--di default solo lettura (per la scrittura serve il login)
    private TipoRequest tipoRequest = TipoRequest.read;

    // collegamento utilizzato
//    protected Login login = null;

    /**
     * Costruttore di default per il sistema (a volte serve)
     */
    public QueryWiki() {
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryWiki(String titlepageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        this.tipoRicerca = tipoRicerca;
        this.tipoRequest = tipoRequest;
        this.inizializza(titlepageid);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryWiki(int pageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        this.tipoRicerca = tipoRicerca;
        this.tipoRequest = tipoRequest;
        this.inizializza("" + pageid);
    }// fine del metodo costruttore


    protected void inizializza(String titlepageid) {
        if (titlepageid != null) {
            title = titlepageid;
            pageid = titlepageid;
            stringaPageIds = titlepageid;
            domain = this.getDomain();
            super.inizializza();
        }// fine del blocco if
    } // fine del metodo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    protected String getDomain() {
        return "";
    } // fine del metodo


    /**
     * Crea la connessione
     * Regola i parametri della connessione
     * Recupera i cookies dal Login di registrazione
     */
    @Override
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn = super.creaConnessione();
        WikiLogin wikiLogin = (WikiLogin) LibSession.getAttribute("login");
        String txtCookies;

        // regolo i cookies
        if (wikiLogin != null && wikiLogin.isValido()) {
            txtCookies = wikiLogin.getStringCookies();
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// fine del blocco if

        return urlConn;
    } // fine del metodo

    /**
     * Informazioni, risultato e validita della risposta
     * Controllo del risultato (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     * <p>
     * Sovrascritto nelle sottoclassi
     */
    protected void regolaRisultato() {
    } // fine del metodo

//    protected boolean isNotMissing() {
//        boolean esistevaPagina = true
//        def valoreMissing
//        HashMap mappa = this.getMappa()
//
//        if (mappa) {
//            valoreMissing = mappa['missing']
//            if (valoreMissing != null && valoreMissing instanceof String) {
//                esistevaPagina = false
//            }// fine del blocco if
//        }// fine del blocco if
//
//        return esistevaPagina
//    } // fine del metodo


//    HashMap getMappa() {
//        return mappa
//    }
//
//    void setMappa(HashMap mappa) {
//        this.mappa = mappa
//    }

    @Override
    public boolean isValida() {
        boolean valida = true;

        if (contenuto.equals("")) {
            valida = false;
        }// fine del blocco if

        if (contenuto.length() < 200 && contenuto.contains("missing")) {
            valida = false;
        }// fine del blocco if

        return valida;
    } // end of getter method

    public String getContenuto() {
        return contenuto;
    }

    public String getContinua() {
        return continua;
    }

} // fine della classe
