package it.algos.vaad.wiki;

import com.vaadin.ui.Notification;
import it.algos.vaad.wiki.request.QueryCat;
import it.algos.webbase.web.lib.LibSession;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:31
 * <p>
 * Wrapper coi dati della connessione.
 * </p>
 * Se arriva la richiesta per la costruzione dell'istanza senza nickname e password:
 * la classe prima cerca nei cookies,
 * poi (se non li trova) cerca nelle preferenze
 * e poi (se non le trova) presenta un dialogo di controllo
 * <p>
 * Questa classe: <ul>
 * <li> Ogni utilizzo del bot deve essere preceduto da un login </li>
 * <li> Il login deve essere effettuato tramite le API </li>
 * <li> Il login deve essere effettuato con lingua e progetto </li>
 * <li> Il login deve essere effettuato con nickname e password </li>
 * <li> Il server wiki rimanda indietro la conferma IN DUE posti: i cookies ed il testo </li>
 * <li> Controlla che l'accesso abbia un risultato positivo </li>
 * <li> Due modalità di controllo: semplice legge SOLO i cookies e non il testo</li>
 * <li> Completa legge anche il testo e LO CONFRONTA con i cookies per ulteriore controllo </li>
 * <li> Mantiene il nome della wiki su cui operare </li>
 * <li> Mantiene il lguserid </li>
 * <li> Mantiene il lgusername </li>
 * <li> Mantiene il lgtoken </li>
 * <li> Mantiene il sessionid </li>
 * <li> Mantiene il cookieprefix </li>
 * </ul>
 * <p>
 * Tipicamente esiste un solo oggetto di questo tipo per il bot
 * L'istanza viene creata all'avvio del programma e mantenuta disponibile nel servletContext
 * <p>
 * <p>
 * -----
 * La prima request non serve che mandi il post
 * <p>
 * action=login is deprecated
 * action=query&meta=tokens&type=login instead
 * <p>
 * // * -domain: lgname e lgpassword
 * -post: nullo
 * -cookies: niente
 * <p>
 * il cookie ritorna: itwikisession
 * il testo ritorna: logintoken
 * <p>
 * La seconda request
 * -domain: action=login
 * -post: lgtoken, lgname e lgpassword
 * -cookie: itwikisession
 *
 * @author Guido Andrea Ceresa
 * @author gac
 * @see //www.mediawiki.org/wiki/API:Login
 */
public class WikiLogin {

    // key to store the Login object in the session
    public static final String WIKI_LOGIN_KEY_IN_SESSION = "wikilogin";
    private static final String FIRST_RESULT = "result";
    private static final String SECOND_RESULT = "result";
    private static final String FIRST_TOKEN = "token";
    private static final String FIRST_NEW_TOKEN = "tokens";
    private static final String LOGIN_TOKEN = "logintoken";
    private static final String SECOND_TOKEN = "lgtoken";
    private static final String COOKIE_PREFIX = "cookieprefix";
    private static final String SESSION_ID = "sessionid";
    private static final String USER_ID = "lguserid";
    private static final String USER_NAME = "lgusername";
    protected static String INPUT = "UTF8";
    //--codifica dei caratteri
    private static String ENCODE = "UTF-8";
    // lingua di default
    private static String LINGUA_DEFAULT = "it";

    // progetto di default
    private static Progetto PROGETTO_DEFAULT = Progetto.wikipedia;

    // lingua della wiki su cui si opera (solo due lettere)
    private String lingua;

    // progetto della wiki su cui si opera (da una Enumeration)
    private Progetto progetto;

    private String firstDomain;
    private String secondDomain;
    private String assertUserDomain;
    private String assertBotDomain;

    private String firstPost;
    private String secondPost;
    private String assertUserPost;
    private String assertBotPost;

    // nome utente (parametro in entrata)
    private String lgname;

    // password dell'utente  (parametro in entrata)
    private String lgpassword;

    // risultato  (parametro di ritorno del primo collegamento)
    private ErrLogin firstResult;

    // risultato  (parametro di ritorno definitivo del secondo collegamento)
    private String result;

    // id utente   (parametro di ritorno)
    private long lguserid;

    // nome utente (parametro di ritorno)
    private String lgusername;

    // token di controllo provvisorio (parametro di ritorno dal primo collegamento)
    private String token;

    // token di controllo definitivo (parametro in entrata al secondo collegamento)
    // parametro usato dai collegamenti successivi al login
    private String lgtoken;

    // prefisso dei cookies (parametro di ritorno)
    private String cookieprefix;

    // sessione (parametro di ritorno)
    private String sessionId;

    // i collegamenti per completare il login sono due
    // siccome la chiamata al metodo è ricorsiva
    // occorre essere sicuri che effettui solo 2 chiamate
    private boolean primoCollegamento = true;

    // controllo di validità del collegamento effettuato 2 volte con risultato positivo
    private boolean valido = false;

    // errore di collegamento (vuoto se collegamento valido)
    private ErrLogin risultato;

    // mappa dei parametri
    // ci metto i valori della enumeration ParLogin
    // la Enumeration non può essere una classe interna, perchgé in groovy non funziona (in java si)
    private HashMap<String, Object> par;

    // mappa dei cookies
    // ci metto tutti i cookies restituiti da URLConnection.responses
    private HashMap<String, String> cookies;

    // flag di controllo per il collegamento come user (equivale al flag valido)
    private boolean user = false;

    // flag di controllo per il collegamento come bot
    private boolean bot = false;

//    /**
//     * Costruttore parziale
//     */
//    public WikiLogin() {
//        this.setLingua(LINGUA_DEFAULT);
//        this.setProgetto(PROGETTO_DEFAULT);
//    }// fine del metodo costruttore

    /**
     * Costruttore parziale con lingua e progetto standard
     *
     * @param lgname     nickName in entrata per il collegamento
     * @param lgpassword password in entrata per il collegamento
     */
    public WikiLogin(String lgname, String lgpassword) {
        this(LINGUA_DEFAULT, PROGETTO_DEFAULT, lgname, lgpassword);
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     *
     * @param lingua     della wikipedia utilizzata
     * @param progetto   della wikipedia foundation utilizzato
     * @param lgname     nickName in entrata per il collegamento
     * @param lgpassword password in entrata per il collegamento
     */
    public WikiLogin(String lingua, Progetto progetto, String lgname, String lgpassword) {
        this.setLingua(lingua);
        this.setProgetto(progetto);
        this.setLgname(lgname);
        this.setLgpassword(lgpassword);

        //--cancella prima i cookies
        // this.logout()

        this.fixDomain();

        // procedura di accesso e registrazione con le API
        // Logging in through the API requires submitting a login query and constructing a cookie
        // In MediaWiki 1.15.3+, you must confirm the login by resubmitting the login request with the token returned.
        try { // prova ad eseguire il codice
            this.preliminaryRequest();
            this.urlRequest();
        } catch (Exception unErrore) { // intercetta l'errore
            this.setRisultato(ErrLogin.generico);
        }// fine del blocco try-catch

        LibSession.setAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION, this);
        chekBot();
    }// fine del metodo costruttore completo

    /**
     * POST request.
     * <p>
     * domain: solo la richiesta del token
     * post: nessuno
     * cookies inviati: nessuno
     * cookies ricevuti: la sessione
     * risposta: token
     */
    private void preliminaryRequest() throws Exception {
        URLConnection urlConn;
        PrintWriter out;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        //--connessione
        urlConn = new URL(firstDomain).openConnection();
        urlConn.setDoOutput(true);

        //--POST
        out = new PrintWriter(urlConn.getOutputStream());
        out.close();

        // regola l'entrata
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        this.downlopadCookies(urlConn);

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
        risposta = textBuffer.toString();
        this.risultatoPreliminaryRequest(risposta);
    } // fine del metodo


    /**
     * Regola il domain del primo collegamento effettuato per ricevere il token di conferma
     */
    private void fixDomain() {
        String domain = "";
        String lingua;
        Progetto progetto;

        lingua = this.getLingua();
        progetto = this.getProgetto();

        if (!lingua.equals("") && progetto != null) {
            domain += Cost.API_HTTP;
            domain += lingua;
            domain += Cost.API_WIKI;
            domain += progetto;
            domain += Cost.API_ACTION;
            domain += Cost.API_LOGIN;
            domain += Cost.API_FORMAT;
        }// fine del blocco if

        this.firstDomain = domain;
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza in una mappa
     *
     * @param urlConn connessione
     */
    private void downlopadCookies(URLConnection urlConn) {
        String headerName;
        String cookie;
        String name;
        String value;
        HashMap<String, String> mappa = new HashMap<String, String>();

        // controllo di congruità
        if (urlConn != null) {
            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = urlConn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    name = cookie.substring(0, cookie.indexOf("="));
                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    mappa.put(name, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        this.setCookies(mappa);
    } // fine del metodo


    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     * <p>
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * Restituisce il parametro risultato
     * <p>
     * Regola i parametri dopo la prima Request (solo se positiva)
     * I parametri dovrebbero essere 4
     *
     * @param testoRisposta della prima Request
     */
    private void risultatoPreliminaryRequest(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        ErrLogin firstresult;
        JSONObject obj;

        // Costruisce la mappa dei dati dalla risposta alla prima Request
        // Restituisce il parametro risultato
        if (!testoRisposta.equals("")) {
            mappa = LibWiki.creaMappaLogin(testoRisposta);
            this.setPar(mappa);
        }// fine del blocco if

        if (mappa != null && mappa.size() > 0) {
            if (mappa.get(FIRST_RESULT) != null && mappa.get(FIRST_RESULT) instanceof String) {
                firstresult = ErrLogin.get((String) mappa.get(FIRST_RESULT));
                this.setFirstResult(firstresult);
            }// fine del blocco if

            if (mappa.get(FIRST_TOKEN) != null && mappa.get(FIRST_TOKEN) instanceof String) {
                this.setToken((String) mappa.get(FIRST_TOKEN));
            }// fine del blocco if

            if (mappa.get(FIRST_NEW_TOKEN) != null && mappa.get(FIRST_NEW_TOKEN) instanceof JSONObject) {
                obj = (JSONObject) mappa.get(FIRST_NEW_TOKEN);
                if (obj.get(LOGIN_TOKEN) != null && obj.get(LOGIN_TOKEN) instanceof String) {
                    token = (String) obj.get(LOGIN_TOKEN);
                    this.setToken(token);
                }// fine del blocco if
            }// fine del blocco if

            if (mappa.get(COOKIE_PREFIX) != null && mappa.get(COOKIE_PREFIX) instanceof String) {
                this.setCookieprefix((String) mappa.get(COOKIE_PREFIX));
            }// fine del blocco if

            if (mappa.get(SESSION_ID) != null && mappa.get(SESSION_ID) instanceof String) {
                this.setSessionId((String) mappa.get(SESSION_ID));
            }// fine del blocco if
        }// fine del blocco if

    } // fine del metodo


    /**
     * POST request.
     * <p>
     * domain: solo la richiesta del token
     * post: lgname, lgpassword, lgtoken
     * cookies inviati: session
     * cookies ricevuti: session (la stessa inviata), userID, userName, forceHTTPS,
     * centralauth_User, centralauth_Token, centralauth_Session,
     * WMF-Last-Access, GeoIP
     * risposta: success, lguserid, lgusername, lgtoken (diverso), cookieprefix, sessionid (la stessa inviata)
     */
    private void urlRequest() throws Exception {
        URLConnection urlConn;
        PrintWriter out;
        String testoPost;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        //--connessione
        urlConn = new URL(firstDomain).openConnection();
        urlConn.setDoOutput(true);

        //--rimanda i cookies arrivati con la prima richiesta
        urlConn.setRequestProperty("Cookie", cookieSession());

        // now we send the data POST
        out = new PrintWriter(urlConn.getOutputStream());
        testoPost = this.getSecondoPost();
        out.print(testoPost);
        out.close();

        // regola l'entrata
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        this.downlopadCookies(urlConn);

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
        risposta = textBuffer.toString();
        this.risultatoRisposta(risposta);
    } // fine del metodo


    /**
     * Costruisce il cookie della sessione
     */
    private String cookieSession() {
        String cookieText = "";
        String sessionKey = "itwikiSession";
        String sep = "=";
        String sessionValue = this.getSessionId();

        return sessionKey + sep + sessionValue;
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    private void uploadCookies(URLConnection urlConn) {
        HashMap<String, String> cookies;
        Object[] keyArray;
        Object[] valArray;
        Object sessionObj = null;
        String sessionKey = "itwikiSession";
//        String sessionTxt = "";
        String sessionValue = "";
        String sep = "=";
        Object valObj = null;
        String valTxt = "";
        String key;

        // controllo di congruità
        if (urlConn != null) {
            cookies = this.getCookies();
            if (cookies != null && cookies.size() > 0) {

                for (Object obj : cookies.keySet()) {
                    if (obj instanceof String) {
                        key = (String) obj;
                        valObj = cookies.get(key);
                        valTxt += key;
                        valTxt += "=";
                        valTxt += valObj;
                        valTxt += ";";
                    }// end of if cycle
                }

                if (cookies.get(sessionKey) != null) {
                    sessionValue = (String) cookies.get(sessionKey);
                }// end of if cycle
            }// fine del blocco if

//            urlConn.setRequestProperty("Cookie", sessionKey + sep + sessionValue);

        }// fine del blocco if
    } // fine del metodo


//    /**
//     * Restituisce il testo del POST per la prima Request
//     *
//     * @return post
//     * @deprecated
//     */
//    private String getPrimoPost() {
//        String testoPost = "";
//        String lgname;
//        String password;
//        String tokenProvvisorio;
//
//        lgname = this.getLgname();
//        password = this.getLgpassword();
//
//        testoPost += "lgname=";
//        testoPost += lgname;
//        testoPost += "&lgpassword=";
//        testoPost += password;
//
//        return testoPost;
//    } // fine del metodo

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     *
     * @return post
     */
    private String getSecondoPost() {
        String testoPost = "";
//        String lgname=this.getLgname();
//        String password;
//        String firstToken = this.getToken();
//
//        lgname = this.getLgname();
//        password = this.getLgpassword();

//        try { // prova ad eseguire il codice
//            firstToken = URLEncoder.encode(firstToken, ENCODE);
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch

        testoPost += "lgname=";
        testoPost += this.getLgname();
        testoPost += "&lgpassword=";
        testoPost += this.getLgpassword();
        testoPost += "&lgtoken=";
        try { // prova ad eseguire il codice
            testoPost += URLEncoder.encode(this.getToken(), ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return testoPost;
    } // fine del metodo


    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     *
     * @param testoRisposta della seconda Request
     */
    private void risultatoRisposta(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        String cookieprefix;
        String tokenProvvisorio;
        String txtResult;
        String password;
        ErrLogin risultato;

        // pulisce il parametro prima di controllare
        this.setValido(false);

        // Costruisce la mappa dei dati dalla risposta alla seconda Request
        // Restituisce il parametro risultato
        mappa = this.elaboraSecondaRisposta(testoRisposta);

//        // elabora il risultato
//        switch (risultato) {
//            case ErrLogin.success:
//                this.setValido(true)
//
        // mette da parte i parametri restituiti dal server
        this.regolaParametriSecondaRequest(mappa);
//
//                break
//            case ErrLogin.noName:
//                break
//            case ErrLogin.illegal:
//                break
//            case ErrLogin.notExists:
//                break
//            case ErrLogin.emptyPass:
//                break
//            case ErrLogin.wrongPass:
//                break
//            case ErrLogin.wrongPluginPass:
//                break
//            case ErrLogin.createBlocked:
//                break
//            case ErrLogin.throttled:
//                break
//            case ErrLogin.blocked:
//                break
//            case ErrLogin.mustbeposted:
//                break
//            case ErrLogin.needToken:
//                break
//            default: // caso non definito
//                break
//        } // fine del blocco switch

        risultato = this.getRisultato();
        if (risultato == ErrLogin.success) {
            this.setValido(true);
        }// fine del blocco if
    } // fine della closure


    /**
     * Costruisce la mappa dei dati dalla risposta alla seconda Request
     * Restituisce il parametro risultato
     *
     * @param testoRisposta della seconda Request
     */
    private HashMap<String, Object> elaboraSecondaRisposta(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        ErrLogin risultato = ErrLogin.generico;

        if (!testoRisposta.equals("")) {
            mappa = LibWiki.creaMappaLogin(testoRisposta);
//            this.setPar(mappa);
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Regola i parametri dopo la seconda Request
     * I parametri dovrebbero essere 6
     */
    private void regolaParametriSecondaRequest(HashMap<String, Object> mappa) {
        ErrLogin firstresult;

        if (mappa != null && mappa.size() >= 6) {
            if (mappa.get(SECOND_RESULT) != null && mappa.get(SECOND_RESULT) instanceof String) {
                risultato = ErrLogin.get((String) mappa.get(SECOND_RESULT));
                this.setRisultato(risultato);
            }// fine del blocco if

            if (mappa.get(USER_ID) != null && mappa.get(USER_ID) instanceof Long) {
                this.setLguserid((Long) mappa.get(USER_ID));
            }// fine del blocco if

            if (mappa.get(USER_NAME) != null && mappa.get(USER_NAME) instanceof String) {
                this.setLgusername((String) mappa.get(USER_NAME));
            }// fine del blocco if

            if (mappa.get(SECOND_TOKEN) != null && mappa.get(SECOND_TOKEN) instanceof String) {
                this.setToken((String) mappa.get(SECOND_TOKEN));
            }// fine del blocco if

            if (mappa.get(COOKIE_PREFIX) != null && mappa.get(COOKIE_PREFIX) instanceof String) {
                this.setCookieprefix((String) mappa.get(COOKIE_PREFIX));
            }// fine del blocco if

            if (mappa.get(SESSION_ID) != null && mappa.get(SESSION_ID) instanceof String) {
                this.setSessionId((String) mappa.get(SESSION_ID));
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo


    /**
     * Controlla se il collegamento è effettuato con i privilegi del bot
     */
    public void chekBot() {
        boolean isBot = false;
        QueryCat query;

        assertRequest();
//        String titoloCategoria = "Nati il 29 febbraio";
//        query = new QueryCat(titoloCategoria);
//
//        if (query != null) {
//            isBot = query.isLimite5000();
//        }// fine del blocco if

//        this.setBot(isBot);
    } // fine del metodo

    /**
     * This module only accepts POST requests.
     * Parameters (testoPost) second request:
     * lgname         - User Name
     * lgpassword     - Password
     * lgdomain       - Domain (optional)
     * lgtoken        - Login token obtained in first request
     * Nei cookies della seconda richiesta DEVE esserci la sessione (ottenuta dalla prima richiesta)
     * Return:
     * result         - "NeedToken"
     * token          - Primo token temporaneo
     * cookieprefix   - "itwiki" (default)
     * sessionid      - codice a 32 cifre
     */
    private void assertRequest() {
        String domain;
        URLConnection connection = null;
        PrintWriter out;
        String testoPost;
        InputStream input = null;
        InputStreamReader inputReader;
        BufferedReader readBuffer = null;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        // find the target
//        domain = "https://it.wikipedia.org/w/api.php?action=query&assert=bot&titles=Utente:Biobot/5";
//        "https://it.wikipedia.org/w/api.php?action=login&format=json&formatversion=2"
        domain = "https://it.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&assert=user";
        try { // prova ad eseguire il codice
            connection = new URL(domain).openConnection();
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("openConnection", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
            return;
        }// fine del blocco try-catch
        connection.setDoOutput(true);

        //--rimanda i cookies arrivati con la prima richiesta
        this.uploadAssertCookies(connection);
//        String txtCookies = this.getStringCookies();
//        connection.setRequestProperty("Cookie", txtCookies);

        try { // prova ad eseguire il codice
            out = new PrintWriter(connection.getOutputStream());
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("getOutputStream", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
            return;
        }// fine del blocco try-catch

        // now we send the data POST
//        testoPost = this.getSecondoPost();
//        out.print(testoPost);
        out.close();

        // regola l'entrata
        try { // prova ad eseguire il codice
            input = connection.getInputStream();
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("getInputStream", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
            return;
        }// fine del blocco try-catch
        try { // prova ad eseguire il codice
            inputReader = new InputStreamReader(input, "UTF8");
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("InputStreamReader", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
            return;
        }// fine del blocco try-catch

//        //--recupera i cookies ritornati e li memorizza nei parametri
//        //--in modo da poterli rinviare nella seconda richiesta
//        this.downlopadCookies(connection);

        // legge la risposta
        try { // prova ad eseguire il codice
            readBuffer = new BufferedReader(inputReader);
            while ((stringa = readBuffer.readLine()) != null) {
                textBuffer.append(stringa);
            }// fine del blocco while
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("BufferedReader", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
        }// fine del blocco try-catch

        // chiude
        try { // prova ad eseguire il codice
            if (readBuffer != null) {
                readBuffer.close();
            }// fine del blocco if
            inputReader.close();
            input.close();
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("readBuffer.close", unErrore.toString(), Notification.Type.WARNING_MESSAGE);
        }// fine del blocco try-catch

        // valore di ritorno della request
        risposta = textBuffer.toString();

        // Controllo del testo di risposta
        this.risultatoTerzaRisposta(risposta);
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    private void uploadAssertCookies(URLConnection urlConn) {
        HashMap<String, String> cookies;
        Object[] keyArray;
        Object[] valArray;
        Object sessionObj = null;
        String sessionKey = "itwikiSession";
//        String sessionTxt = "";
        String sessionValue = "";
        String sep = "=";
        Object valObj = null;
        String valTxt = "";
        String key;

        // controllo di congruità
        if (urlConn != null) {
            cookies = this.getCookies();
            if (cookies != null && cookies.size() > 0) {

                for (Object obj : cookies.keySet()) {
                    if (obj instanceof String) {
                        key = (String) obj;
                        valObj = cookies.get(key);
                        valTxt += key;
                        valTxt += "=";
                        valTxt += valObj;
                        valTxt += ";";
                    }// end of if cycle
                }

//                keyArray = cookies.keySet().toArray();
//                if (keyArray.length > 0) {
//                    sessionObj = keyArray[0];
//                }// fine del blocco if
//                if (sessionObj != null && sessionObj instanceof String) {
//                    sesionTxt = (String) sessionObj;
//                }// fine del blocco if
//
//                valArray = cookies.values().toArray();
//                if (valArray.length > 0) {
//                    valObj = valArray[0];
//                }// fine del blocco if
//                if (valObj != null && valObj instanceof String) {
//                    valTxt = (String) valObj;
//                }// fine del blocco if

            }// fine del blocco if

            if (cookies.get(sessionKey) != null) {
                sessionValue = (String) cookies.get(sessionKey);
            }// end of if cycle

            urlConn.setRequestProperty("Cookie", valTxt);

        }// fine del blocco if
    } // fine del metodo

    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     *
     * @param testoRisposta della seconda Request
     */
    private void risultatoTerzaRisposta(String testoRisposta) {
        HashMap mappa;
        String cookieprefix;
        String tokenProvvisorio;
        String txtResult;
        String password;
        ErrLogin risultato;

        // pulisce il parametro prima di controllare
//        this.setValido(false);

        // Costruisce la mappa dei dati dalla risposta alla seconda Request
        // Restituisce il parametro risultato
//        this.elaboraSecondaRisposta(testoRisposta);

//        // elabora il risultato
//        switch (risultato) {
//            case ErrLogin.success:
//                this.setValido(true)
//
        // mette da parte i parametri restituiti dal server
//        this.regolaParametriSecondaRequest();
//

        if (testoRisposta.contains("assertuserfailed")) {
            this.setValido(false);
            this.setUser(false);
            this.setBot(false);
        } else {
            this.setValido(true);
            this.setUser(true);
            this.setBot(false);
        }// end of if/else cycle


//        risultato = this.getRisultato();
//        if (risultato == ErrLogin.success) {
//            this.setValido(true);
//        }// fine del blocco if
    } // fine della closure

    /**
     * Restituisce i cookies
     */
    public String getStringCookies() {
        String cookies = "";
        String sep = ";";
        String userName;
        long userId;
        String token;
        String session;
        String cookieprefix;

        cookieprefix = this.getCookieprefix();
        userName = this.getLgusername();
        userId = this.getLguserid();
        token = this.getToken();
        session = this.getSessionId();

        cookies = cookieprefix;
        cookies += "UserName=";
        cookies += userName;
        cookies += sep;
        cookies += cookieprefix;
        cookies += "UserID=";
        cookies += userId;
        cookies += sep;
//        cookies += cookieprefix;
//        cookies += "Token=";
//        cookies += token;
//        cookies += sep;
        cookies += cookieprefix;
        cookies += "Session=";
        cookies += session;

        return cookies;
    } // fine della closure

    public String getLingua() {
        return lingua;
    }// end of getter method

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }//end of setter method

    public Progetto getProgetto() {
        return progetto;
    }// end of getter method

    public void setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }//end of setter method

    public String getLgname() {
        return lgname;
    }// end of getter method

    public void setLgname(String lgname) {
        this.lgname = lgname;
    }//end of setter method

    public String getLgpassword() {
        return lgpassword;
    }// end of getter method

    public void setLgpassword(String lgpassword) {
        this.lgpassword = lgpassword;
    }//end of setter method

    public ErrLogin getFirstResult() {
        return firstResult;
    }// end of getter method

    public void setFirstResult(ErrLogin firstResult) {
        this.firstResult = firstResult;
    }//end of setter method

    public String getResult() {
        return result;
    }// end of getter method

    public void setResult(String result) {
        this.result = result;
    }//end of setter method

    public long getLguserid() {
        return lguserid;
    }// end of getter method

    public void setLguserid(long lguserid) {
        this.lguserid = lguserid;
    }//end of setter method

    public String getLgusername() {
        return lgusername;
    }// end of getter method

    public void setLgusername(String lgusername) {
        this.lgusername = lgusername;
    }//end of setter method

    public String getToken() {
        return token;
    }// end of getter method

    public void setToken(String token) {
        this.token = token;
    }//end of setter method

    public String getLgtoken() {
        return lgtoken;
    }// end of getter method

    public void setLgtoken(String lgtoken) {
        this.lgtoken = lgtoken;
    }//end of setter method

    public String getCookieprefix() {
        return cookieprefix;
    }// end of getter method

    public void setCookieprefix(String cookieprefix) {
        this.cookieprefix = cookieprefix;
    }//end of setter method

    public String getSessionId() {
        return sessionId;
    }// end of getter method

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }//end of setter method

    public boolean isPrimoCollegamento() {
        return primoCollegamento;
    }// end of getter method

    public void setPrimoCollegamento(boolean primoCollegamento) {
        this.primoCollegamento = primoCollegamento;
    }//end of setter method

    public boolean isValido() {
        return valido;
    }// end of getter method

    public void setValido(boolean valido) {
        this.valido = valido;
    }//end of setter method

    public ErrLogin getRisultato() {
        return risultato;
    }// end of getter method

    public void setRisultato(ErrLogin risultato) {
        this.risultato = risultato;
    }//end of setter method

    public HashMap<String, Object> getPar() {
        return par;
    }// end of getter method

    public void setPar(HashMap<String, Object> par) {
        this.par = par;
    }//end of setter method

    public HashMap<String, String> getCookies() {
        return cookies;
    }// end of getter method

    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }//end of setter method

    public boolean isBot() {
        return bot;
    }// end of getter method

    public void setBot(boolean bot) {
        this.bot = bot;
    }//end of setter method

    public boolean isUser() {
        return user;
    }// end of getter method

    public void setUser(boolean user) {
        this.user = user;
    }//end of setter method


} //fine della classe
