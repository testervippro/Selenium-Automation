package com.thoaikx;


import com.thoaikx.config.MavenCommandBuilder;
import com.thoaikx.enums.Browser;
import com.thoaikx.enums.Target;
import com.thoaikx.processsbuilder.ProcessManager;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.nio.file.Files;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {

    System.out.printf("hello word");

//    MavenCommandBuilder command = MavenCommandBuilder.builder()
//            .profile("web-execution")
//            .suite(Target.LOCAL_SUITE)
//            .target(Target.LOCAL_SUITE) // Execution target
//            .browser(Browser.CHROME) // Browser for local execution
//            .timeout(60000L) // Global test timeout
//            .baseUrl("https://rahulshettyacademy.com/angularpractice/") // Base URL
//            .headless(true) // Headless mode
//            .autoReport(false) // Auto reporting
//            .gridUrl("localhost") // Selenium Grid URL
//            .gridPort(4444) // Selenium Grid port
//            .gridSeparate(false) // Separate ports for each browser
//            .build();
//
//    int exitCode = command.execute();//   System.out.println("Maven test execution completed with exit code: " + exitCode);
  //ProcessManager.executeCommand("./mvnw test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=true -Dbrowser=firefox");
      //String tmpdir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();



    // Get the path of the downloaded ChromeDriver
    String driverPath = WebDriverManager.chromedriver().getDownloadedDriverPath();
    System.out.printf(driverPath);
  }


}
