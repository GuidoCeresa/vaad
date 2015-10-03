package it.algos.vaad;

import com.vaadin.annotations.Theme;
import it.algos.vaad.ui.VaadAlgosUI;
import it.algos.vaad.wiki.WikiLogin;
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
        new WikiLogin("biobot","fulvia");

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
//        LibWiki.download("Nicola Conte (ufficiale)");

//        return LibPath.getClassName(moduloPartenza);
        return "";
    }// end of method

}//end of UI class
