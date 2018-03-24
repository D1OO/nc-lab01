package main.sorters;

/**
 * Class for representing sorting algorithm.
 *
 * For algorithm's name correct displaying in {@link main.excel.ExcelExport#getOutputFile(java.util.TreeMap, int[])} file,
 * all implementation's names should consist of words, representing algorithms name, and the word "Sorter".
 *
 * All implementations return array with zero length if null array is passed for sorting.
 */
public abstract class Sorter {

    /**
     * Method checks if the passed array is null
     *
     * @param array array to be checked
     * @return true if array is null, false otherwise
     */
    static boolean checkNull(int[] array){
        return (array == null);
    }

    /**
     * Method sorts passed array in ascending order.
     *
     * @param data array for sorting
     * @return array, sorted in ascending order
     */
    public abstract int[] doSorting(int[] data);

    /**
     * Method changes two element's places in the passed array
     *
     * @param array Array with elements
     * @param a First element
     * @param b Second element
     */
    static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
