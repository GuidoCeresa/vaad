package it.algos.vaad.wiki;

/**
 * Created by gac on 04 ago 2015.
 * .
 */

import it.algos.vaad.WrapTime;
import it.algos.vaad.wiki.entities.wiki.Wiki;
import it.algos.vaad.wiki.query.QueryCat;
import it.algos.vaad.wiki.query.QueryReadTitle;
import it.algos.webbase.web.lib.LibText;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Libreria
 */
public abstract class LibWiki {

    //--preferenza
    public static final String DEBUG = "debug";
    public static final String NUM_RECORDS_INDEX_BIO = "numRecordsIndexBio";
    public static final String USA_FLASH_TRUE_DOWNLOAD = "usaFlashTrueDownload";
    public static final String SEND_MAIL_ERROR = "sendMailError";
    public static final String SEND_MAIL_WARN = "sendMailWarn";
    public static final String SEND_MAIL_INFO = "sendMailInfo";
    public static final String LOG_ERROR = "logError";
    public static final String LOG_WARN = "logWarn";
    public static final String LOG_INFO = "logInfo";
    public static final String NEW_BIO = "NewBio";
    public static final String REF = "<ref>";
    public static final String NOTE = "<!--";
    public static final String QUADRA_INI = "[";
    public static final String QUADRE_INI = QUADRA_INI + QUADRA_INI;
    public static final String QUADRA_END = "]";
    public static final String QUADRE_END = QUADRA_END + QUADRA_END;
    public static final String GRAFFA_INI = "{";
    public static final String GRAFFE_INI = GRAFFA_INI + GRAFFA_INI;
    public static final String GRAFFA_END = "}";
    public static final String GRAFFE_END = GRAFFA_END + GRAFFA_END;
    public static final String BOLD = "'''";
    public static final String BIO = Cost.TAG_BIO;

    // tag per la stringa vuota

    public static final String VUOTA = "";

    // tag per il valore falso per una posizione
    public static final int INT_NULLO = -1;

    // key to store objects in a HashMap
    public static final String KEY_PAGINE_VALIDE = "pagineValide";
    public static final String KEY_PAGINE_MANCANTI = "pagineMancanti";
    public static final String TOKEN = "csrftoken";
    private static final String BATCH = "batchcomplete";
    private static final String QUERY = "query";
    private static final String TITLE = "title";
    private static final String PAGEID = "pageid";
    private static final String PAGES = "pages";
    private static final String REVISIONS = "revisions";
    private static final String TOKENS = "tokens";
    private static final String WARNINGS = "warnings";
    private static final String TIMESTAMP = "timestamp";
    private static final String CATEGORY_MEMBERS = "categorymembers";
    private static final String BACK_LINKS = "backlinks";
    private static final String QUERY_CONTINUE = "query-continue";
    private static final String LOGIN = "login";
    private static final String MISSING = "missing";
    private static final String VIR = ",";
    private static final String APICI = "\"";
    private static final String PUNTI = ":";
    public static Date DATA_NULLA = new Date(70, 0, 1);
    /* caratteri finali per la stringa di edittoken da rinviare al server */
    public static String END_TOKEN = "%2B%5C";
    // patch per la key del parametro testo
    private static String PATCH_OLD = "*";
    private static String PATCH_NEW = "text";

//    /**
//     * Converte il valore stringa nel tipo previsto dal parametro PagePar
//     *
//     * @param mappa standard (valori String) in ingresso
//     * @return mappa typizzata secondo PagePar
//     */
//    private static fixValueMap(String key, String valueTxt) {
//        def valueObj = null
//        PagePar.TypeField typo = PagePar.getParField(key)
//
//        switch (typo) {
//            case PagePar.TypeField.string:
//                valueObj = valueTxt
//                break;
//            case PagePar.TypeField.longzero:     //--conversione degli interi
//            case PagePar.TypeField.integernotzero:  //--conversione degli interi
//                try { // prova ad eseguire il codice
//                    valueObj = Integer.decode(valueTxt)
//                } catch (Exception unErrore) { // intercetta l'errore
//                    unErrore = null
//                    valueObj = 0
//                }// fine del blocco try-catch


//                break;
//            case PagePar.TypeField.date:            //--conversione delle date
//                try { // prova ad eseguire il codice
//                    valueObj = convertTxtData(valueTxt)
//                } catch (Exception unErrore) { // intercetta l'errore
//                    unErrore = null
//                    valueObj = DATA_NULLA
//                }// fine del blocco try-catch
//                break;
//            default: // caso non definito
//                break;
//        } // fine del blocco switch
//
//        return valueObj
//    } // fine del metodo

    /**
     * Restituisce il numero di occorrenze di un tag nel testo.
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     *
     * @param testo da spazzolare
     * @param tag   da cercare
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public static int getNumTag(String testo, String tag) {
        int numTag = 0;
        int pos;

        // controllo di congruità
        if (testo != null && tag != null) {
            if (testo.contains(tag)) {
                pos = testo.indexOf(tag);
                while (pos != -1) {
                    pos = testo.indexOf(tag, pos + tag.length());
                    numTag++;
                }// fine di while
            } else {
                numTag = 0;
            }// fine del blocco if-else
        }// fine del blocco if

        return numTag;
    } // fine del metodo

    /**
     * Restituisce il numero di occorrenze di una coppia di graffe iniziali nel testo.
     *
     * @param testo da spazzolare
     * @return numero di occorrenze
     * zero se non ce ne sono
     */
    public static int getNumGraffeIni(String testo) {
        return getNumTag(testo, GRAFFE_INI);
    } // fine del metodo

    /**
     * Restituisce il numero di occorrenze di una coppia di graffe finali nel testo.
     *
     * @param testo da spazzolare
     * @return numero di occorrenze
     * zero se non ce ne sono
     */
    public static int getNumGraffeEnd(String testo) {
        return getNumTag(testo, GRAFFE_END);
    } // fine del metodo

    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo.
     * Ordine ed annidamento NON considerato
     *
     * @param testo  da spazzolare
     * @param tagIni tag iniziale
     * @param tagEnd tag finale
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public static boolean isPariTag(String testo, String tagIni, String tagEnd) {
        boolean pari = false;
        int numIni;
        int numEnd;

        // controllo di congruità
        if (testo != null && tagIni != null && tagEnd != null) {
            numIni = getNumTag(testo, tagIni);
            numEnd = getNumTag(testo, tagEnd);
            pari = (numIni == numEnd);
        }// fine del blocco if

        return pari;
    } // fine del metodo

    /**
     * Controlla che le occorrenze delle graffe iniziali e finali si pareggino all'interno del testo.
     * Ordine ed annidamento NON considerato
     *
     * @param testo da spazzolare
     * @return vero se il numero di GRAFFE_INI è uguale al numero di GRAFFE_END
     */
    public static boolean isPariGraffe(String testo) {
        return isPariTag(testo, GRAFFE_INI, GRAFFE_END);
    } // fine del metodo

    /**
     * Elimina la testa iniziale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la testa, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param testa   da eliminare
     * @return uscita stringa convertita
     */
    public static String levaTesta(String entrata, String testa) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (testa != null) {
                testa = testa.trim();
                if (uscita.startsWith(testa)) {
                    uscita = uscita.substring(testa.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    } // fine del metodo

    /**
     * Elimina la coda terminale della stringa, se esiste.
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la coda, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param coda    da eliminare
     * @return uscita stringa convertita
     */
    public static String levaCoda(String entrata, String coda) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (coda != null) {
                coda = coda.trim();
                if (uscita.endsWith(coda)) {
                    uscita = uscita.substring(0, uscita.length() - coda.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    } // fine del metodo

    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param testoIn    in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     * @return testoOut convertito
     */
    public static String sostituisce(String testoIn, String oldStringa, String newStringa) {
        String testoOut = testoIn;
        int pos = 0;
        String prima = VUOTA;

        if (testoIn != null && oldStringa != null && newStringa != null) {
            testoOut = testoIn.trim();
            if (testoIn.contains(oldStringa)) {

                while (pos != INT_NULLO) {
                    pos = testoIn.indexOf(oldStringa);
                    if (pos != INT_NULLO) {
                        prima += testoIn.substring(0, pos);
                        prima += newStringa;
                        pos += oldStringa.length();
                        testoIn = testoIn.substring(pos);
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    } // fine del metodo


    /**
     * Chiude il template
     * <p>
     * Il testo inizia col template, ma prosegue (forse) anche oltre
     * Cerco la prima doppia graffa che abbia all'interno lo stesso numero di aperture e chiusure
     * Spazzola il testo fino a pareggiare le graffe
     * Se non riesce a pareggiare le graffe, ritorna una stringa nulla
     *
     * @param templateIn da spazzolare
     * @return template
     */
    public static String chiudeTmpl(String templateIn) {
        String templateOut;
        int posIni = 0;
        int posEnd = 0;
        boolean pari = false;

        templateOut = templateIn.substring(posIni, posEnd + GRAFFE_END.length()).trim();

        while (!pari) {
            posEnd = templateIn.indexOf(GRAFFE_END, posEnd + GRAFFE_END.length());
            if (posEnd != -1) {
                templateOut = templateIn.substring(posIni, posEnd + GRAFFE_END.length()).trim();
                pari = isPariGraffe(templateOut);
            } else {
                break;
            }// fine del blocco if-else
        } //fine del ciclo while

        if (!pari) {
            templateOut = VUOTA;
        }// fine del blocco if

        return templateOut;
    } // fine del metodo

    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     * <p>
     * Prova anche col tag minuscolo
     */
    public static String estraeTmplCompresi(String testo, String tag) {
        String template = estraeTmplCompresiBase(testo, tag);

        // forza il primo carattere a maiuscolo
        if (template.equals("")) {
            template = estraeTmplCompresiBase(testo, LibText.primaMaiuscola(tag));
        }// fine del blocco if

        // forza il primo carattere a minuscolo
        if (template.equals("")) {
            template = estraeTmplCompresiBase(testo, LibText.primaMinuscola(tag));
        }// fine del blocco if

        return template;
    } // fine del metodo

    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    private static String estraeTmplCompresiBase(String testo, String tag) {
        String template = VUOTA;
        boolean continua = false;
        String patternTxt = "";
        Pattern patttern = null;
        Matcher matcher = null;
        int posIni;
        int posEnd;
        String tagIniTemplate = VUOTA;

        // controllo di congruita
        if (testo != null && tag != null) {
            // Create a Pattern text
            patternTxt = "\\{\\{(Template:)?" + tag;

            // Create a Pattern object
            patttern = Pattern.compile(patternTxt);

            // Now create matcher object.
            matcher = patttern.matcher(testo);
            if (matcher.find() && matcher.groupCount() > 0) {
                tagIniTemplate = matcher.group(0);
            }// fine del blocco if-else

            // controlla se esiste una doppia graffa di chiusura
            // non si sa mai
            if (!tagIniTemplate.equals("")) {
                posIni = testo.indexOf(tagIniTemplate);
                posEnd = testo.indexOf(GRAFFE_END, posIni);
                template = testo.substring(posIni);
                if (posEnd != -1) {
                    continua = true;
                }// fine del blocco if
            }// fine del blocco if

            // cerco la prima doppia graffa che abbia all'interno
            // lo stesso numero di aperture e chiusure
            // spazzola il testo fino a pareggiare le graffe
            if (continua) {
                template = chiudeTmpl(template);
            }// fine del blocco if
        }// fine del blocco if

        return template;
    }// fine del metodo

    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplEsclusi(String testo, String tag) {
        String template = estraeTmplCompresi(testo, tag);
        template = setNoGraffe(template);
        return template.trim();
    }// fine del metodo

    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova (ce ne dovrebbe essere solo uno)
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplBioCompresi(String testo) {
        return estraeTmplCompresi(testo, BIO);
    }// fine del metodo

    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplBioEsclusi(String testo) {
        return estraeTmplEsclusi(testo, BIO);
    }// fine del metodo


    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> creaMappa(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        boolean batchcomplete = false;
        JSONObject objectQuery = null;
        JSONObject objectToken = null;
        JSONArray arrayPages = null;
        JSONObject objectRev = null;
        JSONArray arrayRev = null;
        String token = "";

        //--recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //--recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        if (objectAll.get(BATCH) != null && objectAll.get(BATCH) instanceof Boolean) {
            batchcomplete = (Boolean) objectAll.get(BATCH);
        }// fine del blocco if

        //--recupera i valori dei parametri pages
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPages = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
            if (objectQuery.get(TOKENS) != null && objectQuery.get(TOKENS) instanceof JSONObject) {
                objectToken = (JSONObject) objectQuery.get(TOKENS);
                if (objectToken.get(TOKEN) != null && objectToken.get(TOKEN) instanceof String) {
                    token = (String) objectToken.get(TOKEN);
                }// end of if cycle
            }// fine del blocco if
        }// fine del blocco if

        //--recupera i valori dei parametri revisions
        if (arrayPages != null && arrayPages.get(0) != null && arrayPages.get(0) instanceof JSONObject) {
            objectRev = (JSONObject) arrayPages.get(0);
            if (objectRev != null) {
                arrayRev = (JSONArray) objectRev.get(REVISIONS);
            }// fine del blocco if
        }// fine del blocco if

        //--crea la mappa
        if (arrayPages != null && arrayRev != null) {
            mappa = mixJSON(batchcomplete, arrayPages, arrayRev, token);
        }// fine del blocco if

        return patchMappa(mappa);
    } // fine del metodo

    /**
     * Crea una mappa login (valori String) dal testo JSON di una pagina di login
     *
     * @param textJSON in ingresso
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> creaMappaLogin(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        JSONObject objectQuery = null;

        // recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //recupera i valori dei parametri base (3) ed info (1)
        if (objectAll.get(LOGIN) != null && objectAll.get(LOGIN) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(LOGIN);
            mappa = new HashMap<String, Object>();

            for (Object key : objectQuery.keySet()) {
                mappa.put((String) key, objectQuery.get(key));
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa per leggere solo i timestamps dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return mappa con due array: timestamps validi e timestamps non validi
     */
    public static HashMap<String, ArrayList<WrapTime>> creaArrayWrapTime(String textJSON) {
        HashMap<String, ArrayList<WrapTime>> mappa;
        ArrayList<WrapTime> listaPagineValide = null;
        ArrayList<WrapTime> listaPagineMancanti = null;
        WrapTime wrap;
        JSONObject objectAll;
        boolean batchcomplete;
        JSONObject objectQuery;
        JSONArray arrayPages = null;
        JSONObject singlePage;
        long pageid;
        JSONArray singleRev;
        JSONObject timeObj;
        String timeStr;

        // recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        if (objectAll.get(BATCH) != null && objectAll.get(BATCH) instanceof Boolean) {
            batchcomplete = (Boolean) objectAll.get(BATCH);
        }// fine del blocco if

        //recupera i valori dei parametri
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPages = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
        }// fine del blocco if

        // crea la mappa
        if (arrayPages != null && arrayPages.get(0) != null && arrayPages.get(0) instanceof JSONObject) {
            listaPagineValide = new ArrayList<WrapTime>();
            listaPagineMancanti = new ArrayList<WrapTime>();
            for (Object obj : arrayPages) {
                if (obj instanceof JSONObject) {
                    singlePage = (JSONObject) obj;
                    pageid = (long) singlePage.get(PAGEID);

                    if (singlePage.get(MISSING) == null) {
                        singleRev = (JSONArray) singlePage.get(REVISIONS);
                        timeObj = (JSONObject) singleRev.get(0);
                        timeStr = (String) timeObj.get(TIMESTAMP);
                        wrap = new WrapTime(pageid, timeStr, true);
                        listaPagineValide.add(wrap);
                    } else {
                        wrap = new WrapTime(pageid, null, false);
                        listaPagineMancanti.add(wrap);
                    }// end of if/else cycle

                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        mappa = new HashMap<String, ArrayList<WrapTime>>();
        mappa.put(KEY_PAGINE_VALIDE, listaPagineValide);
        mappa.put(KEY_PAGINE_MANCANTI, listaPagineMancanti);
        return mappa;
    } // fine del metodo

    /**
     * Correzioni/aggiunte per eventuali patch
     * Il parametro ''anon'' è presente nel ritorno della Request SOLO se l'ultimo utente è un IP
     *
     * @param mappa in ingresso
     * @return mappa in uscita
     */
    public static HashMap<String, Object> patchMappa(HashMap<String, Object> mappa) {
        if (!mappa.containsKey(PagePar.anon.toString())) {
            mappa.put(PagePar.anon.toString(), false);
        }// fine del blocco if

        return mappa;
    } // fine del metodo

    /**
     * Crea una mappa standard (valori String) dalle mappe JSON parziali
     *
     * @param batchcomplete flag di controllo
     * @param arrayPages    parametri base (3) ed info (1)
     * @param arrayRev      parametri revisions (12)
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> mixJSON(boolean batchcomplete, JSONArray arrayPages, JSONArray arrayRev, String token) {
        HashMap<String, Object> mappa = new HashMap<String, Object>();
        HashMap<String, Object> mappaPages = null;
        HashMap<String, Object> mappaRev = null;
        Object value;

        //--recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        mappa.put(BATCH, batchcomplete);

        //--recupera i valori dei parametri info
        mappaPages = estraeMappaJsonPar(arrayPages);
        for (String key : mappaPages.keySet()) {
            value = mappaPages.get(key);
            mappa.put(key, value);
        } // fine del ciclo for-each

        //--recupera i valori dei parametri revisions
        mappaRev = estraeMappaJsonPar(arrayRev);
        for (String key : mappaRev.keySet()) {
            value = mappaRev.get(key);
            mappa.put(key, value);
        } // fine del ciclo for-each

        //--recupera i valori dei parametro di controllo token
        if (token != null && !token.equals("")) {
            token = fixToken(token);
            mappa.put(TOKEN, token);
        }// end of if cycle

        return mappa;
    } // fine del metodo


    private static String fixToken(String edittokenIn) {
        String edittokenOut = edittokenIn;

        if (edittokenIn != null && !edittokenIn.equals("")) {
            edittokenOut = edittokenIn.substring(0, edittokenIn.length() - 2);
            edittokenOut += END_TOKEN;
        }// fine del blocco if

        return edittokenOut;
    } // fine del metodo

    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return mappa standard (valori String)
     * @deprecated
     */
    public static LinkedHashMap creaMappaOld(String textJSON) {
        LinkedHashMap mappa = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        Object pagesObj = queryObj.get(PAGES);

        if (pagesObj instanceof JSONArray) {
            mappa = fixMappa((JSONArray) pagesObj);
        }// fine del blocco if

//        if (pagesObj instanceof JSONArray) {
//            mappa = estraeMappaJson((JSONArray) pagesObj);
//        }// fine del blocco if

        return mappa;
    } // fine del metodo

    /**
     * Crea una mappa standard (valori String) da una mappa JSON di una pagina
     * <p>
     * Prima i parametri delle info
     * Poi, se ci sono, i parametri della revisione
     *
     * @param mappaJson JSONObject in ingresso
     * @return mappa standard (valori String)
     * @deprecated
     */
    private static LinkedHashMap fixMappa(JSONArray mappaJson) {
        LinkedHashMap<String, Object> mappaOut = new LinkedHashMap<String, Object>();
        HashMap<String, Object> mappaValoriInfo = estraeMappaJson(mappaJson);
        HashMap<String, Object> mappaValoriRev = null;
        String key;
        Object value = null;

        //--valori dei parametri ricavati dalle info della pagina
        for (PagePar par : PagePar.getInf()) {
            key = par.toString();
            value = mappaValoriInfo.get(key);
            if (value != null) {
                mappaOut.put(key, value);
            }// fine del blocco if
        } // fine del ciclo for-each

        //--controlla che esistano i valori della revisione
        if (mappaValoriInfo.containsKey(REVISIONS) && mappaValoriInfo.get(REVISIONS) instanceof JSONArray) {
            mappaValoriRev = estraeMappaJson((JSONArray) mappaValoriInfo.get(REVISIONS));
        }// fine del blocco if

        //--valori dei parametri ricavati dall'ultima revisione della pagina
        if (mappaValoriRev != null) {
            for (String keyRev : mappaValoriRev.keySet()) {
                value = mappaValoriRev.get(keyRev);
                mappaOut.put(keyRev, value);
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Estrae una mappa standard da un JSONObject
     *
     * @param mappaJson JSONObject in ingresso
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJson(JSONObject mappaJson) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        Set setJson = mappaJson.keySet();
        String key;
        Object value = null;
        JSONObject mappaGrezza = null;

        if (setJson.size() == 1) {
            for (Object obj : setJson) {
                if (obj instanceof String) {
                    mappaGrezza = (JSONObject) mappaJson.get(obj);
                }// fine del blocco if
            } // fine del ciclo for-each
        } else {
            mappaGrezza = mappaJson;
        }// fine del blocco if-else

        if (mappaGrezza != null) {
            for (Object elemento : mappaGrezza.keySet()) {
                if (elemento instanceof String) {
                    key = (String) elemento;
                    value = mappaGrezza.get(key);
                    mappaOut.put(key, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo

    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        JSONObject mappaJSON = null;
        String key;
        Object value;

        if (arrayJson != null && arrayJson.size() == 1) {
            if (arrayJson.get(0) != null && arrayJson.get(0) instanceof JSONObject) {
                mappaJSON = (JSONObject) arrayJson.get(0);
            }// fine del blocco if
        }// fine del blocco if

        if (mappaJSON != null) {
            for (PagePar par : PagePar.getRead()) {
                key = par.toString();
                if (mappaJSON.get(key) != null) {
                    value = mappaJSON.get(key);
                    mappaOut.put(key, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo

    /**
     * Estrae una mappa standard da un JSONArray
     *
     * @param mappaJson JSONArray in ingresso
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJson(JSONArray mappaJson) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();

        if (mappaJson.size() == 1) {
            for (Object obj : mappaJson) {
                if (obj instanceof JSONObject) {
                    mappaOut = estraeMappaJson((JSONObject) obj);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     *
     * @param mappaIn standard (valori String) in ingresso
     * @return mappa typizzata secondo PagePar
     */
    public static HashMap<String, Object> converteMappa(HashMap mappaIn) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        String key = "";
        String valueTxt;
        Object valueObj = null;

        if (mappaIn != null) {
            for (Object obj : mappaIn.keySet()) {
                if (obj instanceof String) {
                    key = (String) obj;
                    valueObj = mappaIn.get(key);
                    valueObj = fixValueMap(key, valueObj);
                    mappaOut.put(key, valueObj);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo

    /**
     * Crea una lista di pagine (valori pageids) dal titolo di una categoria
     */
    public static ArrayList<Long> creaListaCat(String title) {
        ArrayList<Long> lista = null;
        QueryCat query = new QueryCat(title);

        lista = query.getListaPageids();

        return lista;
    }// end of method

    /**
     * Crea una lista di pagine (valori pageids) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return lista pageid (valori Integer)
     */
    public static ArrayList<Long> creaListaCatJson(String textJSON) {
        ArrayList<Long> lista = null;
        JSONObject jsonObject;
        Object longPageid;
        long pageid = 0;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        JSONArray catObj = (JSONArray) queryObj.get(CATEGORY_MEMBERS);

//        if (catObj != null) {
//            lista = new ArrayList<Long>();
//            for (Object obj : catObj) {
//                if (obj instanceof JSONObject) {
//                    jsonObject = (JSONObject) obj;
//                    longPageid = jsonObject.get(PAGEID);
//                    if (longPageid instanceof Long) {
//                        pageid = ((Long) longPageid).intValue();
//                        lista.add(pageid);
//                    }// fine del blocco if
//                }// fine del blocco if
//            } // fine del ciclo for-each
//        }// fine del blocco if

        return creaListaBaseLongJson(catObj);
    } // fine del metodo

    /**
     * Crea una lista di pagine (valori pageids) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return lista pageid (valori Integer)
     */
    public static ArrayList<Long> creaListaBackJson(String textJSON) {
        ArrayList<Long> lista = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject contObj = (JSONObject) allObj.get(QUERY);
        JSONObject batchObj = (JSONObject) allObj.get(QUERY);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        JSONArray backObj = (JSONArray) queryObj.get(BACK_LINKS);

        lista = creaListaBaseLongJson(backObj);
        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return lista pageid (valori Integer)
     */
    public static ArrayList<String> creaListaBackTxtJson(String textJSON) {
        ArrayList<String> lista = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject contObj = (JSONObject) allObj.get(QUERY);
        JSONObject batchObj = (JSONObject) allObj.get(QUERY);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        JSONArray backObj = (JSONArray) queryObj.get(BACK_LINKS);

        lista = creaListaBaseTextJson(backObj);
        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) da un JSONArray
     *
     * @param objArray in ingresso
     * @return lista pageid (valori Integer)
     */
    private static ArrayList<Long> creaListaBaseLongJson(JSONArray objArray) {
        ArrayList<Long> lista = null;
        JSONObject jsonObject = null;
        Object longPageid = null;
        long pageid = 0;

        if (objArray != null && objArray.size() > 0) {
            lista = new ArrayList<Long>();
            for (Object obj : objArray) {
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;
                    longPageid = jsonObject.get(PAGEID);
                    if (longPageid instanceof Long) {
                        pageid = ((Long) longPageid).intValue();
                        lista.add(pageid);
                    }// fine del blocco if
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    } // fine del metodo

    /**
     * Crea una lista di pagine (valori title) da un JSONArray
     *
     * @param objArray in ingresso
     * @return lista pageid (valori Integer)
     */
    private static ArrayList<String> creaListaBaseTextJson(JSONArray objArray) {
        ArrayList<String> lista = null;
        JSONObject jsonObject = null;
        Object stringTitle = null;
        String title = "";

        if (objArray != null && objArray.size() > 0) {
            lista = new ArrayList<String>();
            for (Object obj : objArray) {
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;
                    stringTitle = jsonObject.get(TITLE);
                    if (stringTitle instanceof String) {
                        title = stringTitle.toString();
                        lista.add(title);
                    }// fine del blocco if
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    } // fine del metodo

    /**
     * Controlla se esiste un warnings nella risposta del server
     *
     * @param textJSON in ingresso
     * @return true se il testo in ingresso contiene un warning
     */
    public static boolean isWarnings(String textJSON) {
        boolean status = false;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject warningsObj = (JSONObject) allObj.get(WARNINGS);

        if (warningsObj != null) {
            status = true;
        }// fine del blocco if

        return status;
    } // fine del metodo

    /**
     * Estrae il valore del parametro continue dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     * @return parametro continue
     */
    public static String creaCatContinue(String textJSON) {
        String textContinue = VUOTA;
        JSONObject allObj;
        JSONObject queryObj = null;
        JSONObject catObj = null;

        allObj = (JSONObject) JSONValue.parse(textJSON);
        if (allObj != null) {
            queryObj = (JSONObject) allObj.get(QUERY_CONTINUE);
        }// fine del blocco if
        if (queryObj != null) {
            catObj = (JSONObject) queryObj.get(CATEGORY_MEMBERS);
        }// fine del blocco if
        if (catObj != null && catObj.size() == 1) {
            textContinue = catObj.values().toString();
        }// fine del blocco if

        textContinue = LibText.levaCoda(textContinue, "]");
        textContinue = LibText.levaTesta(textContinue, "[");
        return textContinue;
    } // fine del metodo

//    /**
//     * Estrae il valore del parametro continue dalla mappa
//     *
//     * @param text in ingresso
//     * @return parametro continue
//     */
//    private static String estraeContinue(LazyMap lazyMap) {
//        String valore=VUOTA;
//        String textContinue = VUOTA;
//        Object mappaValori;
//        String sep = "\\|";
//        Object parti;
//
//        mappaValori = lazyMap.values();
//        valore = mappaValori[0];
//        parti = valore.split(sep);
//        textContinue = parti[1];
//
//        //@todo rimetto il valore intero (e non le parti) perché così adesso funziona
//        return valore;
//    } // fine del metodo

    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     * <p>
     * La mappa in ingresso contiene ns, pageid e title
     * Utilizzo solo il pageid (Integer)
     *
     * @param mappa standard (valori String) in ingresso
     * @return mappa typizzata secondo PagePar
     */
    private static ArrayList<Integer> converteListaCat(ArrayList listaIn) {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        int value;

//        listaIn ?.each {
//            value = (int) it[PAGEID];
//            lista.add(value);
//        } // fine del ciclo each

        return lista;
    } // fine del metodo

    /**
     * Crea una stringa di testo, con tutti i valori della lista, separati dal pipe
     *
     * @param lista (valori Integer) in ingresso
     * @return stringa di valori
     */
    public static String creaListaPageids(ArrayList<Long> lista) {
        String testo = VUOTA;
        String sep = "|";

        for (Long lungo : lista) {
            testo += lungo;
            testo += sep;
        } // fine del ciclo for-each
        testo = levaCoda(testo, sep);

        return testo;
    } // fine del metodo

    /**
     * Converte il valore stringa del timestamp in un timestamp
     * Formato: 2015-06-30T10:18:05Z
     *
     * @param timestampText in ingresso
     * @return data in uscita
     */
    public static Timestamp convertTxtTime(String timestampText) {
        return new Timestamp(convertTxtData(timestampText).getTime());
    } // fine del metodo

    /**
     * Converte il valore stringa della data in una data
     * Formato: 2015-06-30T10:18:05Z
     *
     * @param dataTxt in ingresso
     * @return data in uscita
     */
    public static Date convertTxtData(String dataTxt) {
        String zero = "0";
        String annoStr;
        String meseStr;
        String giornoStr;
        String oraStr;
        String minutoStr;
        String secondoStr;
        int anno = 0;
        int mese = 0;
        int giorno = 0;
        int ora = 0;
        int minuto = 0;
        int secondo = 0;

        annoStr = dataTxt.substring(0, 4);
        if (dataTxt.substring(5, 6).equals(zero)) {
            meseStr = dataTxt.substring(6, 7);
        } else {
            meseStr = dataTxt.substring(5, 7);
        }// fine del blocco if-else

        if (dataTxt.substring(8, 9).equals(zero)) {
            giornoStr = dataTxt.substring(9, 10);
        } else {
            giornoStr = dataTxt.substring(8, 10);
        }// fine del blocco if-else

        if (dataTxt.substring(11, 12).equals(zero)) {
            oraStr = dataTxt.substring(12, 13);
        } else {
            oraStr = dataTxt.substring(11, 13);
        }// fine del blocco if-else

        if (dataTxt.substring(14, 15).equals(zero)) {
            minutoStr = dataTxt.substring(15, 16);
        } else {
            minutoStr = dataTxt.substring(14, 16);
        }// fine del blocco if-else

        if (dataTxt.substring(17, 18).equals(zero)) {
            secondoStr = dataTxt.substring(18, 19);
        } else {
            secondoStr = dataTxt.substring(17, 19);
        }// fine del blocco if-else

        try { // prova ad eseguire il codice
            anno = Integer.decode(annoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            mese = Integer.decode(meseStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            giorno = Integer.decode(giornoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            ora = Integer.decode(oraStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        try { // prova ad eseguire il codice
            minuto = Integer.decode(minutoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            secondo = Integer.decode(secondoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);

        //--patch
        anno = anno - 1900;
        mese = mese - 1;

        return new Date(anno, mese, giorno, ora, minuto, secondo);
    }// fine del metodo

    private static String apici(String entrata) {
        return APICI + entrata + APICI;
    }// fine del metodo

    private static String graffe(String entrata) {
        return GRAFFA_INI + entrata + GRAFFA_END;
    }// fine del metodo

    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param par     parametro PagePar in ingresso
     * @param valueIn in ingresso
     * @return valore della classe corretta
     */
    private static Object fixValueMap(PagePar par, Object valueIn) {
        Object valueOut = null;
        PagePar.TypeField typo = par.getType();

        if (typo == PagePar.TypeField.string) {
            valueOut = valueIn;
        }// fine del blocco if

        if (typo == PagePar.TypeField.booleano) {
            valueOut = valueIn;
        }// fine del blocco if

        if (typo == PagePar.TypeField.longzero || typo == PagePar.TypeField.longnotzero) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = Long.decode((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Integer) {
                try { // prova ad eseguire il codice
                    valueOut = new Long(valueIn.toString());
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Long) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        if (typo == PagePar.TypeField.date) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = convertTxtData((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA;
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Date) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        if (typo == PagePar.TypeField.timestamp) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = convertTxtTime((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA;
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Timestamp) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        return valueOut;
    } // fine del metodo

    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param key     del parametro PagePar in ingresso
     * @param valueIn in ingresso
     * @return valore della classe corretta
     */
    private static Object fixValueMap(String key, Object valueIn) {
        return fixValueMap(PagePar.getPar(key), valueIn);
    } // fine del metodo

    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     * @return differenza
     */
    public static ArrayList delta(ArrayList primo, ArrayList secondo) {
        ArrayList differenza = null;

        if (primo != null && secondo != null) {
            differenza = new ArrayList();
            for (Object value : primo) {
                if (!secondo.contains(value)) {
                    differenza.add(value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return differenza;
    } // fine del metodo

    /**
     * Regola i parametri della tavola in base alla mappa letta dal server
     * Aggiunge le date di riferimento lettura/scrittura
     */
    public static Wiki fixMappa(Wiki wiki, HashMap mappa) {
        List<PagePar> lista = PagePar.getDB();
        String key;
        Object value;

        for (PagePar par : lista) {
            key = par.toString();
            value = null;

            if (mappa.get(key) != null) {
                value = mappa.get(key);
            }// fine del blocco if

            //--controllo dei LONG che POSSONO essere anche zero
            if (par.getType() == PagePar.TypeField.longzero) {
                if (value == null) {
                    value = 0;
                }// fine del blocco if
            }// fine del blocco if

            //--patch
            if (par == PagePar.comment) {
                if (value instanceof String) {
                    if (((String) value).startsWith("[[WP:OA|←]]")) {
                        value = "Nuova pagina";
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if

            par.setWiki(wiki, value);
        } // fine del ciclo for-each

        return wiki;
    }// end of method


    /**
     * Legge dal server wiki
     * Registra la tavola WikiBio
     * <p>
     *
     * @param title della pagina
     */
    public static void download(String title) {
        Page pagina = Api.leggePage(title);
        download(pagina);
    }// end of method

    /**
     * Legge dal server wiki
     * Registra la tavola  WikiBio
     * <p>
     *
     * @param pagina dal server
     */
    public static void download(Page pagina) {
        HashMap mappa;
        Wiki wiki;
        String testoVoce;
        String tmplBio;

        testoVoce = pagina.getText();
        tmplBio = Api.estraeTmplBio(testoVoce);

        if (tmplBio != null) {
            wiki = new Wiki();
            mappa = pagina.getMappaDB();
            wiki = fixMappa(wiki, mappa);

            try { // prova ad eseguire il codice
                wiki.save();
            } catch (Exception unErrore) { // intercetta l'errore
                //@todo inserire log e logger
//                (new Notification("ATTENZIONE", "La pagina " + pagina.getTitle() + " non è stata registrata", Notification.TYPE_WARNING_MESSAGE, true)).show(com.vaadin.server.Page.getCurrent());
            }// fine del blocco try-catch
        }// fine del blocco if
    }// end of method


    /**
     * Restituisce un Timestamp attuale
     */
    public static Timestamp getTime() {
        return new Timestamp(new Date().getTime());
    }// end of method


    /**
     * Elimina (eventuali) doppie graffe in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie graffe eliminate
     */
    public static String setNoGraffe(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, GRAFFA_INI);
            stringaOut = levaTesta(stringaOut, GRAFFA_INI);
            stringaOut = levaCoda(stringaOut, GRAFFA_END);
            stringaOut = levaCoda(stringaOut, GRAFFA_END);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo

    /**
     * Elimina (eventuali) doppie quadre in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con doppie quadre eliminate
     */
    public static String setNoQuadre(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, QUADRA_INI);
            stringaOut = levaTesta(stringaOut, QUADRA_INI);
            stringaOut = levaCoda(stringaOut, QUADRA_END);
            stringaOut = levaCoda(stringaOut, QUADRA_END);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Elimina (eventuali) tripli apici (grassetto) in testa e coda della stringa.
     * Funziona solo se gli apici sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     * @return stringa con tripli apici eliminati
     */
    public static String setNoBold(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, BOLD);
            stringaOut = levaCoda(stringaOut, BOLD);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


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
    public static String setGraffe(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoGraffe(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = GRAFFE_INI + stringaPulita + GRAFFE_END;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


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
    public static String setQuadre(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoQuadre(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = QUADRE_INI + stringaPulita + QUADRE_END;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


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
    public static String setBold(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoBold(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = BOLD + stringaPulita + BOLD;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo

    /**
     * Controlla l'esistenza di una pagina.
     *
     * @param title della pagina da ricercare
     * @return true se la pagina esiste
     */
    public static boolean isEsiste(String title) {
        QueryReadTitle query = new QueryReadTitle(title);
        return query.isLetta();
    } // fine del metodo

} // fine della classe astratta
