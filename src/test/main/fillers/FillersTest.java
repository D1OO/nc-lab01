package main.fillers;

import org.junit.Test;

public class FillersTest {

    @Test
    public void getSorted() throws Exception {
        int[] sortedArray = Fillers.getSorted(10000);
        for (int i = 0; i < sortedArray.length-1; i++) {
            if (sortedArray[i]+1 != sortedArray[i+1])
                throw new Exception("Array is not sorted");
        }
    }

    @Test
    public void getNearlySorted() throws Exception {

    }

    @Test
    public void getReversed() throws Exception {
        int[] reversedArray = Fillers.getReversed(10000);
        for (int i = 0; i < reversedArray.length-1; i++) {
            if (reversedArray[i]-1 != reversedArray[i+1])
                throw new Exception("Array is not reversed");
        }
    }

    @Test
    public void getRandom() throws Exception {

    }

}