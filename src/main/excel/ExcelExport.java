package main.excel;

import main.analyzer.Analyzer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.apache.poi.xssf.usermodel.charts.XSSFLineChartData;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Class for exporting sorting algorithms analysis data to a .xlsx format.
 *
 * Creates a file with a separate sheet for every corresponding array type from passed data, and the following sheet containment:
 * <ul>
 *     <li>Table of all algorithms sorting time for corresponding array size;</li>
 *     <li>Table divisor area with drop-down list for converting table data to another time units from nanosecond to second; </li>
 *     <li>Chart based on table data, which displays algorithm's sorting time vs. array size. </li>
 * </ul>
 *
 * @author Dmytro Storozhenko
 * @version 1.3
 */
public class ExcelExport {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private TreeMap<String, TreeMap<String, long[]>> data;
    private int[] lengthsList;
    private int tableSize;
    private int divRowIndex;
    private int divFirstCellIndex = 1;

    private XSSFCellStyle cellStyleTopHeader;
    private XSSFCellStyle cellStyleLeftHeader;
    private XSSFCellStyle cellStyleTimeCell = workbook.createCellStyle();
    private XSSFCellStyle cellStyleB = workbook.createCellStyle();
    private XSSFCellStyle cellStyleCenterB = workbook.createCellStyle();
    private XSSFCellStyle cellStyleSmallIt = workbook.createCellStyle();
    private XSSFCellStyle cellStyleBIt = workbook.createCellStyle();
    private XSSFCellStyle cellStyleDropDown = workbook.createCellStyle();
    private Font boldFont = workbook.createFont();
    private Font smallerBoldFont = workbook.createFont();
    private Font boldItalicFont = workbook.createFont();
    private Font smallItalicFont = workbook.createFont();
    private Font tableCellFont = workbook.createFont();
    private Font dropDownFont = workbook.createFont();
    private int tableColWidth = 2100;

    /**
     * Method for creating {@link BorderStyle#THIN} border around the passed table cells region in the passed sheet.
     *
     * @param firstRow The first row of the area for border creation
     * @param lastRow The last row of the area for border creation
     * @param firstCol The first column of the area for border creation
     * @param lastCol The last column of the area for border creation
     * @param sheet The sheet with area for border creation
     */
    private void setRegionBorders(int firstRow, int lastRow, int firstCol, int lastCol, XSSFSheet sheet) {
        CellRangeAddress cellRange = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRange, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRange, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRange, sheet);
    }

    /**
     * Method for creating XSSFCellStyle for table's header column and row, for column if
     * passed {@link HorizontalAlignment} value is {@link HorizontalAlignment#RIGHT}
     * and for row if {@link HorizontalAlignment#CENTER}. Difference is in {@link Font} style.
     *
     * @param horizontalAlignment {@link HorizontalAlignment} of XSSFCellStyle to be returned, which also determines it's {@link Font}
     * @return XSSFCellStyle for header column or header row
     */
    private XSSFCellStyle getHeadersCellStyle(HorizontalAlignment horizontalAlignment) {
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        XSSFColor lightBlue = new XSSFColor(new Color(204, 255, 255));

        headerCellStyle.setAlignment(horizontalAlignment);
        headerCellStyle.setFillForegroundColor(lightBlue);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setFont((horizontalAlignment == HorizontalAlignment.CENTER ? smallerBoldFont : boldFont));
        return headerCellStyle;
    }

    /**
     * Sets values for all XSSFCellStyle and Font fields.
     */
    private void initFormatting() {
        boldFont.setFontHeightInPoints((short) 13);
        boldFont.setBold(true);
        smallerBoldFont.setFontHeightInPoints((short) 11);
        smallerBoldFont.setBold(true);
        boldItalicFont.setFontHeightInPoints((short) 13);
        boldItalicFont.setBold(true);
        boldItalicFont.setItalic(true);
        smallItalicFont.setFontHeightInPoints((short) 10);
        smallItalicFont.setItalic(true);
        tableCellFont.setFontHeightInPoints((short) 9);
        dropDownFont.setFontName("Arial");
        dropDownFont.setItalic(true);

        cellStyleTopHeader = getHeadersCellStyle(HorizontalAlignment.CENTER);
        cellStyleLeftHeader = getHeadersCellStyle(HorizontalAlignment.RIGHT);
        cellStyleTimeCell.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
        cellStyleTimeCell.setFont(tableCellFont);
        cellStyleB.setFont(boldFont);
        cellStyleCenterB.setFont(boldFont);
        cellStyleCenterB.setAlignment(HorizontalAlignment.CENTER);
        cellStyleSmallIt.setFont(smallItalicFont);
        cellStyleSmallIt.setAlignment(HorizontalAlignment.CENTER);
        cellStyleBIt.setFont(boldItalicFont);
        cellStyleBIt.setAlignment(HorizontalAlignment.CENTER);
        cellStyleDropDown.setFont(dropDownFont);
    }

    /**
     * Translates passed data to .xlsx file format and tries to save it in "out/output/Sorting Analysis.xlsx".
     *
     * @param data analysis results in format, described in {@link Analyzer#getAnalysisResult()}
     * @param lengthsList array sizes, which where used in analysis
     * @return file in .xlsx format
     * @throws IOException see {@link FileOutputStream} exceptions section
     */
    public File getOutputFile(TreeMap<String, TreeMap<String, long[]>> data, int[] lengthsList) throws IOException {
        ArrayList<ArrayList<ChartDataSource<Number>>> allSheetsChartData = new ArrayList<>();
        File outputFile = new File("out/output/Sorting Analysis.xlsx");

        this.data = data;
        this.lengthsList = lengthsList;
        tableSize = data.firstEntry().getValue().keySet().size();
        divRowIndex = tableSize + 3;

        initFormatting();

        for (String filler : data.keySet()) {
            XSSFSheet sheet = workbook.createSheet(filler.substring(3));
            ArrayList<ChartDataSource<Number>> currSheetChartData = new ArrayList<>();

            int rowInd = 0;
            Row headerRow = sheet.createRow(rowInd++);
            for (int i = 0; i < lengthsList.length; i++) {          //Filling first row with array length values
                Cell headerCell = headerRow.createCell(i + 1);
                headerCell.setCellStyle(cellStyleTopHeader);
                headerCell.setCellValue((double) lengthsList[i]);
            }

            for (String sorter : data.get(filler).keySet()) {
                int cellInd = 0;
                Row sorterRow = sheet.createRow(rowInd++);
                Cell sorterNameCell = sorterRow.createCell(cellInd++);
                sorterNameCell.setCellStyle(cellStyleLeftHeader);
                sorterNameCell.setCellValue(sorter.replace("Sorter", ""));
                long[] sorterTimeStatistics = data.get(filler).get(sorter);
                for (long timeValue : sorterTimeStatistics) {
                    Cell timeCell = sorterRow.createCell(cellInd++);
                    timeCell.setCellFormula(timeValue + "/" +
                            CellReference.convertNumToColString(divFirstCellIndex + 2) + (divRowIndex + 1));
                    timeCell.setCellValue(timeValue);
                    timeCell.setCellStyle(cellStyleTimeCell);
                }
                // Adding sorting time values for this sorter to data sources for chart creation
                currSheetChartData.add(DataSources.fromNumericCellRange(sheet,
                        new CellRangeAddress(rowInd - 1, rowInd - 1, 1, cellInd - 1)));
            }
            allSheetsChartData.add(currSheetChartData);

            for (int i = 1; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.setColumnWidth(i, tableColWidth);
            }

            CellRangeAddress tableCellsRange = new CellRangeAddress(1, tableSize, 1, lengthsList.length);
            RegionUtil.setBorderBottom(BorderStyle.THIN, tableCellsRange, sheet);
            RegionUtil.setBorderTop(BorderStyle.DOUBLE, tableCellsRange, sheet);
            RegionUtil.setBorderLeft(BorderStyle.DOUBLE, tableCellsRange, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, tableCellsRange, sheet);
        }
        createDivDropDownLists();
        createDataCharts(allSheetsChartData);
        FileOutputStream out = new FileOutputStream(outputFile);
        workbook.write(out);
        out.close();
        return outputFile;
    }

    /**
     * Creates an area for possibility to change table divisor to convert table values to another time unit.
     */
    private void createDivDropDownLists() {
        String[] DLItems = {"1 000 000 000", "1 000 000", "100 000", "10 000", "1 000", "100", "10", "1"};
        String[] legendContents = {"10\u2079", "s", "10\u2076", "ms", "1", "ns"};
        int divLegendFirstCellIndex = 6;
        double defaultValue = 1000000;
        CellRangeAddressList divListCellAdress = new CellRangeAddressList(
                divRowIndex, divRowIndex, divFirstCellIndex + 2, divFirstCellIndex + 2);

        for (int currSheetIndex = 0; currSheetIndex < workbook.getNumberOfSheets(); currSheetIndex++) {
            XSSFSheet currentSheet = workbook.getSheetAt(currSheetIndex);
            Row row = currentSheet.createRow(divRowIndex);
            Cell cell = row.createCell(divFirstCellIndex);
            cell.setCellValue("Table divisor:");
            cell.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell, HorizontalAlignment.RIGHT);
            cell = row.createCell(divFirstCellIndex + 1);
            cell.setCellStyle(cellStyleB);
            currentSheet.addMergedRegion(new CellRangeAddress(
                    divRowIndex, divRowIndex, divFirstCellIndex, divFirstCellIndex + 1));

            XSSFDataValidationHelper dvh = new XSSFDataValidationHelper(currentSheet);
            XSSFDataValidationConstraint dvc = (XSSFDataValidationConstraint) dvh.createExplicitListConstraint(DLItems);
            XSSFDataValidation dataValidation = (XSSFDataValidation) dvh.createValidation(dvc, divListCellAdress);
            dataValidation.setShowPromptBox(true);
            dataValidation.setEmptyCellAllowed(false);
            currentSheet.addValidationData(dataValidation);
            row = currentSheet.getRow(divRowIndex);
            cell = row.createCell(divFirstCellIndex + 2);
            cell.setCellValue(defaultValue);
            cell.setCellStyle(cellStyleDropDown);
            CellUtil.setAlignment(cell, HorizontalAlignment.RIGHT);
            currentSheet.addMergedRegion(new CellRangeAddress(
                    divRowIndex, divRowIndex, divFirstCellIndex + 2, divFirstCellIndex + 3));

            for (int i = divRowIndex - 1, l = 0; i <= divRowIndex + 1; i++, l += 2) {   //filling divisor legend cells
                row = currentSheet.getRow(i) == null ? currentSheet.createRow(i) : currentSheet.getRow(i);
                cell = row.createCell(divLegendFirstCellIndex);
                cell.setCellValue(legendContents[l]);
                cell.setCellStyle(cellStyleSmallIt);
                cell = row.createCell(divLegendFirstCellIndex + 1);
                cell.setCellValue(legendContents[l + 1]);
                cell.setCellStyle(cellStyleBIt);
            }

            setRegionBorders(divRowIndex, divRowIndex,
                    divFirstCellIndex, divFirstCellIndex + 3, currentSheet);
            setRegionBorders(divRowIndex - 1, divRowIndex - 1,
                    divLegendFirstCellIndex, divLegendFirstCellIndex + 1, currentSheet);
            setRegionBorders(divRowIndex, divRowIndex,
                    divLegendFirstCellIndex, divLegendFirstCellIndex + 1, currentSheet);
            setRegionBorders(divRowIndex + 1, divRowIndex + 1,
                    divLegendFirstCellIndex, divLegendFirstCellIndex + 1, currentSheet);
            currentSheet.autoSizeColumn(0);
        }
    }

    /**
     * Creates charts, based on passed data, which was generated during table creation.
     *
     * @param chartData Data, on which charts will be based
     */
    private void createDataCharts(ArrayList<ArrayList<ChartDataSource<Number>>> chartData) {
        String[] sorterNames = new String[tableSize];
        Integer[] lengthsListI = Arrays.stream(lengthsList).boxed().toArray(Integer[]::new);  // int[] to Integer[]
        ChartDataSource<Number> xAxis = DataSources.fromArray(lengthsListI);    // Array length values (x axis)
        data.firstEntry().getValue().keySet().toArray(sorterNames);
        int chartPosFirstRow = tableSize + 6;
        int chartPosLastRow = tableSize + 22;
        int chartWidthFirstCol = 0;
        int chartWidthLastCol = 14;

        for (int currSheetIndex = 0; currSheetIndex < workbook.getNumberOfSheets(); currSheetIndex++) {
            XSSFSheet currentSheet = workbook.getSheetAt(currSheetIndex);
            XSSFDrawing xssfDrawing = currentSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = xssfDrawing.createAnchor(0, 0, 0, 0,
                    chartWidthFirstCol, chartPosFirstRow, chartWidthLastCol, chartPosLastRow); //chart size in rows
            XSSFChart dataChart = xssfDrawing.createChart(anchor);
            dataChart.setTitleText("Sorting Time vs. Array Length");
            XSSFChartLegend chartLegend = dataChart.getOrCreateLegend();
            chartLegend.setPosition(LegendPosition.BOTTOM);
            XSSFLineChartData xssfLineChartData = dataChart.getChartDataFactory().createLineChartData();
            ChartAxis bottomAxis = dataChart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
            ValueAxis leftAxis = dataChart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            for (int i = 0; i < chartData.get(currSheetIndex).size(); i++) {
                // time values (y axis)
                LineChartSeries chartSeries = xssfLineChartData.addSeries(xAxis, chartData.get(currSheetIndex).get(i));
                chartSeries.setTitle(sorterNames[i]);    // sorter plot name in chart legend
            }
            dataChart.plot(xssfLineChartData, bottomAxis, leftAxis);
        }
    }
}