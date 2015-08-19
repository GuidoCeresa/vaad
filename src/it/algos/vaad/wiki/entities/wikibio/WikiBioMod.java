package it.algos.vaad.wiki.entities.wikibio;

import it.algos.vaad.ui.AlgosModulePop;
import it.algos.vaad.wiki.entities.wiki.Wiki_;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
public class WikiBioMod extends AlgosModulePop {

    // indirizzo interno del modulo (serve nei menu)
    public static String MENU_ADDRESS = "Wiki";


    public WikiBioMod() {
        super(WikiBio.class, MENU_ADDRESS);
    }// end of constructor

    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Non garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Wiki_.pageid, Wiki_.title, Wiki_.timestamp, Wiki_.user, Wiki_.comment};
    }// end of method

    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        return new WikiBioTablePortal(this);
    }// end of method

}// end of class
