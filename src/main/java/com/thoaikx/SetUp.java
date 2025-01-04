package com.thoaikx;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.thoaikx.network.UtilsNetwork.downloadFile;
import static java.lang.String.valueOf;

public class SetUp {
    public static void main(String[] args) throws IOException, InterruptedException {

        //java -jar selenium-server-4.27.0.jar standalone
        //chmod +r selenium-server-4.27.0.jar && java -jar selenium-server-4.27.0.jar standalone
        // URLs for downloading Selenium and Jenkins
        String urlDownloadSelenium = "https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.27.0/selenium-server-4.27.0.zip";
        String urlDownloadJenkins = "https://get.jenkins.io/war-stable/latest/jenkins.war";

        // Define target directories
        Path targetDirSelenium = Path.of(System.getProperty("user.home"), "Downloads", "selenium-server");
        Path targetDirJenkins = Path.of(System.getProperty("user.home"), "Downloads", "jenkins-server");

        //If not exitst create one
        if (!Files.exists(targetDirSelenium))
            Files.createDirectory(targetDirSelenium);

        if (!Files.exists(targetDirJenkins))
            Files.createDirectory(targetDirJenkins);

        //Define full path
        String fullPathSelenium = Path.of(valueOf(targetDirSelenium),"selenium-server-4.27.0.jar"
        ).toString();

        String fullPathJenkins = Path.of(valueOf(targetDirJenkins),"jenkins.war"
        ).toString();

        //Dowload selenium server
        if (!Files.exists(Path.of(fullPathSelenium))) {
            downloadFile(urlDownloadSelenium, fullPathSelenium);
        }

        if (!Files.exists(Path.of(fullPathJenkins))) {
            downloadFile(urlDownloadJenkins, fullPathJenkins);
        }
    }



}

