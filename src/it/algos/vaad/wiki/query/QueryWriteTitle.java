package it.algos.vaad.wiki.query;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina
 * Necessita di Login per scrivere
 */
public class QueryWriteTitle extends QueryWrite {

    /**
     * Costruttore completo
     * Rinvia al costruttore completo
     */
    public QueryWriteTitle(String title, String testoNew) {
        this(title, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWriteTitle(String title, String testoNew, String summary) {
        super(title, testoNew, summary);
    }// fine del metodo costruttore

}// end of class
