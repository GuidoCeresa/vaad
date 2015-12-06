package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WrapTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 04 dic 2015.
 * .
 */
public class RequestWikiReadMultiPages extends RequestWikiReadMulti {


    //--tag per la costruzione della stringa della request
    protected static String TAG_MULTIPAGES = TAG_PROP + "&pageids=";

    //--lista delle pagine costruite con la risposta
    private ArrayList<Page> listaPages;


    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param listaPageIds elenco di pageids (long)
     */
    public RequestWikiReadMultiPages(long[] listaPageIds) {
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
     */
    public RequestWikiReadMultiPages(ArrayList<Long> arrayPageIds) {
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

        domain += API_QUERY + TAG_MULTIPAGES + stringaPageIds;

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
        JSONArray arrayPages = LibWiki.getArrayPagesJSON(rispostaRequest);
        Page page;

        //--recupera i valori dei parametri info
        if (arrayPages != null) {
            risultato= TipoRisultato.letta;
            listaPages = new ArrayList<Page>();
            for (int k = 0; k < arrayPages.size(); k++) {
                page = new Page((JSONObject)arrayPages.get(k));
                listaPages.add(page);
            }// end of for cycle
        }// end of if cycle

    } // fine del metodo

    public ArrayList<Page> getListaPages() {
        return listaPages;
    }// end of getter method

    public void setListaPages(ArrayList<Page> listaPages) {
        this.listaPages = listaPages;
    }//end of setter method

} // fine della classe
