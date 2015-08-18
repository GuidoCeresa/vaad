package it.algos.vaad.pref;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.vaad.ui.AlgosModulePop;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class PrefMod extends AlgosModulePop {

    // indirizzo interno del modulo (serve nei menu)
    public static String MENU_ADDRESS = "Preferenze";


    public PrefMod() {
        super(Pref.class, MENU_ADDRESS);
    }// end of constructor

    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionNew() {
        return "Nuova preferenza";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Modifica preferenza";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca preferenza";
    }// end of method

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{Pref_.ordine, Pref_.type, Pref_.code, Pref_.descrizione, Pref_.stringa, Pref_.bool, Pref_.intero, Pref_.lungo, Pref_.decimale, Pref_.data};
    }// end of method

    /**
     * Crea i campi visibili nella scheda (form)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{Pref_.ordine, Pref_.code, Pref_.type, Pref_.descrizione, Pref_.stringa, Pref_.bool, Pref_.intero, Pref_.lungo, Pref_.reale, Pref_.doppio, Pref_.decimale, Pref_.data, Pref_.testo};
    }// end of method

    /**
     * Crea i campi visibili
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Non garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Pref_.ordine, Pref_.code, Pref_.type, Pref_.descrizione};
    }// end of method


}// end of class
