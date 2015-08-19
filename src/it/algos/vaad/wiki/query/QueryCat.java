package it.algos.vaad.wiki.query;

import it.algos.vaad.wiki.Cost;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRequest;
import it.algos.vaad.wiki.TipoRicerca;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Query standard per leggere/scrivere il risultato di una pagina
 * NON legge le categorie
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Legge o scrive (a seconda della sottoclasse concreta utilizzata)
 * Legge le informazioni base della pagina (oltre al risultato)
 * Legge una sola Pagina con le informazioni base
 * Necessita di Login per scrivere, non per leggere solamente
 */
public class QueryCat extends QueryWiki {

    //--stringa per la lista di categoria
    private static String CAT = "&list=categorymembers";

    //--stringa selezionare il namespace (0=principale - 14=sottocategorie) (per adesso solo il principale)
    private static String NS = "&cmnamespace=0";

    //--stringa selezionare il tipo di categoria (page, subcat, file) (per adesso solo page)
    private static String TYPE = "&cmtype=page";

    //--stringa per ottenere il codice di continuazione
    private static String CONT = "&rawcontinue";

    //--stringa per selezionare il numero di valori in risposta
    private static String LIMIT = "&cmlimit=500";

    //--stringa per indicare il titolo della pagina
    private static String TITLE = "&cmtitle=Category:";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    private static String API_BASE_CAT = API_BASE + CAT + NS + TYPE + CONT + LIMIT + TITLE;

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";

    // lista di pagine della categoria (namespace=0)
    private ArrayList<Integer> listaPageids;

    /**
     * Costruttore completo
     */
    public QueryCat(String title) {
        super(title, TipoRicerca.title, TipoRequest.read);
    }// fine del metodo costruttore


    protected void inizializza(String titlepageid) {

        if (titlepageid != null) {
            title = titlepageid;
            pageid = "";
            domain = this.getDomain();
        }// fine del blocco if

        try { // prova ad eseguire il codice
            super.request();
            while (!continua.equals("")) {
                domain = this.getDomain();
                super.request();
            } // fine del blocco while
        } catch (Exception unErrore) { // intercetta l'errore
            errore = unErrore.getClass().getSimpleName();
        }// fine del blocco try-catch

    } // fine del metodo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = "";
        String titolo = "";
        String startDomain = API_BASE_CAT;

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, Cost.ENC);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (!titolo.equals("")) {
            domain = startDomain + titolo;
        }// fine del blocco if

        if (!continua.equals("")) {
            domain += CONTINUE + continua;
        }// fine del blocco if

        return domain;
    } // fine del metodo

    /**
     * Regola il risultato
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        ArrayList<Integer> lista;
        String txtContinua;

        lista = LibWiki.creaListaCatJson(risultatoRequest);
        if (lista != null) {
            this.addLista(lista);
        }// fine del blocco if

        txtContinua = LibWiki.creaCatContinue(risultatoRequest);
        this.continua = txtContinua;
    } // fine del metodo


    private void addLista(ArrayList<Integer> listaNew) {
        ArrayList<Integer> lista;

        lista = this.getListaPageids();
        if (lista == null) {
            lista = new ArrayList<Integer>();
        }// fine del blocco if

        for (Integer pageid : listaNew) {
            lista.add(pageid);
        } // fine del ciclo for-each

        this.setListaPageids(lista);
    } // fine del metodo

    public ArrayList<Integer> getListaPageids() {
        return listaPageids;
    } // fine del metodo

    void setListaPageids(ArrayList<Integer> listaPageids) {
        this.listaPageids = listaPageids;
    } // fine del metodo

    public String getTxtPageids() {
        return LibWiki.creaListaPageids(getListaPageids());
    } // fine del metodo


}// end of class
