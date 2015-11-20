import it.algos.vaad.wiki.*;
import it.algos.vaad.wiki.query.Request;
import it.algos.vaad.wiki.query.RequestWeb;
import it.algos.vaad.wiki.query.RequestWikiRead;
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

        request = new RequestWikiRead(TITOLO_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiRead(TITOLO);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiRead(PAGEID_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiRead(PAGEID);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiRead(PAGEID_UTF8);
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
        Request request = new RequestWikiRead(TITOLO);
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
        RequestWikiRead request;
        ArrayList<WrapTime> lista;

        request = new RequestWikiRead(pageIdsPipe);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(pageIdsVirgola);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        lista = request.getListaWrapTime();
        assertNull(lista);

        request = new RequestWikiRead(pageIdsVirgola, TipoRicerca.listaPageids);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(listaPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(arrayPageIds);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
        lista = request.getListaWrapTime();
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

        request = new RequestWikiRead(TITOLO, TipoRicerca.listaPageids);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        lista = request.getListaWrapTime();
        assertNull(lista);
    }// end of single test


    @Test
    /**
     * Query per recuperare le pagine di una categoria
     * NON legge le sottocategorie
     * Non necessita di Login, ma se esiste lo usa
     * Può essere sovrascritta per leggere anche le sottocategorie
     */
    public void cat() {
//        QueryCat query;
//        ArrayList<Long> listaPageids;
//        ArrayList<String> listaTitles;
//        ArrayList<Long> listaCatPageids;
//        ArrayList<String> listaCatTitles;
//        ArrayList<Long> listaAllPageids;
//        ArrayList<String> listaAllTitles;
//
//        query = new QueryCat(TITOLO_CAT_ERRATA);
//        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
//        assertFalse(query.isValida());
//        listaPageids = query.getListaPageids();
//        assertNull(listaPageids);
//        listaTitles = query.getListaTitles();
//        assertNull(listaTitles);
//        listaCatPageids = query.getListaCatPageids();
//        assertNull(listaCatPageids);
//        listaCatTitles = query.getListaCatTitles();
//        assertNull(listaCatTitles);
//        listaAllPageids = query.getListaAllPageids();
//        assertNull(listaAllPageids);
//        listaAllTitles = query.getListaAllTitles();
//        assertNull(listaAllTitles);
    }// end of single test

}// end of testing class
