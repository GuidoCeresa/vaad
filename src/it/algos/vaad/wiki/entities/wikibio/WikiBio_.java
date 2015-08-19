package it.algos.vaad.wiki.entities.wikibio;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@StaticMetamodel(WikiBio.class)
public class WikiBio_ extends BaseEntity_ {
    public static volatile SingularAttribute<WikiBio, String> tmplBio;
}// end of entity class
