package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by gac on 08 nov 2015.
 * <p>
 * Rif: https://www.mediawiki.org/wiki/API:Backlinks
 * Lists pages that link to a given page, similar to Special:Whatlinkshere. Ordered by linking page title.
 * <p>
 * Parametrs:
 * bltitle: List pages linking to this title. The title does not need to exist.
 * blnamespace: Only list pages in these namespaces
 * blfilterredir: How to filter redirects (Default: all)
 * - all: List all pages regardless of their redirect flag
 * - redirects: Only list redirects
 * - nonredirects: Don't list redirects
 * bllimit: Maximum amount of pages to list. Maximum limit is halved if blredirect is set. No more than 500 (5000 for bots) allowed. (Default: 10)
 * blredirect: If set, pages linking to bltitle through a redirect will also be listed. See below for more detailed information.
 * blcontinue: Used to continue a previous request
 * <p>
 * Es:
 * https://it.wikipedia.org/w/api.php?action=query&list=backlinks&bltitle=Piozzano&format=jsonfm
 */
public class QueryBacklinks extends QueryWiki {

    protected static String TAG_BACK = "&list=backlinks&bllimit=max";
    protected static String TAG_NS = "&blnamespace=0";
    protected static String TAG_TITOLO = "&bltitle=";

    private boolean nameSpacePrincipale;

    // lista di pagine della categoria (namespace=0)
    private ArrayList<Long> listaPageids;
    private ArrayList<String> listaTitles;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryBacklinks(String title) {
        this(title, true);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryBacklinks(String title, boolean nameSpacePrincipale) {
        this.tipoRicerca = TipoRicerca.title;
        super.tipoRequest = TipoRequest.read;
        this.nameSpacePrincipale = nameSpacePrincipale;
        super.doInit(title);
    }// fine del metodo costruttore


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String titolo = "";
        String tag;

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (nameSpacePrincipale) {
            tag = API_BASE + TAG_BACK + TAG_NS + TAG_TITOLO;
        } else {
            tag = API_BASE + TAG_BACK + TAG_TITOLO;
        }// end of if/else cycle

        return tag + titolo;
    } // fine del metodo


    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        ArrayList<Long> listaLong;
        ArrayList<String> listaTxt;
        String txtContinua;

        listaLong = LibWiki.creaListaBackJson(risultatoRequest);
        listaTxt = LibWiki.creaListaBackTxtJson(risultatoRequest);

        if (listaLong != null) {
            listaPageids = listaLong;
        }// fine del blocco if

        if (listaTxt != null) {
            listaTitles = listaTxt;
        }// fine del blocco if

        super.regolaRisultato(risultatoRequest);
    } // fine del metodo


    /**
     * Controlla di aver trovato la pagina e di aver letto un contenuto valido
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isLetta() {
        boolean valida = false;
        QueryReadTitle query;

        if (listaPageids != null && listaPageids.size() > 0) {
            valida = true;
        } else {
            query = new QueryReadTitle(title);
            if (query.isLetta()) {
                valida = true;
            }// end of if cycle
        }// end of if/else cycle

        return valida;
    } // fine del metodo


    public ArrayList<Long> getListaPageids() {
        return listaPageids;
    }// end of getter method

    public void setListaPageids(ArrayList<Long> listaPageids) {
        this.listaPageids = listaPageids;
    }//end of setter method

    public ArrayList<String> getListaTitles() {
        return listaTitles;
    }// end of getter method

    public void setListaTitles(ArrayList<String> listaTitles) {
        this.listaTitles = listaTitles;
    }//end of setter method

} // fine della classe
