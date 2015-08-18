package it.algos.vaad.wiki;

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

    @Override
    public boolean isValida() {
        boolean valida = true;

        if (contenuto.equals("")) {
            valida = false;
        }// fine del blocco if

        if (contenuto.length() < 200 && contenuto.contains("missing")) {
            valida = false;
        }// fine del blocco if

        return valida;
    } // end of getter method

}// end of class
