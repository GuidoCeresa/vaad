package it.algos.vaad.wiki.query;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiLogin extends RequestWiki {

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiLogin() {
        super.needToken = true;
        super.doInit();
    }// fine del metodo costruttore completo



} // fine della classe
