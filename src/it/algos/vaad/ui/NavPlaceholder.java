package it.algos.vaad.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SingleComponentContainer;

/**
 * A Placeholder component which a Navigator can populate with different views
 */
@SuppressWarnings("serial")
public class NavPlaceholder extends CustomComponent implements SingleComponentContainer {


    public NavPlaceholder(Component content) {
        super();
        setContent(content);
        setSizeFull();
    }// end of constructor

    @Override
    public void addComponentAttachListener(ComponentAttachListener listener) {
    }// end of method

    @Override
    public void removeComponentAttachListener(ComponentAttachListener listener) {
    }// end of method

    @Override
    public void addComponentDetachListener(ComponentDetachListener listener) {
    }// end of method

    @Override
    public void removeComponentDetachListener(ComponentDetachListener listener) {
    }// end of method

    @Override
    public Component getContent() {
        return getCompositionRoot();
    }// end of method

    @Override
    public void setContent(Component content) {
        setCompositionRoot(content);
    }// end of method


}//end of class
