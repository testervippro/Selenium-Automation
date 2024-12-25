package com.thoaikx;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.thoaikx.network.UtilsNetwork.downloadFile;
import static java.lang.String.valueOf;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String urlDowloadSeleniumServer = "https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.27.0/selenium-java-4.27.0.zip";

        //storage in project
        String targetPath = Path.of(System.getProperty("user.dir"), "lib/selenium-java-4.27.0.jar").toString();

        //Storage selenium server in local
        Path targetDirSelenium = Path.of(System.getProperty("user.home"), "Downloads", "selenium-server");
        Path targetDirJenkin = Path.of(System.getProperty("user.home"), "Downloads", "jenkin");

        //
        if (!Files.exists(targetDirSelenium))
            Files.createDirectory(targetDirSelenium);

        String targetPath2 = Path.of(valueOf(targetDirSelenium),
                "selenium-java-4.27.0.jar"
        ).toString();

        //Dowload selenium server
        if (!Files.exists(Path.of(targetPath2))) {
            downloadFile(urlDowloadSeleniumServer, targetPath2);
        }
    }



}
