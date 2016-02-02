package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;

import java.util.HashMap;

/**
 * Classe concreta per le Request sul Web
 * Fornisce le funzionalità di base per una request standard sul web
 * Usa solo il metodo GET
 * Legge una pagina internet (qualsiasi)
 * Accetta SOLO un webUrl (indirizzo) completo
 */
public class RequestWeb extends ARequest {

    private final static String WEB_MISSING = "UnknownHostException";


    /**
     * Costruttore completo
     *
     * @param webUrl indirizzo webUrl completo
     */
    public RequestWeb(String webUrl) {
        super(webUrl);
    }// fine del metodo costruttore completo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraDomain() {
        domain = wikiTitle;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {
        if (!rispostaRequest.equals("")) {
            valida = true;
            risultato = TipoRisultato.letta;
            testoResponse = rispostaRequest;
        }// fine del blocco if
    } // fine del metodo

} // fine della classe
