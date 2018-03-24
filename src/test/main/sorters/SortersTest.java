package main.sorters;

import main.fillers.Fillers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class SortersTest {

    private int[] getNullAr(){
        return null;
    }

    @Test
    public void doSorting() throws Exception {
        Sorter sorter;
        int[] nullAr = getNullAr();
        int[] random =Fillers.getRandom(100);
        int[] randomSorted = Arrays.copyOf(random, random.length);

        sorter = new BubbleDownSorter();
        assertNotNull(sorter.doSorting(nullAr));
        Arrays.sort(randomSorted);
        assertArrayEquals(randomSorted, sorter.doSorting(random));

        sorter = new BubbleUpSorter();
        assertNotNull(sorter.doSorting(nullAr));
        Arrays.sort(randomSorted);
        assertArrayEquals(randomSorted, sorter.doSorting(random));

        sorter = new MergeSorter();
        assertNotNull(sorter.doSorting(nullAr));
        Arrays.sort(randomSorted);
        assertArrayEquals(randomSorted, sorter.doSorting(random));

        sorter = new RecursiveSorter();
        assertNotNull(sorter.doSorting(nullAr));
        Arrays.sort(randomSorted);
        assertArrayEquals(randomSorted, sorter.doSorting(random));

        sorter = new SwapSorter();
        assertNotNull(sorter.doSorting(nullAr));
        Arrays.sort(randomSorted);
        assertArrayEquals(randomSorted, sorter.doSorting(random));
    }
}