package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

/**
 * Query standard per leggere/scrivere il risultato di una pagina
 * NON legge le categorie
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Legge o scrive (a seconda della sottoclasse concreta utilizzata)
 * Legge le informazioni base della pagina (oltre al risultato)
 * Legge una sola Pagina con le informazioni base
 * Necessita di Login per scrivere, non per leggere solamente
 */
public abstract class QueryPage extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static String TAG_INI = "https://it.wikipedia.org/w/api.php?format=json&action=query";
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_QUERY = TAG_INI + TAG_PROP;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryPage(String titlepageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        super(titlepageid, tipoRicerca, tipoRequest);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryPage(int pageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        super(pageid, tipoRicerca, tipoRequest);
    }// fine del metodo costruttore


}// end of class
