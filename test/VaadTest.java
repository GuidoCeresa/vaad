import it.algos.vaad.VaadApp;
import it.algos.vaad.wiki.Page;
import it.algos.vaad.wiki.PagePar;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.vaad.wiki.WikiLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gac on 09 ago 2015.
 * .
 */
public abstract class VaadTest {

    protected final static String A_CAPO = "\n";
    protected final static String SPAZIO = " ";
    // alcuni valori da soddisfare
    protected static int PARAMETRI_LETTI_DAL_SERVER = 18;
    protected static int PARAMETRI_PER_DATABASE = 16;
    // alcuni valori di prova
    protected static String TITOLO = "Nicola Conte (ufficiale)";
    protected static String TITOLO_2 = "Credito Sammarinese";
    protected static String TITOLO_3 = "Utente:Gac/Sandbox5";
    protected static String TITOLO_ERRATO = "Nicola Conte (xxx)";
    protected static String TITOLO_TMPL = "Fallout Shelter";
    protected static String TITOLO_4 = "Piozzano";
    protected static String TITOLO8 = "Utente:Gac/Sandbox8";
    protected static String TITOLO9 = "Utente:Gac/Sandbox9";
    protected static String TITOLO_ALTRO = "Utente:Gac/Sandbox4";
    protected static String TITOLO_BLOCCATO = "Utente:Rompiballe11";
    protected static String TITOLO_BACK = "Lago di Ha! Ha!";
    protected static String TITOLO_MODULO_ATTIVITA = "Modulo:Bio/Plurale_attività";
    protected static String TITOLO_MODULO_PROFESSIONE = "Modulo:Bio/Link attività";
    protected static String TITOLO_MODULO_GENERE = "Modulo:Bio/Plurale attività genere";
    protected static String SUMMARY = "Sola scrittura";
    protected static int PAGEID = 698528;
    protected static int PAGEID_UTF8 = 2286987;
    protected static int PAGEID_ERRATO = 289455234;
    protected static String PAGEID_COME_STRINGA = "698528";
    protected static String TITOLO_WEB = "http://www.algos.it/estudio";
    //    protected static String TITOLO_WEB_ERRATO = "http://www.treprovincex.it/";
    protected static String TITOLO_WEB_ERRATO = "http://www.pippozbelloz.it/";
    protected static String CONTENUTO_WEB = "<title>Estudio mobile office</title>";
    protected static String TAG_ERRORE = "UnknownHostException";
    protected static String TITOLO_CAT_BREVE = "Eventi del 1902";
    protected static String TITOLO_CAT_ERRATA = "Eventi del 2902";
    protected static String TITOLO_CAT_MEDIA = "Nati nel 1420";
    protected static String TITOLO_CAT_LUNGA = "Cantanti statunitensi";
    protected static String TITOLO_CAT_LUNGHISSIMA = "BioBot";
    protected static String TITOLO_CAT_VUOTA = "Cancellare subito"; // a volte
    protected static String TAG_INI_PAGINA = "{\"batchcomplete\":true,\"query\":";
    protected static String TAG_END_PAGINA = "\"}]}]}}";
    protected static String TAG_INI_VOCE = "{{F|militari italiani|luglio 2013}}";
    protected static String TAG_END_VOCE = "[[Categoria:Italo-libici|Conte, Nicola]]";
    protected static String TAG_INI_TMPL = "{{Videogioco\n|nomegioco";
    protected static String TAG_END_TMPL = "}}";
    protected static String TAG_INI_TMPL_BIO = "{{Bio\n|Nome";
    protected static String TAG_END_TMPL_BIO = "}}";
    protected static String VUOTA = "";
    // numero massimo di elementi restituiti dalle API
    //--500 utente normale
    //--5.000 bot
    protected int limits;
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
    protected String modificato = "";
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
    protected ArrayList<Long> listaLong = null;
    protected ArrayList<String> listaString = null;
    protected WikiLogin wikiLogin = null;


    // Login logic here
    protected void setLogin() {
        String nick = "gacbot";
        String password = "fulvia";
        nick = "Gacbot@Gacbot";
        password = "tftgv0vhl16c0qnmfdqide3jqdp1i5m7";

        if (wikiLogin == null) {
            wikiLogin = new WikiLogin(nick, password);
            VaadApp.WIKI_LOGIN = wikiLogin;
        }// end of if cycle
    } // fine del metodo iniziale

}// end of abstract testing class