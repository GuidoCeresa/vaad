import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.query.Request;
import it.algos.vaad.wiki.query.RequestWeb;
import it.algos.vaad.wiki.query.RequestWikiReadTitle;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 17 nov 2015.
 * .
 */
public class RequestTest extends VaadTest {

    @Test
    /**
     * Classe concreta per le Request sul Web
     * Fornisce le funzionalità di base per una request standard sul web
     * Usa solo il metodo GET
     * Legge una pagina internet (qualsiasi)
     * Accetta SOLO un webUrl (indirizzo) completo
     */
    public void web() {
        Request request;

        request = new RequestWeb(TITOLO_WEB_ERRATO);
        assertFalse(request.isValida());
        risultatoOttenuto = request.getRisultato();
        assertEquals(risultatoOttenuto, TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNull(ottenuto);

        request = new RequestWeb(TITOLO_WEB);
        assertTrue(request.isValida());
        risultatoOttenuto = request.getRisultato();
        assertEquals(risultatoOttenuto, TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.contains(CONTENUTO_WEB));
    }// end of single test

    @Test
    public void read() {
        Request request;

        request = new RequestWikiReadTitle(TITOLO_ERRATO);
        assertFalse(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.nonTrovata);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertFalse(ottenuto.endsWith(TAG_END_PAGINA));

        request = new RequestWikiReadTitle(TITOLO);
        assertTrue(request.isValida());
        assertEquals(request.getRisultato(), TipoRisultato.letta);
        ottenuto = request.getTestoResponse();
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));
    }// end of single test

}// end of testing class
