package main;

import com.sun.javaws.exceptions.InvalidArgumentException;
import main.analyzer.Analyzer;
import main.excel.ExcelExport;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A program for analysing sorting algorithms performance (time vs. array size) and exporting results to a .xlsx file
 *
 * @author Dmytro Storozhenko
 * @version 1.3
 */
public class Controller {
    public static void main(String args[]) {
        System.out.print("\n\tSorting algorithms analysis on whole number arrays\n\tof sizes within a specified interval.\n" +
                "\n- Enter the parameter values in the following order:\n" +
                "\t1. Initial value of array sizes interval;\n" +
                "\t2. Final value of array sizes interval;\n" +
                "\t3. Number of steps on interval.\n" +
                "\n- Enter \"def\" for the next values: 500, 30 000, 10;\n" +
                "\n- Press Enter to exit.\n" +
                "\n");

        programLoop:
        while (true) {
            Analyzer analyzer = new Analyzer();
            while (true) {
                int minLength, maxLength;
                int stepsCount;
                Scanner in = new Scanner(System.in);

                System.out.print("\t1. ");
                String inputString = in.nextLine();
                if (inputString.isEmpty()) {
                    break programLoop;
                } else if (inputString.equals("def")) {
                    minLength = 1000;
                    maxLength = 30000;
                    stepsCount = 20;
                } else try {
                    minLength = Integer.parseInt(inputString);
                    System.out.print("\t2. ");
                    maxLength = in.nextInt();
                    System.out.print("\t3. ");
                    stepsCount = in.nextInt();
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Input invalid\n\nPlease, try again:\n");
                    continue;
                }
                try {
                    analyzer.startAnalysis(minLength, maxLength, stepsCount);
                    break;
                } catch (InvalidArgumentException e) {
                    System.out.print("Invalid arguments:\nInitial length value must be greater than 0;\n" +
                            "Final length value must be greater or equal to initial length;\n" +
                            "Number of steps must be greater than 0.\n" +
                            "Please, try again:\n");
                }
            }
            System.out.print("Success.\nExporting data to a .xlsx file... ");
            ExcelExport excelExport = new ExcelExport();
            try {
                File excelFile = excelExport.getOutputFile(analyzer.getAnalysisResult(), analyzer.getLengthSteps());
                System.out.print("Success.\nTrying to open the file with default system application... ");
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(excelFile);
                    System.out.println("Success.");
                } catch (IOException e) {
                    System.out.println("Failed.\nPlease, open the file in \"lab01/out/output/\" manually.");
                }
            } catch (IOException e) {
                System.out.println("I/O error. (check if file for writing 'Sorting Analysis.xlsx' is closed");
            }
            System.out.println("\nEnter the parameter values:");
        }
    }
}