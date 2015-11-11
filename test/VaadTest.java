import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gac on 09 ago 2015.
 * .
 */
public abstract class VaadTest {

    // alcuni valori da soddisfare
    protected static int PARAMETRI_LETTI_DAL_SERVER = 18;
    protected static int PARAMETRI_PER_DATABASE = 16;


    // alcuni valori di prova
    protected static String TITOLO = "Nicola Conte (ufficiale)";
    protected static String TITOLO_2 = "Credito Sammarinese";
    protected static String TITOLO_3 = "Utente:Gac/Sandbox5";
    protected static String TITOLO_ERRATO = "Nicola Conte (xxx)";
    protected static String TITOLO_TMPL = "Fallout Shelter";


    protected static int PAGEID = 698528;
    protected static int PAGEID_UTF8 = 2286987;
    protected static int PAGEID_ERRATO = 289455234;
    protected static String PAGEID_COME_STRINGA = "698528";

    protected static String TITOLO_WEB = "http://www.quattroprovince.it/";
    //    protected static String TITOLO_WEB_ERRATO = "http://www.treprovincex.it/";
    protected static String TITOLO_WEB_ERRATO = "http://www.pippozbelloz.it/";
    protected static String CONTENUTO_WEB = "http://www.danielapossenti.it/quattroprovince/";
    protected static String TAG_ERRORE = "UnknownHostException";

    protected static String TITOLO_CAT_BREVE = "Eventi del 1902";
    protected static String TITOLO_CAT_ERRATA = "Eventi del 2902";
    protected static String TITOLO_CAT_MEDIA = "Nati nel 1420";
    protected static String TITOLO_CAT_LUNGA = "Cantanti statunitensi";
    protected static String TITOLO_CAT_LUNGHISSIMA = "BioBot";

    // numero massimo di elementi restituiti dalle API
    //--500 utente normale
    //--5.000 bot
    protected int limits;

    protected static String TAG_INI_PAGINA = "{\"batchcomplete\":true,\"query\":";
    protected static String TAG_END_PAGINA = "\"}]}]}}";
    protected static String TAG_INI_VOCE = "{{F|militari italiani|luglio 2013}}";
    protected static String TAG_END_VOCE = "[[Categoria:Italo-libici|Conte, Nicola]]";
    protected static String TAG_INI_TMPL = "{{Videogioco\n|nomegioco";
    protected static String TAG_END_TMPL = "}}";
    protected static String TAG_INI_TMPL_BIO = "{{Bio\n|Nome";
    protected static String TAG_END_TMPL_BIO = "}}";
    protected static String VUOTA = "";

    // alcuni parametri utilizzati
    protected List<PagePar> listaPrevista = null;
    protected List<PagePar> listaOttenuta = null;
    protected long numPrevisto = 0;
    protected long numOttenuto = 0;
    protected PagePar parPrevisto = null;
    protected PagePar parOttenuto = null;
    protected PagePar.TypeField fieldPrevisto = null;
    protected PagePar.TypeField fieldOttenuto = null;
    protected HashMap<String, Object> mappa = null;
    protected boolean boolPrevisto = false;
    protected boolean boolOttenuto = false;
    protected String sorgente = "";
    protected String previsto = "";
    protected String ottenuto = "";
    protected String tag = "";
    protected HashMap mappaTxt;
    protected HashMap mappaObj;
    protected HashMap mappaDB;
    protected String contenuto;
    protected Page page = null;
    protected Page pagePrevista = null;
    protected Page pageOttenuta = null;
    protected TipoRisultato risultatoPrevisto = null;
    protected TipoRisultato risultatoOttenuto = null;

}// end of abstract class