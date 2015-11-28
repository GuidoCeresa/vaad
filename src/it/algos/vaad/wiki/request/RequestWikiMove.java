package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.webbase.web.lib.LibSession;

import java.net.URLEncoder;

/**
 * Created by gac on 27 nov 2015.
 * <p>
 * Double request
 * First for obtaining movetoken
 * <p>
 * Second with parameters:
 * from: Title of the page you want to move. Cannot be used together with fromid
 * fromid: Page ID of the page you want to move. Cannot be used together with from
 * to: Title you want to rename the page to
 * token: A move token previously retrieved through prop=info. Take care to urlencode the '+' as '%2B'.
 * reason: Reason for the move (optional)
 * movetalk: Move the talk page, if it exists
 * movesubpages: Move subpages, if applicable
 * noredirect: Don't create a redirect. Requires the suppressredirect right, which by default is granted only to bots and sysops
 * watch: Add the page and the redirect to your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * unwatch: Remove the page and the redirect from your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * watchlist: Unconditionally add or remove the page from your watchlist, use preferences or do not change watch (see API:Edit)
 * ignorewarnings: Ignore any warnings
 */
public class RequestWikiMove extends RequestWiki {

    private static final String TAG_MOVE = "&intoken=move";
    private static final String FROM = "&from=";
    private static final String TO = "&to=";
    private static final String REASON = "&reason=";
    private static final String TOKEN = "&token=";

    private static final String SUMMARY = "e ha modificato i wikilinks in entrata";

    private String oldTitle;
    private String newTitle;
    private String token;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     */
    public RequestWikiMove(String oldTitle, String newTitle) {
        this(oldTitle, newTitle, SUMMARY);
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo for testing purpose only
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     * @param summary  oggetto della modifica
     */
    public RequestWikiMove(String oldTitle, String newTitle, String summary) {
        this(oldTitle, newTitle, summary, null);
    }// fine del metodo costruttore completo

    /**
     * Costruttore for testing purpose only
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param oldTitle  della pagina da spostare
     * @param newTitle  definitivo della pagina
     * @param summary   oggetto della modifica
     * @param loginTest del collegamento
     */
    public RequestWikiMove(String oldTitle, String newTitle, String summary, WikiLogin loginTest) {
        this.doInit(oldTitle, newTitle, summary, loginTest);
    }// fine del metodo costruttore completo


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit(String oldTitle, String newTitle, String summary, WikiLogin loginTest) {
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
        super.summary = summary;
        super.needPost = false;
        super.needLogin = true;
        super.needToken = true;
        super.needBot = true;
        super.needContinua = false;

        if (loginTest != null) {
            wikiLogin = loginTest;
        } else {
            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if/else cycle

        if (needLogin && wikiLogin == null) {
            risultato = TipoRisultato.noLogin;
            valida = false;
            return;
        }// end of if cycle

        super.doInit();
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    protected String getDomain2() {
        String domain;
        String tag = "&prop=info&movetalk";

        //@todo NON serve il titolo per ottenere il token
        domain = API_BASE + tag;

        return domain;
    } // fine del metodo

    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getDomain() {
        String domain = "";
        String from = "";
        String to = "";
        String reason = "";
        String tag = "https://it.wikipedia.org/w/api.php?format=json&formatversion=2&action=move";

        try { // prova ad eseguire il codice
            from = URLEncoder.encode(oldTitle, "UTF-8");
            to = URLEncoder.encode(newTitle, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        domain += tag;
        domain += FROM;
        domain += from;
        domain += TO;
        domain += to;

        return domain;
    } // fine del metodo

} // fine della classe
