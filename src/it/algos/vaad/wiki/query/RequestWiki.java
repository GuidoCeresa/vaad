package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.*;

import java.net.URLEncoder;
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

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_QUERY + Cost.API_FORMAT;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

//    protected String title;
//    protected String pageid;

    protected boolean needToken;
    protected boolean needPost;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano direttamente il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse (questa classe astratta)
     //     * L'istanza di questa classe viene chiamata con l'indirizzo webUrl (indispensabile)
     //     * Viene invocato il metodo doSetup() che permette alle sottoclassi di regolare alcuni parametri statici
     //     * Viene invocato subito dopo il metodo doInit() che esegue la urlRequest()
     */
    public RequestWiki() {
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il titolo della pagina wiki (indispensabile)
     * Rinvia al costruttore della superclasse
     * Dalla superclasse viene chiamato il metodo doSetup() che può essere sovrascritto
     * Dalla superclasse viene chiamato il metodo doInit() che può essere sovrascritto
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWiki(String wikiTitle) {
//        super(wikiTitle);
        super.doInit();
    }// fine del metodo costruttore completo

    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il pageid della pagina wiki (indispensabile)
     * Rinvia al costruttore della superclasse
     * Dalla superclasse viene chiamato il metodo doSetup() che può essere sovrascritto
     * Dalla superclasse viene chiamato il metodo doInit() che può essere sovrascritto
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public RequestWiki(long wikiPageid) {
//        super(wikiPageid);
        super.doInit();
    }// fine del metodo costruttore completo

//    /**
//     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     */
//    protected void doInit() {
//    } // fine del metodo

    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void doRequest() {
//        preliminaryRequest();

        try { // prova ad eseguire il codice
            urlRequest();
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getClass().getSimpleName();
            valida = false;
        }// fine del blocco try-catch
    } // fine del metodo

//    /**
//     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     */
//    @Override
//    protected void preliminaryRequest() {
//    } // fine del metodo

//    /**
//     * Costruisce la stringa della request
//     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     *
//     * @return domain
//     */
//    @Override
//    protected String getDomain() {
//        String domain = "";
//
//        try { // prova ad eseguire il codice
//            domain = API_BASE + TAG_PROP + TAG_TITOLO + URLEncoder.encode(webUrl, ENCODE);
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//
//        return domain;
//    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap mappa = null;
        super.elaboraRisposta(rispostaRequest);

        if (rispostaRequest != null) {
            mappa = LibWiki.creaMappaQuery(rispostaRequest);
        }// fine del blocco if

        if (mappa != null) {
            if (mappa.get(PagePar.missing.toString()) != null && (Boolean) mappa.get(PagePar.missing.toString())) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && !(Boolean) mappa.get(PagePar.missing.toString())) {
                if (mappa.get(PagePar.content.toString()) != null) {
                    risultato = TipoRisultato.letta;
                    valida = true;
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if
    } // fine del metodo


} // fine della classe
