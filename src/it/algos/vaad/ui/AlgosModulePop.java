package it.algos.vaad.ui;

import it.algos.webbase.web.module.ModulePop;

/**
 * Created by gac on 20 lug 2015.
 * Module displaying a table and allowing to edit the records in a form.
 */
@SuppressWarnings("serial")
public abstract class AlgosModulePop extends ModulePop {

    // indirizzo interno del modulo (serve nei menu)
    private String menuAddress;


    public AlgosModulePop(Class entity) {
        this(entity, "");
    }// end of constructor

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AlgosModulePop(Class entity, String menuAddress) {
        super(entity);
        this.setMenuAddress(menuAddress);
    }// end of constructor

    public String getMenuAddress() {
        return menuAddress;
    }// end of method

    private void setMenuAddress(String menuAddress) {
        this.menuAddress = menuAddress;
    }// end of method

}// end of abstract class
