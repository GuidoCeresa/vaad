package it.algos.vaad.wiki.query;

import java.net.URLEncoder;

/**
 * Created by gac on 18 nov 2015.
 * <p>
 * .
 */
public class RequestWikiReadTitle extends RequestWikiRead {

    private String wikiTitle;


    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il titolo della pagina wiki (indispensabile)
     * Rinvia al costruttore della superclasse
     * Dalla superclasse viene chiamato il metodo doSetup() che può essere sovrascritto
     * Dalla superclasse viene chiamato il metodo doInit() che può essere sovrascritto
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiReadTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        super.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = "";

        try { // prova ad eseguire il codice
            domain = API_BASE + TAG_PROP + TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

} // fine della classe
