package it.algos.vaad.pref;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.Date;

@StaticMetamodel(Pref.class)
public class Pref_ extends BaseEntity_ {
    public static volatile SingularAttribute<Pref, Integer> ordine;
    public static volatile SingularAttribute<TypePref, String> type;
    public static volatile SingularAttribute<Pref, String> code;
    public static volatile SingularAttribute<Pref, String> descrizione;
    public static volatile SingularAttribute<Pref, Date> dateCreated;
    public static volatile SingularAttribute<Pref, Date> lastUpdated;
    public static volatile SingularAttribute<Pref, String> stringa;
    public static volatile SingularAttribute<Pref, Boolean> bool;
    public static volatile SingularAttribute<Pref, Integer> intero;
    public static volatile SingularAttribute<Pref, Long> lungo;
    public static volatile SingularAttribute<Pref, Float> reale;
    public static volatile SingularAttribute<Pref, Double> doppio;
    public static volatile SingularAttribute<Pref, BigDecimal> decimale;
    public static volatile SingularAttribute<Pref, Date> data;
    public static volatile SingularAttribute<Pref, String> testo;
}// end of entity class

//TypePref type;

//String code;
//String descrizione;
//Date dateCreated;
//Date lastUpdated;
//String stringa;      // VARCHAR(255)

//Boolean bool;     // BIT(1)
//Integer intero;    // INTEGER
//Long lungo;   // BIGINT(20)
//Float reale;  // FLOAT
//Double doppio; // DOUBLE
//BigDecimal decimale;// DECIMAL(19,2)
//Date data;// DATETIME
//String testo;// LONGTEXT
