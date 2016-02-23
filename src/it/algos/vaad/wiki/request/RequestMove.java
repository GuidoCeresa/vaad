package it.algos.vaad.wiki.request;

/**
 * Created by gac on 27 nov 2015.
 * <p>
 * Action=move requires POST requests
 * GET requests will cause an error
 * <p>
 * Double request
 * <p>
 * <deprecated> First for obtaining movetoken
 * <deprecated> To move a page, a move token is required.
 * <deprecated> This token is equal to the edit token and the same for all pages, but changes at every login.
 * <deprecated> Moves tokens can be obtained via action=tokens with type=move (MW 1.20+), or by using the following method:
 * <deprecated> Obtaining a move token
 * <deprecated> api.php?action=query&prop=info&intoken=move&titles=Main%20Page
 * <p>
 * Gets tokens required by data-modifying actions.
 * If you request one of these actions without providing a token, the API returns an error code such as notoken.
 * This module does not use a prefix.
 * The csrf (cross-site request forgery) token corresponds to the majority of older tokens, like edit and move, that were retrieved using the API action tokens (deprecated in MediaWiki 1.24).
 * api.php?action=query&meta=tokens
 * <tokens csrftoken="00112233445566778899aabbccddeeff+\" />
 * <p>
 * Second with parameters:
 * -    from: Title of the page you want to move. Cannot be used together with fromid
 * -    fromid: Page ID of the page you want to move. Cannot be used together with from
 * -    to: Title you want to rename the page to
 * -    token: A move token previously retrieved through prop=info. Take care to urlencode the '+' as '%2B'.
 * -    reason: Reason for the move (optional)
 * -    movetalk: Move the talk page, if it exists
 * -    movesubpages: Move subpages, if applicable
 * -    noredirect: Don't create a redirect. Requires the suppressredirect right, which by default is granted only to bots and sysops
 * -    watch: Add the page and the redirect to your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * -    unwatch: Remove the page and the redirect from your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * -    watchlist: Unconditionally add or remove the page from your watchlist, use preferences or do not change watch (see API:Edit)
 * -    ignorewarnings: Ignore any warnings
 * <p>
 * Link: https://www.mediawiki.org/wiki/API:Move
 */
public class RequestMove extends ARequest {


//    private static final String TAG_MOVE = "&intoken=move";
    private static final String FROM = "&from=";
    private static final String TO = "&to=";
    private static final String REASON = "&reason=";
    private static final String TOKEN = "&token=";

    private static final String SUMMARY = "e ha modificato i wikilinks in entrata";

    private String newTitle;


    /**
     * Costruttore
     * Rinvia al costruttore completo
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     */
    public RequestMove(String oldTitle, String newTitle) {
        this(oldTitle, newTitle, SUMMARY);
    }// fine del metodo costruttore

    /**
     * Costruttore
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     * @param summary  oggetto dello spostamento
     */
    public RequestMove(String oldTitle, String newTitle, String summary) {
        super(oldTitle, newTitle, summary);
    }// fine del metodo costruttore completo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected void elaboraParametri() {
        super.elaboraParametri();
        this.newTitle = super.newTitleNewText;
        needPreliminary = true;
        needBot = true;
    }// fine del metodo

    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY;

        String tag = "https://it.wikipedia.org/w/api.php?format=json&formatversion=2&action=move";

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo

} // fine della classe
