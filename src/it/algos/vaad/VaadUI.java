package it.algos.vaad;

import com.vaadin.annotations.Theme;
import it.algos.webbase.web.login.LoginEvent;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.ui.AlgosUI;

//@Theme("valo")
@Theme("algos")
public class VaadUI extends AlgosUI {

    private ModulePop moduloPartenza;

    /**
     * Crea i menu specifici
     * Sovrascritto nella sottoclasse
     */
//    @Override
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
//    @Override
    protected String startModulo() {
//        return LogMod.MENU_ADDRESS;
//        LibWiki.download("Nicola Conte (ufficiale)");

//        return LibPath.getClassName(moduloPartenza);
        return "";
    }// end of method

    @Override
    public void onUserLogin(LoginEvent loginEvent) {

    }
}//end of UI class
