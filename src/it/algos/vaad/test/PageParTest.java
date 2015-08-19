package it.algos.vaad.test;

import it.algos.vaad.wiki.PagePar;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 28 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class PageParTest extends VaadTest{

    @Before
    public void creaMappa() {
        mappa = new HashMap<String, Object>();

        //--parametri wiki base
        add(PagePar.pageid, 24);
        add(PagePar.ns, 0);
        add(PagePar.title, "Titolo della voce");

        //--parametri wiki info
        add(PagePar.contentmodel, "modello");
        add(PagePar.pagelanguage, "linguaggio");
        add(PagePar.touched, new Date());
        add(PagePar.lastrevid, 24);
        add(PagePar.length, 24);
        add(PagePar.csrftoken, "token");
        add(PagePar.starttimestamp, new Date());

        //--parametri wiki revisions
        add(PagePar.revid, 2435);
        add(PagePar.parentid, 6574);
        add(PagePar.user, "utente");
        add(PagePar.userid, 8765);
        add(PagePar.timestamp, new Date());
        add(PagePar.size, 8239);
        add(PagePar.comment, "commento");
        add(PagePar.contentformat, "formato");
        add(PagePar.content, "testo");

        //--parametri altri
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
        fieldPrevisto = PagePar.lastrevid.getType();
        String tagNome = "lastrevid";
        fieldOttenuto = PagePar.getParField(tagNome);
        assertNotNull(fieldOttenuto);
        assertEquals(fieldOttenuto, fieldPrevisto);

        fieldPrevisto = PagePar.TypeField.integerzero;
        tagNome = "parentid";
        fieldOttenuto = PagePar.getParField(tagNome);
        assertNotNull(fieldOttenuto);
        assertEquals(fieldOttenuto, fieldPrevisto);
    }// end of single test

    @Test
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


    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
    /**
     * Restituisce una collezione degli elementi da restituire in lettura
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
        boolOttenuto = PagePar.isParValidi(mappa);
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
        HashMap<String, Object> mappaLoc = (HashMap<String, Object>)mappa.clone();

        mappaLoc.put(PagePar.pageid.toString(),0);
        boolOttenuto = PagePar.isParValidi(mappaLoc);
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
        HashMap<String, Object> mappaLoc = (HashMap<String, Object>)mappa.clone();

        mappaLoc.remove(PagePar.title.toString());
        boolOttenuto = PagePar.isParValidi(mappaLoc);
        assertFalse(boolOttenuto);
    }// end of single test

}// end of class
