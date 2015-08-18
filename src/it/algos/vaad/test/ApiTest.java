package it.algos.vaad.test;

import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.TipoRicerca;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 28 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class ApiTest extends VaadTest {


    @Test
    /**
     * Legge una pagina
     * <p/>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return pagina (con i metadati mediawiki)
     */
    public void leggePage() {
        // titolo corretto
        pageOttenuta = Api.leggePage(TITOLO);
        assertNotNull(pageOttenuta);
        assertTrue(pageOttenuta.isValida());

        // pageid corretto
        pageOttenuta = Api.leggePage(PAGEID);
        assertNotNull(pageOttenuta);
        assertTrue(pageOttenuta.isValida());

        // pageid corretto
        pageOttenuta = Api.leggePage(PAGEID_UTF8);
        assertNotNull(pageOttenuta);
        assertTrue(pageOttenuta.isValida());

        // titolo NON corretto
        pageOttenuta = Api.leggePage(TITOLO_ERRATO);
        assertNull(pageOttenuta);

        // pageid NON corretto
        pageOttenuta = Api.leggePage(PAGEID_ERRATO);
        assertNull(pageOttenuta);

        // titolo corretto, ma TipoRicerca errato
        pageOttenuta = Api.leggePage(TITOLO, TipoRicerca.pageid);
        assertNull(pageOttenuta);

        // titolo corretto, ma TipoRicerca errato
        pageOttenuta = Api.leggePage(TITOLO, TipoRicerca.listaPageids);
        assertNull(pageOttenuta);
    }// end of method

    @Test
    /**
     * Legge il contenuto (tutto) di una pagina
     * <p/>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public void leggePagina() {
        // titolo corretto
        ottenuto = Api.leggePagina(TITOLO);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        // pageid corretto
        ottenuto = Api.leggePagina(PAGEID);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_PAGINA));
        assertTrue(ottenuto.endsWith(TAG_END_PAGINA));

        // titolo NON corretto
        ottenuto = Api.leggePagina(TITOLO_ERRATO);
        assertEquals(ottenuto, "");

        // pageid NON corretto
        ottenuto = Api.leggePagina(PAGEID_ERRATO);
        assertEquals(ottenuto, "");
    }// end of single test

    @Test
    /**
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     *
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public void leggeVoce() {
        // titolo corretto
        ottenuto = Api.leggeVoce(TITOLO);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_VOCE));
        assertTrue(ottenuto.endsWith(TAG_END_VOCE));

        // pageid corretto
        ottenuto = Api.leggeVoce(PAGEID);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_VOCE));
        assertTrue(ottenuto.endsWith(TAG_END_VOCE));

        // titolo NON corretto
        ottenuto = Api.leggeVoce(TITOLO_ERRATO);
        assertEquals(ottenuto, "");

        // pageid NON corretto
        ottenuto = Api.leggeVoce(PAGEID_ERRATO);
        assertEquals(ottenuto, "");
    }// end of single test


    @Test
    /**
     * Legge un template da una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param tag         nome del template
     *
     * @return contenuto del template
     */
    public void leggeTmpl() {
        // tag corretto
        tag = "videogioco";
        ottenuto = Api.leggeTmpl(TITOLO_TMPL, tag);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL));

        // tag NON corretto
        tag = "gioco";
        ottenuto = Api.leggeTmpl(TITOLO_TMPL, tag);
        assertEquals(ottenuto, "");

        // tag bio
        tag = "bio";
        ottenuto = Api.leggeTmpl(TITOLO, tag);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL_BIO));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL_BIO));
        ottenuto = Api.leggeTmpl(PAGEID, tag);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL_BIO));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL_BIO));
    }// end of single test


    @Test
    /**
     * Legge il template bio di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     *
     * @return contenuto del template bio
     */
    public void leggeTmplBio() {
        ottenuto = Api.leggeTmplBio(TITOLO);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL_BIO));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL_BIO));

        ottenuto = Api.leggeTmplBio(PAGEID);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL_BIO));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL_BIO));
    }// end of single test

    @Test
    /**
     * Estrae un template dal testo
     * <p>
     *
     * @param testo completo della voce
     *
     * @return contenuto del template bio
     */
    public void estraeTmpl() {
        contenuto = Api.leggeVoce(TITOLO_TMPL);
        assertNotNull(contenuto);

        tag = "videogioco";
        ottenuto = Api.estraeTmpl(contenuto, tag);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL));
    }// end of method

    @Test
    /**
     * Estrae il template bio dal testo
     * <p>
     *
     * @param testo completo della voce
     *
     * @return contenuto del template bio
     */
    public void estraeTmplBio() {
        contenuto = Api.leggeVoce(TITOLO);
        assertNotNull(contenuto);

        ottenuto = Api.estraeTmplBio(contenuto);
        assertNotNull(ottenuto);
        assertTrue(ottenuto.startsWith(TAG_INI_TMPL_BIO));
        assertTrue(ottenuto.endsWith(TAG_END_TMPL_BIO));
    }// end of method

}// end of testing class