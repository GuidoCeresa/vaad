import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.query.QueryReadTitle;
import it.algos.vaad.wiki.query.QueryWiki;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 28 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class PageTest extends VaadTest {

    @Test
    public void creaPagina() {
        String contenuto;
        QueryWiki query = new QueryReadTitle(TITOLO);

        assertNotNull(query);
        contenuto = query.getContenuto();
        assertNotNull(contenuto);
        page = new Page(contenuto);
        assertNotNull(page);

        mappaTxt = page.getMappaTxt();
        assertNotNull(mappaTxt);
        LibWikiTest.isMappaStringheValidaRead(mappaTxt);

        mappaObj = page.getMappa();
        LibWikiTest.isMappaValoriValidaRead(mappaObj);

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