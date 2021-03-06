package it.algos.vaad.wiki;

import it.algos.vaad.wiki.request.*;
import it.algos.webbase.web.lib.LibText;

import java.util.ArrayList;
import java.util.Map;

public class Api {

    private static String MAP_KEY_TYPE = "type";
    private static String MAP_KEY_NOME = "nome";

    // Di default suppone il template ''Bio''
    private static String TAG_BIO = "Bio";

    /**
     * Legge dal server wiki
     * <p>
     * Rimanda al metodo completo
     * Di default suppone il title
     *
     * @param title della pagina wiki
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String title) {
        return legge(title, TipoRicerca.title);
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     * Rimanda al metodo completo
     * Di default suppone il contenuto della Pagina come ritorno
     *
     * @param title       della pagina wiki
     * @param tipoRicerca title o pageId
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String title, TipoRicerca tipoRicerca) {
        return legge(title, tipoRicerca, TipoQuery.pagina);
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     * Rimanda al metodo completo
     * Di default suppone il template ''Bio''
     *
     * @param title       della pagina wiki
     * @param tipoRicerca title o pageId
     * @param tipoQuery   (pagina, voce o template)
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String title, TipoRicerca tipoRicerca, TipoQuery tipoQuery) {
        return legge(title, tipoRicerca, tipoQuery, TAG_BIO);
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     * Mtodo completo
     *
     * @param title       della pagina wiki
     * @param tipoRicerca title o pageId
     * @param tipoQuery   (pagina, voce o template)
     * @param tagTemplate da usare
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String title, TipoRicerca tipoRicerca, TipoQuery tipoQuery, String tagTemplate) {
        String testo = "";

        if (tipoQuery == TipoQuery.pagina) {
            testo = leggePagina(title);
        }// fine del blocco if

        if (tipoQuery == TipoQuery.voce) {
            testo = leggeVoce(title);
        }// fine del blocco if

        if (tipoQuery == TipoQuery.template) {
            testo = leggeTmpl(title, tagTemplate);
        }// fine del blocco if

        if (tipoQuery == TipoQuery.categoria) {
//            testo = leggeCat(titlePageid);
        }// fine del blocco if

        return testo;
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     *
     * @param title       della pagina wiki
     * @param tipoRicerca title o pageId
     * @param params      passati
     * @return contenuto del template bio
     */
    public static String legge(String title, TipoRicerca tipoRicerca, Map params) {
        String testo = "";
        String type = "";

        if (params != null) {
            if (params.containsKey(MAP_KEY_TYPE)) {
                type = (String) params.get(MAP_KEY_TYPE);
            }// fine del blocco if
        } else {
            type = TipoQuery.voce.toString();
        }// fine del blocco if-else

        if (!type.equals("")) {
            if (type.equals(TipoQuery.pagina.toString())) {
                testo = leggePagina(title);
            }// fine del blocco if

            if (type.equals(TipoQuery.voce.toString())) {
                testo = leggeVoce(title);
            }// fine del blocco if

            if (type.equals(TipoQuery.template.toString())) {
                testo = leggeTmpl(title, params);
            }// fine del blocco if
        }// fine del blocco if

        return testo;
    }// end of method


    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    @SuppressWarnings("all")
    public static String leggePagina(String title) {
        ARequest request = new RequestRead(title);

        if (request != null && request.getRisultato() == TipoRisultato.letta) {
            return request.getTestoResponse();
        } else {
            return "";
        }// fine del blocco if-else
    }// end of method

    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    @SuppressWarnings("all")
    public static String leggePagina(long pageId) {
        ARequest request = new RequestRead(pageId);

        if (request != null && request.getRisultato() == TipoRisultato.letta) {
            return request.getTestoResponse();
        } else {
            return "";
        }// fine del blocco if-else
    }// end of method

//    /**
//     * Legge il contenuto (tutto) di una pagina
//     * <p>
//     *
//     * @param titlePageid (title oppure pageid)
//     * @param tipoRicerca title o pageId
//     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
//     */
//    public static String leggePagina(String titlePageid, TipoRicerca tipoRicerca) {
//        Request query = null;
//
//        if (tipoRicerca == TipoRicerca.title) {
//            query = new RequestWikiReadPageid(titlePageid);
//        }// fine del blocco if
//
//        if (tipoRicerca == TipoRicerca.pageid) {
//            query = new RequestWikiReadTitle(titlePageid);
//        }// fine del blocco if
//
//        if (query != null && query.getRisultato() == TipoRisultato.letta) {
//            return query.getTestoResponse();
//        } else {
//            return "";
//        }// fine del blocco if-else
//    }// end of method


    /**
     * Legge una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    public static Page leggePage(String title) {
        Page page = null;
        String contenuto = leggePagina(title);

        if (contenuto != null && !contenuto.equals("")) {
            page = new Page(contenuto);
        }// fine del blocco if

        return page;
    }// end of method

    /**
     * Legge una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    public static Page leggePage(long pageId) {
        Page page = null;
        String contenuto = leggePagina(pageId);

        if (contenuto != null && !contenuto.equals("")) {
            page = new Page(contenuto);
        }// fine del blocco if

        return page;
    }// end of method

    /**
     * Legge una serie di pagine
     * <p>
     *
     * @param arrayPageIds elenco di pageids (ArrayList)
     * @return pagina (con i metadati mediawiki)
     */
    public static ArrayList<Page> leggePages(ArrayList<Long> arrayPageIds) {
        ArrayList<Page> listaPages = null;
        RequestWikiReadMultiPages request = new RequestWikiReadMultiPages(arrayPageIds);

        if (request.getRisultato() == TipoRisultato.letta) {
            return request.getListaPages();
        } else {
            return null;
        }// fine del blocco if-else
    }// end of method


    /**
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param title della pagina
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public static String leggeVoce(String title) {
        String testo = "";
        Page pagina = leggePage(title);

        if (pagina != null) {
            testo = pagina.getText();
        }// fine del blocco if

        return testo;
    }// end of method

//    /**
//     * Legge il contenuto (testo) di una voce
//     * <p>
//     *
//     * @param pageId della pagina
//     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
//     */
//    public static String leggeVoce(int pageId) {
//        return leggeVoce("" + pageId, TipoRicerca.pageid);
//    }// end of method

    /**
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public static String leggeVoce(long pageId) {
        String testo = "";
        Page pagina = leggePage(pageId);

        if (pagina != null) {
            testo = pagina.getText();
        }// fine del blocco if

        return testo;
    }// end of method

//    /**
//     * Legge il contenuto (testo) di una voce
//     * <p>
//     *
//     * @param titlePageid (title oppure pageid)
//     * @param tipoRicerca title o pageId
//     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
//     */
//    public static String leggeVoce(String titlePageid) {
//        String testo = "";
//        Page pagina = leggePage(titlePageid);
//
//        if (pagina != null) {
//            testo = pagina.getText();
//        }// fine del blocco if
//
//        return testo;
//    }// end of method

    /**
     * Legge un template da una voce
     * <p>
     *
     * @param title della pagina
     * @param tag   nome del template
     * @return contenuto del template
     */
    public static String leggeTmpl(String title, String tag) {
        String tmpl = "";
        String testo = leggeVoce(title);

        if (!testo.equals("") && !tag.equals("")) {
            tmpl = LibWiki.estraeTmplCompresi(testo, tag);
        }// fine del blocco if
        return tmpl;
    }// end of method

    /**
     * Legge un template da una voce
     * <p>
     *
     * @param pageId della pagina
     * @param tag    nome del template
     * @return contenuto del template
     */
    public static String leggeTmpl(long pageId, String tag) {
        String tmpl = "";
        String testo = leggeVoce(pageId);

        if (!testo.equals("") && !tag.equals("")) {
            tmpl = LibWiki.estraeTmplCompresi(testo, tag);
        }// fine del blocco if
        return tmpl;
    }// end of method

//    /**
//     * Legge un template da una voce
//     * <p>
//     *
//     * @param titlePageid (title oppure pageid)
//     * @param tipoRicerca title o pageId
//     * @param tag         nome del template
//     * @return contenuto del template
//     */
//    public static String leggeTmpl(String titlePageid, TipoRicerca tipoRicerca, String tag) {
//        String tmpl = "";
//        String testo = leggeVoce(titlePageid, tipoRicerca);
//
//        if (!testo.equals("") && !tag.equals("")) {
//            tmpl = LibWiki.estraeTmplCompresi(testo, tag);
//        }// fine del blocco if
//        return tmpl;
//    }// end of method

    /**
     * Legge un template di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param params      passati
     * @return contenuto del template
     */
    public static String leggeTmpl(String titlePageid, Map params) {
        String tmpl = "";
        String tag = "";
        String testo = leggeVoce(titlePageid);

        if (params != null) {
            if (params.containsKey(MAP_KEY_NOME)) {
                tag = (String) params.get(MAP_KEY_NOME);
            }// fine del blocco if
        }// fine del blocco if

        if (!testo.equals("") && !tag.equals("")) {
            tmpl = LibWiki.estraeTmplCompresi(testo, tag);
        }// fine del blocco if

        return tmpl;
    }// end of method

    /**
     * Legge il template bio di una voce
     * <p>
     *
     * @param title della pagina
     * @return contenuto del template bio
     */
    public static String leggeTmplBio(String title) {
        String testoTemplate = "";
        String testo = leggeVoce(title);

        if (!testo.equals("")) {
            testoTemplate = LibWiki.estraeTmplBioCompresi(testo);
        }// fine del blocco if

        return testoTemplate;
    }// end of method

    /**
     * Legge il template bio di una voce
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto del template bio
     */
    public static String leggeTmplBio(long pageId) {
        String testoTemplate = "";
        String testo = leggeVoce(pageId);

        if (!testo.equals("")) {
            testoTemplate = LibWiki.estraeTmplBioCompresi(testo);
        }// fine del blocco if

        return testoTemplate;
    }// end of method

//    /**
//     * Legge il template bio di una voce
//     * <p>
//     *
//     * @param titlePageid (title oppure pageid)
//     * @return contenuto del template bio
//     */
//    public static String leggeTmplBio(String titlePageid) {
//        String testoTemplate = "";
//        String testo = leggeVoce(titlePageid);
//
//        if (!testo.equals("")) {
//            testoTemplate = LibWiki.estraeTmplBioCompresi(testo);
//        }// fine del blocco if
//
//        return testoTemplate;
//    }// end of method


    /**
     * Legge una lista di pageids per costruire una lista di WrapTime
     * <p>
     *
     * @param bloccoPageids elenco di pageids delle pagine da controllare
     * @return lista di WrapTime (wrapper)
     */
    public static ArrayList<WrapTime> leggeWrapTime(ArrayList<Long> bloccoPageids) {
        ArrayList<WrapTime> listaWrapTime = null;
        ARequest request;

        if (bloccoPageids != null) {
            request = new RequestTime(bloccoPageids);
            listaWrapTime = request.getListaWrapTime();
        }// end of if cycle

        return listaWrapTime;
    }// end of method


    /**
     * Estrae un template dal testo
     * <p>
     *
     * @param testo completo della voce
     * @return contenuto del template bio
     */
    public static String estraeTmpl(String testo, String tag) {
        return LibWiki.estraeTmplCompresi(testo, tag);
    }// end of method

    /**
     * Estrae il template bio dalla pagina
     * <p>
     *
     * @param page completa
     * @return contenuto del template bio
     */
    public static String estraeTmplBio(Page page) {
        String testo = page.getText();
        return LibWiki.estraeTmplBioCompresi(testo);
    }// end of method

    /**
     * Estrae il template bio dal testo
     * <p>
     *
     * @param testo completo della voce
     * @return contenuto del template bio
     */
    public static String estraeTmplBio(String testo) {
        return LibWiki.estraeTmplBioCompresi(testo);
    }// end of method

    /**
     * Controlla l'esistenza di una pagina.
     *
     * @param title della pagina da ricercare
     * @return true se la pagina esiste
     */
    public static boolean esiste(String title) {
        ARequest request = new RequestRead(title);
        return request.getRisultato() == TipoRisultato.letta;
    } // fine del metodo

    /**
     * Modifica il contenuto di una pagina.
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param oldText   da eliminare
     * @param newText   da inserire
     */
    public static boolean modificaVoce(String wikiTitle, String oldText, String newText) {
        return modificaVoce(wikiTitle, oldText, newText, "", null);
    } // fine del metodo

    /**
     * Modifica il contenuto di una pagina.
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param oldText   da eliminare
     * @param newText   da inserire
     * @param summary   oggetto della modifica
     */
    public static boolean modificaVoce(String wikiTitle, String oldText, String newText, String summary) {
        return modificaVoce(wikiTitle, oldText, newText, summary, null);
    } // fine del metodo

    /**
     * Modifica il contenuto di una pagina.
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param oldText   da eliminare
     * @param newText   da inserire
     * @param summary   oggetto della modifica
     * @param login     for testing purpose only
     */
    public static boolean modificaVoce(String wikiTitle, String oldText, String newText, String summary, WikiLogin login) {
        boolean status = false;
        String oldContenuto;
        String testoTmp;
        Request request;
        String botName;

        if (wikiTitle.equals("")) {
            return false;
        }// end of if cycle

        if (summary.equals("")) {
            if (login != null) {
                botName = login.getLgusername();
                summary = "[[Utente:" + botName + "|" + botName + "]]: ";
            }// end of if cycle

            if (oldText.length() < 10 && newText.length() < 10) {
                summary += oldText + " -> " + newText;
            } else {
                summary += "subst";
            }// end of if/else cycle
        }// end of if cycle

        oldContenuto = Api.leggeVoce(wikiTitle);
        testoTmp = LibText.sostituisce(oldContenuto, oldText, newText);
        request = new RequestWikiWrite(wikiTitle, testoTmp, summary, login);

        if (request.getRisultato() == TipoRisultato.modificaRegistrata) {
            status = true;
        }// end of if cycle

        return status;
    } // fine del metodo


    /**
     * Scrive una pagina
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param newText   da inserire
     */
    public static boolean scriveVoce(String wikiTitle, String newText) {
        return scriveVoce(wikiTitle, newText, "");
    }// fine del metodo costruttore completo

    /**
     * Scrive una pagina
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param newText   da inserire
     * @param summary   oggetto della modifica
     */
    public static boolean scriveVoce(String wikiTitle, String newText, String summary) {
        boolean status = false;
        ARequest request;

        if (wikiTitle.equals("") || newText.equals("")) {
            return false;
        }// end of if cycle

        request = new RequestWrite(wikiTitle, newText, summary);
        if (request.getRisultato() == TipoRisultato.modificaRegistrata | request.getRisultato() == TipoRisultato.nochange) {
            status = true;
        }// end of if cycle

        return status;
    }// fine del metodo costruttore completo


//    /**
//     * Esegue la query
//     *
//     * @param titleCat della categoria da ricercare
//     * @return query valida
//     */
//    private static QueryCat getQuey(String titleCat) {
//        QueryCat query = null;
//
//        if (titleCat != null && !titleCat.equals("")) {
//            query = new QueryCat(titleCat);
//        }// end of if cycle
//
//        if (query != null && query.isValida() && query.getRisultato() == TipoRisultato.letta) {
//            return query;
//        } else {
//            return null;
//        }// end of if/else cycle
//    } // fine del metodo

    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli sia delle voci che delle subcategorie
     */
    public static ArrayList<String> leggeCat(String titleCat) {
        return leggeCatTitles(titleCat);
    } // fine del metodo

    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid delle voci
     */
    public static ArrayList<Long> leggeCatLong(String titleCat) {
        return leggeCatPageidsOnlyVoci(titleCat);
    } // fine del metodo


    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli sia delle voci che delle subcategorie
     */
    public static ArrayList<String> leggeCatTitles(String titleCat) {
        ArrayList<String> lista = null;
        ARequest request;

        if (titleCat != null && !titleCat.equals("")) {
            request = new RequestCat(titleCat);
            if (request.isValida()) {
                lista = request.getListaAllTitles();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid sia delle voci che delle subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid sia delle voci che delle subcategorie
     */
    public static ArrayList<Long> leggeCatPageids(String titleCat) {
        ArrayList<Long> lista = null;
        ARequest request;

        if (titleCat != null && !titleCat.equals("")) {
            request = new RequestCat(titleCat);
            if (request.isValida()) {
                lista = request.getListaAllPageids();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di titoli solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista titoli delle voci
     */
    public static ArrayList<String> leggeCatTitlesOnlyVoci(String titleCat) {
        ArrayList<String> lista = null;
        ARequest request;

        if (titleCat != null && !titleCat.equals("")) {
            request = new RequestCat(titleCat);
            if (request.isValida()) {
                lista = request.getListaVociTitles();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Legge gli elementi appartenenti ad una categoria.
     * Restituisce una lista (ArrayList) di pageid solo delle voci senza le subcategorie
     *
     * @param titleCat della categoria da ricercare
     * @return lista pageid delle voci
     */
    public static ArrayList<Long> leggeCatPageidsOnlyVoci(String titleCat) {
        ArrayList<Long> lista = null;
        ARequest request;

        if (titleCat != null && !titleCat.equals("")) {
            request = new RequestCat(titleCat);
            if (request.isValida()) {
                lista = request.getListaVociPageids();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Legge i titoli delle pagine che puntano ad una pagina.
     * Restituisce una lista (ArrayList) di titoli in tutti i namespaces
     *
     * @param title della pagina da controllare
     * @return lista titoli delle pagine
     */
    public static ArrayList<String> leggeBacklinks(String title) {
        ArrayList<String> lista = null;
        ARequest request;

        if (title != null && !title.equals("")) {
            request = new RequestLinks(title);
            if (request.isValida()) {
                lista = request.getListaPagineTitles();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Legge i titoli delle voci che puntano ad una pagina.
     * Restituisce una lista (ArrayList) di titoli solo delle voci nel namespace principale
     *
     * @param title della pagina da controllare
     * @return lista titoli delle voci
     */
    public static ArrayList<String> leggeBacklinksOnlyVoci(String title) {
        ArrayList<String> lista = null;
        ARequest request;

        if (title != null && !title.equals("")) {
            request = new RequestLinks(title);
            if (request.isValida()) {
                lista = request.getListaVociTitles();
            }// end of if cycle
        }// end of if cycle

        return lista;
    } // fine del metodo

    /**
     * Sposta una pagina (sposta il titolo)
     *
     * @param oldTitle vecchio titolo della pagina
     * @param newTitle nuovo titolo della pagina
     */
    public static boolean sposta(String oldTitle, String newTitle) {
        return sposta(oldTitle, newTitle, "");
    } // fine del metodo

    /**
     * Sposta una pagina (sposta il titolo)
     *
     * @param oldTitle vecchio titolo della pagina
     * @param newTitle nuovo titolo della pagina
     * @param summary  oggetto della modifica
     */
    public static boolean sposta(String oldTitle, String newTitle, String summary) {
        boolean status = false;
        ARequest request;

        if (oldTitle != null && !oldTitle.equals("") && newTitle != null && !newTitle.equals("")) {
            request = new RequestMove(oldTitle, newTitle, summary);
            if (request.isValida() && request.getRisultato() == TipoRisultato.spostata) {
                status = true;
            }// end of if cycle
        }// end of if cycle

        return status;
    } // fine del metodo

}// end of service class