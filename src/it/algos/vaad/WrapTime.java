package it.algos.vaad;

import it.algos.vaad.lib.VaadWiki;
import it.algos.webbase.web.lib.LibTime;

import java.sql.Timestamp;


/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 27-8-13
 * Time: 19:20
 */
public class WrapTime {

    private long pageid;
    private Timestamp timestamp;


    public WrapTime(long pageid, Timestamp timestamp) {
        this.setPageid(pageid);
        this.setTimestamp(timestamp);
    }// fine del metodo costruttore

    public WrapTime(long pageid, String timestampStr) {
        this.setPageid(pageid);
        this.setTimestamp(VaadWiki.getWikiTime(timestampStr));
    }// fine del metodo costruttore


    public long getPageid() {
        return pageid;
    }// end of getter method

    public void setPageid(long pageid) {
        this.pageid = pageid;
    }//end of setter method

    public Timestamp getTimestamp() {
        return timestamp;
    }// end of getter method

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }//end of setter method

} // fine della classe
