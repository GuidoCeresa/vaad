package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

/**
 * Created by Gac on 15 ago 2015.
 * .
 */
public class QueryReadPageid extends QueryPage{

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryReadPageid(String title) {
        super(title, TipoRicerca.pageid, TipoRequest.read);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryReadPageid(int pageid) {
        super(pageid, TipoRicerca.pageid, TipoRequest.read);
    }// fine del metodo costruttore

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = "";

        try { // prova ad eseguire il codice
            domain = API_BASE + TAG_PROP + TAG_PAGEID + pageid;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

}// end of class
