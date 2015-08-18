package it.algos.vaad.pref;

/**
 * Created by Gac on 17 lug 2015.
 * Typi usati nelle preferenze
 */
public enum TypePref {
    stringa("'stringa'", "'stringa'", "'stringa'"),
    testo("testo", "testo", "testo"),
    booleano("booleano", "booleano", "booleano"),
    intero("intero", "intero", "intero"),
    lungo("lungo", "lungo", "lungo"),
    reale("reale", "reale", "reale"),
    doppio("doppio", "doppio", "doppio"),
    decimale("decimale", "decimale", "decimale"),
    data("data", "data", "data");

    String nomeDB;

    String nomeListView;

    String nomeFormView;

    TypePref(String nomeDB, String nomeListView, String nomeFormView) {
        this.setNomeDB(nomeDB);
        this.setNomeListView(nomeListView);
        this.setNomeFormView(nomeFormView);
    }// fine del costruttore

    String getNomeDB() {
        return nomeDB;
    }

    void setNomeDB(String nomeDB) {
        this.nomeDB = nomeDB;
    }

    String getNomeListView() {
        return nomeListView;
    }

    void setNomeListView(String nomeListView) {
        this.nomeListView = nomeListView;
    }

    String getNomeFormView() {
        return nomeFormView;
    }

    void setNomeFormView(String nomeFormView) {
        this.nomeFormView = nomeFormView;
    }
}// fine della classe Enumeration
