package main.fillers;

import main.analyzer.Analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Random;
import java.util.TreeMap;

/**
 * Class with static methods for different types of array generation.
 * <p>
 * Due to {@link Analyzer#startAnalysis(int, int, int)}} use of reflection, all array filling methods
 * must be annotated with @Filler annotation, defined in this class, to be used during analysis.
 *
 * For correct displaying of the array generation type name
 * in the {@link main.excel.ExcelExport#getOutputFile(TreeMap, int[])} file,
 * filler method's names must consist of word "get" and a following name of array generation type.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 */
public class Fillers {

    private static int length;
    private static int[] array;

    /**
     * Must be used to annotate methods, which will be used in sorting algorithms analysis.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filler {
    }

    /**
     * Method returns an array of whole numbers from 1 to array size, sorted in ascending order.
     *
     * @param num size of the returned array
     * @return array of sorted whole numbers
     */
    @Filler
    public static int[] getSorted(int num) {
        length = num;
        array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    /**
     * Method returns an array of whole numbers from 1 to array size - 1, sorted in ascending order,
     * and random number chosen from 1 to array size as last element.
     *
     * @param num size of the returned array
     * @return array of whole numbers
     */
    @Filler
    public static int[] getNearlySorted(int num) {
        length = num;
        array = new int[length];
        for (int i = 0; i < length - 1; i++) {
            array[i] = i + 1;
        }
        array[length - 1] = (int) (Math.random() * (length - 1) + 1);
        return array;
    }

    /**
     * Method returns an array of whole numbers from 1 to array size, sorted in descending order.
     *
     * @param num size of the returned array
     * @return array of whole numbers, sorted in descending order
     */
    @Filler
    public static int[] getReversed(int num) {
        length = num;
        array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = length - i;
        }
        return array;
    }

    /**
     * Method returns an array of whole numbers from 1 to array size, arranged randomly in array.
     *
     * @param num size of the returned array
     * @return array of whole numbers
     */
    @Filler
    public static int[] getRandom(int num) {
        length = num;
        array = getSorted(length);
        Random rnd = new Random();
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, rnd.nextInt(i));
        }
        return array;
    }

    /**
     * Method changes two element's places in the passed array.
     *
     * @param array Array with elements
     * @param a First element
     * @param b Second element
     */
    private static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
