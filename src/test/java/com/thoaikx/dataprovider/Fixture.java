package com.thoaikx.dataprovider;

import static com.thoaikx.ultis.ExcelUtils.getCSV;
import static com.thoaikx.ultis.ExcelUtils.getExcelData;

import java.io.IOException;
import java.util.List;
import org.testng.annotations.DataProvider;

public class Fixture {

  @DataProvider(name = "data-provider")
  public Object[][] dpMethod() {
        return new Object[][]{
            {"First-Value"},
            {"Second-Value"}
        };
      }



  @DataProvider(name = "excel")
  public Object[][] excelData() throws IOException {
    return getExcelData("src/test/resources/data/thoaikx.xlsx");
  }

  @DataProvider(name = "csv")
  public Object[][] csvData () {
    return getCSV("src/test/resources/data/csvfile.csv");
  }








}
