package com.thoaikx.utils;

import static java.lang.Character.getNumericValue;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Log4j2
public class ExcelUtils {

  //default use index sheet 0
  public static  String[][] getExcelData(String fileName,  int indexSheet) throws IOException {
    log.info("getExcelData with indexSheet is: " + indexSheet );
    String[][] data = null;
    try {

      FileInputStream fis = new FileInputStream(fileName);
      XSSFWorkbook workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheetAt(indexSheet);
      XSSFRow row = sheet.getRow(0);
      int noOfRows = sheet.getPhysicalNumberOfRows();
      int noOfCols = row.getLastCellNum();
      Cell cell;
      data = new String[noOfRows - 1][noOfCols];

      for (int i = 1; i < noOfRows; i++) {
        for (int j = 0; j < noOfCols; j++) {
          row = sheet.getRow(i);
          //when blank retur null
          cell = row.getCell(j,Row.RETURN_BLANK_AS_NULL);
          switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING -> data[i - 1][j] = cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC ->  data[i - 1][j] = String.valueOf(
                cell.getNumericCellValue());
            default -> data[i - 1][j] = "";
          }

        }
      }
    } catch (Exception e) {
      System.out.println("The exception is: " + e.getMessage());
    }
    return data;
  }

  //default use index sheet 0
  public static  String[][] getExcelData(String fileName) throws IOException{
     return  getExcelData(fileName,0);
  }

  //default not include header
  public static Object[][] getCSV(String filePath, boolean includeHeader) {
    List<String[]> rows = new ArrayList<>();

    try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
      // Read all rows into a list of string arrays
      rows = csvReader.readAll();
    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }
    if(!includeHeader) {
      // Skip the header row by starting from index 1
      rows = rows.subList(1, rows.size());
    }

    // Convert List<String[]> to Object[][]
    Object[][] data = new Object[rows.size()][];

    // Fill the 2D array with the row data
    for (int i = 0; i < rows.size(); i++) {
      data[i] = rows.get(i);
    }
    return data;
  }


  //default not include header
  public static Object[][] getCSV(String filePath) {
    return  getCSV(filePath,false);
  }
}
