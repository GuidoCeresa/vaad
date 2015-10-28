package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Necessita di Login per scrivere
 */
public abstract class QueryWrite extends QueryWiki {

    // contenuto della pagina
    private String testoNew;
    private String testoPrimaRequest;
    private String testoSecondaRequest;

    // oggetto della modifica
    private String summary;


    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(String titlepageid, String testoNew) {
        this(titlepageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(int pageid, String testoNew) {
        this(pageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(String titlepageid, String testoNew, String summary) {
        super.tipoRicerca = TipoRicerca.title;
        this.doInit(titlepageid, testoNew, summary);
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(int pageid, String testoNew, String summary) {
        super.tipoRicerca = TipoRicerca.pageid;
        this.doInit("" + pageid, testoNew, summary);
    }// fine del metodo costruttore

    protected void doInit(String titlepageid, String testoNew, String summary) {
        this.testoNew = testoNew;
        this.summary = summary;
        super.tipoRequest = TipoRequest.write;
        if (titlepageid != null) {
            title = titlepageid;
            pageid = titlepageid;
            stringaPageIds = titlepageid;
            domain = this.getDomain();
            super.doInit();
        }// fine del blocco if
    } // fine del metodo

}// end of class
