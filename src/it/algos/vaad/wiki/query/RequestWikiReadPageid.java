package it.algos.vaad.wiki.query;

/**
 * Created by gac on 18 nov 2015.
 * .
 */
    public class RequestWikiReadPageid extends RequestWikiRead {

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
        public RequestWikiReadPageid(long wikiPageid) {
            super(wikiPageid);
        }// fine del metodo costruttore completo

    } // fine della classe
