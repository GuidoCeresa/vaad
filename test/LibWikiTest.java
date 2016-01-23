import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.request.QueryCat;
import it.algos.vaad.wiki.request.QueryReadTitle;
import it.algos.vaad.wiki.request.RequestWikiWrite;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibTime;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by gac on 20 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */
public class LibWikiTest extends VaadTest {

    private static String PAGINA_PROVA = "Utente:Gac/Sandbox5";

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

    @Test
    /**
     * Legge il modulo dalla pagina
     *
     * @param titolo della pagina da cui recuperare il modulo
     * @return testo del modulo
     */
    public void leggeModulo() {
        ottenuto = LibWiki.leggeModulo(TITOLO_MODULO);

        HashMap<String, String> mappa = LibWiki.leggeMappaModulo(TITOLO_MODULO);
        int a = 87;
    }// end of single test


    @Test
    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     * @return differenza
     */
    public void delta() {
        long inizio;
        long fine;
        long durata;
        int dim = 300000;
        ArrayList<Integer> listaUno = new ArrayList<Integer>();
        ArrayList<Integer> listaDue = new ArrayList<Integer>();
        ArrayList<Integer> listaDifferenza;

        for (int k = 0; k < dim; k = k + 2) {
            listaUno.add(k);
        }// end of for cycle

        for (int k = 0; k < dim; k = k + 3) {
            listaDue.add(k);
        }// end of for cycle

        inizio = System.currentTimeMillis();
        listaDifferenza = LibWiki.delta(listaUno, listaDue);
        fine = System.currentTimeMillis();
        durata = fine - inizio;

        int a = 87;
    }// end of single test


    @Test
    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     * @return differenza
     */
    public void delta2() {
        long inizio;
        long fine;
        long durata;
        int dim = 300000;
        ArrayList<Long> listaUno = new ArrayList<Long>();
        ArrayList<Long> listaDue = new ArrayList<Long>();
        ArrayList<Long> listaDifferenza;

        for (int k = 0; k < dim; k = k + 2) {
            listaUno.add((long) k);
        }// end of for cycle

        for (int k = 0; k < dim; k = k + 3) {
            listaDue.add((long) k);
        }// end of for cycle

        inizio = System.currentTimeMillis();
        listaDifferenza = LibWiki.delta(listaUno, listaDue);
        fine = System.currentTimeMillis();
        durata = fine - inizio;

        int a = 87;
    }// end of single test

    @Test
    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     * @return differenza
     */
    public void delta3() {
        long inizio;
        long fine;
        long durata;
        int dim = 300000;
        ArrayList<Long> listaUno = new ArrayList<Long>();
        ArrayList<Long> listaDue = new ArrayList<Long>();
        ArrayList<Long> listaDifferenza;

        for (int k = 0; k < dim; k = k + 2) {
            listaUno.add((long) k);
        }// end of for cycle

        for (int k = 0; k < dim; k = k + 3) {
            listaDue.add((long) k);
        }// end of for cycle

        inizio = System.currentTimeMillis();

        List set_a;
        List set_b;

        ArrayList<Long> listaTmp = (ArrayList<Long>) listaUno.clone();
        listaTmp.retainAll(listaDue);
        listaUno.removeAll(listaTmp);

//        temp_set.retainAll(set_b); //it finds out the common elements between two sets.
//        set_a.removeAll(temp_set); //now the final desired set is ready

//        listaDifferenza = LibWiki.delta(listaUno, listaDue);
        fine = System.currentTimeMillis();
        durata = fine - inizio;

        int a = 87;
    }// end of single test

    /**
     * Titoli della tabella di sintesi
     */
    private ArrayList<String> getCaption() {
        ArrayList<String> titoli = new ArrayList<String>();
        String statistiche = "Statistiche";
        Date oldDate = LibTime.adesso();
        String vecchiaData = LibTime.getGioMeseAnnoLungo(oldDate);
        String nuovaData = LibTime.getGioMeseAnnoLungo(new Date());
        String differenze = "&nbsp;&nbsp;&nbsp;&nbsp;diff.";

        statistiche = SPAZIO + statistiche + SPAZIO;
        vecchiaData = SPAZIO + vecchiaData + SPAZIO;
        nuovaData = SPAZIO + nuovaData + SPAZIO;
        differenze = SPAZIO + differenze + SPAZIO;

        titoli.add(statistiche);
        titoli.add(vecchiaData);
        titoli.add(nuovaData);
        titoli.add(differenze);

        return titoli;
    }// fine del metodo

    /**
     * Riga col numero di voci
     */
    private ArrayList getRigaVoci() {
        String prefCode = "Non usato in questo test";
        String descrizione = ":Categoria:BioBot|Template bio";
        int oldValue = 250000;
        int newValue = 299999;
        String nota = "Una differenza di '''poche unità''' tra le pagine della categoria e le voci gestite dal bot è '''fisiologica''' e dovuta ad imprecisioni nella redazione del tmpl Bio";

        return getRigaBase(true, descrizione, nota, oldValue, newValue, prefCode);
    }// fine del metodo

    /**
     * Riga base
     *
     * @param wikiLink      collegamento a pagina wiki con doppie quadre
     * @param descrizione   della riga
     * @param eventualeNota a piè pagina
     * @param oldValue      stringa del valore precedente
     * @param newValue      stringa del valore attuale
     * @param prefCode      codice della preferenza da aggiornare
     */
    private ArrayList getRigaBase(boolean wikiLink, String descrizione, String eventualeNota, int oldValue, int newValue, String prefCode) {
        ArrayList riga = new ArrayList();
        int diff = newValue - oldValue;

        if (wikiLink) {
            descrizione = LibWiki.setQuadre(descrizione);
        }// end of if cycle

        descrizione = LibWiki.setBold(descrizione);
        if (!eventualeNota.equals("")) {
            eventualeNota = SPAZIO + "<ref>" + eventualeNota + "</ref>";
            descrizione += eventualeNota;
        }// end of if cycle

        riga.add(descrizione);
        riga.add(oldValue);
        riga.add(newValue);
        if (diff != 0) {
            riga.add(diff);
        } else {
            riga.add(SPAZIO);
        }// end of if/else cycle

        return riga;
    }// fine del metodo

    /**
     * Riga col numero di giorni
     */
    private ArrayList getRigaGiorni() {
        String prefCode = "Non usato in questo test";
        String descrizione = "Giorni interessati";
        int oldValue = 350;
        int newValue = 350;
        String nota = "Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]";

        return getRigaBase(false, descrizione, nota, oldValue, newValue, prefCode);
    }// fine del metodo

    /**
     * Riga col numero di anni
     */
    private ArrayList getRigaAnni() {
        String prefCode = "Non usato in questo test";
        int anniPreCristo = 1000;
        String descrizione = "Anni interessati";
        int oldValue = 3000;
        int newValue = 2016 + anniPreCristo;
        String nota = "Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]]";

        return getRigaBase(false, descrizione, nota, oldValue, newValue, prefCode);
    }// fine del metodo

    @Test
    /**
     * Crea una table di classe wikitable
     * <p>
     * default:
     * width=50%
     * align=center
     * text-align=right
     * font-size=100%
     * background:#FFF
     * bgcolor="#EFEFEF"
     * <p>
     * Facoltativi:
     * Cost.KEY_MAPPA_LISTA_SORTABLE
     * Cost.KEY_MAPPA_CAPTION
     * Cost.KEY_MAPPA_TITOLI_SCURI
     *
     * @param mappa con i vari parametri e valori
     * @return testo
     */
    public void creaTable() {
        HashMap mappa = new HashMap();
        String testoPagina = "";
        super.setLogin();
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<ArrayList> righe = new ArrayList<>();
        ArrayList<Boolean> colonne = new ArrayList<>();
        ArrayList lista;
        testoPagina += LibWiki.creaTable(mappa);
        assertEquals(testoPagina, "");

        testoPagina += "Tavola base. Solo i titoli sono obbligatori. Default numeri progressivi a sinistra.";
        mappa.put(Cost.KEY_MAPPA_TITOLI, "Solo,Titoli,Stringa");
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        assertTrue("Manca il testo della table", testoPagina.length() > 0);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Tavola base. Titolo sotto forma di array.";
        titoli.add("Altri");
        titoli.add("Titoli");
        titoli.add("Come");
        titoli.add("Array");
        mappa.put(Cost.KEY_MAPPA_TITOLI, titoli);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Tavola normale di default con alcune righe.";
        lista = new ArrayList();
        lista.add("Prima");
        lista.add(27);
        righe.add(lista);
        lista = new ArrayList();
        lista.add("Seconda");
        lista.add(51);
        righe.add(lista);
        lista = new ArrayList();
        lista.add("Terza");
        lista.add(1250);
        righe.add(lista);
        mappa.put(Cost.KEY_MAPPA_TITOLI, "Testo,Numeri");
        mappa.put(Cost.KEY_MAPPA_RIGHE_LISTA, righe);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Tavola senza i progressivi.";
        mappa.put(Cost.KEY_MAPPA_NUMERAZIONE_PROGRESSIVA, false);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Tavola tutta sortable.";
        mappa.put(Cost.KEY_MAPPA_NUMERAZIONE_PROGRESSIVA, true);
        mappa.put(Cost.KEY_MAPPA_SORTABLE_BOOLEAN, true);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Tavola mista.";
        mappa = new HashMap();
        righe = new ArrayList<>();
        lista = new ArrayList();
        mappa.put(Cost.KEY_MAPPA_TITOLI, "TestoFix,TestoSort,NumSort,NumFix");
        lista.add("Primo");
        lista.add("Beta");
        lista.add(825);
        lista.add(34820);
        righe.add(lista);
        lista = new ArrayList();
        lista.add("Seconda");
        lista.add("Gamma");
        lista.add(37);
        lista.add(51);
        righe.add(lista);
        lista = new ArrayList();
        lista.add("Terza");
        lista.add("Alfa");
        lista.add(231000);
        lista.add(1250);
        righe.add(lista);
        mappa.put(Cost.KEY_MAPPA_RIGHE_LISTA, righe);
        colonne.add(false);
        colonne.add(true);
        colonne.add(true);
        colonne.add(false);
        mappa.put(Cost.KEY_MAPPA_SORTABLE_LISTA, colonne);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

        testoPagina += "Statistiche.";
        mappa = new HashMap();
        righe = new ArrayList<>();
        lista = new ArrayList();
        lista.add(getRigaVoci());
        lista.add(getRigaGiorni());
        lista.add(getRigaAnni());

        mappa.put(Cost.KEY_MAPPA_TITOLI, getCaption());
        mappa.put(Cost.KEY_MAPPA_RIGHE_LISTA, lista);
//        mappa.put(Cost.KEY_MAPPA_SORTABLE_BOOLEAN, false);
        testoPagina += A_CAPO;
        testoPagina += LibWiki.creaTable(mappa);
        new RequestWikiWrite(PAGINA_PROVA, testoPagina, "", wikiLogin);

    }// end of single test


    @Test
    public void modificaLink() {
        sorgente = "George Abbey";
        modificato = "George Abbey (calciatore)";

        previsto = "George Abbey (calciatore)|George Abbey";
        ottenuto = LibWiki.modificaLink(modificato);
        assertEquals(previsto,ottenuto);


        contenuto = "Scritto completo dove George Abbey compare diverse volte";
        previsto = "Scritto completo dove George Abbey compare diverse volte";
        ottenuto = LibWiki.modificaLink(contenuto, sorgente, modificato);
        assertEquals(previsto,ottenuto);

        contenuto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey]] oppure Abbey ma anche";
        previsto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey (calciatore)|George Abbey]] oppure Abbey ma anche";
        ottenuto = LibWiki.modificaLink(contenuto, sorgente, modificato);
        assertEquals(previsto,ottenuto);


        contenuto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey]] oppure Abbey ma anche";
        contenuto += " sotto forma di [[George Abbey|George Abbey]]";
        previsto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey (calciatore)|George Abbey]] oppure Abbey ma anche";
        previsto += " sotto forma di [[George Abbey (calciatore)|George Abbey]]";
        ottenuto = LibWiki.modificaLink(contenuto, sorgente, modificato);
        assertEquals(previsto,ottenuto);


        contenuto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey]] oppure Abbey ma anche";
        contenuto += " sotto forma di [[George Abbey|George Abbey]] o di [[George Abbey (pittore)]], oltre che di ";
        contenuto += " [[George Abbey (scultore)]] ovvero [[George Abbey (cantante)|George Abbey]] e anche [[George Abbey|Abbey]]";

        previsto = "Scritto completo dove George Abbey compare diverse volte, anche come [[George Abbey (calciatore)|George Abbey]] oppure Abbey ma anche";
        previsto += " sotto forma di [[George Abbey (calciatore)|George Abbey]] o di [[George Abbey (pittore)]], oltre che di ";
        previsto += " [[George Abbey (scultore)]] ovvero [[George Abbey (cantante)|George Abbey]] e anche [[George Abbey (calciatore)|Abbey]]";

        ottenuto = LibWiki.modificaLink(contenuto, sorgente, modificato);
        assertEquals(previsto,ottenuto);

    }// end of method

}// end of testing class