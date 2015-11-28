import it.algos.vaad.wiki.*;
import it.algos.vaad.wiki.request.*;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 11 nov 2015.
 * .
 */
public class QueryTest extends VaadTest {


    private static final String[] listaPageIds = {"3397115", "4452510", "1691379", "3520373", "4956588", "5136975", "2072357", "4700355"};
    private static final String pipe = "|";
    private static final String virgola = ",";

    private static String pageIdsPipe;
    private static String pageIdsVirgola;
    private static ArrayList<String> arrayPageIds;

    private WikiLogin loginWiki = null;

    // Login logic here
    private void setLogin() {
        String nick = "gacbot";
        String password = "fulvia";

        if (loginWiki == null) {
            loginWiki = new WikiLogin(nick, password);
        }// end of if cycle
    } // fine del metodo iniziale

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
    /**
     * Classe concreta per le Request sul Web
     * Legge una pagina internet (qualsiasi)
     * Accetta SOLO un domain (indirizzo) completo
     */
    public void web() {
        Query query;

        query = new QueryWeb(TITOLO_WEB_ERRATO);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        assertFalse(query.isValida());

        query = new QueryWeb(TITOLO_WEB);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        contenuto = query.getContenuto();
        assertNotNull(contenuto);
        assertTrue(contenuto.contains(CONTENUTO_WEB));
    }// end of single test

    @Test
    /**
     * Query standard per leggere il risultato di una pagina
     * NON legge le categorie
     * Usa il titolo della pagina
     * Non necessita di Login
     */
    public void read() {
        Query query;

        query = new QueryReadTitle(TITOLO_ERRATO);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        assertFalse(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadTitle(TITOLO);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadTitle(TITOLO_2);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_ERRATO);
        assertNotNull(query);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        assertFalse(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_UTF8);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_COME_STRINGA);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
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
        String contenuto;
        Query query = new QueryReadTitle(TITOLO);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());

        contenuto = query.getContenuto();
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
        assertEquals(numOttenuto, numPrevisto);

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


    @Test
    /**
     * Query per recuperare le pagine di una categoria
     * NON legge le sottocategorie
     * Non necessita di Login, ma se esiste lo usa
     * Può essere sovrascritta per leggere anche le sottocategorie
     */
    public void cat() {
        QueryCat query;
        ArrayList<Long> listaPageids;
        ArrayList<String> listaTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;

        query = new QueryCat(TITOLO_CAT_ERRATA);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        assertFalse(query.isValida());
        listaPageids = query.getListaPageids();
        assertNull(listaPageids);
        listaTitles = query.getListaTitles();
        assertNull(listaTitles);
        listaCatPageids = query.getListaCatPageids();
        assertNull(listaCatPageids);
        listaCatTitles = query.getListaCatTitles();
        assertNull(listaCatTitles);
        listaAllPageids = query.getListaAllPageids();
        assertNull(listaAllPageids);
        listaAllTitles = query.getListaAllTitles();
        assertNull(listaAllTitles);

        query = new QueryCat(TITOLO_CAT_BREVE, false);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() == 2);

        query = new QueryCat(TITOLO_CAT_BREVE);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() == 2);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);
        assertTrue(listaTitles.size() == 2);
        listaCatPageids = query.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertTrue(listaCatPageids.size() == 12);
        listaCatTitles = query.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertTrue(listaCatTitles.size() == 12);
        listaAllPageids = query.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertTrue(listaAllPageids.size() == 14);
        listaAllTitles = query.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertTrue(listaAllTitles.size() == 14);

        query = new QueryCat(TITOLO_CAT_MEDIA, false);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() == 36);

        query = new QueryCat(TITOLO_CAT_MEDIA);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() == 36);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);
        assertTrue(listaTitles.size() == 36);
        listaCatPageids = query.getListaCatPageids();
        assertNull(listaCatPageids);
        listaCatTitles = query.getListaCatTitles();
        assertNull(listaCatTitles);
        listaAllPageids = query.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertTrue(listaAllPageids.size() == 36);
        listaAllTitles = query.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertTrue(listaAllTitles.size() == 36);

        query = new QueryCat(TITOLO_CAT_LUNGA, false);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() > 2300);

        query = new QueryCat(TITOLO_CAT_LUNGA);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        assertTrue(listaPageids.size() > 2300);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);
        assertTrue(listaTitles.size() > 2300);
        listaCatPageids = query.getListaCatPageids();
        assertNotNull(listaCatPageids);
        assertTrue(listaCatPageids.size() > 15);
        listaCatTitles = query.getListaCatTitles();
        assertNotNull(listaCatTitles);
        assertTrue(listaCatTitles.size() > 15);
        listaAllPageids = query.getListaAllPageids();
        assertNotNull(listaAllPageids);
        assertTrue(listaAllPageids.size() > 2300);
        listaAllTitles = query.getListaAllTitles();
        assertNotNull(listaAllTitles);
        assertTrue(listaAllTitles.size() > 2300);

//        //--temporanea e dinamica, potrebbe NON essere vuota
//        //--se da errore, controllare la categoria
//        if (true) {
//            query = new QueryCat(TITOLO_CAT_VUOTA);
//            assertEquals(query.getRisultato(), TipoRisultato.letta);
//            assertFalse(query.isValida());
//            listaPageids = query.getListaPageids();
//            assertNull(listaPageids);
//            listaTitles = query.getListaTitles();
//            assertNull(listaTitles);
//        }// end of if cycle

        //--circa 2-3 minuti
        if (false) {
            query = new QueryCat(TITOLO_CAT_LUNGHISSIMA, false);
            assertEquals(query.getRisultato(), TipoRisultato.letta);
            assertTrue(query.isValida());
            listaPageids = query.getListaPageids();
            assertNotNull(listaPageids);
            assertTrue(listaPageids.size() > 290000);
            listaTitles = query.getListaTitles();
            assertNull(listaTitles);
        }// end of if cycle
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
        QueryBacklinks query;
        ArrayList<Long> listaPageids;
        ArrayList<String> listaTitles;

        query = new QueryBacklinks(TITOLO_4);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);

        query = new QueryBacklinks(TITOLO_2);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertFalse(query.isValida());

        query = new QueryBacklinks(TITOLO_ERRATO);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        assertFalse(query.isValida());
        listaPageids = query.getListaPageids();
        assertNull(listaPageids);

        query = new QueryBacklinks(TITOLO_ALTRO);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertFalse(query.isValida());
        listaPageids = query.getListaPageids();
        assertNull(listaPageids);

        query = new QueryBacklinks(TITOLO_ALTRO, true);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertFalse(query.isValida());
        listaPageids = query.getListaPageids();
        assertNull(listaPageids);

        query = new QueryBacklinks(TITOLO_ALTRO, false);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);
    }// end of single test


    @Test
    /**
     * Created by gac on 21 set 2015.
     * Query per leggere il timestamp di molte pagine tramite una lista di pageIds
     * Legge solamente
     * Mantiene una lista di pageIds e timestamps
     */
    public void time() {
        QueryTimestamp query;
        ArrayList<WrapTime> lista;

        query = new QueryTimestamp(pageIdsPipe);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(pageIdsVirgola);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(arrayPageIds);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(listaPageIds);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertTrue(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        arrayPageIds.add("pippo");
        query = new QueryTimestamp(arrayPageIds);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        assertFalse(query.isValida());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = query.getListaWrapTime();
        assertNotNull(lista);
    }// fine metodo test

    @Test
    /**
     * Query standard per scrivere il contenuto di una pagina
     * Usa il titolo della pagina
     * Necessita di Login per scrivere
     */
    public void write() {
        Query query;
        String testoIniziale;
        String testoA;
        String testoB;
        String summaryA = LibWiki.getSummary("test add x");
        String summaryB = LibWiki.getSummary("test minus x");

        //--login obbligatorio
        setLogin();

        //--recupera il testo esistente per partire da una situazione pulita
        testoIniziale = Api.leggeVoce(TITOLO_3);

        query = new QueryWriteTitle(TITOLO_3, "", "", null);
        assertEquals(query.getRisultato(), TipoRisultato.erroreGenerico);
        assertFalse(query.isValida());

        query = new QueryWriteTitle(TITOLO_3, testoIniziale, "", null);
        assertEquals(query.getRisultato(), TipoRisultato.noLogin);
        assertFalse(query.isValida());

        query = new QueryWriteTitle(TITOLO_3, testoIniziale, "", loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.nonRegistrata);
        assertTrue(query.isValida());

        query = new QueryWriteTitle(TITOLO_3, testoIniziale, summaryA, loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.nonRegistrata);
        assertTrue(query.isValida());

        testoA = testoIniziale + "x";
        query = new QueryWriteTitle(TITOLO_3, testoA, summaryA, loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.registrata);
        assertTrue(query.isValida());

        query = new QueryWriteTitle(TITOLO_3, testoA, summaryA, loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.nonRegistrata);
        assertTrue(query.isValida());

        testoB = testoIniziale;
        query = new QueryWriteTitle(TITOLO_3, testoB, summaryB, loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.registrata);
        assertTrue(query.isValida());

        query = new QueryWriteTitle(TITOLO_3, testoB, summaryB, loginWiki);
        assertEquals(query.getRisultato(), TipoRisultato.nonRegistrata);
        assertTrue(query.isValida());
    }// end of single test


    @Test
    public void move() {
        Query query;
        String reason = "test";

        //--login obbligatorio
        setLogin();

        query = new QueryMove("", "");
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.erroreGenerico);

        query = new QueryMove(TITOLO8, TITOLO9);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.noLogin);

        query = new QueryMove(TITOLO8, TITOLO9, reason);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.noLogin);

        query = new QueryMove("", "", reason, loginWiki);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.erroreGenerico);

        query = new QueryMove(TITOLO8, TITOLO8, reason, loginWiki);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.selfmove);

        query = new QueryMove(TITOLO8, TITOLO_ALTRO, reason, loginWiki);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.articleexists);

        query = new QueryMove(TITOLO8, TITOLO_BLOCCATO, reason, loginWiki);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.protectedtitle);

        query = new QueryMove(TITOLO8, "", reason, loginWiki);
        assertFalse(query.isValida());
        assertEquals(query.getRisultato(), TipoRisultato.erroreGenerico);
    }// end of single test


}// end of testing class
