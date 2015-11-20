package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.vaad.wiki.TipoRisultato;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiReadTitle extends RequestWikiRead{

    //--titolo della pagina
    private String wikiTitle;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiReadTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        tipoRicerca = TipoRicerca.title;
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
        String domain = super.getDomain() + API_QUERY + TAG_PROP;

        try { // prova ad eseguire il codice
            domain += TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo


} // fine della classe
