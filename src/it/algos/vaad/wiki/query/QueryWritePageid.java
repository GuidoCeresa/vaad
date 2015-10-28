package it.algos.vaad.wiki.query;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il pageid della pagina
 * Necessita di Login per scrivere
 */
public class QueryWritePageid extends QueryWrite {
    /**
     * Costruttore completo
     * Rinvia al costruttore completo
     */
    public QueryWritePageid(String titlepageid, String testoNew) {
        this(titlepageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore completo
     */
    public QueryWritePageid(int pageid, String testoNew) {
        this(pageid, testoNew, "");
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWritePageid(String titlepageid, String testoNew, String summary) {
        super(titlepageid, testoNew, summary);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWritePageid(int pageid, String testoNew, String summary) {
        super(pageid, testoNew, summary);
    }// fine del metodo costruttore


}// end of class
