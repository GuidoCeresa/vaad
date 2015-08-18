package it.algos.vaad.wiki;

import java.util.*;

public enum PagePar {

    //--parametri wiki base
    pageid(true, true, true, true, TypePar.letturascrittura, TypeField.integernotzero),
    ns(true, true, true, true, TypePar.letturascrittura, TypeField.integerzero),
    title(true, true, true, true, TypePar.letturascrittura, TypeField.string),

    //--parametri wiki info
    contentmodel(true, true, true, true, TypePar.letturascrittura, TypeField.string),
    pagelanguage(true, true, true, true, TypePar.letturascrittura, TypeField.string),
    touched(true, true, true, true, TypePar.letturascrittura, TypeField.date),
    lastrevid(true, true, true, true, TypePar.letturascrittura, TypeField.integernotzero),
    length(true, true, true, true, TypePar.letturascrittura, TypeField.integernotzero),
    csrftoken(false, false, false, true, TypePar.soloscrittura, TypeField.string),
    starttimestamp(false, false, false, true, TypePar.soloscrittura, TypeField.date),

    //--parametri wiki revisions
    revid(false, true, false, false, TypePar.letturascrittura, TypeField.integernotzero),
    parentid(false, true, false, false, TypePar.letturascrittura, TypeField.integerzero),
    user(false, true, false, false, TypePar.letturascrittura, TypeField.string),
    userid(false, true, false, false, TypePar.letturascrittura, TypeField.integerzero),
    timestamp(false, true, false, false, TypePar.letturascrittura, TypeField.date),
    size(false, true, false, false, TypePar.letturascrittura, TypeField.integernotzero),
    comment(false, true, false, false, TypePar.letturascrittura, TypeField.string),
    contentformat(true, true, true, false, TypePar.letturascrittura, TypeField.string),
    text(true, false, false, false, TypePar.letturascrittura, TypeField.string),

    //--parametri altri
    missing(false, false, false, false, TypePar.provvisorio, TypeField.string),
    revisions(false, false, false, false, TypePar.provvisorio, TypeField.string);

    private boolean obbligatorioRequest;
    private boolean permanente;
    private boolean obbligatorioDatabase;
    private boolean info;
    private TypePar typePar;
    private TypeField typeField;

    PagePar(boolean obbligatorioRequest, boolean permanente, boolean obbligatorioDatabase, boolean info, TypePar typePar, TypeField typeField) {
        this.obbligatorioRequest = obbligatorioRequest;
        this.permanente = permanente;
        this.obbligatorioDatabase = obbligatorioDatabase;
        this.info = info;
        this.typePar = typePar;
        this.typeField = typeField;
    }// fine del metodo costruttore

    /**
     * Restituisce una collezione di tutti gli elementi
     *
     * @return collezione
     */
    public static List<PagePar> getAll() {
        return (List<PagePar>) Arrays.asList(values());
    }// end of method

    /**
     * Restituisce il parametro, individuato dal nome
     *
     * @param key nome del parametro
     * @return parametro
     */
    public static PagePar getPar(String key) {
        PagePar pagePar = null;

        for (PagePar pageParTmp : values()) {
            if (key.equals(pageParTmp.toString())) {
                pagePar = pageParTmp;
            }// fine del blocco if
        } // fine del ciclo for-each

        return pagePar;
    }// end of method

    /**
     * Restituisce il tipo di campo di un parametro, individuato dal nome
     *
     * @param key nome del parametro
     * @return tipo di campo
     */
    public static TypeField getParField(String key) {
        return getPar(key).getType();
    }// end of method

    /**
     * Restituisce una collezione limitata agli elementi permanenti col flag info valido
     *
     * @return collezione
     */
    public static List<PagePar> getInf() {
        List<PagePar> lista = new ArrayList<PagePar>();
        TypePar typeParLoc;

        for (PagePar par : values()) {
            typeParLoc = par.typePar;
            if (typeParLoc != TypePar.provvisorio) {
                if (par.info) {
                    lista.add(par);
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione limitata agli elementi permanenti col flag info NON valido
     *
     * @return collezione
     */
    public static List<PagePar> getRev() {
        List<PagePar> lista = new ArrayList<PagePar>();
        TypePar typeParLoc;

        for (PagePar par : values()) {
            typeParLoc = par.typePar;
            if (typeParLoc != TypePar.provvisorio) {
                if (!par.info) {
                    lista.add(par);
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione degli elementi permanenti (per il database)
     *
     * @return collezione
     */
    public static List<PagePar> getPerm() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.permanente) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione degli elementi obbligatori (per la respons della request)
     *
     * @return collezione
     */
    public static List<PagePar> getObbReq() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.obbligatorioRequest) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione degli elementi obbligatori (per il save del database)
     *
     * @return collezione
     */
    public static List<PagePar> getObbDb() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.obbligatorioDatabase) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione degli elementi da restituire in lettura
     *
     * @return collezione
     */
    public static List<PagePar> getRead() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.typePar == TypePar.letturascrittura) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method

    /**
     * Restituisce una collezione degli elementi da restituire in lettura e scrittura
     *
     * @return collezione
     */
    public static List<PagePar> getWrite() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.typePar == TypePar.letturascrittura || par.typePar == TypePar.soloscrittura) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Controlla che tutti i parametri abbiano un valore valido (ai fini della registrazione sul database)
     *
     * @param mappa dei valori
     * @return true se tutti sono validi
     */
    public static boolean isParValidi(HashMap mappa) {
        boolean status = true;
        String key;
        Object value = null;

        for (PagePar par : getPerm()) {
            key = par.toString();
            value = mappa.get(key);
            if (par.obbligatorioDatabase) {
                if (!isParValido(par, value)) {
                    status = false;
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo for-each

        return status;
    }// end of method

    /**
     * Controlla che il parametro abbia un valore valido
     *
     * @param par   parametro
     * @param value del parametro
     * @return true se il valore è valido
     */
    private static boolean isParValido(PagePar par, Object value) {
        boolean status = false;
        TypeField type = par.getType();

        if (type == TypeField.string) {
            if (value != null && value instanceof String) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.integerzero) {
            if (value instanceof Integer) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.integernotzero) {
            if (value instanceof Integer && (Integer) value > 0) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.date) {
            if (value != null && value instanceof Date) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of method

    public TypeField getType() {
        return typeField;
    }// end of method

    /**
     * Enumeration di tipologie dei campi
     */
    public static enum TypePar {
        sololettura, letturascrittura, soloscrittura, provvisorio
    }// fine della Enumeration interna

    /**
     * Enumeration di tipologie dei campi
     */
    public static enum TypeField {
        string, integerzero, integernotzero, date
    }// fine della Enumeration interna

} // fine della Enumeration
