package it.algos.vaad.wiki;

/**
 * Created by gac on 04 ago 2015.
 * .
 */

import it.algos.webbase.web.lib.LibText;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Libreria
 */
public abstract class LibWiki {

    //--preferenza
    public static final String USA_CRONO_BIO = "usaCroonoBio";
    public static final String USA_LIMITE_DOWNLOAD = "usaLimiteDownload";
    public static final String MAX_DOWNLOAD = "maxDownload";
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
    public static final String GRAFFA_INI = "{";
    public static final String GRAFFE_INI = GRAFFA_INI + GRAFFA_INI;
    public static final String GRAFFA_END = "}";
    public static final String GRAFFE_END = GRAFFA_END + GRAFFA_END;
    public static final String BIO = Cost.TAG_BIO;

    // tag per la stringa vuota
    public static final String VUOTA = "";

    // tag per il valore falso per una posizione
    public static final int INT_NULLO = -1;

    public static Date DATA_NULLA = new Date(70, 0, 1);
    private static String QUERY = "query";
    private static String PAGEID = "pageid";
    private static String PAGES = "pages";
    private static String REVISIONS = "revisions";
    private static String CATEGORY_MEMBERS = "categorymembers";
    private static String QUERY_CONTINUE = "query-continue";
    private static String VIR = ",";
    private static String APICI = "\"";
    private static String PUNTI = ":";

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
//            case PagePar.TypeField.integerzero:     //--conversione degli interi
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
    public static LinkedHashMap creaMappa(String textJSON) {
        LinkedHashMap mappa = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        Object pagesObj = queryObj.get(PAGES);

        if (pagesObj instanceof JSONObject) {
            mappa = fixMappa((JSONObject) pagesObj);
        }// fine del blocco if

        if (pagesObj instanceof JSONArray) {
            mappa = estraeMappaJson((JSONArray) pagesObj);
        }// fine del blocco if

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
     */
    private static LinkedHashMap fixMappa(JSONObject mappaJson) {
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

        //--patch per il nome 'atipico' del campo text
        if (mappaOut.containsKey(PATCH_OLD)) {
            value = mappaOut.get(PATCH_OLD);
            mappaOut.remove(PATCH_OLD);
            mappaOut.put(PATCH_NEW, value);
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo

    /**
     * Estrae una mappa standard da un JSONObject
     *
     * @param mappaJson JSONObject in ingresso
     * @return mappa standard (valori String)
     */
    private static LinkedHashMap<String, Object> estraeMappaJson(JSONObject mappaJson) {
        LinkedHashMap<String, Object> mappaOut = new LinkedHashMap<String, Object>();
        Set setJson = mappaJson.keySet();
        Object mappaValoriInfo = mappaJson.values();
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
     *
     * @param mappaJson JSONArray in ingresso
     * @return mappa standard (valori String)
     */
    private static LinkedHashMap<String, Object> estraeMappaJson(JSONArray mappaJson) {
        LinkedHashMap<String, Object> mappaOut = new LinkedHashMap<String, Object>();

        if (mappaJson.size() == 1) {
            for (Object obj : mappaJson) {
                if (obj instanceof JSONObject) {
                    mappaOut = estraeMappaJson((JSONObject) obj);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo

//    /**
//     * Crea una mappa standard (valori String) da una mappa JSON di una pagina
//     * <p/>
//     * Prima i parametri delle info
//     * Poi, se ci sono, i parametri della revisione
//     *
//     * @param mappa JSON in ingresso
//     * @return mappa standard (valori String)
//     */
//    private static LinkedHashMap fixMappaJson(Object mappaIn) {
//        LinkedHashMap mappaOut = new LinkedHashMap();
//        String key;
//        Object value;
//
////        //--valori dei parametri ricavati dal testo json
////        PagePar.getPerm2().each {
////            key = it.toString()
////            value = mappaIn["${key}"]
////            mappaOut.put(key, value)
////        } // fine del ciclo each
//
//        return mappaOut;
//    } // fine del metodo

    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     *
     * @param mappaIn standard (valori String) in ingresso
     * @return mappa typizzata secondo PagePar
     */
    public static LinkedHashMap<String, Object> converteMappa(LinkedHashMap mappaIn) {
        LinkedHashMap<String, Object> mappaOut = new LinkedHashMap<String, Object>();
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
    public static ArrayList<Integer> creaListaCat(String title) {
        ArrayList<Integer> lista = null;
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
    public static ArrayList<Integer> creaListaCatJson(String textJSON) {
        ArrayList<Integer> lista = null;
        JSONObject jsonObject = null;
        Object longPageid = null;
        int pageid = 0;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        JSONArray catObj = (JSONArray) queryObj.get(CATEGORY_MEMBERS);

        if (catObj != null) {
            lista = new ArrayList<>();
            for (Object obj : catObj) {
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
    public static String creaListaPageids(ArrayList<Integer> lista) {
        String testo = VUOTA;
        String sep = "|";

        for (Integer intero : lista) {
            testo += intero;
            testo += sep;
        } // fine del ciclo for-each
        testo = levaCoda(testo, sep);

        return testo;
    } // fine del metodo

    /**
     * Converte il valore stringa della data in una data
     * Formato: 2015-06-30T10:18:05Z
     *
     * @param dataTxt in ingresso
     * @return data in uscita
     */
    public static Date convertTxtData(String dataTxt) {
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
        meseStr = dataTxt.substring(5, 7);
        giornoStr = dataTxt.substring(8, 10);
        oraStr = dataTxt.substring(11, 13);
        minutoStr = dataTxt.substring(14, 16);
        secondoStr = dataTxt.substring(17, 19);

        anno = Integer.decode(annoStr);
        mese = Integer.decode(meseStr);
        giorno = Integer.decode(giornoStr);
        ora = Integer.decode(oraStr);
        minuto = Integer.decode(minutoStr);
        secondo = Integer.decode(secondoStr);

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

        if (typo == PagePar.TypeField.integerzero || typo == PagePar.TypeField.integernotzero) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = Integer.decode((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Long) {
                try { // prova ad eseguire il codice
                    valueOut = new Integer(valueIn.toString());
                } catch (Exception unErrore) { // intercetta l'errore
                    int a = 67;
                }// fine del blocco try-catch
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


} // fine della classe astratta
