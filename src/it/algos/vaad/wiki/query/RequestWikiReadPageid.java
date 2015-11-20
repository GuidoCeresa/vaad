package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiReadPageid extends RequestWikiRead {

    //--pageid della pagina
    private long wikiPageid;

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public RequestWikiReadPageid(long wikiPageid) {
        this.wikiPageid = wikiPageid;
        tipoRicerca = TipoRicerca.pageid;
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

        domain += TAG_PAGEID + wikiPageid;

        return domain;
    } // fine del metodo


} // fine della classe
