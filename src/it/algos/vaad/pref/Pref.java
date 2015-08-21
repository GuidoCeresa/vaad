package it.algos.vaad.pref;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

@Entity
public class Pref extends BaseEntity {

    private int ordine;
    private TypePref type;

    @NotEmpty
    private String code;
    private String descrizione;
    private Date dateCreated;
    private Date lastUpdated;
    private String stringa;      // VARCHAR(255)
    private Boolean bool;     // BIT(1)
    private Integer intero;    // INTEGER
    private Long lungo;   // BIGINT(20)
    private Float reale;  // FLOAT
    private Double doppio; // DOUBLE
    private BigDecimal decimale;// DECIMAL(19,2)
    private Date data;// DATETIME
    private String testo;// LONGTEXT

    public Pref() {
        super();
    }// end of constructor


    /**
     * Recupera una istanza di Pref usando la query specifica
     *
     * @return istanza di Pref, null se non trovata
     */
    public static Pref find(long id) {
        Pref instance = null;
        BaseEntity entity = AQuery.queryById(Pref.class, id);

        if (entity != null) {
            if (entity instanceof Pref) {
                instance = (Pref) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method


    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Pref.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    public synchronized static ArrayList<Pref> findAll() {
        ArrayList<Pref> lista;
        Vector vettore = (Vector) AQuery.getList(Pref.class);
        lista = new ArrayList<Pref>(vettore);
        return lista;

    }// end of method

    public static Pref findByCode(String code) {
        ArrayList<Pref> lista = findAll();

        for (Pref pref : lista) {
            if (pref.getCode().equals(code)) {
                return pref;
            }// fine del blocco if
        } // fine del ciclo for-each

        return null;
    } // end of method

    public static Boolean getBool(String code) {
        return findByCode(code).getBool();
    } // end of method

    public static Boolean getBool(String code, boolean suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.booleano) {
            return pref.getBool();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    public static Integer getInt(String code) {
        return findByCode(code).getInt();
    } // end of method

    public static Integer getInt(String code, int suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.intero) {
            return pref.getInt();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    public Integer getInt() {
        if (type == TypePref.intero) {
            return intero;
        } else {
            return null;
        }// fine del blocco if-else
    } // end of method

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public TypePref getType() {
        return type;
    }

    public void setType(TypePref type) {
        this.type = type;
    }

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStringa() {
        return stringa;
    }

    public void setStringa(String stringa) {
        this.stringa = stringa;
    }

    public Integer getIntero() {
        return intero;
    }

    public void setIntero(Integer intero) {
        this.intero = intero;
    }

    public Long getLungo() {
        return lungo;
    }

    public void setLungo(Long lungo) {
        this.lungo = lungo;
    }

    public Float getReale() {
        return reale;
    }

    public void setReale(Float reale) {
        this.reale = reale;
    }

    public Double getDoppio() {
        return doppio;
    }

    public void setDoppio(Double doppio) {
        this.doppio = doppio;
    }

    public BigDecimal getDecimale() {
        return decimale;
    }

    public void setDecimale(BigDecimal decimale) {
        this.decimale = decimale;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Boolean getBool() {
        if (type == TypePref.booleano) {
            return bool;
        } else {
            return null;
        }// fine del blocco if-else
    } // end of method

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public Pref clone() throws CloneNotSupportedException {
        try {
            return (Pref) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of entity class
