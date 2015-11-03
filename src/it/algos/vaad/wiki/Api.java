package it.algos.vaad.wiki;

import it.algos.vaad.wiki.query.QueryReadPageid;
import it.algos.vaad.wiki.query.QueryReadTitle;
import it.algos.vaad.wiki.query.QueryWiki;

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
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    @SuppressWarnings("all")
    public static String leggePagina(String title) {
        try { // prova ad eseguire il codice
            Integer.decode(title);
            return leggePagina(title, TipoRicerca.pageid);
        } catch (Exception unErrore) { // intercetta l'errore
            return leggePagina(title, TipoRicerca.title);
        }// fine del blocco try-catch
    }// end of method

    /**
     * Legge il contenuto (tutto) di una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public static String leggePagina(long pageId) {
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

        if (query != null && query.isLetta()) {
            return query.getContenuto();
        } else {
            return "";
        }// fine del blocco if-else
    }// end of method


    /**
     * Legge una pagina
     * <p>
     *
     * @param title della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    @SuppressWarnings("all")
    public static Page leggePage(String title) {
        try { // prova ad eseguire il codice
            Integer.decode(title);
            return leggePage(title, TipoRicerca.pageid);
        } catch (Exception unErrore) { // intercetta l'errore
            return leggePage(title, TipoRicerca.title);
        }// fine del blocco try-catch
    }// end of method

    /**
     * Legge una pagina
     * <p>
     *
     * @param pageId della pagina
     * @return contenuto completo della pagina (con i metadati mediawiki)
     */
    public static Page leggePage(long pageId) {
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
     * Legge il contenuto (testo) di una voce
     * <p>
     *
     * @param title della pagina
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    @SuppressWarnings("all")
    public static String leggeVoce(String title) {
        try { // prova ad eseguire il codice
            Integer.decode(title);
            return leggeVoce(title, TipoRicerca.pageid);
        } catch (Exception unErrore) { // intercetta l'errore
            return leggeVoce(title, TipoRicerca.title);
        }// fine del blocco try-catch
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
     * @param pageId della pagina
     * @return contenuto (solo testo) della pagina (senza i metadati mediawiki)
     */
    public static String leggeVoce(long pageId) {
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

}// end of service class