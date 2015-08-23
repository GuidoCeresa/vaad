package it.algos.vaad.lib;

/**
 * Created by gac on 22 ago 2015.
 * .
 */
public abstract class LibVaad {

    /**
     * Chiude il template
     * <p>
     *
     * @param inizio del controllo
     * @return template
     */
    public static String getTime(long inizio) {
        long fine = System.currentTimeMillis();
        long durata = fine - inizio;

        durata = durata / 1000;

        return durata + " sec.";
    } // fine del metodo statico

}// end of static class
