import it.algos.vaad.lib.VaadWiki;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gac on 15 set 2015.
 * .
 */
public class VaadWikiTest extends VaadTest {

    private static final String TAG_INI = "[[";
    private static final String TAG_END = "]]";

    @Before
    public void startUp() {
        sorgente = "nato nell'allora [[Feudi imperiali|Feudo imperiale]] di [[Cabella Ligure|Cabella]], fu l'ultimo console della [[Repubblica di Genova|Superba]] a [[Feodosia|Caffa]], colonia genovese nell'odierna [[Ucraina]]";
    }// end of single test

    @Test
    public void estrae() {
        ArrayList<String> arrayPrevisto = new ArrayList<String>();
        ArrayList<String> arrayOttenuto;
        arrayPrevisto.add("[[Feudi imperiali|Feudo imperiale]]");
        arrayPrevisto.add("[[Cabella Ligure|Cabella]]");
        arrayPrevisto.add("[[Repubblica di Genova|Superba]]");
        arrayPrevisto.add("[[Feodosia|Caffa]]");
        arrayPrevisto.add("[[Ucraina]]");

        arrayOttenuto = VaadWiki.estrae(sorgente, TAG_INI, TAG_END);
        assertEquals(arrayOttenuto, arrayPrevisto);
    }// end of single test

    @Test
    public void estra2() {
        ArrayList<String> arrayPrevisto = new ArrayList<String>();
        ArrayList<String> arrayOttenuto;
        arrayPrevisto.add("Feudi imperiali|Feudo imperiale");
        arrayPrevisto.add("Cabella Ligure|Cabella");
        arrayPrevisto.add("Repubblica di Genova|Superba");
        arrayPrevisto.add("Feodosia|Caffa");
        arrayPrevisto.add("Ucraina");

        arrayOttenuto = VaadWiki.estraeEsclusi(sorgente, TAG_INI, TAG_END);
        assertEquals(arrayOttenuto, arrayPrevisto);
    }// end of single test

    @Test
    public void estraeQuadre() {
        ArrayList<String> arrayPrevisto = new ArrayList<String>();
        ArrayList<String> arrayOttenuto;
        arrayPrevisto.add("Feudo imperiale");
        arrayPrevisto.add("Cabella");
        arrayPrevisto.add("Superba");
        arrayPrevisto.add("Caffa");
        arrayPrevisto.add("Ucraina");

        arrayOttenuto = VaadWiki.estraeDoppieQuadre(sorgente);
        assertEquals(arrayOttenuto, arrayPrevisto);
    }// end of single test

    @Test
    public void estraeQuadrea() {
        previsto = "Feudo imperiale";

        ottenuto = VaadWiki.estraeDoppiaQuadra(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Estrae da una stringa la parte del link (dopo il pipe)
     * Prevede che la stringa inizi e finisca con le doppie quadre
     * Prevede che la stringa contenga il tag pipe
     *
     * @param stringa di testo da analizzare
     * @return stringa con solo il link
     */
    public void estraeSingoloLink() {
        sorgente = "[[Feudi imperiali|Feudo imperiale]]";
        previsto = "Feudo imperiale";
        ottenuto = VaadWiki.estraeSingoloLink(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "[[Cabella Ligure|Cabella]]";
        previsto = "Cabella";
        ottenuto = VaadWiki.estraeSingoloLink(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "[[Repubblica di Genova|Superba]]";
        previsto = "Superba";
        ottenuto = VaadWiki.estraeSingoloLink(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "[[Feodosia|Caffa]]";
        previsto = "Caffa";
        ottenuto = VaadWiki.estraeSingoloLink(sorgente);
        assertEquals(ottenuto, previsto);

        sorgente = "[[Ucraina]]";
        previsto = "Ucraina";
        ottenuto = VaadWiki.estraeSingoloLink(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

    @Test
    /**
     * Sostituisce tutte le occorrenze di doppie quadre con il link visibile
     *
     * @param testo da elaborare
     * @return testo con i soli link visibili e senza doppie qaudre
     */
    public void sostituisceLink() {
        previsto = "nato nell'allora Feudo imperiale di Cabella, fu l'ultimo console della Superba a Caffa, colonia genovese nell'odierna Ucraina";
        ottenuto = VaadWiki.sostituisceLink(sorgente);
        assertEquals(ottenuto, previsto);
    }// end of single test

}// end of testing class
