package it.algos.vaad.wiki.query;

import it.algos.vaad.WrapTime;
import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

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
        super();
    }// fine del metodo costruttore

    public QueryTimestamp(ArrayList arrayPageIds) {
//        super(LibArray.creaStringaPipe(arrayPageIds));
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
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        ArrayList<Long> lista;
        String txtContinua;

        HashMap<Long,String>  mappa = LibWiki.creaMappaTimestamp(risultatoRequest);

//        lista = LibWiki.creaListaCatJson(risultatoRequest);
//        if (lista != null) {
//            this.addLista(lista);
//        }// fine del blocco if

        txtContinua = LibWiki.creaCatContinue(risultatoRequest);
        this.continua = txtContinua;
    } // fine del metodo

    public static WrapTime creaWrap(String singoloElemento) {
        WrapTime wrapTime = null;
        int pageid;
        String tagPageIni = "'pageid'";
        String tagPageEnd = "}";
        String tagRev = "revisions";
        String tagTime = "timestamp";
        int posIni;
        int posEnd;
        String pageTxt;
        String timeTxt;
        HashMap mappa;
        Timestamp timestamp;

        //--pageid
        posIni = singoloElemento.indexOf(tagPageIni) + tagPageIni.length() + 1;
        posEnd = singoloElemento.indexOf(tagPageEnd, posIni);
        pageTxt = singoloElemento.substring(posIni, posEnd);
        try { // prova ad eseguire il codice
            pageid = Integer.decode(pageTxt);
        } catch (Exception unErrore) { // intercetta l'errore
            Object nonUsato = unErrore;
        }// fine del blocco try-catch

        //--timestamp
//        mappa = WikiLib.getMappaJson(singoloElemento, tagRev)
//        if (mappa && mappa[tagTime]) {
//            timeTxt = mappa[tagTime]
//            timestamp = LibTime.getWikiTimestamp(timeTxt)
//        }// fine del blocco if

//        if (pageid != null && timestamp != null) {
//            wrapTime = new WrapTime(pageid, timestamp)
//        }// fine del blocco if

        return wrapTime;
    } // fine del metodo


    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        boolean continua = false;
        ArrayList<WrapTime> listaWrapTime = new ArrayList<WrapTime>();
        ArrayList listaErrori = new ArrayList();
        String risultatoRequest = "";
        String query = "query";
        String pages = "pages";
        HashMap mappaQuery = null;
        HashMap mappaPages = null;
        HashMap mappaNumId = null;
        HashMap mappaVoci = null;
        WrapTime wrapTime;

//        risultatoRequest = this.getRisultato();

        if (!risultatoRequest.equals("")) {
            continua = true;
        } else {
//            log.error 'risultato vuoto'
        }// fine del blocco if-else

//        if (continua) {
//            mappaQuery = (HashMap) JSON.parse(risultatoRequest)
//            continua = (mappaQuery)
//        }// fine del blocco if
//
//        if (continua) {
//            mappaPages = (HashMap) mappaQuery[query]
//            continua = (mappaPages)
//        }// fine del blocco if
//
//        if (continua) {
//            mappaNumId = (HashMap) mappaPages[pages]
//            continua == (mappaNumId && mappaNumId.size == 1)
//        }// fine del blocco if
//
//        if (continua) {
//            mappaVoci = mappaNumId.values()
//            continua = (mappaVoci)
//        }// fine del blocco if
//
//        if (continua) {
//            mappaVoci ?.each {
//                wrapTime = creaWrap((String) it)
//                if (wrapTime) {
//                    listaWrapTime.add(creaWrap((String) it))
//                } else {
//                    if (it['title']) {
//                        listaErrori.add(it['title'])
//                    } else {
//                        if (it['pageid']) {
//                            listaErrori.add(it['pageid'])
//                        } else {
//                            listaErrori.add('generico')
//                        }// fine del blocco if-else
//                    }// fine del blocco if-else
//                }// fine del blocco if-else
//            } // fine del ciclo each
//        }// fine del blocco if
//
//        if (listaWrapTime) {
//            this.setListaWrapTime(listaWrapTime)
//        }// fine del blocco if
//
//        if (listaErrori) {
//            this.setListaErrori(listaErrori)
//        }// fine del blocco if

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
