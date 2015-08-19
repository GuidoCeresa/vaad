package it.algos.vaad;

import com.vaadin.annotations.Theme;
import it.algos.vaad.wiki.Api;
import it.algos.vaad.wiki.WikiBioMod;
import it.algos.webbase.domain.versione.VersioneModulo;
import it.algos.vaad.log.LogMod;
import it.algos.vaad.pref.PrefMod;
import it.algos.vaad.ui.VaadAlgosUI;
import it.algos.webbase.web.lib.LibPath;
import it.algos.webbase.web.module.ModulePop;

@Theme("valo")
//@Theme("algos")
public class VaadUI extends VaadAlgosUI {

    private ModulePop moduloPartenza;

    /**
     * Crea i menu specifici
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addAllModuli() {
        moduloPartenza = new VersioneModulo();

        this.addModulo(new PrefMod());
        this.addModulo(new LogMod());
        this.addModulo(new WikiBioMod());
        this.addModulo(moduloPartenza);
    }// end of method

    /**
     * Modulo da visualizzare alla partenza
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected String startModulo() {
//        return LogMod.MENU_ADDRESS;
        return LibPath.getClassName(moduloPartenza);
    }// end of method

}//end of UI class
