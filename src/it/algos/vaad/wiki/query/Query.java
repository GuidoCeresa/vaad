package it.algos.vaad.wiki.query;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Superclasse astratta per le Request sul Web
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 */
public abstract class Query {

    // codifica dei caratteri
    protected static String INPUT = "UTF8";

    // indirizzo internet da leggere
    protected String domain;

    // contenuto della pagina
    protected String contenuto;

    // verifica finale
    protected boolean trovata = false;
    protected String errore = "";

    /**
     * Metodo iniziale
     */
    protected void doInit() {
        try { // prova ad eseguire il codice
            this.request();
        } catch (Exception unErrore) { // intercetta l'errore
            errore = unErrore.getClass().getSimpleName();
        }// fine del blocco try-catch
    } // fine del metodo

    /**
     * Recupera il contenuto completo della pagina web
     */
    protected void request() throws Exception {
        URLConnection connection = null;
        InputStream input = null;
        InputStreamReader inputReader = null;
        BufferedReader readBuffer = null;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        // find the target
        connection = creaConnessione();

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
        regolaRisultato(textBuffer.toString());
        trovata = isValida();
    } // fine del metodo


    /**
     * Crea la connessione
     * Regola i parametri della connessione
     */
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn;

        // regola le property
        urlConn = new URL(domain).openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("Accept-Encoding", "GZIP");
        urlConn.setRequestProperty("Content-Encoding", "GZIP");
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");

        return urlConn;
    } // fine del metodo


    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultato(String risultatoRequest) {
        contenuto = risultatoRequest;
    } // fine del metodo

    public String getContenuto() {
        return contenuto;
    } // end of getter method

    public boolean isTrovata() {
        return trovata;
    } // end of getter method

    public String getErrore() {
        return errore;
    } // end of getter method

    /**
     * Controlla la validità del risultato
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    protected abstract boolean isValida();

} // fine della classe
