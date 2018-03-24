package main.sorters;

/**
 * Class, representing recursive sorting algorithm.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 */
public final class RecursiveSorter extends Sorter {
    private int[] data;

    public int[] doSorting(int[] array) {
        data = array;
        if (checkNull(data)) {
            return new int[0];
        }
        recSort(0, data.length - 1);
        return data;
    }

    private void recSort(int left, int right) {
        if (left >= right) {
            return;
        }
        int i = left;
        int j = right;
        int S = i - (i - j) / 2;
        while (i < j) {
            while (data[i] <= data[S] && i < S) i++;
            while (data[j] >= data[S] && j > S) j--;
            if (i < j) {
                swap(data, i, j);
                if (i == S) {
                    S = j;
                } else if (j == S) {
                    S = i;
                }
            }
        }
        recSort(left, S);
        recSort(S + 1, right);
    }
}
