package com.thoaikx;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String urlDowload = "https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.27.0/selenium-java-4.27.0.zip";

         String targetPath = Path.of(System.getProperty("user.dir"), "lib/selenium-java-4.27.0.jar").toString(); 


         downloadSeleniumServer(urlDowload, targetPath);
     
        }

  public static void downloadSeleniumServer (String url , String outputPath ) throws FileNotFoundException, IOException, InterruptedException{

    HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();


     HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        try (InputStream inputStream = response.body();
             FileOutputStream outputStream = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

  }      


}
