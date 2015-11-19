package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.*;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe concreta per le Request sul server di Wikipedia per leggere una o più pagine
 * Usa solo il GET
 * La ricerca può essere fatta col pageid, col title oppure con una lista di pageids
 */
public class RequestWikiRead extends RequestWiki {

    //--tag per la costruzione della stringa della request
    protected static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";


    //--pageid della pagina
    private long wikiPageid;

    //--titolo della pagina
    private String wikiTitle;


    //--stringa (separata da pipe oppure da virgola) delle pageids
    private String stringaPageIds;

    //--lista di wrapper con pagesid e timestamp
    private ArrayList<WrapTime> listaWrapTime;
    private ArrayList<WrapTime> listaWrapTimeMissing;

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public RequestWikiRead(long wikiPageid) {
        this.wikiPageid = wikiPageid;
        tipoRicerca = TipoRicerca.pageid;
        super.doInit();
    }// fine del metodo costruttore completo

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiRead(String wikiTitle) {
        this(wikiTitle, TipoRicerca.title);
    }// fine del metodo costruttore

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param listaPageIds elenco di pageids
     */
    public RequestWikiRead(String[] listaPageIds) {
        this(LibArray.fromStringToStringaPipe(listaPageIds), TipoRicerca.listaPageids);
    }// fine del metodo costruttore

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param arrayPageIds elenco di pageids
     */
    public RequestWikiRead(ArrayList arrayPageIds) {
        this(LibArray.toStringaPipe(arrayPageIds), TipoRicerca.listaPageids);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param titleOppureStringaPageIds title oppure stringa (separata da pipe oppure da virgola) delle pageids
     * @param tipoRicerca               da effettuare
     */
    public RequestWikiRead(String titleOppureStringaPageIds, TipoRicerca tipoRicerca) {

        if (tipoRicerca != null) {
            if (titleOppureStringaPageIds.contains("|")) {
                tipoRicerca = TipoRicerca.listaPageids;
            }// end of if/else cycle

            switch (tipoRicerca) {
                case title:
                    this.wikiTitle = titleOppureStringaPageIds;
                    break;
                case listaPageids:
                    if (titleOppureStringaPageIds.contains(",")) {
                        titleOppureStringaPageIds = LibText.sostituisce(titleOppureStringaPageIds,",","|");
                    }// end of if/else cycle
                    this.stringaPageIds = titleOppureStringaPageIds;
                    break;
                default: // caso non definito
                    break;
            } // fine del blocco switch
            super.tipoRicerca = tipoRicerca;
        }// end of if cycl

        super.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = super.getDomain() + API_QUERY + TAG_PROP;

        if (tipoRicerca != null) {
            switch (tipoRicerca) {
                case title:
                    try { // prova ad eseguire il codice
                        domain += TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
                    } catch (Exception unErrore) { // intercetta l'errore
                    }// fine del blocco try-catch
                    break;
                case pageid:
                    domain += TAG_PAGEID + wikiPageid;
                    break;
                case listaPageids:
                    domain += TAG_PROP_PAGEIDS + stringaPageIds;
                    break;
                default: // caso non definito
                    break;
            } // fine del blocco switch
        } else {
            return "";
        }// end of if/else cycle

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
        HashMap<String, Object> mappaQuery;
        HashMap<String, ArrayList<WrapTime>> mappaWrap;
        super.elaboraRisposta(rispostaRequest);

        if (rispostaRequest != null) {
            switch (tipoRicerca) {
                case title:
                case pageid:
                    mappaQuery = LibWiki.creaMappaQuery(rispostaRequest);

                    if (mappaQuery != null) {
                        if (mappaQuery.get(PagePar.missing.toString()) != null && (Boolean) mappaQuery.get(PagePar.missing.toString())) {
                            risultato = TipoRisultato.nonTrovata;
                            valida = false;
                        }// end of if cycle

                        if (mappaQuery.get(PagePar.missing.toString()) != null && !(Boolean) mappaQuery.get(PagePar.missing.toString())) {
                            if (mappaQuery.get(PagePar.content.toString()) != null) {
                                risultato = TipoRisultato.letta;
                                valida = true;
                            }// end of if cycle
                        }// end of if cycle
                    }// fine del blocco if
                    break;

                case listaPageids:
                    mappaWrap = LibWiki.creaArrayWrapTime(rispostaRequest);
                    if (mappaWrap != null) {
                        listaWrapTime = mappaWrap.get(LibWiki.KEY_PAGINE_VALIDE);
                        listaWrapTimeMissing = mappaWrap.get(LibWiki.KEY_PAGINE_MANCANTI);
                        risultato = TipoRisultato.letta;
                        valida = true;
                    } else {
                        risultato = TipoRisultato.nonTrovata;
                        valida = false;
                    }// end of if/else cycle
                    break;
                default: // caso non definito
                    break;
            } // fine del blocco switch
        }// fine del blocco if

    } // fine del metodo

    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }// end of getter method


    public ArrayList<WrapTime> getListaWrapTimeMissing() {
        return listaWrapTimeMissing;
    }// end of getter method

} // fine della classe
