package it.algos.vaad.wiki.query;

/**
 * Created by gac on 18 nov 2015.
 * .
 */
public abstract class RequestWikiRead extends RequestWiki {

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return super.getDomain() + API_QUERY + TAG_PROP;
    } // fine del metodo

} // fine della classe
