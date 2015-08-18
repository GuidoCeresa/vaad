package it.algos.vaad.pref;

import java.util.ArrayList;

/**
 * Created by Gac on 17 lug 2015.
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 23-9-12
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
class LibPref {

    public static int getNewOrdine() {
        Object value;
        int ordine = 1;
        Object obj=null;
        ArrayList lista;

//        obj = Pref.executeQuery('select max(ordine) from Pref');

        if (obj != null && obj instanceof ArrayList) {
            lista = (ArrayList) obj;
            if (lista.size() > 0) {
                value = lista.get(0);
                if (value != null && value instanceof Integer) {
                    ordine = (Integer) value;
                    ordine++;
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return ordine;
    }// fine del metodo

}// fine della classe libreria
