package it.algos.vaad.wiki;

/**
 * Created by gac on 26 giu 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 * .
 * .
 */
public abstract class Cost {


    // campi di una mappa per la table
    public static final String KEY_MAPPA_TITOLI = "mappaTableListaTitoli";
    public static final String KEY_MAPPA_LISTA = "mappaTableListaRighe";
    public static final String KEY_MAPPA_SORTABLE = "mappaTableSortable";
    public static final String KEY_MAPPA_CAPTION = "mappaTableCaption";
    public static final String KEY_MAPPA_TITOLI_SCURI = "mappaTableTitoliScuri";

    /* Formato dati selezionato per la risposta alla Request */
    public static WikiFormat FORMAT = WikiFormat.json;
    /* codifica dei caratteri */
    public static String ENC = "UTF-8";
    /* prefisso URL base */
    public static String API_HTTP = "https://";
    /* prefisso iniziale (prima del progetto) */
    public static String API_WIKI = ".";
    /* azione API generica */
    public static String API_PHP = ".org/w/api.php?";
    /* azione API generica */
    public static String API_ACTION = ".org/w/api.php?action=";
    /* azione API login */
    public static String API_LOGIN = "login";
    /* suffisso per il formato della risposta */
    public static String API_FORMAT = "&format=" + FORMAT.toString() + "&formatversion=2";
    /* azione API delle query */
    public static String API_QUERY = API_ACTION + "query";
    /* prefisso URL per leggere la pagina in modifica in formato txt anzichè html */
    public static String WIKI_PRE = ".org/w/index.php?title=";
    /* suffisso URL per leggere la pagina in modifica in formato txt anzichè html */
    public static String WIKI_POST = "&action=edit";
    /* parametro API query normale */
    public static String CONTENT = "&prop=revisions&rvprop=content";
    /* parametro API query estesa */
    // ids: Get both of these IDs: revid, parentid
    // flags: Whether the revision was a minor edit
    public static String CONTENT_ALL = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size";
    /* parametro API */
    public static String TITLES = "&titles=";
    /* parametro API */
    public static String PAGEIDS = "&pageids=";
    /* parametro API */
    public static String QUERY = "query";
    /* parametro API */
    public static String TOKEN = "&meta=tokens";
    public static String NON_TROVATA = "Pagina non trovata";
    public static String SUCCESSO = "Success";
    public static String TAG_BIO = "Bio";

}// end of static class
