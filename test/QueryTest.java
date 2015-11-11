import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.query.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 11 nov 2015.
 * .
 */
public class QueryTest extends VaadTest {



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

        query = new QueryWeb(TITOLO_WEB);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
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
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadTitle(TITOLO);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadTitle(TITOLO_2);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_ERRATO);
        assertNotNull(query);
        assertEquals(query.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_UTF8);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID_COME_STRINGA);
        assertEquals(query.getRisultato(), TipoRisultato.letta);
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


}// end of testing class
