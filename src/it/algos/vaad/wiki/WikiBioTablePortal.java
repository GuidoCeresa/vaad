package it.algos.vaad.wiki;

import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@SuppressWarnings("serial")
public class WikiBioTablePortal extends TablePortal {

    public WikiBioTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        final TableToolbar toolbar = super.createToolbar();

        // bottone Test...
        MenuBar.MenuItem item = toolbar.addButton("Test...", new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Api.downloadBio("Nicola Conte (ufficiale)");
//                Api.downloadBio("Giuliana De Sio");
                Api.downloadBio("Bernardo Bellotto");
            }// end of method
        });// end of anonymous class

        return toolbar;
    }// end of method

}// end of class
