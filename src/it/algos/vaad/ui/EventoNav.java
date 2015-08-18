package it.algos.vaad.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.vaad.base.MenuCommand;
import it.algos.webbase.web.screen.ErrorScreen;

import java.util.List;

@SuppressWarnings("serial")
public class EventoNav extends Navigator {

    public EventoNav(UI ui, SingleComponentContainer container) {
        super(ui, container);
        setErrorView(new ErrorView());
    }// end of constructor

    /**
     * Configura il Navigator in base a una MenuBar.
     * <p/>
     * Recupera i Component dalla Menubar e crea le View per il Navigator
     */
    public void configureFromMenubar(MenuBar mb) {
        List<MenuItem> items = mb.getItems();
        for (MenuItem item : items) {
            scanItem(item);
        } // fine del ciclo for
    }// end of method

    /**
     * Crea le View per il Navigator e vi aggiunge i
     * componenti referenziati dal MenuItem
     * (esegue ricorsivamente per i sottomenu).
     */
    private void scanItem(MenuItem item) {
        Command cmd = item.getCommand();
        if (cmd instanceof MenuCommand) {
            MenuCommand mcmd = (MenuCommand) cmd;
            String key = mcmd.getAddress();
            Component comp = mcmd.getComponent();
            View view = new NavigatorView(comp);
            addView(key, view);
        }// fine del blocco if

        // esamina i children dell'item
        List<MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuItem childItem : items) {
                scanItem(childItem);
            } // fine del ciclo for
        }// fine del blocco if
    }// end of method


    /**
     * A class encapsulating a Component in a View for the Navigator
     */
    class NavigatorView extends CustomComponent implements View {

        public NavigatorView(Component content) {
            super();
            setSizeFull();
            content.setSizeFull();
            setCompositionRoot(content);
        }// end of constructor


        @Override
        public void enter(ViewChangeEvent event) {
        }// end of method
    }//end of inner class


    /**
     * A class encapsulating an ErrorScreen in a View
     */
    class ErrorView extends CustomComponent implements View {

        ErrorScreen errScreen;

        public ErrorView() {
            super();
            setSizeFull();
            errScreen = new ErrorScreen();
            setCompositionRoot(errScreen);
        }// end of constructor

        @Override
        public void enter(ViewChangeEvent event) {
            String msg = "Questa pagina non esiste: " + event.getViewName();
            errScreen.setMessage(msg);
        }// end of method
    }//end of inner class


}//end of class
