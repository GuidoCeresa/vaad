package it.algos.vaad.wiki;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:33
 * Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
 * Quattordici (14) parametri letti SEMPRE:
 * titolo, pageid, testo, ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
 */
public class Page implements Serializable {

    private static String APICI = "\"";
    private static String PUNTI = ":";
    private static String GRAFFA_INI = "{";
    private static String GRAFFA_END = "}";
    private static String VIR = ",";

    private boolean valida = false;
//    private boolean paginaScrittura = false

    //--tipo di request - solo una per leggere - due per scrivere
    //--di default solo lettura (per la scrittura serve il login)
    private TipoRequest tipoRequest = TipoRequest.read;

//    private String text //risultato completo della pagina

    private LinkedHashMap mappaTxt = new LinkedHashMap();
    private LinkedHashMap mappaObj = new LinkedHashMap();

    public Page() {
    }// fine del metodo costruttore


    public Page(String testoPagina) {
        this(testoPagina, TipoRequest.read);
    }// fine del metodo costruttore


    public Page(String testoPagina, TipoRequest tipoRequest) {
        this.tipoRequest = tipoRequest;
        mappaTxt = LibWiki.creaMappa(testoPagina);
        mappaObj = LibWiki.converteMappa(mappaTxt);
        valida = PagePar.isParValidi(mappaObj);
    }// fine del metodo costruttore

    private static String apici(String entrata) {
        return APICI + entrata + APICI;
    }// fine del metodo

    private static String graffe(String entrata) {
        return GRAFFA_INI + entrata + GRAFFA_END;
    }// fine del metodo

//    public String getJSON() {
//        if (tipoRequest == TipoRequest.read) {
//            return getRead();
//        } else {
//            return getWrite();
//        }// fine del blocco if-else
//    }// fine del metodo
//
//    public String getRead() {
//        return getJSONBase(PagePar.getRead());
//    }// fine del metodo
//
//    public String getWrite() {
//        return getJSONBase(PagePar.getWrite());
//    }// fine del metodo

    private String getJSONBase(ArrayList<PagePar> lista) {
        String textJSON = "";
        String sep = VIR;
        String textPulito;
        String key;
        Object obj;
        LinkedHashMap mappaObj = this.getMappa();

//        lista ?.each {
//            if (it == PagePar.text) {
//                textPulito = LibWiki.sostituisce(getText(), '"', '\\"')
//                textJSON += apici(it) + PUNTI + apici(textPulito)
//            } else {
//                key = it.toString()
//                obj = mappaObj["${key}"]
//                textJSON += apici(it) + PUNTI + apici(obj)
//            }// fine del blocco if-else
//            textJSON += sep
//        } // fine del ciclo each
//        textJSON = LibWiki.levaCoda(textJSON, sep)

        return graffe(textJSON);
    }// fine del metodo

    public LinkedHashMap getMappaTxt() {
        return mappaTxt;
    }// fine del metodo

    public LinkedHashMap getMappa() {
        return mappaObj;
    }// fine del metodo

    public int getPageid() {
        return (Integer) mappaObj.get(PagePar.pageid.toString());
    }// fine del metodo

    public String getTitle() {
        return (String)mappaObj.get(PagePar.title.toString());
    }// fine del metodo

    public String getText() {
        return (String)mappaObj.get(PagePar.text.toString());
    }// fine del metodo

    public boolean isValida() {
        return valida;
    }// fine del metodo


    public void setValida(boolean valida) {
        this.valida = valida;
    }// fine del metodo
} //fine della classe
