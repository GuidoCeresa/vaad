package it.algos.vaad.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.algos.vaad.base.AlgosNavigator;
import it.algos.vaad.base.MenuCommand;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.lib.LibPath;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.ui.AlgosUI;

//@Theme("valo")
@Theme("asteriacultura")
public abstract class VaadAlgosUI extends AlgosUI {

    protected static final boolean DEBUG_GUI = true;
    private MenuBar mainBar = new MenuBar();


    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        startUI();
    }// end of method


    /**
     * Mostra la UI
     */
    private void startUI() {

        // crea la UI di base, un VerticalLayout
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        vLayout.setSpacing(false);
        vLayout.setSizeFull();
        if (DEBUG_GUI) {
            vLayout.addStyleName("blueBg");
        }// fine del blocco if

        // crea la MenuBar principale
        this.addAllModuli();
//        vLayout.addComponent(mainBar);

        // aggiunge la menubar principale e la menubar login
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setHeight("40px");
        menuLayout.setWidth("100%");
        menuLayout.addComponent(mainBar);
        mainBar.setHeight("100%");
        menuLayout.setExpandRatio(mainBar, 1.0f);

        if (AlgosApp.USE_SECURITY) {
            MenuBar loginBar = createLoginMenuBar();
            loginBar.setHeight("100%");
            menuLayout.addComponent(loginBar);
        }// fine del blocco if

        vLayout.addComponent(menuLayout);

        // crea e aggiunge uno spaziatore verticale
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setMargin(false);
        spacer.setSpacing(false);
        spacer.setHeight("5px");
        vLayout.addComponent(spacer);
//
        // crea e aggiunge il placeholder dove il Navigator inserirà le varie pagine
        // a seconda delle selezioni di menu
        NavPlaceholder placeholder = new NavPlaceholder(null);
        placeholder.setSizeFull();
        if (DEBUG_GUI) {
            placeholder.addStyleName("yellowBg");
        }// fine del blocco if
        vLayout.addComponent(placeholder);
        vLayout.setExpandRatio(placeholder, 1.0f);

        // assegna la UI
        setContent(vLayout);

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        AlgosNavigator nav = new AlgosNavigator(getUI(), placeholder);
        nav.configureFromMenubar(mainBar);
        nav.navigateTo(startModulo());

//        // set browser window title
//        Page.getCurrent().setTitle("Sistemare");

    }// end of method

    /**
     * Crea la menubar di Login
     */
    private MenuBar createLoginMenuBar() {
        MenuBar menubar = new MenuBar();
        MenuBar.MenuItem loginItem; // il menuItem di login


        ThemeResource icon = new ThemeResource("img/action_user.png");
//        MenuBar.Command command = new MenuBar.Command() {
//
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//                loginCommandSelected();
//            }
//        };

        loginItem = menubar.addItem("Login", null, null);
//        updateLoginUI();

//        loginItem.addItem("Logout", new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//                logout();
//            }
//        });

        return menubar;
    }


    /**
     * Crea i menu specifici
     * Sovrascritto nella sottoclasse
     */
    protected void addAllModuli() {
    }// end of method

    /**
     * Modulo da visualizzare alla partenza
     * Sovrascritto nella sottoclasse
     */
    protected String startModulo() {
        return "";
    }// end of method

    /**
     * Crea il singolo menu
     */
    protected void addModulo(ModulePop modulo) {
        UI ui = this.getUI();
        String address = "";

        if (modulo instanceof AlgosModulePop) {
            address = ((AlgosModulePop) modulo).getMenuAddress();
        } else {
            address = LibPath.getClassName(modulo.getEntityClass());
        }// fine del blocco if-else

        MenuCommand command = new MenuCommand(ui, mainBar, address, modulo);
        mainBar.addItem(address, null, command);
    }// end of method


}//end of UI abstract class
