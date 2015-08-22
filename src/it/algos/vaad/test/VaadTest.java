import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by gac on 09 ago 2015.
 * .
 */
public abstract class VaadTest {

    // alcuni valori di prova
    protected static String TITOLO = "Nicola Conte (ufficiale)";
    protected static String TITOLO_ERRATO = "Nicola Conte (xxx)";
    protected static String TITOLO_TMPL = "Fallout Shelter";
    protected static int PAGEID = 698528;
    protected static int PAGEID_UTF8 = 2286987;
    protected static int PAGEID_ERRATO = 289455234;
    protected static String TITOLO_WEB = "http://www.quattroprovince.it/";
    //    protected static String TITOLO_WEB_ERRATO = "http://www.treprovincex.it/";
    protected static String TITOLO_WEB_ERRATO = "http://www.pippozbelloz.it/";
    protected static String CONTENUTO_WEB = "http://www.danielapossenti.it/quattroprovince/";
    protected static String TAG_ERRORE = "UnknownHostException";
    protected static String TAG_INI_PAGINA = "{\"batchcomplete\":true,\"query\":";
    protected static String TAG_END_PAGINA = "\"}]}]}}";
    protected static String TAG_INI_VOCE = "{{F|militari italiani|luglio 2013}}";
    protected static String TAG_END_VOCE = "[[Categoria:Italo-libici|Conte, Nicola]]";
    protected static String TAG_INI_TMPL = "{{Videogioco\n|nomegioco";
    protected static String TAG_END_TMPL = "}}";
    protected static String TAG_INI_TMPL_BIO = "{{Bio\n|Nome";
    protected static String TAG_END_TMPL_BIO = "}}";

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
    protected String contenuto;
    protected Page page = null;
    protected Page pagePrevista = null;
    protected Page pageOttenuta = null;

}// end of abstract class