package it.algos.vaad.wiki;

import java.util.HashMap;
import java.util.List;
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
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String titlePageid, TipoRicerca tipoRicerca) {
        return legge(titlePageid, tipoRicerca, TipoQuery.pagina);
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     * Rimanda al metodo completo
     * Di default suppone il template ''Bio''
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param tipoQuery   (pagina, voce o template)
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String titlePageid, TipoRicerca tipoRicerca, TipoQuery tipoQuery) {
        return legge(titlePageid, tipoRicerca, tipoQuery, TAG_BIO);
    }// end of method

    /**
     * Legge dal server wiki
     * <p>
     * Mtodo completo
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param tipoQuery   (pagina, voce o template)
     * @param tagTemplate da usare
     * @return risultato della pagina (JSON) oppure della voce (text) oppure del template (text)
     */
    public static String legge(String titlePageid, TipoRicerca tipoRicerca, TipoQuery tipoQuery, String tagTemplate) {
        String testo = "";
        Page pagina;

        if (tipoQuery == TipoQuery.pagina) {
            testo = leggePagina(titlePageid, tipoRicerca);
        }// fine del blocco if

        if (tipoQuery == TipoQuery.voce) {
            testo = leggeVoce(titlePageid, tipoRicerca);
        }// fine del blocco if

        if (tipoQuery == TipoQuery.template) {
            testo = leggeTmpl(titlePageid, tipoRicerca, tagTemplate);
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
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param params      passati
     * @return contenuto del template bio
     */
    public static String legge(String titlePageid, TipoRicerca tipoRicerca, Map params) {
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
                testo = leggePagina(titlePageid, tipoRicerca);
            }// fine del blocco if

            if (type.equals(TipoQuery.voce.toString())) {
                testo = leggeVoce(titlePageid, tipoRicerca);
            }// fine del blocco if

            if (type.equals(TipoQuery.template.toString())) {
                testo = leggeTmpl(titlePageid, tipoRicerca, params);
            }// fine del blocco if
        }// fine del blocco if

        return testo;
    }// end of method

    /**
     * Legge una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    public static Page leggePage(String title) {
        return leggePage(title, TipoRicerca.title);
    }// end of method

    /**
     * Legge una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    public static Page leggePage(int pageId) {
        return leggePage("" + pageId, TipoRicerca.pageid);
    }// end of method

    /**
     * Legge una pagina
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return pagina (con i metadati mediawiki)
     */
    public static Page leggePage(String titlePageid, TipoRicerca tipoRicerca) {
        Page page = null;
        String contenuto = leggePagina(titlePageid, tipoRicerca);

        if (contenuto != null && !contenuto.equals("")) {
            page = new Page(contenuto);
        }// fine del blocco if

        return page;
    }// end of method

    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public static String leggePagina(String title) {
        return leggePagina(title, TipoRicerca.title);
    }// end of method

    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public static String leggePagina(int pageId) {
        return leggePagina("" + pageId, TipoRicerca.pageid);
    }// end of method

    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public static String leggePagina(String titlePageid, TipoRicerca tipoRicerca) {
        QueryWiki query = null;

        if (tipoRicerca == TipoRicerca.title) {
            query = new QueryReadTitle(titlePageid);
        }// fine del blocco if

        if (tipoRicerca == TipoRicerca.pageid) {
            query = new QueryReadPageid(titlePageid);
        }// fine del blocco if

        if (query != null && query.isTrovata()) {
            return query.getContenuto();
        } else {
            return "";
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
        return leggeVoce(title, TipoRicerca.title);
    }// end of method

    /**
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public static String leggeVoce(int pageId) {
        return leggeVoce("" + pageId, TipoRicerca.pageid);
    }// end of method

    /**
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public static String leggeVoce(String titlePageid, TipoRicerca tipoRicerca) {
        String testo = "";
        Page pagina = leggePage(titlePageid, tipoRicerca);

        if (pagina != null) {
            testo = pagina.getText();
        }// fine del blocco if

        return testo;
    }// end of method

    /**
     * Legge un template da una voce
     * <p>
     *
     * @param title della pagina
     * @param tag   nome del template
     * @return contenuto del template
     */
    public static String leggeTmpl(String title, String tag) {
        return leggeTmpl(title, TipoRicerca.title, tag);
    }// end of method

    /**
     * Legge un template da una voce
     * <p>
     *
     * @param pageId della pagina
     * @param tag    nome del template
     * @return contenuto del template
     */
    public static String leggeTmpl(int pageId, String tag) {
        return leggeTmpl("" + pageId, TipoRicerca.pageid, tag);
    }// end of method

    /**
     * Legge un template da una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param tag         nome del template
     * @return contenuto del template
     */
    public static String leggeTmpl(String titlePageid, TipoRicerca tipoRicerca, String tag) {
        String tmpl = "";
        String testo = leggeVoce(titlePageid, tipoRicerca);

        if (!testo.equals("") && !tag.equals("")) {
            tmpl = LibWiki.estraeTmplCompresi(testo, tag);
        }// fine del blocco if
        return tmpl;
    }// end of method

    /**
     * Legge un template di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @param params      passati
     * @return contenuto del template
     */
    public static String leggeTmpl(String titlePageid, TipoRicerca tipoRicerca, Map params) {
        String tmpl = "";
        String tag = "";
        String testo = leggeVoce(titlePageid, tipoRicerca);

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
        return leggeTmplBio(title, TipoRicerca.title);
    }// end of method

    /**
     * Legge il template bio di una voce
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto del template bio
     */
    public static String leggeTmplBio(int pageId) {
        return leggeTmplBio("" + pageId, TipoRicerca.pageid);
    }// end of method

    /**
     * Legge il template bio di una voce
     * <p>
     *
     * @param titlePageid (title oppure pageid)
     * @param tipoRicerca title o pageId
     * @return contenuto del template bio
     */
    public static String leggeTmplBio(String titlePageid, TipoRicerca tipoRicerca) {
        String testoTemplate = "";
        String testo = leggeVoce(titlePageid, tipoRicerca);

        if (!testo.equals("")) {
            testoTemplate = LibWiki.estraeTmplBioCompresi(testo);
        }// fine del blocco if

        return testoTemplate;
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
     * Legge dal server wiki
     * Registra la tavola WikiBio
     * <p>
     *
     * @param title della pagina
     */
    public static void downloadBio(String title) {
        Page pagina = leggePage(title);
        downloadBio(pagina);
    }// end of method

    /**
     * Legge dal server wiki
     * Registra la tavola WikiBio
     * <p>
     *
     * @param pageId della pagina
     */
    public static void downloadBio(int pageId) {
        Page pagina = leggePage(pageId);
        downloadBio(pagina);
    }// end of method


    /**
     * Legge dal server wiki
     * Registra la tavola  WikiBio
     * <p>
     *
     * @param tmplBio template bio
     */
    public static void saveBio(String tmplBio) {
        HashMap mappa;
        WikiBio wiki;
        String testoVoce;

        if (tmplBio != null) {
            wiki = new WikiBio();
            wiki.setTmplBio(tmplBio);
            wiki.save();
            int a = 89;
//            mappa = pagina.getMappa();
//            wiki = (WikiBio) fixMappa(wiki, mappa);
//            wiki.tmplBio = tmplBio;
//            if (Pref.getBool(LibWiki.USA_FLASH_TRUE_DOWNLOAD)) {
//                wiki.save(flush: true);
//            } else {
//                wiki.save(flush: false);
//            }// fine del blocco if-else
        }// fine del blocco if

    }// end of method


    /**
     * Legge dal server wiki
     * Registra la tavola  WikiBio
     * <p>
     *
     * @param pagina dal server
     */
    public static void downloadBio(Page pagina) {
        HashMap mappa;
        WikiBio wiki;
        String testoVoce;
        String tmplBio;

        testoVoce = pagina.getText();
        tmplBio = estraeTmplBio(testoVoce);

        if (tmplBio != null) {
            wiki = new WikiBio();
            mappa = pagina.getMappa();
            wiki = (WikiBio) fixMappa(wiki, mappa);
//            wiki.setTmplBio(tmplBio);
            wiki.save();
        }// fine del blocco if

    }// end of method

    /**
     * Regola i parametri della tavola in base alla mappa letta dal server
     * Aggiunge le date di riferimento lettura/scrittura
     */
    private static Wiki fixMappa(Wiki wiki, HashMap mappa) {
        List<PagePar> lista = PagePar.getPerm();
        String key;
        Object value;

        for (PagePar par : lista) {
            key = par.toString();
            value = null;

            if (mappa.get(key) != null) {
                value = mappa.get(key);
            }// fine del blocco if

            //--controllo degli interi che POSSONO esser anche zero
            if (par.getType() == PagePar.TypeField.integerzero) {
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

            par.setWiki(wiki,value);
        } // fine del ciclo for-each

//        lista?.each {
//            key = it
//            value = null
//            if (mappa["${key}"]) {
//                value = mappa["${key}"]
//            }// fine del blocco if
//
//            //--controllo degli interi che POSSONO esser anche zero
//            if (it.type == PagePar.TypeField.integerzero) {
//                if (!value) {
//                    value = 0
//                }// fine del blocco if
//            }// fine del blocco if
//
//            //--patch
//            if (it == PagePar.comment) {
//                if (value in String) {
//                    if (value.startsWith('[[WP:OA|←]]')) {
//                        value = 'Nuova pagina'
//                    }// fine del blocco if
//                }// fine del blocco if
//            }// fine del blocco if
//
//            try { // prova ad eseguire il codice
//                wiki."${key}" = value
//            } catch (Exception unErrore) { // intercetta l'errore
//                log.error unErrore + ' : key= ' + key + ' value= ' + value
//            }// fine del blocco try-catch
//        } // fine del ciclo each

        return wiki;
    }// end of method

}// end of service class