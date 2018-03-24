package main.sorters;

/**
 * Abstract class, representing bubble sorting algorithm.
 *
 * @author Dmytro Storozhenko
 * @version 1.1
 */
public abstract class BubbleSorter extends Sorter {
    int[] data;

    public int[] doSorting(int[] array) {
        this.data = array;
        if (checkNull(data))
            return new int[0];
        initPointers();
        int i = nextI();
        while (i >= 0) {
            int j = nextJ();
            while (j >= 0) {
                if (isSwapNeeded()) {
                    swap();
                }
                j = nextJ();
            }
            i = nextI();
        }
        return data;
    }

    /**
     * Sets pointers to initial value.
     */
    abstract void initPointers();

    /**
     * Changes outer cycle's pointer value for the next iteration.
     *
     * @return outer cycle's pointer value
     */
    abstract int nextI();

    /**
     * Changes inner cycle's pointer value for the next iteration.
     *
     * @return inner cycle's pointer value
     */
    abstract int nextJ();

    /**
     * Checks the need for replacing neighbour element's places.
     *
     * @return true, if swap is needed and false otherwise
     */
    abstract boolean isSwapNeeded();

    /**
     * Replaces the two element's places.
     */
    abstract void swap();
}