package main.analyzer;

import com.sun.javaws.exceptions.InvalidArgumentException;
import main.ReflectionUtils;
import main.fillers.Fillers;
import main.sorters.Sorter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Class for evaluating sorting time for all implementations of {@link Sorter}.
 * <p>
 * Object of this class gets lists of all non-abstract realisations of Sorter abstract class from {@link main.sorters} and
 * all array filling methods from {@link Fillers} class on creation.
 * <p>
 * Contains methods to evaluate array sizes on specified values interval, then analyze and save sorting time
 * for each pair of algorithm and array from aforementioned lists.
 *
 * @author Dmytro Storozhenko
 * @version 1.1
 */
public class Analyzer {
    private TreeMap<String, TreeMap<String, long[]>> analysisResult = new TreeMap<>();
    private int[] lengthSteps;
    private static LinkedHashSet<Class<? extends Sorter>> sorterClasses;
    private static LinkedHashSet<Method> fillerMethods;

    public Analyzer() {
        sorterClasses = ReflectionUtils.getNonAbstrSubTypesOfClass("main.sorters", Sorter.class);
        fillerMethods = ReflectionUtils.getAnnotatedMethods(Fillers.class, Fillers.Filler.class);
    }

    /**
     * Returns resulting data of the analysis.
     *
     * @return The container of the following structure: {@code TreeMap<String F, TreeMap<String S, long[] T>>},
     * where:
     * <ul>
     * <li>F - {@link Fillers} method</li>
     * <li>S - sorting algorithm class</li>
     * <li>T - sorting time for every array length</li>
     * </ul>
     */
    public TreeMap<String, TreeMap<String, long[]>> getAnalysisResult() {
        return analysisResult;
    }

    /**
     * Returns array size values, calculated with {@link Analyzer#calcStepsOnInterval(int, int, int)}.
     *
     * @return int[] of array sizes.
     */
    public int[] getLengthSteps() {
        return lengthSteps;
    }

    /**
     * Method that performs analysis.
     * <p>
     * Based on input data, calculates array sizes for analysis, and
     * evaluates every {@link main.sorters} algorithm's sorting time for
     * arrays, filled with every {@link main.fillers.Fillers} method.
     */
    public void startAnalysis(int minLength, int maxLength, int stepsCount) throws InvalidArgumentException {
        calcStepsOnInterval(minLength, maxLength, stepsCount);
        System.out.print("\nAnalysis is in progress... ");
        for (Method arrayGenerator : fillerMethods) {
            TreeMap<String, long[]> currFillerSortingTimes = new TreeMap<>();
            try {
                int[][] arraysForTesting = new int[lengthSteps.length][];
                for (int i = 0; i < lengthSteps.length; i++) {
                    arraysForTesting[i] = (int[]) arrayGenerator.invoke(null, lengthSteps[i]);
                }
                for (Class<? extends Sorter> sorterClass : sorterClasses) {
                    Object sorterObject = sorterClass.newInstance();
                    Method sortingMethod = sorterClass.getMethod("doSorting", int[].class);
                    long[] currSorterSortingTimes = new long[lengthSteps.length];
                    for (int i = 0; i < lengthSteps.length; i++) {
                        long start = System.nanoTime();
                        sortingMethod.invoke(sorterObject, (Object) Arrays.copyOf(arraysForTesting[i], arraysForTesting[i].length));
                        long finish = System.nanoTime();
                        currSorterSortingTimes[i] = (finish - start);
                    }
                    currFillerSortingTimes.put(sorterClass.getSimpleName(), currSorterSortingTimes);
                }
                analysisResult.put(arrayGenerator.getName(), currFillerSortingTimes);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Evaluates values on interval from minLength to maxLength.
     * Values grow exponentially by calculations that use natural exponential function and natural logarithm.
     * Saves them in {@link Analyzer} int[] field in ascending order.
     * <p>
     * Values are rounded to:
     * <li>Hundreds if value is bigger than 1000;</li>
     * <li>Tens if value is from 100 to 999;</li>
     * <li>None if value equals or lower than 99.</li>
     *
     * @param minLength Initial interval value
     * @param maxLength Final interval value
     * @throws InvalidArgumentException If some of the following is true:
     *                                  <ul>
     *                                  <li>minLength < 0</li>
     *                                  <li>maxLength < 1</li>
     *                                  <li>stepsCount < 1</li>
     *                                  <li>minLength > maxLength</li>
     *                                  </ul>
     */
    private void calcStepsOnInterval(int minLength, int maxLength, int stepsCount) throws InvalidArgumentException {
        if (minLength < 0 || maxLength < 1 || stepsCount < 1 || minLength > maxLength) {
            String[] s = {"One or more arguments are invalid"};
            throw new InvalidArgumentException(s);
        }
        int[] result = new int[stepsCount];
        double logInterval = Math.log((double) maxLength) - Math.log((double) minLength);
        double stepLog = Math.log((double) minLength);
        for (int i = 0; i < result.length; i++) {
            int step = (int) Math.exp(stepLog);
            if (step > 100) {
                if (step > 1000) step = (int) Math.round(step / 100.0) * 100;
                else step = (int) Math.round(step / 10.0) * 10;
            }
            result[i] = step;
            stepLog += (logInterval) / (stepsCount - 1);
        }
        lengthSteps = result;
    }
}
