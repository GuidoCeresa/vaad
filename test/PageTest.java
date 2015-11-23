import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.request.QueryReadTitle;
import it.algos.vaad.wiki.request.QueryWiki;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:33
 * Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
 * Quattordici (14) parametri letti SEMPRE:
 * titolo, pageid, testo, ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
 */
public class PageTest extends VaadTest {

    @Test
    public void page() {
        String contenuto;
        QueryWiki query = new QueryReadTitle(TITOLO);

        assertNotNull(query);
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
    public void page2() {
        String contenuto;
        QueryWiki query = new QueryReadTitle(TITOLO8);

        assertNotNull(query);
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

        previsto = TITOLO8;
        ottenuto = page.getTitle();
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 5706423;
        numOttenuto = page.getPageid();
        assertEquals(numOttenuto, numPrevisto);

        boolOttenuto = (boolean) mappaObj.get(PagePar.minor.toString());
        assertEquals(boolOttenuto, false);

        boolOttenuto = (boolean) mappaObj.get(PagePar.anon.toString());
        assertEquals(boolOttenuto, false);

        ottenuto = page.getText();
        assertNotNull(ottenuto);
    }// end of single test

}// end of testing class

