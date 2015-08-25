package it.algos.vaad.lib;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 17-7-13
 * Time: 06:59
 */

import java.util.*;

/**
 * Created by Gac on 24 ago 2015.
 * Classe statica di libreria
 * <p>
 * 1) First and Major difference between Array and ArrayList in Java is that Array is a fixed length data structure
 * while ArrayList is a variable length Collection class.
 * You can not change length of Array once created in Java but ArrayList re-size itself when gets full depending upon capacity
 * and load factor. Since ArrayList is internally backed by Array in Java, any resize operation in ArrayList will slow down performance
 * as it involves creating new Array and copying content from old array to new array.
 * 2) Another difference between Array and ArrayList in Java is that you can not use Generics along with Array,
 * as Array instance knows about what kind of type it can hold and throws ArrayStoreException,
 * if you try to store type which is not convertible into type of Array. ArrayList allows you to use Generics to ensure type-safety.
 * 3) You can also compare Array vs ArrayList on How to calculate length of Array or size of ArrayList.
 * All kinds of Array provides length variable which denotes length of Array while ArrayList provides size()
 * method to calculate size of ArrayList in Java.
 * 4) One more major difference between ArrayList and Array is that, you can not store primitives in ArrayList, it can only contain Objects.
 * While Array can contain both primitives and Objects in Java. Though Autoboxing of Java 5 may give you an impression
 * of storing primitives in ArrayList, it actually automatically converts primitives to Object. e.g.
 * <p>
 * Resizable :   Array is static in size that is fixed length data structure, One can not change the length after creating the Array object.
 * Performance : Performance of Array and ArrayList depends on the operation you are performing :
 * Primitives :  ArrayList can not contains primitive data types (like int , float , double) it can only contains Object while
 * Array can contain both primitive data types as well as objects.
 * Iterating the values : We can use iterator  to iterate through ArrayList
 * Type-Safety :  In Java , one can ensure Type Safety through Generics. while Array is a homogeneous data structure ,
 * thus it will contain objects of specific class or primitives of specific  data type.
 * Length :  Length of the ArrayList is provided by the size()
 * Adding elements : We can insert elements into the arraylist object using the add() method while
 * in array we insert elements using the assignment operator.
 * Multi-dimensional :  Array can be multi dimensional , while ArrayList is always single dimensional.
 */
public class LibArray {

    /**
     * Convert a stringArray to ArrayList
     *
     * @param stringArray to convert
     * @return the corresponding casted ArrayList
     */
    public static List<String> fromString(String[] stringArray) {
        return new ArrayList<String>(Arrays.asList(stringArray));
    } // end of static method

    /**
     * Convert a intArray to ArrayList
     *
     * @param intArray to convert
     * @return the corresponding casted ArrayList
     */
    public static List<Integer> fromInt(int[] intArray) {
        ArrayList<Integer> intList = new ArrayList<Integer>();

        for (Integer intero : intArray) {
            intList.add(intero);
        } // fine del ciclo for-each

        return intList;
    } // end of static method

    /**
     * Convert a longArray to ArrayList
     *
     * @param longArray to convert
     * @return the corresponding casted ArrayList
     */
    public static List<Long> fromLong(long[] longArray) {
        ArrayList<Long> longList = new ArrayList<Long>();

        for (Long lungo : longArray) {
            longList.add(lungo);
        } // fine del ciclo for-each

        return longList;
    } // end of static method


    /**
     * Convert a objArray to ArrayList
     *
     * @param objArray to convert
     * @return the corresponding casted ArrayList
     */
    public static List<Object> fromObj(Object[] objArray) {
        ArrayList<Object> objList = new ArrayList<Object>();

        for (Object lungo : objArray) {
            objList.add(lungo);
        } // fine del ciclo for-each

        return objList;
    } // end of static method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     *
     * @param listaValoriDoppi in ingresso
     * @return valoriUnici NON ordinati, null se listaValoriDoppi è null
     */
    public static List valoriUniciDisordinati(List listaValoriDoppi) {
        return valoriUniciBase(listaValoriDoppi, false);
    } // fine del metodo


    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     *
     * @param objArray in ingresso
     * @return valoriUnici NON ordinati, null se objArray è null
     */
    public static List valoriUniciDisordinati(Object[] objArray) {
        return valoriUniciDisordinati(fromObj(objArray));
    } // fine del metodo


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaValoriDoppi in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static List valoriUnici(List listaValoriDoppi) {
        return valoriUniciBase(listaValoriDoppi, true);
    } // fine del metodo


    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param objArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static List valoriUnici(Object[] objArray) {
        return valoriUnici(fromObj(objArray));
    } // fine del metodo

    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param intArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static List valoriUnici(int[] intArray) {
        return valoriUnici(fromInt(intArray));
    } // fine del metodo

    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param longArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static List valoriUnici(long[] longArray) {
        return valoriUnici(fromLong(longArray));
    } // fine del metodo


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Eventualmente (tag booleano) ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     *
     * @param listaValoriDoppi in ingresso
     * @param ordina           tag per forzare l'ordinamento
     * @return valoriUnici disordinati oppure ordinati, null se listaValoriDoppi è null
     */
    @SuppressWarnings("all")
    private static List valoriUniciBase(List listaValoriDoppi, boolean ordina) {
        ArrayList listaValoriUniciNonOrdinati = null;
        Set set;

        if (listaValoriDoppi != null) {
            set = new LinkedHashSet((List) listaValoriDoppi);
            listaValoriUniciNonOrdinati = new ArrayList(set);
            if (ordina) {
                return sort(listaValoriUniciNonOrdinati);
            } else {
                return listaValoriUniciNonOrdinati;
            }// fine del blocco if-else
        }// fine del blocco if

        return null;
    } // fine del metodo

    /**
     * Ordina la lista
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaDisordinata in ingresso
     * @return lista ordinata, null se listaDisordinata è null
     */
    public static List sort(List listaDisordinata) {
        List<Object> objList;
        Object[] objArray = listaDisordinata.toArray();

        try { // prova ad eseguire il codice
            Arrays.sort(objArray);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        objList = fromObj(objArray);
        return objList;
    } // fine del metodo

}// end of static class
