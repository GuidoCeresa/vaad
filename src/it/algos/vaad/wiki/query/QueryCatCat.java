package it.algos.vaad.wiki.query;

/**
 * Query per recuperare le pagine e le sottocategorie di una categoria
 * Non necessita di Login, ma se esiste lo usa
 */
public class QueryCatCat extends QueryCat {

    /**
     * Costruttore completo
     */
    public QueryCatCat(String title) {
        this(title, LIMITE);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryCatCat(String title, int limite) {
        super(title, limite);
    }// fine del metodo costruttore


    @Override
    protected void doInit(String titlepageid) {
        namespace = NS_0+"&cmtype=subcat";
        super.doInit(titlepageid);
    } // fine del metodo

}// end of class
