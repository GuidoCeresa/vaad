import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.WikiLogin;
import it.algos.vaad.wiki.WrapTime;
import it.algos.webbase.web.lib.LibArray;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gac on 28 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class ApiTest extends VaadTest {

    private static final long[] listaPageIds = {3397115L, 4452510L, 1691379L, 3520373L, 4956588L, 5136975L, 2072357L, 4700355L};
    @SuppressWarnings("all")
    private static ArrayList<Long> arrayPageIds = (ArrayList) LibArray.fromLong(listaPageIds);
    private WikiLogin loginWiki;

//    @Before
    // Setup logic here
//    public void setUp() {
//        String nick = "biobot";
//        String password = "fulvia";
//
//        loginWiki = new WikiLogin(nick, password);
//        arrayPageIds = (ArrayList) LibArray.fromLong(listaPageIds);
//    } // fine del metodo iniziale


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

        // pageid CORRETTO, inviato come stringa
        ottenuto = Api.leggePagina(PAGEID_COME_STRINGA);
        assertEquals(ottenuto, "");
    }// end of single test


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

        // pageid CORRETTO, inviato come stringa
        pageOttenuta = Api.leggePage(PAGEID_COME_STRINGA);
        assertNull(pageOttenuta);
    }// end of method


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

        // pageid CORRETTO, inviato come stringa
        ottenuto = Api.leggeVoce(PAGEID_COME_STRINGA);
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


    @Test
    /**
     * Controlla l'esistenza di una pagina.
     *
     * @param title della pagina da ricercare
     * @return true se la pagina esiste
     */
    public void esiste() {
        boolean status;

        status = Api.esiste(TITOLO);
        assertTrue(status);

        status = Api.esiste(TITOLO_2);
        assertTrue(status);

        status = Api.esiste(TITOLO_ERRATO);
        assertFalse(status);

    }// end of single test


//    @Test

    /**
     * Modifica il contenuto di una pagina.
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param oldText   da eliminare
     * @param newText   da inserire
     * @param summary   oggetto della modifica
     * @param login     for testing purpose only
     */
    public void modificaVoce2() {
        String wikiTitle = "Utente:Gac/Sandbox4";
        String oldTxt = "diciotto senza remore";
        String newTxt = "senza";

        boolOttenuto = Api.modificaVoce(wikiTitle, oldTxt, newTxt, "", loginWiki);
        assertFalse(boolOttenuto);
    }// end of single test

    @Test
    /**
     * Modifica il contenuto di una pagina.
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param oldText   da eliminare
     * @param newText   da inserire
     * @param summary   oggetto della modifica
     * @param login     for testing purpose only
     */
    public void modificaVoce() {
        String testoIniziale;
        String testoTemporaneo;
        String testoA;
        String testoB;
        String summaryA;
        String summaryB;
        boolean status;

        //--recupera il testo esistente per partire da una situazione pulita
        testoIniziale = Api.leggeVoce(TITOLO_3);

        //--login obbligatorio
        setLogin();

        //--summary utilizza il login esistente
        summaryA = LibWiki.getSummary("test add h");
        summaryB = LibWiki.getSummary("test minus h");

        //--modifica da scrivere
        testoA = testoIniziale + "h";

        //--prima scrittura
        status = Api.scriveVoce(TITOLO_3, testoA, summaryA);
        assertTrue(status);

        //--recupera il testo temporaneo dopo una prima scrittura
        testoTemporaneo = Api.leggeVoce(TITOLO_3);

        //--controllo per vedere se il testo attualmente è diverso
        assertFalse(testoIniziale.equals(testoTemporaneo));

        //--seconda scrittura
        testoB = testoIniziale;
        status = Api.scriveVoce(TITOLO_3, testoB, summaryB);
        assertTrue(status);

        //--controllo finale
        assertFalse(testoIniziale.equals(testoTemporaneo));
        testoTemporaneo = Api.leggeVoce(TITOLO_3);
        assertTrue(testoIniziale.equals(testoTemporaneo));

//        String wikiTitle = "Utente:Biobot/6";
//        String tag5 = "quinta";
//        String tag6 = "sesta";
//        String oldTxt = "";
//        String newTxt = "";
//        String oldTestoVoce = Api.leggeVoce(wikiTitle);
//
//        if (oldTestoVoce.contains(tag5)) {
//            oldTxt = tag5;
//            newTxt = tag6;
//        } else {
//            if (oldTestoVoce.contains(tag6)) {
//                oldTxt = tag6;
//                newTxt = tag5;
//            }// end of if cycle
//        }// end of if/else cycle
//
//        if (!oldTxt.equals("")) {
//            boolOttenuto = Api.modificaVoce(wikiTitle, oldTxt, newTxt, "", loginWiki);
//            assertTrue(boolOttenuto);
//        }// end of if cycle
    }// end of single test

    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli sia delle voci che delle subcategorie
     */
    public void leggeCat() {
        setLogin();
        listaString = Api.leggeCat(TITOLO_CAT_BREVE);
        assertNotNull(listaString);
        assertEquals(listaString.size(), 14);
    } // fine del metodo

    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid delle voci
     */
    public void leggeCatLong() {
        setLogin();
        listaLong = Api.leggeCatLong(TITOLO_CAT_BREVE);
        assertNotNull(listaLong);
        assertEquals(listaLong.size(), 2);
    } // fine del metodo

    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli sia delle voci che delle subcategorie
     */
    public void leggeCatTitles() {
        setLogin();
        listaString = Api.leggeCatTitles(TITOLO_CAT_BREVE);
        assertNotNull(listaString);
        assertEquals(listaString.size(), 14);
    }// end of single test

    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid sia delle voci che delle subcategorie
     */
    public void leggeCatPageids() {
        setLogin();
        listaLong = Api.leggeCatPageids(TITOLO_CAT_BREVE);
        assertNotNull(listaLong);
        assertEquals(listaLong.size(), 14);
    }// end of single test

    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli delle voci
     */
    public void leggeCatTitlesOnlyVoci() {
        setLogin();
        listaString = Api.leggeCatTitlesOnlyVoci(TITOLO_CAT_BREVE);
        assertNotNull(listaString);
        assertEquals(listaString.size(), 2);
    }// end of single test


    @Test
    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid delle voci
     */
    public void leggeCatPageidsOnlyVoci() {
        setLogin();
        listaLong = Api.leggeCatPageidsOnlyVoci(TITOLO_CAT_BREVE);
        assertNotNull(listaLong);
        assertEquals(listaLong.size(), 2);
    }// end of single test

    @Test
    /**
     * Legge le voci che puntano ad una pagina.
     * Restituisce una lista (ArrayList) di titoli
     *
     * @param title della pagina da controllare
     * @return lista titoli delle voci
     */
    public void leggeBacklinks() {
        setLogin();
        listaString = Api.leggeBacklinks("Lago di Ha! Ha!");
        assertNotNull(listaString);
        assertEquals(listaString.size(), 6);

        listaString = Api.leggeBacklinksOnlyVoci("Lago di Ha! Ha!");
        assertNotNull(listaString);
        assertEquals(listaString.size(), 3);
    }// end of single test


    @Test
    /**
     * Legge una lista di pageids per costruire una lista di WrapTime
     * <p>
     *
     * @param bloccoPageids elenco di pageids delle pagine da controllare
     * @return lista di WrapTime (wrapper)
     */
    public void leggeWrapTime() {
        ArrayList<WrapTime> lista;

        lista = Api.leggeWrapTime(arrayPageIds);
        assertNotNull(lista);
        assertEquals(lista.size(), 8);

    }// end of single test


//    @Test
//    /**
//     * Sposta una pagina (sposta il titolo)
//     *
//     * @param oldTitle vecchio titolo della pagina
//     * @param newTitle nuovo titolo della pagina
//     */
//    public void sposta() {
//        boolOttenuto = Api.sposta(TITOLO_ALTRO, TITOLO_BLOCCATO);
//        assertTrue(boolOttenuto);
//    } // fine del metodo

}// end of testing class