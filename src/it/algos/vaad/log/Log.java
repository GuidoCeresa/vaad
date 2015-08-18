package it.algos.vaad.log;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class Log extends BaseEntity {

    @NotEmpty
    private String code;
    private String descrizione;
    private Date data;
//	Evento evento
//	String utente
//	String ruolo
//	Livello livello
//	String ip

    public Log() {
        this("");
    }// end of constructor

    public Log(String code) {
        super();
        this.setCode(code);
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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
