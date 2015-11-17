import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.query.QueryCat;
import it.algos.vaad.wiki.query.QueryReadTitle;
import it.algos.webbase.web.lib.LibArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by gac on 20 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class LibWikiTest extends VaadTest {

    // alcuni valori di riferimento
    private static String SPAZI_MULTIPLI = "Abc    def ghi   lmn";
    private static String SPAZI_MULTIPLI_PIU_ESTERNI = " Abc    def ghi   lmn ";
    private static String SPAZIO_SINGOLO = "Abc def ghi lmn";
    private static String SPAZIO_SINGOLO_PIU_ESTERNI = " Abc def ghi lmn ";

    private ArrayList<String> listaPrevista = null;
    private ArrayList<String> listaOttenuta = null;

    /**
     * Controllo di validità della mappa di stringhe lette dal server
     */
    public static void isMappaReadTxtValida(Map mappa) {
        String key;
        Object value;

        assertNotNull("'Mappa non esistente", mappa);
        assertTrue("Mappa di lunghezza errata", mappa.size() == PARAMETRI_LETTI_DAL_SERVER);

        for (PagePar par : PagePar.getRead()) {
            key = par.toString();
            value = mappa.get(key);
            assertNotNull("Manca il valore del campo: " + key, value);
        } // fine del ciclo for-each
    }// end of method

    /**
     * Controllo di validità della mappa di oggetti letti dal server
     */
    public static void isMappaReadObjValida(Map mappa) {
        String key;
        Object value;

        assertNotNull("'Mappa non esistente", mappa);
        assertTrue("Mappa di lunghezza errata", mappa.size() == PARAMETRI_LETTI_DAL_SERVER);

        for (PagePar par : PagePar.getRead()) {
            isCampoTypoValido(mappa, par);
        } // fine del ciclo for-each
    }// end of method

    /**
     * Controllo di validità della mappa di oggetti da rendere permanenti nel database
     */
    public static void isMappaDBValida(Map mappa) {
        String key;
        Object value;

        assertNotNull("'Mappa non esistente", mappa);
        assertTrue("Mappa di lunghezza errata", mappa.size() == PARAMETRI_PER_DATABASE);

        for (PagePar par : PagePar.getDB()) {
            isCampoTypoValido(mappa, par);
        } // fine del ciclo for-each
    }// end of method

    /**
     * Controllo di validità del singolo campo (field)
     *
     * @param field
     * @return status booleano
     */
    private static void isCampoTypoValido(Map mappa, PagePar field) {
        PagePar.TypeField type = field.getType();
        String key = field.toString();
        Object value = mappa.get(key);

        if (type == PagePar.TypeField.string) {
            assertNotNull("Manca il campo: " + key, value);
        }// fine del blocco if

        if (type == PagePar.TypeField.longzero) {
            assertTrue("Il campo " + key + " non è un lungo", value instanceof Long);
        }// fine del blocco if

        if (type == PagePar.TypeField.longnotzero) {
            assertNotSame("Il valore del campo " + key + " non può essere uguale a zero", value, 0);
            assertTrue("Il campo " + key + " non è un lungo", value instanceof Long);
        }// fine del blocco if

        if (type == PagePar.TypeField.date) {
            assertTrue("Il campo " + key + " non è una data valida", value instanceof Date);
        }// fine del blocco if

        if (type == PagePar.TypeField.timestamp) {
            assertTrue("Il campo " + key + " non è un timestamp valido", value instanceof Timestamp);
        }// fine del blocco if

        if (type == PagePar.TypeField.booleano) {
            assertTrue("Il campo " + key + " non è un valore booleano", value instanceof Boolean);
        }// fine del blocco if

    }// end of method

    @Test
    /**
     * Restituisce il numero di occorrenze di un tag nel testo.
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     *
     * @param testo da spazzolare
     * @param tag da cercare
     * @return numero di occorrenze
     *         zero se non ce ne sono
     */
    public void getNumTag() {
        sorgente = "Testo di ter controllo per ter vedere se terpassa anche questo ter";

        tag = "ter"; // senza spazi
        numPrevisto = 4;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);

        tag = " ter "; // con spazi
        numPrevisto = 2;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);

        sorgente = "Testo di un {{Template con altro {{annidato}} dopo}}";

        tag = "{"; // singolo
        numPrevisto = 4;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);

        tag = "{{"; // doppio
        numPrevisto = 2;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);

        tag = "{{ "; // doppio con spazio dopo
        numPrevisto = 0;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);

        tag = " {{"; // doppio con spazio prima
        numPrevisto = 2;
        numOttenuto = LibWiki.getNumTag(sorgente, tag);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Restituisce il numero di occorrenze di una coppia di graffe iniziali nel testo.
     *
     * @param testo da spazzolare
     * @param graffe da cercare
     * @return numero di occorrenze
     *         zero se non ce ne sono
     */
    public void getNumGraffeIni() {
        sorgente = "Testo di un {{Templa}}te con altro {{annidato}} dopo}}";
        numPrevisto = 2;
        numOttenuto = LibWiki.getNumGraffeIni(sorgente);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Restituisce il numero di occorrenze di una coppia di graffe finali nel testo.
     *
     * @param testo da spazzolare
     * @param graffe da cercare
     * @return numero di occorrenze
     *         zero se non ce ne sono
     */
    public void getNumGraffeEnd() {
        sorgente = "Testo di un {{Templa}}te con altro {{annidato}} dopo}}";
        numPrevisto = 3;
        numOttenuto = LibWiki.getNumGraffeEnd(sorgente);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Controlla che le occorrenze delle graffe iniziali e finali si pareggino all'interno del testo.
     * Ordine ed annidamento NON considerato
     *
     * @param testo da spazzolare
     * @return vero se il numero di GRAFFE_INI è uguale al numero di GRAFFE_END
     */
    public void isPariGraffe() {
        sorgente = "Testo di un {{Templa}}te con altro {{annidato}} dopo}}";
        boolPrevisto = false;
        boolOttenuto = LibWiki.isPariGraffe(sorgente);
        assertEquals(boolOttenuto, boolPrevisto);

        sorgente = "Testo di un {{Templa}}te con altro {{annidato}} dopo}}{{";
        boolPrevisto = true;
        boolOttenuto = LibWiki.isPariGraffe(sorgente);
        assertEquals(boolOttenuto, boolPrevisto);
    }// end of single test

    @Test
    /**
     * Elimina la testa iniziale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la testa, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param testa da eliminare
     * @return uscita stringa convertita
     */
    public void levaTesta() {
        String testa = null;

        sorgente = SPAZIO_SINGOLO_PIU_ESTERNI;
        previsto = SPAZIO_SINGOLO;
        ottenuto = LibWiki.levaTesta(sorgente, null);
        assertEquals(ottenuto, previsto);

        sorgente = SPAZIO_SINGOLO_PIU_ESTERNI;
        testa = "";
        previsto = SPAZIO_SINGOLO;
        ottenuto = LibWiki.levaTesta(sorgente, testa);
        assertEquals(ottenuto, previsto);

        sorgente = " due Ancora  ";
        testa = "due";
        previsto = "Ancora";
        ottenuto = LibWiki.levaTesta(sorgente, testa);
        assertEquals(ottenuto, previsto);

        sorgente = " due Ancora  ";
        testa = "ora";
        previsto = "due Ancora";
        ottenuto = LibWiki.levaTesta(sorgente, testa);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Elimina la coda terminale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la coda, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param coda da eliminare
     * @return uscita stringa convertita
     */
    public void levaCoda() {
        String coda;

        sorgente = SPAZIO_SINGOLO_PIU_ESTERNI;
        previsto = SPAZIO_SINGOLO;
        ottenuto = LibWiki.levaCoda(sorgente, "");
        assertEquals(ottenuto, previsto);

        sorgente = " Ancora due ";
        coda = "due";
        previsto = "Ancora";
        ottenuto = LibWiki.levaCoda(sorgente, coda);
        assertEquals(ottenuto, previsto);

        sorgente = " Ancora due ";
        coda = "ora";
        previsto = "Ancora due";
        ottenuto = LibWiki.levaCoda(sorgente, coda);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param testoIn in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     * @return testoOut convertito
     */
    public void sostituisce() {
        sorgente = "Testo di ter controllo per ter vedere se terpassa anche questo ter";
        previsto = "Testo di dop controllo per dop vedere se doppassa anche questo dop";
        ottenuto = LibWiki.sostituisce(sorgente, "ter", "dop");
        assertEquals(ottenuto, previsto);


        sorgente = "Testo di \" controllo per \" vedere se \"passa anche questo \"";
        previsto = "Testo di xx controllo per xx vedere se xxpassa anche questo xx";
        ottenuto = LibWiki.sostituisce(sorgente, "\"", "xx");
        assertEquals(ottenuto, previsto);

        sorgente = "Testo di \" controllo per \" vedere se \"passa anche questo \"";
        previsto = "Testo di \\\" controllo per \\\" vedere se \\\"passa anche questo \\\"";
        ottenuto = LibWiki.sostituisce(sorgente, "\"", "\\\"");
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Elimina (eventuali) doppie graffe in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con doppie graffe eliminate
     */
    public void setNoGraffe() {
        String testoUno = "{{In questo testo la trovo}}";
        String testoDue = " In questo testo la trovo}}";
        String testoTre = "{In questo testo la trovo }}";
        previsto = "In questo testo la trovo";
        String vuota = "";

        ottenuto = LibWiki.setNoGraffe(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(testoTre);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(vuota);
        assertEquals(ottenuto, vuota);
    } // fine del metodo

    @Test
    /**
     * Chiude il template
     *
     * Il testo inizia col template, ma prosegue (forse) anche oltre
     * Cerco la prima doppia graffa che abbia all'interno lo stesso numero di aperture e chiusure
     * Spazzola il testo fino a pareggiare le graffe
     * Se non riesce a pareggiare le graffe, ritorna una stringa nulla
     *
     * @param testo da spazzolare
     * @return template
     */
    public void chiudeTmpl() {
        sorgente = "{{Template:Bio con altro {{annidato}} prima della fine}} e poi prosegue";
        previsto = "{{Template:Bio con altro {{annidato}} prima della fine}}";
        ottenuto = LibWiki.chiudeTmpl(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "{{Template:Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "{{Template:Bio con altro {{annidato}} prima della fine}}";
        ottenuto = LibWiki.chiudeTmpl(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "{{Template:Bio con altro {{annidato prima}} della {{fine}} e poi prosegue {{ancora}}";
        previsto = "";
        ottenuto = LibWiki.chiudeTmpl(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono COMPRESI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public void estraeTmpl() {

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo}}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo}}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{bio |Alfa=Mario |Beta=Pippo}}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{bio |Alfa=Mario |Beta=Pippo}}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo {{Bio}} della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio}}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo {{Bio}} della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio}}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce {{Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "{{Bio con altro {{annidato}} prima della fine}}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce {{Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "{{Bio con altro {{annidato}} prima della fine}}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo }} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo }}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo }} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo }}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo\n}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo\n}}";
        tag = "Bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo\n}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo\n}}";
        tag = "bio";
        ottenuto = LibWiki.estraeTmplCompresi(sorgente, tag);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public void estraeTmplEsclusi() {
        tag = "Bio";

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplEsclusi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "";
        previsto = "";
        ottenuto = LibWiki.estraeTmplEsclusi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce {{Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "Bio con altro {{annidato}} prima della fine";
        ottenuto = LibWiki.estraeTmplEsclusi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo }} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplEsclusi(sorgente, tag);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo\n}} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplEsclusi(sorgente, tag);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova (ce ne dovrebbe essere solo uno)
     * Gli estremi sono COMPRESI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public void estraeTmplBioCompresi() {
        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo {{Bio}} della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "{{Bio}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce {{Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "{{Bio con altro {{annidato}} prima della fine}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo }} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo }}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo\n}} e poi prosegue";
        previsto = "{{Bio |Alfa=Mario |Beta=Pippo\n}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public void estraeTmplBioEsclusi() {
        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplBioEsclusi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo {{Bio}} della voce con {{Bio |Alfa=Mario |Beta=Pippo}} e poi prosegue";
        previsto = "Bio";
        ottenuto = LibWiki.estraeTmplBioEsclusi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce {{Bio con altro {{annidato}} prima della fine}} e poi prosegue {{ancora}}";
        previsto = "Bio con altro {{annidato}} prima della fine";
        ottenuto = LibWiki.estraeTmplBioEsclusi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo }} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplBioEsclusi(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "Testo della voce con {{Bio |Alfa=Mario |Beta=Pippo\n}} e poi prosegue";
        previsto = "Bio |Alfa=Mario |Beta=Pippo";
        ottenuto = LibWiki.estraeTmplBioEsclusi(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Converte il valore stringa della data in una data
     *
     * @param dataTxt in ingresso
     * @return data in uscita
     */
    public void convertTxtData() {
        String sorgente;
        Date dataPrevista;
        Date dataOttenuta;

        sorgente = "2015-06-30T10:18:05Z";
        dataPrevista = new Date(115, 5, 30, 10, 18, 5);
        dataOttenuta = LibWiki.convertTxtData(sorgente);
        assertEquals(dataOttenuta, dataPrevista);
    }// end of single test

    @Test
    /**
     * Converte il valore stringa della data in una data
     *
     * @param dataTxt in ingresso
     * @return data in uscita
     */
    public void convertTxtData2() {
        String sorgente;
        Date dataPrevista;
        Date dataOttenuta;

        sorgente = "2015-05-26T09:01:17Z";
        dataPrevista = new Date(115, 4, 26, 9, 1, 17);
        dataOttenuta = LibWiki.convertTxtData(sorgente);
        assertEquals(dataOttenuta, dataPrevista);
    }// end of single test


    @Test
    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     *
     * @param mappa standard (valori String) in ingresso
     * @return mappa typizzata secondo PagePar
     */
    public void converteMappa() {
        HashMap mappaString;
        HashMap mappaConvertita;

        String textPagina = new QueryReadTitle(TITOLO).getContenuto();
        mappaString = LibWiki.creaMappaQuery(textPagina);
        isMappaReadTxtValida(mappaString);

        mappaConvertita = LibWiki.converteMappa(mappaString);
        isMappaReadObjValida(mappaConvertita);
    }// end of single test

    @Test
    /**
     * Crea una lista di pagine (valori pageids) dal testo JSON di una pagina
     *
     * @param text in ingresso
     * @return mappa standard (valori String)
     */
    public void creaListaCatJson() {
        ArrayList<Long> lista;
        String titolo = "Eventi del 1902";
        QueryCat categoria;
        String risultato;

        categoria = new QueryCat(titolo);
        risultato = categoria.getContenuto();
        lista = LibWiki.creaListaCatJson(risultato);
        assertNotNull(lista);
        assertTrue(lista.size() == 14);
    }// end of single test

    @Test
    /**
     * Crea una lista di pagine (valori pageids) dal titolo di una categoria
     */
    public void creaListaCat() {
        ArrayList<Long> lista;
        String titolo = "Eventi del 1902";

        lista = LibWiki.creaListaCat(titolo);
        assertNotNull(lista);
        assertTrue(lista.size() == 2);
    }// end of single test

    @Test
    /**
     * Crea una lista di pagine (valori pageids) dal titolo di una categoria
     */
    public void creaListaCat2() {
        ArrayList<Long> lista;
        String titolo = "Nati nel 1420";

        lista = LibWiki.creaListaCat(titolo);
        assertNotNull(lista);
        assertTrue(lista.size() == 36);
    }// end of single test

    @Test
    /**
     * Crea una stringa di testo, con tutti i valori della lista, separati dal pipe
     *
     * @param lista (valori Integer) in ingresso
     * @return stringa di valori
     */
    public void creaListaPageids() {
        ArrayList<Long> lista = new ArrayList<Long>();
        lista.add((long) 23);
        lista.add((long) 45);
        lista.add((long) 5389);
        lista.add((long) 7);
        lista.add((long) 98);

        previsto = "23|45|5389|7|98";
        ottenuto = LibWiki.creaListaPageids(lista);
        assertEquals(ottenuto, previsto);
    }// end of single test


    @Test
    /**
     * Elimina (eventuali) doppie graffe in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie graffe eliminate
     */
    public void testSetNoGraffe() {
        String testoUno = "{{In questo testo la trovo}}";
        String testoDue = " In questo testo la trovo}}";
        String testoTre = "{In questo testo la trovo }}";
        previsto = "In questo testo la trovo";

        ottenuto = LibWiki.setNoGraffe(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(testoTre);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoGraffe(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test


    @Test
    /**
     * Elimina (eventuali) doppie quadre in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie quadre eliminate
     */
    public void testSetNoQuadre() {
        String testoUno = "[[In questo testo la trovo]]";
        String testoDue = " In questo testo la trovo]]";
        String testoTre = "[In questo testo la trovo ]]";
        String testoQuattro = "[In questo testo la trovo]";
        previsto = "In questo testo la trovo";

        ottenuto = LibWiki.setNoQuadre(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoQuadre(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoQuadre(testoTre);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoQuadre(testoQuattro);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoQuadre(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoQuadre(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test


    @Test
    /**
     * Elimina (eventuali) tripli apici (grassetto) in testa e coda della stringa.
     * Funziona solo se gli apici sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con tripli apici eliminati
     */
    public void testSetNoBold() {
        String testoUno = "'''In questo testo la trovo'''";
        String testoDue = "  In questo testo la trovo'''";
        String testoTre = "'''In questo testo la trovo '''";
        previsto = "In questo testo la trovo";

        ottenuto = LibWiki.setNoBold(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoBold(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoBold(testoTre);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoBold(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setNoBold(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test

    @Test
    /**
     * Aggiunge doppie graffe in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali graffe già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie graffe aggiunte
     */
    public void testSetGraffe() {
        String testoUno = " In questo testo la trovo";
        String testoDue = "{In questo testo la trovo} ";
        previsto = "{{In questo testo la trovo}}";

        ottenuto = LibWiki.setGraffe(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setGraffe(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setGraffe(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setGraffe(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test


    @Test
    /**
     * Aggiunge doppie quadre in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali quadre già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie quadre aggiunte
     */
    public void testSetQuadre() {
        String testoUno = " In questo testo la trovo";
        String testoDue = "[In questo testo la trovo] ";
        previsto = "[[In questo testo la trovo]]";

        ottenuto = LibWiki.setQuadre(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setQuadre(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setQuadre(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setQuadre(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test


    @Test
    /**
     * Aggiunge tripli apici (grassetto) in testa ed in coda della stringa.
     * Aggiunge SOLO se gia non esistono
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali apici già presenti, per evitare di metterli doppi
     *
     * @param stringaIn in ingresso
     * @return stringa con tripli apici aggiunte
     */
    public void testSetBold() {
        String testoUno = "'''In questo testo la trovo'''";
        String testoDue = "  In questo testo la trovo'''";
        previsto = "'''In questo testo la trovo'''";

        ottenuto = LibWiki.setBold(testoUno);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setBold(testoDue);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setBold(previsto);
        assertEquals(ottenuto, previsto);

        ottenuto = LibWiki.setBold(VUOTA);
        assertEquals(ottenuto, VUOTA);
    }// end of single test


    @Test
    /**
     * Crea una lista standard (valori String) dewlle chiavi del testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return lista standard di chiavi (valori String)
     */
    public void creaListaKeys() {
        String textJSON;
        listaPrevista = new ArrayList<String>();
        listaPrevista.add(LibWiki.BATCH);
        listaPrevista.add(LibWiki.QUERY);

        textJSON = new QueryReadTitle(TITOLO).getContenuto();
        listaOttenuta = LibWiki.creaListaKeys(textJSON);
        assertEquals(listaOttenuta, listaPrevista);

        textJSON = "{\"edit\":{\"result\":\"Success\",\"pageid\":2847116,\"title\":\"Utente:Gac/Sandbox5\",\"contentmodel\":\"wikitext\",\"nochange\":\"\"}}";
        listaPrevista = new ArrayList<String>();
        listaPrevista.add("edit");
        listaOttenuta = LibWiki.creaListaKeys(textJSON);
        assertEquals(listaOttenuta, listaPrevista);

    }// end of single test

    @Test
    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina
     *
     * @param text in ingresso
     * @return mappa standard (valori String)
     */
    public void creaMappaQuery() {
        HashMap mappa;
        String textPagina = new QueryReadTitle(TITOLO).getContenuto();

        mappa = LibWiki.creaMappaQuery(textPagina);
        assertNotNull(mappa);
        isMappaReadTxtValida(mappa);
    }// end of single test

    @Test
    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina action=edit
     *
     * @param textJSON in ingresso
     * @return mappa edit (valori reali)
     */
    public void creaMappaEdit() {
        sorgente = "{\"edit\":{\"result\":\"Success\",\"pageid\":2847116,\"title\":\"Utente:Gac/Sandbox5\",\"contentmodel\":\"wikitext\",\"oldrevid\":76121198,\"newrevid\":76469856,\"newtimestamp\":\"2015-11-12T14:57:22Z\"}}";
        HashMap<String, Object> mappaPrevista = new HashMap<String, Object>();
        HashMap<String, Object> mappaOttenuta;
        mappaPrevista.put(LibWiki.RESULT, "Success");
        mappaPrevista.put(LibWiki.PAGEID, 2847116L);
        mappaPrevista.put(LibWiki.TITLE, "Utente:Gac/Sandbox5");
        mappaPrevista.put(LibWiki.CONTENT_MODEL, "wikitext");
        mappaPrevista.put(LibWiki.OLD_REV_ID, 76121198L);
        mappaPrevista.put(LibWiki.NEW_REV_ID, 76469856L);
        mappaPrevista.put(LibWiki.NEW_TIME_STAMP, "2015-11-12T14:57:22Z");
        mappaPrevista.put(LibWiki.NOCHANGE, false);

        mappaOttenuta = LibWiki.creaMappaEdit(sorgente);
        boolOttenuto = LibArray.isMapEquals(mappaOttenuta, mappaPrevista);
        assertTrue(boolOttenuto);
    }// end of single test

    @Test
    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return mappa standard (valori reali)
     */
    public void creaMappa() {
        String titolo = "Shortest path";
        sorgente = new QueryReadTitle(titolo).getContenuto();
        HashMap<String, Object> mappaPrevista = new HashMap<String, Object>();
        HashMap<String, Object> mappaOttenuta;
        mappaPrevista.put(LibWiki.BATCH, true);
        mappaPrevista.put(LibWiki.QUERY, "wikitext");

        mappaOttenuta = LibWiki.creaMappa(sorgente);
        assertNotNull(mappaOttenuta);
        assertEquals(mappaOttenuta.size(), 2);
        assertTrue(mappaOttenuta.containsKey(LibWiki.BATCH));
        assertTrue(mappaOttenuta.containsKey(LibWiki.QUERY));
        assertTrue(mappaOttenuta.get(LibWiki.BATCH) instanceof Boolean);
        assertEquals(mappaOttenuta.get(LibWiki.BATCH), true);
        assertTrue(mappaOttenuta.get(LibWiki.QUERY) instanceof JSONObject);
    }// end of single test

    @Test
    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina action=move
     *
     * @param textJSON in ingresso
     * @return mappa edit (valori reali)
     */
    public void creaMappaMove() {
        sorgente = "{\"servedby\":\"mw1200\",\"error\":{\"code\":\"selfmove\",\"info\":\"Source and destination titles are the same;\\ncannot move a page over itself.\",\"docref\":\"See https://it.wikipedia.org/w/api.php for API usage\"}}";

        mappa = LibWiki.creaMappaMove(sorgente);
        assertNotNull(mappa);
        assertEquals(mappa.get(LibWiki.CODE), "selfmove");
    }// end of single test

}// end of testing class