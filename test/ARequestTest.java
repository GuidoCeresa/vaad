import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WrapTime;
import it.algos.vaad.wiki.request.*;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 31 gen 2016.
 * .
 */
public class ARequestTest extends VaadTest {

    private static final Long[] listaPageIdsForArray = {3397115L, 4452510L, 1691379L, 3520373L, 4956588L, 5136975L, 2072357L, 4700355L};
    private static final long[] listaPageIds = {3397115L, 4452510L, 1691379L, 3520373L, 4956588L, 5136975L, 2072357L, 4700355L};
    private static final String pipe = "|";
    private static final String virgola = ",";
    private static String pageIdsPipe;
    private static String pageIdsVirgola;
    private static ArrayList<Long> arrayPageIds;
    private ARequest request;
    private ArrayList<WrapTime> lista;


    @Before
    @SuppressWarnings("all")
    // Setup logic here
    public void setUp() {
        pageIdsPipe = "";
        pageIdsVirgola = "";

        for (Long lungo : listaPageIds) {
            pageIdsPipe += lungo;
            pageIdsPipe += pipe;
        } // fine del ciclo for-each
        pageIdsPipe = LibText.levaCoda(pageIdsPipe, pipe);

        for (Long lungo : listaPageIds) {
            pageIdsVirgola += lungo;
            pageIdsVirgola += virgola;
        } // fine del ciclo for-each
        pageIdsVirgola = LibText.levaCoda(pageIdsVirgola, virgola);

        arrayPageIds = (ArrayList) LibArray.fromLong(listaPageIds);
    } // fine del metodo iniziale

    @Test
    public void web() {
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
        request = new RequestRead(TITOLO_ERRATO);
        ottenutoNullo();
    }// end of single test

    @Test
    public void read2() {
        request = new RequestRead(TITOLO);
        checkValidaLetta();
    }// end of single test

    @Test
    public void read3() {
        request = new RequestRead(PAGEID_ERRATO);
        ottenutoNullo();
    }// end of single test

    @Test
    public void read4() {
        request = new RequestRead(PAGEID);
        checkValidaLetta();
    }// end of single test

    @Test
    public void read5() {
        request = new RequestRead(PAGEID_UTF8);
        checkValidaLetta();
    }// end of single test


    @Test
    /**
     * Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
     * Quattordici (14) parametri letti SEMPRE:
     * titolo, pageid, testo, ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
     */
    public void page() {
        request = new RequestRead(TITOLO);
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


    @Test
    public void cat() {
        //--login obbligatorio - quindi la prima volta NON funziona
        request = new RequestCat(TITOLO_CAT_BREVE);
        assertFalse(request.isValida());

        //--effettua il login
        setLogin();

        //--login obbligatorio - adesso funziona
        request = new RequestCat(TITOLO_CAT_BREVE);
        assertTrue(request.isValida());
        checkListeCat(2, 12);
    }// end of single test

    @Test
    public void cat2() {
        request = new RequestCat(TITOLO_CAT_MEDIA);
        checkListeCat(36, 0);
    }// end of single test


    @Test
    public void time() {
        request = new RequestTime(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestTime(listaPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);
    }// end of single test


    @Test
    public void links() {
        request = new RequestLinks(TITOLO_BACK);
        checkListeTime(6, 3);

        request = new RequestLinks(TITOLO_2);
        checkListeTime(3, 0);

        request = new RequestLinks(TITOLO_ERRATO);
        ottenutoNullo();

        request = new RequestLinks(TITOLO_ALTRO);
        checkListeTime(1, 0);
    }// end of single test


//    @Test
    public void move() {
        String reason = "test";

        request = new RequestMove(TITOLO8, TITOLO9, reason);
        assertTrue(request.isValida());
    }// end of single test

    private void ottenutoNullo() {
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);
    }// fine del metodo

    private void checkValidaLetta() {
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        checkOttenuto();
    }// fine del metodo


    private void checkOttenuto() {
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
    }// fine del metodo

    private void checkListeCat(int voci, int categorie) {
        checkListe(voci, categorie, 0);
    }// fine del metodo

    private void checkListeTime(int pagine, int voci) {
        checkListe(voci, 0, pagine);
    }// fine del metodo

    private void checkListe(int voci, int categorie, int pagine) {
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaPageids;
        ArrayList<String> listaTitles;

        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);

        listaVociPageids = request.getListaVociPageids();
        if (voci > 0) {
            assertNotNull(listaVociPageids);
            assertEquals(listaVociPageids.size(), voci);
        } else {
            assertNull(listaVociPageids);
        }// end of if/else cycle

        listaVociTitles = request.getListaVociTitles();
        if (voci > 0) {
            assertNotNull(listaVociTitles);
            assertEquals(listaVociTitles.size(), voci);
        } else {
            assertNull(listaVociTitles);
        }// end of if/else cycle

        listaCatPageids = request.getListaCatPageids();
        if (categorie > 0) {
            assertNotNull(listaCatPageids);
            assertEquals(listaCatPageids.size(), categorie);
        } else {
            assertNull(listaCatPageids);
        }// end of if/else cycle

        listaCatTitles = request.getListaCatTitles();
        if (categorie > 0) {
            assertNotNull(listaCatTitles);
            assertEquals(listaCatTitles.size(), categorie);
        } else {
            assertNull(listaCatTitles);
        }// end of if/else cycle

        listaPageids = request.getListaPaginePageids();
        if (pagine > 0) {
            assertNotNull(listaPageids);
            assertEquals(listaPageids.size(), pagine);
        } else {
            assertNull(listaPageids);
        }// end of if/else cycle

        listaTitles = request.getListaPagineTitles();
        if (pagine > 0) {
            assertNotNull(listaTitles);
            assertEquals(listaTitles.size(), pagine);
        } else {
            assertNull(listaTitles);
        }// end of if/else cycle
    }// fine del metodo

}// end of testing class
