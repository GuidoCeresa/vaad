import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.query.QueryReadTitle;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 29 ott 2015.
 * .
 */
public class QueryReadTest {

    private static String TITOLO = "Utente:Gac/Sandbox5";
    private static String TITOLO_2 = "Credito Sammarinese";
    private static String TITOLO_ERRATO = "Titolo che non esiste";

    @Test
    /**
     * Scrive una pagina
     * Serve il login
     */
    public void readPage() {
        QueryReadTitle query;
        String testo;

        query = new QueryReadTitle(TITOLO);
        assertNotNull(query);
        assertTrue(query.isLetta());
        testo = query.getContenuto();
        LibWiki.creaMappa(testo);

        query = new QueryReadTitle(TITOLO_2);
        assertNotNull(query);
        assertTrue(query.isLetta());
    }// end of single test

    @Test
    /**
     * Controlla l'esistenza di una pagina.
     *
     * @param title della pagina da ricercare
     * @return true se la pagina esiste
     */
    public void esiste() {
        boolean status;

        status = LibWiki.isEsiste(TITOLO);
        assertTrue(status);

        status = LibWiki.isEsiste(TITOLO_2);
        assertTrue(status);

        status = LibWiki.isEsiste(TITOLO_ERRATO);
        assertFalse(status);

    }// end of single test

}// end of testing class
