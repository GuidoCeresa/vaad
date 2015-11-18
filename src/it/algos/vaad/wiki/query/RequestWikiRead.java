package it.algos.vaad.wiki.query;

/**
 * Created by gac on 18 nov 2015.
 * .
 */
public abstract class RequestWikiRead extends RequestWiki {

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano direttamente il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse (questa classe astratta)
     //     * L'istanza di questa classe viene chiamata con l'indirizzo webUrl (indispensabile)
     //     * Viene invocato il metodo doSetup() che permette alle sottoclassi di regolare alcuni parametri statici
     //     * Viene invocato subito dopo il metodo doInit() che esegue la urlRequest()
     */
    public RequestWikiRead() {
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il titolo della pagina wiki (indispensabile)
     * Rinvia al costruttore della superclasse
     * Dalla superclasse viene chiamato il metodo doSetup() che può essere sovrascritto
     * Dalla superclasse viene chiamato il metodo doInit() che può essere sovrascritto
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiRead(String wikiTitle) {
        super(wikiTitle);
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il pageid della pagina wiki (indispensabile)
     * Rinvia al costruttore della superclasse
     * Dalla superclasse viene chiamato il metodo doSetup() che può essere sovrascritto
     * Dalla superclasse viene chiamato il metodo doInit() che può essere sovrascritto
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public RequestWikiRead(long wikiPageid) {
        super(wikiPageid);
    }// fine del metodo costruttore completo

} // fine della classe
