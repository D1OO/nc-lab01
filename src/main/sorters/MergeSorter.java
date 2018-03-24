package main.sorters;

import java.util.Arrays;

/**
 * Class, representing merge sorting algorithm.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 */
public final class MergeSorter extends Sorter {

    public int[] doSorting(int[] data){
        if (checkNull(data))
            return new int[0];
        return mergeSort(data);
    }

    private int[] mergeSort(int[] data) {

        if (data.length < 2) {
            return data;
        }
        int midIndex = data.length / 2;
        int[] lHalf = Arrays.copyOfRange(data, 0, midIndex);
        int[] rHalf = Arrays.copyOfRange(data, midIndex, data.length);
        return merge(mergeSort(lHalf), mergeSort(rHalf));
    }

    private int[] merge(int[] lPart, int[] rPart) {
        int n = lPart.length + rPart.length;
        int[] sortedArr = new int[n];
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < n; i++) {
            if (i1 == lPart.length) {
                sortedArr[i] = rPart[i2++];
            } else if (i2 == rPart.length) {
                sortedArr[i] = lPart[i1++];
            } else {
                if (lPart[i1] < rPart[i2]) {
                    sortedArr[i] = lPart[i1++];
                } else {
                    sortedArr[i] = rPart[i2++];
                }
            }
        }
        return sortedArr;
    }
}
