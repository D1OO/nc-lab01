package main.sorters;

public final class BubbleDownSorter extends BubbleSorter {

    private int i;
    private int j;

    void initPointers() {
        i = -1;
        j = data.length;
    }

    int nextI() {
        if (i + 1 < data.length - 1) {
            return ++i;
        }
        return i = -1;
    }

    int nextJ() {
        if (j - 1 > i) {
            return --j;
        }
        j = data.length;
        return -1;
    }

    boolean isSwapNeeded() {
        return (data[j - 1] > data[j]);
    }

    void swap() {
        swap(data, j - 1, j);
    }
}