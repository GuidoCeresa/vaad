package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRisultato;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiBacklinks extends RequestWiki {

    protected static String TAG_BACK = "&list=backlinks&bllimit=max";
    protected static String TAG_NS = "&blnamespace=0";
    protected static String TAG_TITOLO = "&bltitle=";


    //--lista di pagine linkate dalla pagina (tutti i namespace)
    private ArrayList<Long> listaAllPageids;
    private ArrayList<String> listaAllTitles;


    //--lista di voci linkate dalla pagina (namespace=0)
    private ArrayList<Long> listaVociPageids;
    private ArrayList<String> listaVociTitles;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiBacklinks(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        super.doInit();
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

        try { // prova ad eseguire il codice
            domain += API_QUERY + TAG_BACK + TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

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
        super.elaboraRisposta(rispostaRequest);

        listaAllPageids = LibWiki.creaListaBackLongJson(rispostaRequest);
        listaAllTitles = LibWiki.creaListaBackTxtJson(rispostaRequest);

        listaVociPageids = LibWiki.creaListaBackLongVociJson(rispostaRequest);
        listaVociTitles = LibWiki.creaListaBackTxtVociJson(rispostaRequest);

        if (listaAllPageids != null && listaAllTitles != null) {
            if (listaAllPageids.size() == listaAllTitles.size()) {
                risultato = TipoRisultato.letta;
                valida = true;
            }// end of if cycle
        } else {
            valida = false;
            if (Api.esiste(wikiTitle)) {
                risultato = TipoRisultato.letta;
            } else {
                risultato = TipoRisultato.nonTrovata;
            }// end of if/else cycle
        }// end of if/else cycle

    } // fine del metodo


    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method

    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method

    public ArrayList<Long> getListaAllPageids() {
        return listaAllPageids;
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return listaAllTitles;
    }// end of getter method

} // fine della classe
