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
    limitOver("cmlimit may not be over"),

    creata("Creata nuova pagina."),
    esistenteNuova("Pagina già esistente."),

    noLogin("Manca il login"),
    noToken("Manca il token"),
    mustbeposted("The login module requires a POST request"),
    assertuserfailed("Assertion that the user is logged in failed"),
    assertbotfailed("Assertion that the user has the bot right failed"),
    loginUser("Collegato come utente"),
    loginBot("Collegato come bot"),
    loginSysop("Collegato come admin"),
    notExists("The username you provided doesn't exist"),
    wrongPass("The password you provided is incorrect"),
    throttled("You've logged in too many times in a short time"),

    registrata("Pagina registrata."),
    nonRegistrata("Pagina non registrata (probabilmente cancellata)."),
    modificaRegistrata("Registrata modifica alla voce."),
    modificaInutile("La voce aveva già il testo richiesto."),

    spostata("Pagina spostata"),
    selfmove("Can't move a page to itself"),
    articleexists("The destination article already exists and is not a redirect to the source article"),
    protectedtitle("The destination article has been protected from creation");


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
