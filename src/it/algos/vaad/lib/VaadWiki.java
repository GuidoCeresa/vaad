package it.algos.vaad.lib;

/**
 * Created by gac on 04 ago 2015.
 * .
 */
public abstract class VaadWiki {
    /**
     * Tries to convert an Object in int.
     *
     * @param obj to convert
     * @return the corresponding int
     */
    public static int getInt(Object obj) {
        int intero = 0;

        if (obj == null) {
            return 0;
        }// fine del blocco if

        if (obj instanceof Number) {
            Number number = (Number) obj;
            intero = number.intValue();
            return intero;
        }// fine del blocco if

        if (obj instanceof String) {
            String string = (String) obj;
            try { // prova ad eseguire il codice
                intero = Integer.parseInt(string);
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            return intero;
        }// fine del blocco if

        return 0;
    }// end of static method

}// end of static class
