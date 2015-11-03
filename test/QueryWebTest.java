import it.algos.vaad.wiki.query.QueryWeb;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Gac on 15 ago 2015.
 * .
 */
public class QueryWebTest extends VaadTest {

    @Test
    /**
     * Legge una pagina internet
     * Accetta SOLO un domain (indirizzo) completo
     */
    public void read() {
        QueryWeb query;

        query = new QueryWeb(TITOLO_WEB);
        assertNotNull(query);
        assertTrue(query.isLetta());
        contenuto = query.getContenuto();
        assertNotNull(contenuto);
        assertTrue(contenuto.contains(CONTENUTO_WEB));

        query = new QueryWeb(TITOLO_WEB_ERRATO);
        assertNotNull(query);
        assertFalse(query.isLetta());
        contenuto = query.getContenuto();
        assertNull(contenuto);
        assertTrue(query.getErrore().equals(TAG_ERRORE));
    }// end of single test

}// end of testing class
