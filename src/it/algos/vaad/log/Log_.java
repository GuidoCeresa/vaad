package it.algos.vaad.log;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Log.class)
public class Log_ extends BaseEntity_ {
	public static volatile SingularAttribute<Log, String> code;
	public static volatile SingularAttribute<Log, String> descrizione;
	public static volatile SingularAttribute<Log, Date> data;
}// end of entity class
