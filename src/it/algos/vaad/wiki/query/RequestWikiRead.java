package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.TipoRicerca;

import java.net.URLEncoder;

/**
 * Created by gac on 18 nov 2015.
 * .
 */
public class RequestWikiRead extends RequestWiki {

    //--pageid della pagina
    private long wikiPageid;

    //--titolo della pagina
    private String wikiTitle;

    //--tag per la costruzione della stringa della request
    protected static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";

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
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiRead(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        tipoRicerca = TipoRicerca.title;
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
            if (tipoRicerca == TipoRicerca.title) {
                try { // prova ad eseguire il codice
                    domain += TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// end of if cycle

            if (tipoRicerca == TipoRicerca.pageid) {
                domain += TAG_PAGEID + wikiPageid;
            }// end of if cycle
        } else {
            return "";
        }// end of if/else cycl

        return domain;
    } // fine del metodo

} // fine della classe
