import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.request.ARequest;
import it.algos.vaad.wiki.request.RequestCat;
import it.algos.vaad.wiki.request.RequestRead;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 31 gen 2016.
 * .
 */
public class ARequestTest extends VaadTest {
    private ARequest request;
    private RequestCat requestCat;

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
        requestCat = new RequestCat(TITOLO_CAT_BREVE);
        assertFalse(requestCat.isValida());

        //--effettua il login
        setLogin();

        //--login obbligatorio - adesso funziona
        requestCat = new RequestCat(TITOLO_CAT_BREVE);
        checkCat(2, 12);
    }// end of single test

    @Test
    public void cat2() {
        requestCat = new RequestCat(TITOLO_CAT_MEDIA);
        checkCat(36, 0);
    }// end of single test


    private void ottenutoNullo() {
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.equals(""));
    }// end of single test

    private void checkValidaLetta() {
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        checkOttenuto();
    }// end of single test


    private void checkOttenuto() {
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
    }// end of single test


    private void checkCat(int voci, int categorie) {
        ArrayList<Long> listaVociPageids;
        ArrayList<String> listaVociTitles;
        ArrayList<Long> listaCatPageids;
        ArrayList<String> listaCatTitles;
        ArrayList<Long> listaAllPageids;
        ArrayList<String> listaAllTitles;

        assertTrue(requestCat.isValida());
        assertEquals(requestCat.getRisultato(), TipoRisultato.letta);
        ottenuto = requestCat.getTestoResponse();
        assertNotNull(ottenuto);

        listaVociPageids = requestCat.getListaVociPageids();
        if (voci > 0) {
            assertNotNull(listaVociPageids);
            assertEquals(listaVociPageids.size(), voci);
        } else {
            assertNull(listaVociPageids);
        }// end of if/else cycle

        listaVociTitles = requestCat.getListaVociTitles();
        if (voci > 0) {
            assertNotNull(listaVociTitles);
            assertEquals(listaVociTitles.size(), voci);
        } else {
            assertNull(listaVociTitles);
        }// end of if/else cycle

        listaCatPageids = requestCat.getListaCatPageids();
        if (categorie > 0) {
            assertNotNull(listaCatPageids);
            assertEquals(listaCatPageids.size(), categorie);
        } else {
            assertNull(listaCatPageids);
        }// end of if/else cycle

        listaCatTitles = requestCat.getListaCatTitles();
        if (categorie > 0) {
            assertNotNull(listaCatTitles);
            assertEquals(listaCatTitles.size(), categorie);
        } else {
            assertNull(listaCatTitles);
        }// end of if/else cycle

        listaAllPageids = requestCat.getListaAllPageids();
        if (voci + categorie > 0) {
            assertNotNull(listaAllPageids);
            assertEquals(listaAllPageids.size(), voci + categorie);
        } else {
            assertNull(listaAllPageids);
        }// end of if/else cycle

        listaAllTitles = requestCat.getListaAllTitles();
        if (voci + categorie > 0) {
            assertNotNull(listaAllTitles);
            assertEquals(listaAllTitles.size(), voci + categorie);
        } else {
            assertNull(listaAllTitles);
        }// end of if/else cycle
    }// end of single test

}// end of testing class
