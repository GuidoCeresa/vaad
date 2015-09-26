package it.algos.vaad.wiki.query;

import it.algos.vaad.WrapTime;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.webbase.web.lib.LibArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 21 set 2015.
 * Query per leggere il timestamp di molte pagine tramite una lista di pageIds
 * Legge solamente
 * Mantiene una lista di pageIds e timestamps
 */
public class QueryTimestamp extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";


    //--lista di wrapper con pagesid e timestamp
    ArrayList<WrapTime> listaWrapTime;

    //--lista di errori  (titolo della voce)
    ArrayList listaErrori;

    /**
     * La stringa (unica) può avere come separatore il pipe oppure la virgola
     */
    public QueryTimestamp(String stringaPageIds) {
        super(stringaPageIds, TipoRicerca.listaPageids, TipoRequest.read);
    }// fine del metodo costruttore

    public QueryTimestamp(String[] listaPageIds) {
        super(LibArray.fromStringToStringaPipe(listaPageIds), TipoRicerca.listaPageids, TipoRequest.read);
    }// fine del metodo costruttore

    public QueryTimestamp(ArrayList arrayPageIds) {
        super(LibArray.toStringaPipe(arrayPageIds), TipoRicerca.listaPageids, TipoRequest.read);
    }// fine del metodo costruttore

    public QueryTimestamp(List arrayPageIds) {
        super(LibArray.toStringaPipe(arrayPageIds), TipoRicerca.listaPageids, TipoRequest.read);
    }// fine del metodo costruttore


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    protected String getDomain() {
        String domain = "";

        try { // prova ad eseguire il codice
            domain = API_BASE + TAG_PROP_PAGEIDS + stringaPageIds;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        this.listaWrapTime = LibWiki.creaArrayWrapTime(risultatoRequest);
        this.continua = LibWiki.creaCatContinue(risultatoRequest);
    } // fine del metodo


    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }

    public void setListaWrapTime(ArrayList<WrapTime> listaWrapTime) {
        this.listaWrapTime = listaWrapTime;
    }

    public ArrayList getListaErrori() {
        return listaErrori;
    }

    public void setListaErrori(ArrayList listaErrori) {
        this.listaErrori = listaErrori;
    }

} // fine della classe
