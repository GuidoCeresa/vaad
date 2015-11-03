import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.query.QueryReadTitle;
import org.junit.Test;

/**
 * Created by gac on 29 ott 2015.
 * .
 */
public class QueryReadTest {

    private static String TITOLO = "Utente:Gac/Sandbox5";

    @Test
    /**
     * Scrive una pagina
     * Serve il login
     */
    public void readPage() {
        QueryReadTitle query;
        String testo;

        query = new QueryReadTitle(TITOLO);
        testo = query.getContenuto();
        LibWiki.creaMappa(testo);

        int a=78;
    }// end of single test

}// end of testing class
