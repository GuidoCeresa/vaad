package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.WikiLogin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Superclasse astratta per le Request sul Web
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 */
public abstract class Query {

    // codifica dei caratteri
    protected static String INPUT = "UTF8";

    // contenuto della pagina
//    protected String contenuto;

    // verifica finale
    protected boolean trovata = false;
    protected String errore = "";

    // indirizzo internet da leggere
    protected String domain;


    //--tipo di request - solo una per leggere - due per scrivere
    //--di default solo lettura (per la scrittura serve il login)
    protected TipoRequest tipoRequest = TipoRequest.read;
    protected WikiLogin wikiLogin;

    private String testoPrimaRequest;
    private String testoSecondaRequest;

    // utilizzo indispensabile del login
    private boolean serveLogin = false;

    // contenuto della pagina in scrittura
    private String testoNew;

    // oggetto della modifica in scrittura
    private String summary;

    /**
     * Metodo iniziale
     */
    protected void doInit() {
        try { // prova ad eseguire il codice
            testoPrimaRequest = this.firstRequest();
            if (tipoRequest == TipoRequest.write && isTrovata() && isValida()) {
                testoSecondaRequest = secondRequest();
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            errore = unErrore.getClass().getSimpleName();
        }// fine del blocco try-catch
    } // fine del metodo

    /**
     * This module only accepts POST requests.
     * <p>
     * Legge la pagina per ottenere il testo ed il token per la eventuale scrittura
     * <p>
     * Parameters first request:
     * action  = query
     * format  = json
     * prop    = info|revision
     * intoken = edit
     * titles  = xxx
     * <p>
     * Return:
     * "pageid": "22958",
     * "ns": "2",
     * "title": "Utente:Gac/Sandbox4",
     * "contentmodel": "wikitext",
     * "pagelanguage": "it",
     * "touched": "2012-11-05T09:32:37Z",
     * "lastrevid": 53714557,
     * "counter": "",
     * "length": 10,
     * "starttimestamp": "2013-09-15T05:54:35Z",
     * "edittoken": "c3c28fbdf02b792bbcd367377d6ed6d5+\\",
     * "revid": 53714557,
     * "parentid": 53714550,
     * "minor": "",
     * "user": "Gac",
     * "timestamp": "2012-11-05T09:32:37Z",
     * "comment": "test"
     */
    protected String firstRequest() throws Exception {
        URLConnection connection = null;
        InputStream input = null;
        InputStreamReader inputReader = null;
        BufferedReader readBuffer = null;
        StringBuilder textBuffer = new StringBuilder();
        String domain = this.getFirstDomain();
        String stringa;
        String contenuto;

        // find the target
        connection = creaConnessione(domain);

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        // legge la risposta
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        // chiude
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        contenuto = textBuffer.toString();
        regolaRisultato(contenuto);
        trovata = isValida();

        return contenuto;
    } // fine del metodo

    /**
     * This module only accepts POST requests.
     * <p>
     * Parameters (testoPost) second request:
     * lgname         - User Name
     * lgpassword     - Password
     * lgdomain       - Domain (optional)
     * lgtoken        - Login token obtained in first request
     * <p>
     * Nei cookies della seconda richiesta DEVE esserci la sessione (ottenuta dalla prima richiesta)
     * <p>
     * Return:
     * result         - "NeedToken"
     * token          - Primo token temporaneo
     * cookieprefix   - "itwiki" (default)
     * sessionid      - codice a 32 cifre
     */
    private String secondRequest() throws Exception {
        URLConnection connection = null;
        InputStream input = null;
        InputStreamReader inputReader = null;
        BufferedReader readBuffer = null;
        StringBuilder textBuffer = new StringBuilder();
        String domain = this.getSecondDomain();
        PrintWriter out;
        String testoPost;
        String stringa;
        String contenuto;

        // find the target
        connection = creaConnessione(domain);

        // now we send the data POST
        out = new PrintWriter(connection.getOutputStream());
        testoPost = this.getSecondoPost();
        out.print(testoPost);
        out.close();

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        // legge la risposta
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        // chiude
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        contenuto = textBuffer.toString();
        regolaRisultatoSecond(contenuto);
        trovata = isValida();

        return contenuto;
    } // fine del metodo

    /**
     * Crea la connessione
     * Regola i parametri della connessione
     */
    protected URLConnection creaConnessione(String domain) throws Exception {
        URLConnection urlConn = null;
        String txtCookies = "";
        WikiLogin login = null;

        if (isServeLogin()) {
            login = this.wikiLogin;
            if (login == null) {
                return null;
            }// end of if cycle
            txtCookies = login.getStringCookies();
        }// end of if cycle

        // regola le property
        if (domain != null && !domain.equals("")) {
            urlConn = new URL(domain).openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Accept-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
            if (isServeLogin()) {
                urlConn.setRequestProperty("Cookie", txtCookies);
            }// end of if cycle
        }// end of if cycle

        return urlConn;
    } // fine del metodo

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     *
     * @return post
     */
    private String getSecondoPost() {
        String testoPost;
        String testo = this.getTestoNew();
        String summary = this.getSummary();
//        String edittoken = this.getToken();

        if (testo != null && !testo.equals("")) {
            try { // prova ad eseguire il codice
                testo = URLEncoder.encode(testo, "UTF-8");

            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if
        if (summary != null && !summary.equals("")) {
            try { // prova ad eseguire il codice
                summary = URLEncoder.encode(summary, "UTF-8");
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        testoPost = "text=" + testo;
        testoPost += "&bot=true";
        testoPost += "&minor=true";
        testoPost += "&summary=" + summary;
//        testoPost += "&token=" + edittoken;

        return testoPost;
    } // fine della closure

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultato(String risultatoRequest) {
    } // end of getter method

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultatoSecond(String risultatoRequest) {
    } // end of getter method


    /**
     * Restituisce il contenuto della pagina
     */
    public String getContenuto() {
        if (tipoRequest == TipoRequest.read) {
            return testoPrimaRequest;
        } else {
            return testoSecondaRequest;
        }// end of if/else cycle
    } // end of getter method

    /**
     * Stringa del browser per la prima request
     * <p>
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String getFirstDomain() {
        return domain;
    } // end of getter method

    /**
     * Stringa del browser per la seconda request
     * <p>
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String getSecondDomain() {
        return domain;
    } // end of getter method


    public boolean isTrovata() {
        return trovata;
    } // end of getter method

    public String getErrore() {
        return errore;
    } // end of getter method

    public boolean isServeLogin() {
        return serveLogin;
    }// end of getter method

    public void setServeLogin(boolean serveLogin) {
        this.serveLogin = serveLogin;
    }//end of setter method

    public WikiLogin getWikiLogin() {
        return wikiLogin;
    }// end of getter method

    public void setWikiLogin(WikiLogin wikiLogin) {
        this.wikiLogin = wikiLogin;
    }//end of setter method

    public String getTestoNew() {
        return testoNew;
    }// end of getter method

    public void setTestoNew(String testoNew) {
        this.testoNew = testoNew;
    }//end of setter method

    public String getSummary() {
        return summary;
    }// end of getter method

    public void setSummary(String summary) {
        this.summary = summary;
    }//end of setter method

    /**
     * Controlla la validità del risultato
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    protected abstract boolean isValida();

} // fine della classe
