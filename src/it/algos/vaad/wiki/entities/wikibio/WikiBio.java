package it.algos.vaad.wiki.entities.wikibio;

import it.algos.vaad.wiki.entities.wiki.Wiki;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@Entity
public class WikiBio extends Wiki {

    @Column(length = 5000)
    private String tmplBio;

    private long sizeBio;

    public WikiBio() {
        super();
    }// end of constructor


    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(WikiBio.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method


    public synchronized static ArrayList<Long> findAllPageid() {
        ArrayList<Long> lista = null;
        List<WikiBio> listaAll = findAll();

        if (listaAll != null && listaAll.size() > 0) {
            lista = new ArrayList<Long>();
            for (WikiBio bio : listaAll) {
                lista.add(bio.getPageid());
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    }// end of method

    public synchronized static List findAll() {
        return (List<WikiBio>) AQuery.getList(WikiBio.class);
    }// end of method

    public String getTmplBio() {
        return tmplBio;
    }

    public void setTmplBio(String tmplBio) {
        this.tmplBio = tmplBio;
    }

    public long getSizeBio() {
        return sizeBio;
    }

    public void setSizeBio(long sizeBio) {
        this.sizeBio = sizeBio;
    }
}// end of entity class
