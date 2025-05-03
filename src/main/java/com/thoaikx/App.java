package com.thoaikx;


import com.thoaikx.network.UtilsNetwork;
import com.thoaikx.processsbuilder.ProcessManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException, HttpException {
    // Replace with your Selenium Grid hub URL (e.g., localhost or your specific hub)
    String hubUrl = "http://localhost:4444";
    // Get and print the list of browser names from all slots
    String urlselenium = "https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.27.0/selenium-server-4.27.0.jar";
    String urlJenkin = "https://get.jenkins.io/war-stable/2.479.3/jenkins.war";
   // UtilsNetwork.downloadFile(urlselenium, Path.of(System.getProperty("user.dir"),"lib","selenium-server-4.27.0.jar").toString());
    //UtilsNetwork.downloadFile(urlselenium, Path.of(System.getProperty("user.dir"),"lib","jenkins.war").toString());



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


  }


}
