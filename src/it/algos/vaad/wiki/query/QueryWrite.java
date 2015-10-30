package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.webbase.web.lib.LibSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Necessita di Login per scrivere
 */
public abstract class QueryWrite extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static  String TAG_PROP = Cost.CONTENT_ALL;
    protected static  String TAG_TITOLO = "&titles=";
    protected static  String TAG_PAGESID = "&pageids=";
    protected static  String TAG_EDIT = "&meta=tokens";

    // contenuto della pagina
    private String testoNew;
    private String testoPrimaRequest;
    private String testoSecondaRequest;

    // oggetto della modifica
    private String summary;


    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(String titlepageid, String testoNew) {
        this(titlepageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(int pageid, String testoNew) {
        this(pageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(String titlepageid, String testoNew, String summary) {
        this(titlepageid, testoNew, summary, null);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(String titlepageid, String testoNew, String summary, WikiLogin login) {
        super.tipoRicerca = TipoRicerca.title;
        this.doInit(titlepageid, testoNew, summary, login);
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(int pageid, String testoNew, String summary) {
        super.tipoRicerca = TipoRicerca.pageid;
        this.doInit("" + pageid, testoNew, summary, null);
    }// fine del metodo costruttore

    protected void doInit(String titlepageid, String testoNew, String summary, WikiLogin login) {
        this.testoNew = testoNew;
        this.summary = summary;
        super.tipoRequest = TipoRequest.write;
        super.setServeLogin(true);

        if (login == null) {
            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        } else {
            wikiLogin = login;
        }// end of if/else cycle

        if (titlepageid != null) {
            title = titlepageid;
            pageid = titlepageid;
            stringaPageIds = titlepageid;
            domain = this.getDomain();
        }// fine del blocco if

        super.doInit();
    } // fine del metodo



//    /**
//     * Scrive il contenuto completo della pagina web
//     */
//    private void writeRequest() throws Exception {
//        URLConnection connection = null;
//        InputStream input = null;
//        InputStreamReader inputReader = null;
//        BufferedReader readBuffer = null;
//        StringBuilder textBuffer = new StringBuilder();
//        String stringa;
//
//        // find the target
//        connection = creaConnessioneWrite();
//
//        // regola l'entrata
//        input = connection.getInputStream();
//        inputReader = new InputStreamReader(input, INPUT);
//
//        // legge la risposta
//        readBuffer = new BufferedReader(inputReader);
//        while ((stringa = readBuffer.readLine()) != null) {
//            textBuffer.append(stringa);
//        }// fine del blocco while
//
//        // chiude
//        readBuffer.close();
//        inputReader.close();
//        input.close();
//
//        // controlla il valore di ritorno della request e regola il risultato
//        contenuto = textBuffer.toString();
//        regolaRisultatoWrite(textBuffer.toString());
//        trovata = isValida();
//    } // fine del metodo

//    /**
//     * Crea la connessione
//     * Regola i parametri della connessione
//     */
//    protected URLConnection creaConnessioneWrite() throws Exception {
//        URLConnection urlConn = null;
//        String txtCookies = "";
//        WikiLogin login = null;
//
//        if (isServeLogin()) {
//            login = this.wikiLogin;
//            if (login == null) {
//                return null;
//            }// end of if cycle
//            txtCookies = login.getStringCookies();
//        }// end of if cycle
//
//        // regola le property
//        if (domain != null && !domain.equals("")) {
//            urlConn = new URL(domain).openConnection();
//            urlConn.setDoOutput(true);
//            urlConn.setRequestProperty("Accept-Encoding", "GZIP");
//            urlConn.setRequestProperty("Content-Encoding", "GZIP");
//            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
//            if (isServeLogin()) {
//                urlConn.setRequestProperty("Cookie", txtCookies);
//            }// end of if cycle
//        }// end of if cycle
//
//        return urlConn;
//    } // fine del metodo

}// end of class
