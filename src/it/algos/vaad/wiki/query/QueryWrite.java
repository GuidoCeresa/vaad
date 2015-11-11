package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.*;
import it.algos.webbase.web.lib.LibSession;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Necessita di Login per scrivere
 */
public abstract class QueryWrite extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGESID = "&pageids=";
    protected static String TAG_EDIT = "&meta=tokens";

    private boolean scritta = false;
    // mappa dati di passaggio tra la prima e la seconda request
    private HashMap mappa;

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
    public QueryWrite(long pageid, String testoNew) {
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
    public QueryWrite(long pageid, String testoNew, String summary) {
        super.tipoRicerca = TipoRicerca.pageid;
        this.doInit("" + pageid, testoNew, summary, null);
    }// fine del metodo costruttore

    protected void doInit(String titlepageid, String testoNew, String summary, WikiLogin login) {
        super.testoNew = testoNew;
        super.summary = summary;
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


    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        super.regolaRisultato(risultatoRequest);
        this.elaboraPrimaRequest(risultatoRequest);
    } // fine del metodo


    /**
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * I dati servono per costruire la seconda request
     *
     * @param testoRisposta alla prima Request
     */
    protected void elaboraPrimaRequest(String testoRisposta) {
        HashMap mappa = null;
        int pageid;

        // controllo di congruità
        if (testoRisposta != null) {
            mappa = LibWiki.creaMappaQuery(testoRisposta);
        }// fine del blocco if

        if (mappa != null) {
            this.mappa = mappa;
        }// fine del blocco if
    } // fine del metodo

    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getSecondoDomain() {
        String domain = "";
        String titolo = "";
        String tag = "https://it.wikipedia.org/w/api.php?format=json&action=edit";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        domain = tag + "&title=" + titolo;

        return domain;
    } // fine del metodo

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return post
     */
    @Override
    protected String getSecondoPost() {
        String testoPost;
        String testo = this.getTestoNew();
        String summary = this.getSummary();
        String edittoken = (String) this.mappa.get(LibWiki.TOKEN);

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
        testoPost += "&token=" + edittoken;

        return testoPost;
    } // fine della closure

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultatoSecondo(String risultatoRequest) {
        super.regolaRisultatoSecondo(risultatoRequest);
        this.elaboraSecondaRequest(risultatoRequest);
    } // end of getter method


    /**
     * Elabora la risposta alla seconda Request
     *
     * @param testoRisposta alla prima Request
     */
    protected void elaboraSecondaRequest(String testoRisposta) {
        boolean trovata = false;
        boolean scritta = false;
        HashMap mappa = LibWiki.creaMappaEdit(testoRisposta);

        if (testoRisposta.equals("")) {
            return;
        }// end of if cycle


        if (testoRisposta.contains(Cost.SUCCESSO)) {
            scritta = true;
        }// end of if cycle


    } // fine del metodo

    /**
     * Controlla di aver scritto la pagina
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isScritta() {
        return scritta;
    } // fine del metodo

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
