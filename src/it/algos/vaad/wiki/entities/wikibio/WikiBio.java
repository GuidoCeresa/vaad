package it.algos.vaad.wiki.entities.wikibio;

import it.algos.vaad.wiki.entities.wiki.Wiki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@Entity
public class WikiBio extends Wiki {

    @Column(length = 5000)
    private String tmplBio;

    public String getTmplBio() {
        return tmplBio;
    }

    public void setTmplBio(String tmplBio) {
        this.tmplBio = tmplBio;
    }
}// end of entity class
