package it.algos.vaad.wiki.request;

import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.LibWiki;
import it.algos.vaad.wiki.TipoRisultato;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibNum;
import it.algos.webbase.web.lib.LibTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 01 feb 2016.
 * .
 */
public class RequestCat extends ARequest {

    /**
     * Il limite massimo ''for users'' è di 500
     * Il limite massimo ''for bots or sysops'' è di 5.000
     * Il limite 'max' equivale a 5.000
     */
    protected static final int LIMITE = 5000;

    protected static final String START_LIMIT_ERROR = "cmlimit may not be over";

    //--stringa per la lista di categoria
    protected static String CAT = "&list=categorymembers";

    //--stringa per selezionare il namespace (0=principale - 14=sottocategorie) ed il tipo di categoria (page, subcat, file)
    protected static String TYPE_VOCI = "&cmnamespace=0&cmtype=page";
    protected static String TYPE_ALL = "&cmnamespace=0|14&cmtype=page|subcat";

    //--stringa per selezionare il numero di valori in risposta
    protected static String TAG_LIMIT = "&cmlimit=";

    //--stringa per indicare il titolo della pagina
    protected static String TAG_TITOLO_CAT = "&cmtitle=Category:";

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";

    protected int limite;



    /**
     * Costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestCat(String wikiTitle) {
        super(wikiTitle);
    }// fine del metodo costruttore completo


    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected void elaboraParametri() {
        super.elaboraParametri();
        needContinua = true;
        needPost = false;
        needCookies = false;
        needLogin = true;
        needToken = false;
        needBot = true;
    }// fine del metodo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY + CAT + TYPE_ALL;

        if (wikiTitle != null && !wikiTitle.equals("")) {
            domainTmp += TAG_TITOLO_CAT + titleEncoded();
        }// end of if/else cycle

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        if (limite > 0) {
            domainTmp += TAG_LIMIT + limite;
        }// end of if cycle

        if (!tokenContinua.equals("")) {
            domainTmp += CONTINUE + tokenContinua;
        }// fine del blocco if

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, ArrayList> mappa;
        super.elaboraRisposta(rispostaRequest);
        String warningMessage = "";
        mappa = LibWiki.getMappaWrap(rispostaRequest);

        if (mappa == null) {
            if (Api.esiste("Category:" + titleEncoded())) {
                risultato = TipoRisultato.letta;
            } else {
                risultato = TipoRisultato.nonTrovata;
            }// end of if/else cycle
            valida = false;
            return;
        }// fine del blocco if

        if (mappa.get(LibWiki.KEY_VOCE_PAGEID) != null && mappa.get(LibWiki.KEY_VOCE_PAGEID).size() > 0) {
            this.listaVociPageids = LibArray.sommaDisordinata(this.listaVociPageids, mappa.get(LibWiki.KEY_VOCE_PAGEID));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_VOCE_TITLE) != null && mappa.get(LibWiki.KEY_VOCE_TITLE).size() > 0) {
            this.listaVociTitles = LibArray.sommaDisordinata(this.listaVociTitles, mappa.get(LibWiki.KEY_VOCE_TITLE));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_CAT_PAGEID) != null && mappa.get(LibWiki.KEY_CAT_PAGEID).size() > 0) {
            this.listaCatPageids = LibArray.sommaDisordinata(this.listaCatPageids, mappa.get(LibWiki.KEY_CAT_PAGEID));
        }// end of if cycle

        if (mappa.get(LibWiki.KEY_CAT_TITLE) != null && mappa.get(LibWiki.KEY_CAT_TITLE).size() > 0) {
            this.listaCatTitles = LibArray.sommaDisordinata(this.listaCatTitles, mappa.get(LibWiki.KEY_CAT_TITLE));
        }// end of if cycle

        risultato = TipoRisultato.letta;
        valida = true;
        tokenContinua = LibWiki.creaCatContinue(rispostaRequest);

        warningMessage = LibWiki.getWarning(rispostaRequest);
        if (warningMessage != null && warningMessage.startsWith(START_LIMIT_ERROR)) {
            risultato = TipoRisultato.limitOver;
        }// end of if cycle

    } // fine del metodo



} // fine della classe
