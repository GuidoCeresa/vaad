package it.algos.vaad.wiki.entities.wikibio;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import it.algos.vaad.wiki.Api;
import it.algos.webbase.web.dialog.EditDialog;
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

        // bottone Download...
        MenuBar.MenuItem item = toolbar.addButton("Download...", new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                EditDialog dialog = new EditDialog("Download", "Download","title or pageid", new EditDialog.EditListener() {
                    @Override
                    public void onClose() {
                    }// end of method

                    @Override
                    public void onClose(String value) {
                        Api.downloadBio(value);
                    }// end of method
                });// end of anonymous class
                dialog.show(UI.getCurrent());
            }// end of method
        });// end of anonymous class

        return toolbar;
    }// end of method

}// end of class
