package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibNum;
import it.algos.webbase.web.lib.LibTime;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiCat extends RequestWiki {

    /**
     * Il limite massimo ''for users'' è di 500
     * Il limite massimo ''for bots or sysops'' è di 5.000
     * Il limite 'max' equivale a 5.000
     */
    protected static final int LIMITE = 5000;

    protected static final String START_LIMIT_ERROR = "cmlimit may not be over";

    //--stringa per la lista di categoria
    protected static String CAT = "&list=categorymembers";

    //--stringa per selezionare il namespace (0=principale - 14=sottocategorie) ed il tipo di categoria (page, subcat, file)
    protected static String TYPE_VOCI = "&cmnamespace=0&cmtype=page";
    protected static String TYPE_ALL = "&cmnamespace=0|14&cmtype=page|subcat";

    //--stringa per selezionare il numero di valori in risposta
    protected static String TAG_LIMIT = "&cmlimit=";

    //--stringa per indicare il titolo della pagina
    protected static String TITLE = "&cmtitle=Category:";

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";

    protected int limite;

    // liste di pagine della categoria (namespace=0)
    private ArrayList<Long> listaVociPageids;
    private ArrayList<String> listaVociTitles;

    // liste di sottocategorie della categoria (namespace=14)
    private ArrayList<Long> listaCatPageids;
    private ArrayList<String> listaCatTitles;

    private boolean debug = false;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiCat(String wikiTitle) {
        this(wikiTitle, LIMITE);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     * @param limite    di pagine da caricare
     */
    public RequestWikiCat(String wikiTitle, int limite) {
        super.wikiTitle = wikiTitle;
        this.limite = limite;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Costruttore for testing purpose only
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     * @param wikiLogin del collegamento
     */
    public RequestWikiCat(String wikiTitle, WikiLogin wikiLogin) {
        this(wikiTitle, LIMITE, wikiLogin);
    }// fine del metodo costruttore completo

    /**
     * Costruttore completo for testing purpose only
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     * @param limite    di pagine da caricare
     * @param wikiLogin del collegamento
     */
    public RequestWikiCat(String wikiTitle, int limite, WikiLogin wikiLogin) {
        super.wikiTitle = wikiTitle;
        this.limite = limite;
        super.wikiLogin = wikiLogin;
        this.debug = false;
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit() {
        super.needPost = false;
        super.needLogin = false;
        super.needToken = false;
        super.needContinua = true;
        super.doInit();
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = super.getDomain();

        try { // prova ad eseguire il codice
            domain += API_QUERY + CAT + TYPE_ALL + TITLE + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (limite > 0) {
            domain += TAG_LIMIT + limite;
        }// end of if cycle

        if (!tokenContinua.equals("")) {
            domain += CONTINUE + tokenContinua;
        }// fine del blocco if

        return domain;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, ArrayList> mappa;
        super.elaboraRisposta(rispostaRequest);
        String warningMessage = "";
        long inizio = System.currentTimeMillis();
        String primoStep;
        String secondStep;

        mappa = LibWiki.getMappaWrap(rispostaRequest);

        if (mappa == null) {
            if (Api.esiste("Category:" + wikiTitle)) {
                risultato = TipoRisultato.letta;
            } else {
                risultato = TipoRisultato.nonTrovata;
            }// end of if/else cycle
            valida = false;
            return;
        }// fine del blocco if

        if (debug) {
            primoStep = LibTime.difText(inizio);
            System.out.println( "primoStep: " + primoStep);
        }// end of if cycle


        if (mappa.get(LibWiki.KEY_VOCE_PAGEID) != null && mappa.get(LibWiki.KEY_VOCE_PAGEID).size() > 0) {
            this.listaVociPageids = LibArray.sommaDisordinata(this.listaVociPageids, mappa.get(LibWiki.KEY_VOCE_PAGEID));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_VOCE_TITLE) != null && mappa.get(LibWiki.KEY_VOCE_TITLE).size() > 0) {
            this.listaVociTitles = LibArray.sommaDisordinata(this.listaVociTitles, mappa.get(LibWiki.KEY_VOCE_TITLE));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_CAT_PAGEID) != null && mappa.get(LibWiki.KEY_CAT_PAGEID).size() > 0) {
            this.listaCatPageids = LibArray.sommaDisordinata(this.listaCatPageids, mappa.get(LibWiki.KEY_CAT_PAGEID));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_CAT_TITLE) != null && mappa.get(LibWiki.KEY_CAT_TITLE).size() > 0) {
            this.listaCatTitles = LibArray.sommaDisordinata(this.listaCatTitles, mappa.get(LibWiki.KEY_CAT_TITLE));
        }// end of if cycle

        if (debug) {
            secondStep = LibTime.difText(inizio);
            System.out.println(LibNum.format(listaVociPageids.size()) + " voci - " + "secondStep: " + secondStep);
        }// end of if cycle

        risultato = TipoRisultato.letta;
        valida = true;
        tokenContinua = LibWiki.creaCatContinue(rispostaRequest);
//        this.continua = txtContinua;

        warningMessage = LibWiki.getWarning(rispostaRequest);
        if (warningMessage != null && warningMessage.startsWith(START_LIMIT_ERROR)) {
            risultato = TipoRisultato.limitOver;
        }// end of if cycle

    } // fine del metodo

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
        return LibArray.somma(listaVociPageids, listaCatPageids);
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return LibArray.somma(listaVociTitles, listaCatTitles);
    }// end of getter method
} // fine della classe
