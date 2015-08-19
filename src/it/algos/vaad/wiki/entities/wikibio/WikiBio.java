package it.algos.vaad.wiki.entities.wikibio;

import it.algos.vaad.wiki.entities.wiki.Wiki;

import javax.persistence.Entity;

/**
 * Created by gac on 18 ago 2015.
 */
@Entity
public class WikiBio extends Wiki {

    private String tmplBio;

    public String getTmplBio() {
        return tmplBio;
    }

    public void setTmplBio(String tmplBio) {
        this.tmplBio = tmplBio;
    }
}// end of entity class
