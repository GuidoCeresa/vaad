import it.algos.vaad.wiki.PagePar;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by gac on 28 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class PageParTest extends VaadTest {

    @Before
    public void creaMappa() {
        mappa = new HashMap<String, Object>();

        //--parametro di controllo
        add(PagePar.batchcomplete, true);

        //--parametri wiki base
        add(PagePar.pageid, 24L);
        add(PagePar.ns, 0L);
        add(PagePar.title, "Titolo della voce");

        //--parametri wiki info
        add(PagePar.pagelanguage, "linguaggio");
        add(PagePar.csrftoken, "token");
        add(PagePar.starttimestamp, new Date());

        //--parametri wiki revisions
        add(PagePar.revid, 2435L);
        add(PagePar.parentid, 6574L);
        add(PagePar.minor, false);
        add(PagePar.user, "utente");
        add(PagePar.anon, false);
        add(PagePar.userid, 8765L);
        add(PagePar.timestamp, new Date());
        add(PagePar.size, 8239L);
        add(PagePar.comment, "commento");
        add(PagePar.contentformat, "formato");
        add(PagePar.contentmodel, "modello");
        add(PagePar.content, "testo");

        //--parametri altri controlli
        add(PagePar.missing, "missing");
        add(PagePar.revisions, "revisions");

    }// end of setup method

    private void add(PagePar par, Object value) {
        mappa.put(par.toString(), value);
    }// end of setup method

    @Test
    /**
     * Restituisce una collezione di tutti gli elementi
     *
     * @return collezione
     */
    public void getAll() {
        numPrevisto = 21;
        listaOttenuta = PagePar.getAll();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Restituisce una collezione degli elementi di una Request Read
     *
     * @return collezione
     */
    public void getRead() {
        numPrevisto = 17;
        listaOttenuta = PagePar.getRead();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Restituisce il parametro, individuato dal nome
     *
     * @param key nome del parametro
     * @return parametro
     */
    public void getPar() {
        parPrevisto = PagePar.csrftoken;
        String tagNome = "csrftoken";

        parOttenuto = PagePar.getPar(tagNome);
        assertNotNull(parOttenuto);
        assertEquals(parOttenuto, parPrevisto);
    }// end of single test


    @Test
    /**
     * Restituisce il tipo di campo di un parametro, individuato dal nome
     *
     * @param key nome del parametro
     * @return tipo di campo
     */
    public void getParField() {
        fieldPrevisto = PagePar.parentid.getType();
        String tagNome = "parentid";
        fieldOttenuto = PagePar.getParField(tagNome);
        assertNotNull(fieldOttenuto);
        assertEquals(fieldOttenuto, fieldPrevisto);

        fieldPrevisto = PagePar.TypeField.longzero;
        tagNome = "parentid";
        fieldOttenuto = PagePar.getParField(tagNome);
        assertNotNull(fieldOttenuto);
        assertEquals(fieldOttenuto, fieldPrevisto);
    }// end of single test

//    @Test
    /**
     * Restituisce una collezione limitata agli elementi col flag info valido
     *
     * @return collezione
     */
    public void getInfos() {
        numPrevisto = 10;
        listaOttenuta = PagePar.getInf();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


//    @Test
    /**
     * Restituisce una collezione limitata agli elementi col flag info NON valido
     *
     * @return collezione
     */
    public void getRevisions() {
        numPrevisto = 9;
        listaOttenuta = PagePar.getRev();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

//    @Test
    /**
     * Restituisce una collezione degli elementi permanenti (per il database)
     *
     * @return collezione
     */
    public void getPerm() {
        numPrevisto = 16;
        listaOttenuta = PagePar.getPerm();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

//    @Test
    /**
     * Restituisce una collezione degli elementi obbligatori (per la respons della request)
     *
     * @return collezione
     */
    public void getObbReq() {
        numPrevisto = 10;
        listaOttenuta = PagePar.getObbReq();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

//    @Test
    /**
     * Restituisce una collezione degli elementi obbligatori (per il save del database)
     *
     * @return collezione
     */
    public void getObbDb() {
        numPrevisto = 9;
        listaOttenuta = PagePar.getObbDb();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


//    @Test
    /**
     * Restituisce una collezione degli elementi da restituire in lettura e scrittura
     *
     * @return collezione
     */
    public void getWrite() {
        numPrevisto = 19;
        listaOttenuta = PagePar.getWrite();
        assertNotNull(listaOttenuta);
        numOttenuto = listaOttenuta.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Controlla che tutti i parametri abbiano un valore valido
     *
     * @param mappa dei valori
     * @return true se tutti sono validi
     */
    public void isParValidi() {
        boolOttenuto = PagePar.isParValidiRead(mappa);
        assertTrue(boolOttenuto);
    }// end of single test

    @Test
    /**
     * Controlla che tutti i parametri abbiano un valore valido
     *
     * @param mappa dei valori
     * @return true se tutti sono validi
     */
    public void isParValidi2() {
        //mappa taroccata
        HashMap<String, Object> mappaLoc = (HashMap<String, Object>) mappa.clone();

        mappaLoc.put(PagePar.pageid.toString(), 0);
        boolOttenuto = PagePar.isParValidiRead(mappaLoc);
        assertFalse(boolOttenuto);
    }// end of single test

    @Test
    /**
     * Controlla che tutti i parametri abbiano un valore valido
     *
     * @param mappa dei valori
     * @return true se tutti sono validi
     */
    public void isParValidi3() {
        //mappa taroccata
        HashMap<String, Object> mappaLoc = (HashMap<String, Object>) mappa.clone();

        mappaLoc.remove(PagePar.title.toString());
        boolOttenuto = PagePar.isParValidiRead(mappaLoc);
        assertFalse(boolOttenuto);
    }// end of single test

}// end of class
