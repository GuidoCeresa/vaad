import it.algos.vaad.wiki.*;
import it.algos.vaad.wiki.request.*;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 17 nov 2015.
 * .
 */
public class RequestTest extends VaadTest {


    private static final String[] listaPageIds = {"3397115", "4452510", "1691379", "3520373", "4956588", "5136975", "2072357", "4700355"};
    private static final String pipe = "|";
    private static final String virgola = ",";

    private static String pageIdsPipe;
    private static String pageIdsVirgola;
    private static ArrayList<String> arrayPageIds;

    private WikiLogin wikiLogin = null;

    @Test
    /**
     * Classe concreta per le Request sul Web
     * Fornisce le funzionalità di base per una request standard sul web
     * Usa solo il metodo GET
     * Legge una pagina internet (qualsiasi)
     * Accetta SOLO un webUrl (indirizzo) completo
     */
    public void web() {
        Request request;

        request = new RequestWeb(TITOLO_WEB_ERRATO);
        assertFalse(request.isValida());
        risultatoOttenuto = request.getRisultato();
        assertEquals(risultatoOttenuto, TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);

        request = new RequestWeb(TITOLO_WEB);
        assertTrue(request.isValida());
        risultatoOttenuto = request.getRisultato();
        assertEquals(risultatoOttenuto, TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.contains(CONTENUTO_WEB));
    }// end of single test

    @Test
    public void read() {
        Request request;

        request = new RequestWikiReadTitle(TITOLO_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiReadTitle(TITOLO);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiReadPageid(PAGEID_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiReadPageid(PAGEID);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiReadPageid(PAGEID_UTF8);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
    }// end of single test


    @Test
    /**
     * Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
     * Quattordici (14) parametri letti SEMPRE:
     * titolo, pageid, testo, ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
     */
    public void page() {
        Request request = new RequestWikiReadTitle(TITOLO);
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        assertTrue(request.isValida());

        contenuto = request.getTestoResponse();
        assertNotNull(contenuto);
        page = new Page(contenuto);
        assertNotNull(page);

        mappaTxt = page.getMappaReadTxt();
        assertNotNull(mappaTxt);
        LibWikiTest.isMappaReadTxtValida(mappaTxt);

        mappaObj = page.getMappaReadObj();
        LibWikiTest.isMappaReadObjValida(mappaObj);

        mappaDB = page.getMappaDB();
        LibWikiTest.isMappaDBValida(mappaDB);

        previsto = TITOLO;
        ottenuto = page.getTitle();
        assertEquals(ottenuto, previsto);

        numPrevisto = PAGEID;
        numOttenuto = page.getPageid();
        assertEquals(numOttenuto, numPrevisto);

        boolPrevisto = true;
        boolOttenuto = (boolean) mappaObj.get(PagePar.minor.toString());
        assertEquals(boolOttenuto, boolPrevisto);

        boolPrevisto = false;
        boolOttenuto = (boolean) mappaObj.get(PagePar.anon.toString());
        assertEquals(boolOttenuto, boolPrevisto);

        ottenuto = page.getText();
        assertTrue(ottenuto.startsWith(TAG_INI_VOCE));
        assertTrue(ottenuto.endsWith(TAG_END_VOCE));
    }// end of single test

    @Before
    @SuppressWarnings("all")
    // Setup logic here
    public void setUp() {
        pageIdsPipe = "";
        pageIdsVirgola = "";

        for (String stringa : listaPageIds) {
            pageIdsPipe += stringa;
            pageIdsPipe += pipe;
        } // fine del ciclo for-each
        pageIdsPipe = LibText.levaCoda(pageIdsPipe, pipe);

        for (String stringa : listaPageIds) {
            pageIdsVirgola += stringa;
            pageIdsVirgola += virgola;
        } // fine del ciclo for-each
        pageIdsVirgola = LibText.levaCoda(pageIdsVirgola, virgola);

        arrayPageIds = (ArrayList) LibArray.fromString(listaPageIds);
    } // fine del metodo iniziale

    @Test
    public void timestamp() {
        RequestWikiTimestamp request;
        ArrayList<WrapTime> lista;

        request = new RequestWikiTimestamp(pageIdsPipe);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(pageIdsVirgola);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(listaPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiTimestamp(TITOLO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        lista = request.getListaWrapTime();
        assertNull(lista);
    }// end of single test


    @Test
    /**
     * Created by gac on 08 nov 2015.
     * <p>
     * Rif: https://www.mediawiki.org/wiki/API:Backlinks
     * Lists pages that link to a given page, similar to Special:Whatlinkshere. Ordered by linking page title.
     * <p>
     * Parametrs:
     * bltitle: List pages linking to this title. The title does not need to exist.
     * blnamespace: Only list pages in these namespaces
     * blfilterredir: How to filter redirects (Default: all)
     * - all: List all pages regardless of their redirect flag
     * - redirects: Only list redirects
     * - nonredirects: Don't list redirects
     * bllimit: Maximum amount of pages to list. Maximum limit is halved if blredirect is set. No more than 500 (5000 for bots) allowed. (Default: 10)
     * blredirect: If set, pages linking to bltitle through a redirect will also be listed. See below for more detailed information.
     * blcontinue: Used to continue a previous request
     * <p>
     * Es:
     * https://it.wikipedia.org/w/api.php?action=query&list=backlinks&bltitle=Piozzano&format=jsonfm
     */
    public void back() {
        RequestWikiBacklinks request;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;

        request = new RequestWikiBacklinks(TITOLO_BACK);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 18);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 18);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 10);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 10);

        request = new RequestWikiBacklinks(TITOLO_2);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 3);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 3);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 0);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 0);

        request = new RequestWikiBacklinks(TITOLO_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaAllPageids = request.getListaAllPageids();
        assertNull(listaAllPageids);
        listaAllTitles = request.getListaAllTitles();
        assertNull(listaAllTitles);
        listaVociPageids = request.getListaVociPageids();
        assertNull(listaVociPageids);
        listaVociTitles = request.getListaVociTitles();
        assertNull(listaVociTitles);

        request = new RequestWikiBacklinks(TITOLO_ALTRO);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 1);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 1);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 0);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 0);
    }// end of single test

    // Login logic here
    private void setLogin() {
        String nick = "gacbot";
        String password = "fulvia";

        if (wikiLogin == null) {
            wikiLogin = new WikiLogin(nick, password);
        }// end of if cycle
    } // fine del metodo iniziale

    @Test
    /**
     * Query per recuperare le pagine di una categoria
     * NON legge le sottocategorie
     * Non necessita di Login, ma se esiste lo usa
     * Può essere sovrascritta per leggere anche le sottocategorie
     */
    public void cat() {
        RequestWikiCat request;
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;

        request = new RequestWikiCat(TITOLO_CAT_BREVE);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.limitOver);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 2);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 2);
        listaCatPageids = request.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertEquals(listaCatPageids.size(), 12);
        listaCatTitles = request.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertEquals(listaCatTitles.size(), 12);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 14);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 14);

        request = new RequestWikiCat(TITOLO_CAT_BREVE, 500);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 2);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 2);
        listaCatPageids = request.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertEquals(listaCatPageids.size(), 12);
        listaCatTitles = request.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertEquals(listaCatTitles.size(), 12);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 14);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 14);

        request = new RequestWikiCat(TITOLO_CAT_MEDIA, 20);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 36);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 36);
        listaCatPageids = request.getListaCatPageids();
        assertNull(listaCatPageids);
        listaCatTitles = request.getListaCatTitles();
        assertNull(listaCatTitles);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 36);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 36);

        request = new RequestWikiCat(TITOLO_CAT_LUNGA, 500);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 2355);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 2355);
        listaCatPageids = request.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertEquals(listaCatPageids.size(), 16);
        listaCatTitles = request.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertEquals(listaCatTitles.size(), 16);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 2371);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 2371);

        request = new RequestWikiCat(TITOLO_CAT_LUNGA);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.limitOver);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 2355);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 2355);
        listaCatPageids = request.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertEquals(listaCatPageids.size(), 16);
        listaCatTitles = request.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertEquals(listaCatTitles.size(), 16);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 2371);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 2371);
    }// end of single test


    @Test
    /**
     * Query per recuperare le pagine di una categoria
     * NON legge le sottocategorie
     * Non necessita di Login, ma se esiste lo usa
     * Può essere sovrascritta per leggere anche le sottocategorie
     */
    public void cat2() {
        RequestWikiCat request;
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;

        setLogin();
        request = new RequestWikiCat(TITOLO_CAT_LUNGA, wikiLogin);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertEquals(listaVociPageids.size(), 2355);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertEquals(listaVociTitles.size(), 2355);
        listaCatPageids = request.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertEquals(listaCatPageids.size(), 16);
        listaCatTitles = request.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertEquals(listaCatTitles.size(), 16);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertEquals(listaAllPageids.size(), 2371);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertEquals(listaAllTitles.size(), 2371);
    }// end of single test


    /**
     * Query per recuperare le pagine di una categoria
     * NON legge le sottocategorie
     * Non necessita di Login, ma se esiste lo usa
     * Può essere sovrascritta per leggere anche le sottocategorie
     */
    public void cat3() {
        RequestWikiCat request;
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;

        request = new RequestWikiCat(TITOLO_CAT_LUNGHISSIMA, wikiLogin);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        listaVociPageids = request.getListaVociPageids();
        assertNotNull(listaVociPageids);
        assertTrue(listaVociPageids.size() > 290000);
        listaVociTitles = request.getListaVociTitles();
        assertNotNull(listaVociTitles);
        assertTrue(listaVociTitles.size() > 290000);
        listaCatPageids = request.getListaCatPageids();
        assertNull(listaCatPageids);
        listaCatTitles = request.getListaCatTitles();
        assertNull(listaCatTitles);
        listaAllPageids = request.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertTrue(listaAllPageids.size() > 290000);
        listaAllTitles = request.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertTrue(listaAllTitles.size() > 290000);
    }// end of single test

    @Test
    public void assertAPI() {
        RequestWikiAssert request;

        request = new RequestWikiAssertUser();
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.assertuserfailed);

        request = new RequestWikiAssertBot();
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.assertbotfailed);
    }// end of single test

//        @Test
    public void login() {
        RequestWikiLogin request;
        String nick;
        String pass;

        nick = "nomechesicuramentenonesiste";
        pass = "pippoz";
        request = new RequestWikiLogin(nick, pass);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.notExists);

        nick = "gac";
        pass = "pippozbelloz";
        request = new RequestWikiLogin(nick, pass);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.wrongPass);

        nick = "gac";
        pass = "alfa";
        request = new RequestWikiLogin(nick, pass);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.loginUser);

        nick = "biobot";
        pass = "fulvia";
        request = new RequestWikiLogin(nick, pass);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.loginBot);
    }// end of single test


    @Test
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
    public void move() {
        RequestWikiMove request;
        String reason = "test";

        //--login obbligatorio
        setLogin();

        request = new RequestWikiMove("", "", "", wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.erroreGenerico);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, TITOLO9);
        assertEquals(request.getRisultato(), TipoRisultato.noLogin);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, TITOLO9, reason);
        assertEquals(request.getRisultato(), TipoRisultato.noLogin);
        assertFalse(request.isValida());

        request = new RequestWikiMove("", "", reason, wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.erroreGenerico);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, TITOLO8, reason, wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.selfmove);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, TITOLO_ALTRO, reason, wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.articleexists);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, TITOLO_BLOCCATO, reason, wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.protectedtitle);
        assertFalse(request.isValida());

        request = new RequestWikiMove(TITOLO8, "", reason, wikiLogin);
        assertEquals(request.getRisultato(), TipoRisultato.erroreGenerico);
        assertFalse(request.isValida());

    }// end of single test

}// end of testing class
