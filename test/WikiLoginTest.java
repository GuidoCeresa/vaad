import it.algos.vaad.wiki.WikiLogin;
import it.algos.vaad.wiki.ErrLogin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 27 set 2015.
 * .
 */
public class WikiLoginTest {

    private static final String NICK = "biobot";
    private static final String PASS = "fulvia";

    private WikiLogin wikiLogin;

    @Test
    public void login() {
        wikiLogin = new WikiLogin(NICK, PASS);
        assertNotNull(wikiLogin);
        assertTrue(wikiLogin.isValido());
        assertEquals(wikiLogin.getFirstResult(), ErrLogin.needToken);
        assertEquals(wikiLogin.getRisultato(), ErrLogin.success);
        assertTrue(wikiLogin.getToken().length() > 20);
        assertEquals(wikiLogin.getCookieprefix(), "itwiki");
        assertTrue(wikiLogin.getSessionId().length() > 20);
    }// end of single test

}// end of testing class
