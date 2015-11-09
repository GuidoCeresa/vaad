import it.algos.vaad.wiki.WikiLogin;
import it.algos.vaad.wiki.query.QueryBacklinks;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 08 nov 2015.
 * .
 */
public class QueryBacklinksTest {

    private static String TITOLO = "Piozzano";
    private static String TITOLO_2 = "Credito Sammarinese";
    private static String TITOLO_ERRATO = "Titolo che non esiste";
    private static String SUMMARY = "Sola scrittura";
    private WikiLogin loginWiki;

//    @Before
//    // Setup logic here
//    public void setUp() {
//        String nick = "biobot";
//        String password = "fulvia";
//
//        loginWiki = new WikiLogin(nick, password);
//    } // fine del metodo iniziale

    @Test
    /**
     * Scrive una pagina
     * Serve il login
     */
    public void writePage() {
        QueryBacklinks query;
        ArrayList<Long> listaPageids;
        ArrayList<String> listaTitles;

        query = new QueryBacklinks(TITOLO);
        assertNotNull(query);
        assertTrue(query.isLetta());
        listaPageids = query.getListaPageids();
        assertNotNull(listaPageids);
        listaTitles = query.getListaTitles();
        assertNotNull(listaTitles);

        query = new QueryBacklinks(TITOLO_2);
        assertNotNull(query);
        assertTrue(query.isLetta());

        query = new QueryBacklinks(TITOLO_ERRATO);
        assertNotNull(query);
        assertFalse(query.isLetta());
        listaPageids = query.getListaPageids();
        assertNull(listaPageids);

    }// end of single test

}// end of testing class
