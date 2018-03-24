package main.sorters;

/**
 * Class, representing swap soring algorithm.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 */
public final class SwapSorter extends Sorter {
    public int[] doSorting(int[] data) {
        if (checkNull(data))
            return new int[0];
        int minElemIndex;
        for (int i = 0; i < data.length - 1; i++) {
            minElemIndex = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[minElemIndex]) {
                    minElemIndex = j;
                }
            }
            swap(data, i, minElemIndex);
        }
        return data;
    }
}
