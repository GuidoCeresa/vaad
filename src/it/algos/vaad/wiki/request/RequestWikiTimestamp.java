package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WrapTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiTimestamp extends RequestWikiReadMulti {

    //--tag per la costruzione della stringa della request
    private final static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";

    //--lista di wrapper con pagesid e timestamp
    private ArrayList<WrapTime> listaWrapTime;
    private ArrayList<WrapTime> listaWrapTimeMissing;


    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param listaPageIds elenco di pageids (long)
     * @deprecated
     */
    public RequestWikiTimestamp(long[] listaPageIds) {
        super(listaPageIds);
    }// fine del metodo costruttore

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param arrayPageIds elenco di pageids (ArrayList)
     * @deprecated
     */
    public RequestWikiTimestamp(ArrayList<Long> arrayPageIds) {
        super(arrayPageIds);
    }// fine del metodo costruttore

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

        domain += API_QUERY + TAG_PROP_PAGEIDS + stringaPageIds;

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
        HashMap<String, ArrayList<WrapTime>> mappa;
        super.elaboraRisposta(rispostaRequest);

        mappa = LibWiki.creaArrayWrapTime(rispostaRequest);
        if (mappa != null) {
            listaWrapTime = mappa.get(LibWiki.KEY_PAGINE_VALIDE);
            listaWrapTimeMissing = mappa.get(LibWiki.KEY_PAGINE_MANCANTI);
            risultato = TipoRisultato.letta;
            valida = true;
        } else {
            risultato = TipoRisultato.nonTrovata;
            valida = false;
        }// end of if/else cycle

    } // fine del metodo

    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }// end of getter method


    public ArrayList<WrapTime> getListaWrapTimeMissing() {
        return listaWrapTimeMissing;
    }// end of getter method

} // fine della classe
