package it.algos.vaad.wiki.entities.wiki;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@StaticMetamodel(Wiki.class)
public class Wiki_ extends BaseEntity_ {
    public static volatile SingularAttribute<Wiki, Long> pageid;
    public static volatile SingularAttribute<Wiki, String> title;
    public static volatile SingularAttribute<Wiki, Long> ns;

    public static volatile SingularAttribute<Wiki, String> contentmodel;
    public static volatile SingularAttribute<Wiki, String> pagelanguage;
    public static volatile SingularAttribute<Wiki, String> touched;
    public static volatile SingularAttribute<Wiki, Long> lastrevid;
    public static volatile SingularAttribute<Wiki, Long> length;

    public static volatile SingularAttribute<Wiki, Long> revid;
    public static volatile SingularAttribute<Wiki, Long> parentid;
    public static volatile SingularAttribute<Wiki, String> user;
    public static volatile SingularAttribute<Wiki, Date> timestamp;
    public static volatile SingularAttribute<Wiki, Long> userid;
    public static volatile SingularAttribute<Wiki, Long> size;
    public static volatile SingularAttribute<Wiki, String> comment;
    public static volatile SingularAttribute<Wiki, String> contentformat;
}// end of entity class

