package main.sorters;

import java.util.Arrays;

/**
 * Class that represents Java language arrays sorting method.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 * @see java.util.Arrays#sort(int[])
 */
public final class JavaSorter extends Sorter {
    public int[] doSorting(int[] data) {
        if (checkNull(data))
            return new int[0];
        Arrays.sort(data);
        return data;
    }
}
