package it.algos.vaad.wiki;
/**
 * Created by Gac on 05 ago 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Pagina.class)
public class Pagina_ extends BaseEntity_ {
    public static volatile SingularAttribute<Pagina, String> pippo;
}// end of entity class
