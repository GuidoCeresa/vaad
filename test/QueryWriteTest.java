import it.algos.vaad.wiki.WikiLogin;
import it.algos.vaad.wiki.query.QueryWriteTitle;
import it.algos.webbase.web.lib.LibSession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by gac on 29 ott 2015.
 * .
 */
public class QueryWriteTest {

    private static String TITOLO = "Utente:Gac/Sandbox5";
    private static String TESTO = "Proviamo a scriverex";
    private static String SUMMARY = "Sola scrittura";
    private WikiLogin loginWiki;

    @Before
    // Setup logic here
    public void setUp() {
        String nick = "biobot";
        String password = "fulvia";

        loginWiki = new WikiLogin(nick, password);
    } // fine del metodo iniziale

    @Test
    /**
     * Scrive una pagina
     * Serve il login
     */
    public void writePage() {
        QueryWriteTitle query;
        String contenuto;

        query = new QueryWriteTitle(TITOLO, TESTO, SUMMARY,loginWiki);
        assertTrue("Pagina non trovata", query.isLetta());
        contenuto = query.getContenuto();
        int a = 76;
    }// end of single test

}// end of testing class
