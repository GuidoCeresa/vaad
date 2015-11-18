package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.TipoRisultato;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Superclasse astratta per le Request sul Web
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 */
public abstract class Request {

    //--codifica dei caratteri
    protected static String INPUT = "UTF8";

    //--validità specifica della request
    protected TipoRisultato risultato = TipoRisultato.erroreGenerico;

    //--validità generale della request (webUrl esistente e letto)
    protected boolean valida;

    //--indirizzo internet da leggere
    protected String webUrl;

    //--contenuto testuale completo della risposta (la seconda, se ci sono due request)
    private String testoResponse;

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
    public Request() {
    }// fine del metodo costruttore


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit() {
//        preliminaryRequest();
        doRequest();
    } // fine del metodo

    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doRequest() {
        try { // prova ad eseguire il codice
            urlRequest();
        } catch (Exception unErrore) { // intercetta l'errore
            valida = false;
        }// fine del blocco try-catch
    } // fine del metodo

//    /**
//     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     */
//    protected void preliminaryRequest() {
//    } // fine del metodo


    /**
     * Request
     * Quella base usa il GET
     * In alcune request (non tutte) è obbligatorio anche il POST
     */
    protected void urlRequest() throws Exception {
        URLConnection urlConn;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        //--GET
        urlConn = creaConnessione();
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--POST
        this.creaPost(urlConn);

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        elaboraRisposta(textBuffer.toString());
    } // fine del metodo


    /**
     * Stringa del browser per la request
     * <p>
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String getDomain() {
        return getWebUrl();
    } // end of getter method

    /**
     * Crea la connessione
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn = null;
        String domain = this.getDomain();

        if (domain != null && !domain.equals("")) {
            urlConn = new URL(domain).openConnection();
            urlConn.setDoOutput(true);
        }// end of if cycle

        return urlConn;
    } // fine del metodo

    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void creaPost(URLConnection urlConn) throws Exception {
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {

        if (rispostaRequest != null && !rispostaRequest.equals("")) {
            this.testoResponse = rispostaRequest;
            valida = true;
        }// end of if cycle

    } // end of getter method


    private String getWebUrl() {
        return webUrl;
    }// end of getter method


    private void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }//end of setter method


    public boolean isValida() {
        return valida;
    }// end of getter method

    public TipoRisultato getRisultato() {
        return risultato;
    }// end of getter method

    public String getTestoResponse() {
        return testoResponse;
    }// end of getter method

} // fine della classe