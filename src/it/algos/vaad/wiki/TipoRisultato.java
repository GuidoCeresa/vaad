package it.algos.vaad.wiki;

/**
 * Created by gac on 11 nov 2015.
 * .
 */
public enum TipoRisultato {

    erroreGenerico("Errore generico."),
    nonTrovata("Pagina inesistente."),
    esistente("Pagina esistente."),
    letta("Letta la pagina."),

    creata("Creata nuova pagina."),
    esistenteNuova("Pagina già esistente."),

    noLogin("Manca il login"),
    registrata("Pagina registrata."),
    nonRegistrata("Pagina non registrata (probabilmente cancellata)."),
    modificaRegistrata("Registrata modifica alla voce."),
    modificaInutile("La voce aveva già il testo richiesto.");

    private String tag;


    /**
     * Costruttore completo con parametri.
     *
     * @param tag in entrata
     */
    TipoRisultato(String tag) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag);
    }// fine del metodo costruttore

    public String getTag() {
        return tag;
    }// end of getter method

    public void setTag(String tag) {
        this.tag = tag;
    }//end of setter method

} // fine della Enumeration
