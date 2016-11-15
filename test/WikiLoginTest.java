import it.algos.vaad.wiki.ErrLogin;
import it.algos.vaad.wiki.WikiLogin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gac on 27 set 2015.
 * .
 */
public class WikiLoginTest {

    private static final String NICK_USER = "gac";
    private static final String PASS_USER = "alfa";
    private static final String NICK_BOT = "biobot";
    private static final String PASS_BOT = "fulvia";
    private static final String NICK_BOT_GAC = "Gacbot@Gacbot";
    private static final String PASS_BOT_GAC = "tftgv0vhl16c0qnmfdqide3jqdp1i5m7";

    private WikiLogin wikiLogin;

    @Test
    public void login() {
//        wikiLogin = new WikiLogin(NICK_USER, PASS_USER);
//        assertNotNull(wikiLogin);
//        assertTrue(wikiLogin.isValido());
//        assertEquals(wikiLogin.getFirstResult(), ErrLogin.needToken);
//        assertEquals(wikiLogin.getRisultato(), ErrLogin.success);
//        assertTrue(wikiLogin.getToken().length() > 20);
//        assertEquals(wikiLogin.getCookieprefix(), "itwiki");
//        assertTrue(wikiLogin.getSessionId().length() > 20);
//        assertTrue(wikiLogin.isUser());
//        assertFalse(wikiLogin.isBot());

        wikiLogin = new WikiLogin(NICK_BOT_GAC, PASS_BOT_GAC);
        assertNotNull(wikiLogin);
        assertTrue(wikiLogin.isValido());
        assertEquals(wikiLogin.getFirstResult(), ErrLogin.needToken);
        assertEquals(wikiLogin.getRisultato(), ErrLogin.success);
        assertTrue(wikiLogin.getToken().length() > 20);
        assertTrue(wikiLogin.getSessionId().length() > 20);
        assertTrue(wikiLogin.isUser());
        assertTrue(wikiLogin.isBot());

//        wikiLogin = new WikiLogin(NICK_BOT, PASS_BOT);
//        assertNotNull(wikiLogin);
//        assertTrue(wikiLogin.isValido());
//        assertEquals(wikiLogin.getFirstResult(), ErrLogin.needToken);
//        assertEquals(wikiLogin.getRisultato(), ErrLogin.success);
//        assertTrue(wikiLogin.getToken().length() > 20);
////        assertEquals(wikiLogin.getCookieprefix(), "itwiki");
//        assertTrue(wikiLogin.getSessionId().length() > 20);
//        assertTrue(wikiLogin.isUser());
//        assertTrue(wikiLogin.isBot());
    }// end of single test

}// end of testing class
