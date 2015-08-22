package it.algos.vaad.log;

import it.algos.vaad.wiki.LibWiki;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;

@Entity
public class Log extends BaseEntity {

    @NotNull
    private Livello livello;
    @NotEmpty
    private String code;
    @NotEmpty
    private String descrizione;
    @NotNull
    private Timestamp time;


    public Log() {
        super();
    }// end of constructor

    public Log(Livello livello, String code, String descrizione, Timestamp time) {
        super();
        this.setLivello(livello);
        this.setCode(code);
        this.setDescrizione(descrizione);
        this.setTime(time);
    }// end of constructor

    /**
     * Recupera una istanza di Log usando la query specifica
     *
     * @return istanza di Log, null se non trovata
     */
    public static Log find(long id) {
        Log instance = null;
        BaseEntity entity = AQuery.queryById(Log.class, id);

        if (entity != null) {
            if (entity instanceof Log) {
                instance = (Log) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Log usando la query specifica
     *
     * @return istanza di Log, null se non trovata
     */
    public static Log find(String code) {
        Log instance = null;
        BaseEntity entity = AQuery.queryOne(Log.class, Log_.code, code);

        if (entity != null) {
            if (entity instanceof Log) {
                instance = (Log) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Log.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    public synchronized static ArrayList<Log> findAll() {
        return (ArrayList<Log>) AQuery.getList(Log.class);
    }// end of method

    //--registra un avviso
    public static void setInfo(String code, String descrizione) {
        setBase(Livello.info, code, descrizione);
    }// fine del metodo

    //--registra un avviso
    public static void setWarn(String code, String descrizione) {
        setBase(Livello.warn, code, descrizione);
    }// fine del metodo

    //--registra un avviso
    public static void setError(String code, String descrizione) {
        setBase(Livello.error, code, descrizione);
    }// fine del metodo

    //--registra un evento generico
    private static void setBase(Livello livello, String code, String descrizione) {
        Log logo = new Log(livello, code, descrizione, LibWiki.getTime());
        logo.save();
    }// fine del metodo statico

    //--registra un evento generico
    private static void setBase(Livello livello, String code, String descrizione, Timestamp time) {
        Log logo = new Log(livello, code, descrizione, time);
        logo.save();
    }// fine del metodo statico

    @Override
    public String toString() {
        return code;
    }// end of method

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Livello getLivello() {
        return livello;
    }

    public void setLivello(Livello livello) {
        this.livello = livello;
    }

    @Override
    public Log clone() throws CloneNotSupportedException {
        try {
            return (Log) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of entity class
