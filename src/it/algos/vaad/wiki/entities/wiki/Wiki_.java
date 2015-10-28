package it.algos.vaad.wiki.entities.wiki;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
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

    public static volatile SingularAttribute<Wiki, String> pagelanguage;

    public static volatile SingularAttribute<Wiki, Long> revid;
    public static volatile SingularAttribute<Wiki, Long> parentid;

    public static volatile SingularAttribute<Wiki, Boolean> minor;
    public static volatile SingularAttribute<Wiki, String> user;
    public static volatile SingularAttribute<Wiki, Boolean> anon;
    public static volatile SingularAttribute<Wiki, Long> userid;
    public static volatile SingularAttribute<Wiki, Timestamp> timestamp;
    public static volatile SingularAttribute<Wiki, Long> size;
    public static volatile SingularAttribute<Wiki, String> comment;
    public static volatile SingularAttribute<Wiki, String> contentformat;
    public static volatile SingularAttribute<Wiki, String> contentmodel;

    public static volatile SingularAttribute<Wiki, Timestamp> ultimalettura;
}// end of entity class

