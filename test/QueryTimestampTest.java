import it.algos.vaad.WrapTime;
import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.query.QueryTimestamp;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by gac on 21 set 2015.
 * .
 */
public class QueryTimestampTest extends VaadTest{

    private static final String[] listaPageIds = {"3397115", "4452510", "1691379", "3520373", "4956588", "5136975", "2072357", "4700355"};
    private static final String pipe = "|";
    private static final String virgola = ",";

    private static String pageIdsPipe = "";
    private static String pageIdsVirgola = "";
    private static ArrayList<String> arrayPageIds;

    @Before
    @SuppressWarnings("all")
    // Setup logic here
    public void setUp() {

        for (String stringa : listaPageIds) {
            pageIdsPipe += stringa;
            pageIdsPipe += pipe;
        } // fine del ciclo for-each
        pageIdsPipe = LibText.levaCoda(pageIdsPipe, pipe);

        for (String stringa : listaPageIds) {
            pageIdsVirgola += stringa;
            pageIdsVirgola += virgola;
        } // fine del ciclo for-each
        pageIdsVirgola = LibText.levaCoda(pageIdsVirgola, virgola);

        arrayPageIds = (ArrayList) LibArray.fromString(listaPageIds);
    } // fine del metodo iniziale

    @After
    // Tear down logic here
    public void tearDown() {
    } // fine del metodo iniziale

    @Test
    public void creaQuery() {
        QueryTimestamp query;
        ArrayList<WrapTime> lista;

        query = new QueryTimestamp(pageIdsPipe);
        assertNotNull(query);
        ottenuto = query.getContenuto();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));


        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(pageIdsVirgola);
        assertNotNull(query);
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(arrayPageIds);
        assertNotNull(query);
        lista = query.getListaWrapTime();
        assertNotNull(lista);

        query = new QueryTimestamp(listaPageIds);
        assertNotNull(query);
        lista = query.getListaWrapTime();
        assertNotNull(lista);
    }// fine metodo test


} // fine della classe di test
