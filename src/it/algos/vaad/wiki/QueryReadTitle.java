package it.algos.vaad.wiki;

import java.net.URLEncoder;

/**
 * Query standard per leggere il risultato di una pagina
 * NON legge le categorie
 * Usa il titolo della pagina
 * Non necessita di Login
 */
public class QueryReadTitle extends QueryPage {

    // tag per la costruzione della stringa della request
    private static String TAG_INI = "https://it.wikipedia.org/w/api.php?format=json&action=query";
    private static String TAG_PROP = "&prop=info|revisions&rvprop=content";
    private static String TAG_QUERY = TAG_INI + TAG_PROP;
    private static String TAG_TITOLO = "&titles=";
    private static String TAG_PAGEID = "&pageids=";

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryReadTitle(String title) {
        super(title, TipoRicerca.title, TipoRequest.read);
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
            domain = TAG_QUERY + TAG_TITOLO + URLEncoder.encode(title, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

}// end of class
