package main.sorters;

public final class BubbleUpSorter extends BubbleSorter {

    private int i;
    private int j;

    void initPointers() {
        i = data.length;
        j = -1;
    }

    int nextI() {
        return --i;
    }

    int nextJ() {
        if (j + 1 < i) {
            return ++j;
        }
        return j = -1;
    }

    boolean isSwapNeeded() {
        return (data[j] > data[j + 1]);
    }

    void swap() {
        swap(data, j, j + 1);
    }
}