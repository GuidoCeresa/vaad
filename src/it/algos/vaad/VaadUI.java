package it.algos.vaad;

import com.vaadin.annotations.Theme;
import it.algos.vaad.wiki.LibWiki;
import it.algos.webbase.domain.log.LogMod;
import it.algos.webbase.domain.pref.PrefMod;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.vaad.ui.VaadAlgosUI;
import it.algos.webbase.web.lib.LibPath;
import it.algos.webbase.web.module.ModulePop;

//@Theme("valo")
@Theme("algos")
public class VaadUI extends VaadAlgosUI {

    private ModulePop moduloPartenza;

    /**
     * Crea i menu specifici
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addAllModuli() {
//        moduloPartenza = new VersMod();

//        this.addModulo(new PrefMod());
//        this.addModulo(new LogMod());
//        this.addModulo(moduloPartenza);
    }// end of method

    /**
     * Modulo da visualizzare alla partenza
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected String startModulo() {
//        return LogMod.MENU_ADDRESS;
        LibWiki.download("Nicola Conte (ufficiale)");
        return LibPath.getClassName(moduloPartenza);
    }// end of method

}//end of UI class
