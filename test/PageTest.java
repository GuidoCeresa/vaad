import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.request.QueryReadTitle;
import it.algos.vaad.wiki.request.QueryWiki;
import it.algos.vaad.wiki.request.Request;
import it.algos.vaad.wiki.request.RequestWikiReadTitle;
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
        Request request = new RequestWikiReadTitle(TITOLO);

        assertNotNull(request);
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

