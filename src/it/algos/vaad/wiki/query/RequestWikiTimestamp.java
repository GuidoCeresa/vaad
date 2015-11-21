package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WrapTime;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiTimestamp extends RequestWiki {

    //--tag per la costruzione della stringa della request
    protected static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";

    //--stringa (separata da pipe oppure da virgola) delle pageids
    private String stringaPageIds;

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
     * @param listaPageIds elenco di pageids
     */
    public RequestWikiTimestamp(String[] listaPageIds) {
        this(LibArray.fromStringToStringaPipe(listaPageIds));
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
    public RequestWikiTimestamp(ArrayList arrayPageIds) {
        this(LibArray.toStringaPipe(arrayPageIds));
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param stringaPageIds stringa (separata da pipe oppure da virgola) delle pageids
     */
    public RequestWikiTimestamp(String stringaPageIds) {

        if (stringaPageIds.contains(",")) {
            stringaPageIds = LibText.sostituisce(stringaPageIds, ",", "|");
        }// end of if/else cycle

        if (stringaPageIds.contains("|")) {
            this.stringaPageIds = stringaPageIds;
            tipoRicerca = TipoRicerca.listaPageids;
        }// end of if/else cycle

        super.doInit();
    }// fine del metodo costruttore completo

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit() {
        super.needPost = false;
        super.needLogin = false;
        super.needToken = false;
        super.needContinua = false;
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

        domain += API_QUERY + TAG_PROP + TAG_PROP_PAGEIDS + stringaPageIds;

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
