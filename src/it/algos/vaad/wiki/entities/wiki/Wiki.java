package it.algos.vaad.wiki.entities.wiki;

import it.algos.webbase.domain.versione.Versione_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@Entity
public class Wiki extends BaseEntity {

    /**
     * nomi interni dei campi (ordine non garantito)
     */
    //--parametri wiki base
    @Column(unique = true)
    private long pageid;

    private long ns;
    private String title;

    //--parametri wiki info
    private String pagelanguage;
//    private int lastrevid;
//    private int length;

    //--parametri wiki revisions
    private long revid;
    private long parentid;
    private boolean minor;
    private String user;
    private boolean anon;
    private long userid;
    private Date timestamp;
    private long size;
    private String comment;
    private String contentformat;
    private String contentmodel;

    //--forse
//    private String starttimestamp;
//    private String testo; //contenuto completo della pagina


    public Wiki() {
    }// end of constructor

    /**
     * Recupera una istanza di Versione usando la query specifica
     *
     * @return istanza di Versione, null se non trovata
     */
    public static Wiki find(long id) {
        Wiki instance = null;
        BaseEntity entity = AQuery.queryById(Wiki.class, id);

        if (entity != null) {
            if (entity instanceof Wiki) {
                instance = (Wiki) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Versione usando la query specifica
     *
     * @return istanza di Versione, null se non trovata
     */
    public static Wiki find(String titolo) {
        Wiki instance = null;
        BaseEntity entity = AQuery.queryOne(Wiki.class, Versione_.titolo, titolo);

        if (entity != null) {
            if (entity instanceof Wiki) {
                instance = (Wiki) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Wiki.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    public synchronized static List<Wiki> findAllWiki() {
        return (List<Wiki>) AQuery.getList(Wiki.class);
    }// end of method

    @Override
    public String toString() {
        return title;
    }// end of method

    public long getPageid() {
        return pageid;
    }

    public void setPageid(long pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNs() {
        return ns;
    }

    public void setNs(long ns) {
        this.ns = ns;
    }

    public String getContentmodel() {
        return contentmodel;
    }

    public void setContentmodel(String contentmodel) {
        this.contentmodel = contentmodel;
    }

    public String getPagelanguage() {
        return pagelanguage;
    }

    public void setPagelanguage(String pagelanguage) {
        this.pagelanguage = pagelanguage;
    }

    public long getRevid() {
        return revid;
    }

    public void setRevid(long revid) {
        this.revid = revid;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContentformat() {
        return contentformat;
    }

    public void setContentformat(String contentformat) {
        this.contentformat = contentformat;
    }

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public boolean isAnon() {
        return anon;
    }

    public void setAnon(boolean anon) {
        this.anon = anon;
    }
}// end of entity class
