package it.algos.vaad.test;

import it.algos.vaad.wiki.query.Query;
import it.algos.vaad.wiki.query.QueryReadPageid;
import it.algos.vaad.wiki.query.QueryReadTitle;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gac on 07 ago 2015.
 * .
 */
public class ReadPageTest extends VaadTest{

    @Test
    /**
     * Legge una pagina
     * Non serve il login
     * Legge solamente e NON scrive
     */
    public void readPage() {
        Query query;

        query = new QueryReadTitle(TITOLO);
        assertTrue(query.isTrovata());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadTitle(TITOLO_ERRATO);
        assertFalse(query.isTrovata());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        query = new QueryReadPageid(PAGEID);
        assertTrue(query.isTrovata());
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
    }// end of single test


}// end of testing class
