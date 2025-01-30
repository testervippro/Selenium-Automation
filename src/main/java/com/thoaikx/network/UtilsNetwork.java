package com.thoaikx.network;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UtilsNetwork {

    public static void downloadFile(String url, String outputPath) throws IOException, InterruptedException {
        // Create the HTTP client
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS) // Ensure automatic redirect handling
                .build();

        // Create the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Send the request and handle the response
        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(outputPath)));

        // Check the response status code
        if (response.statusCode() == 200) {
            System.out.println("File downloaded successfully to: " + outputPath);
        } else {
            throw new IOException("Failed to download file. Status code: " + response.statusCode());
        }
    }

    public static List<String> getBrowserNamesFrommGrid(String hubUrl) {
        try {
            // Send the HTTP GET request to the Selenium Grid hub
            Response response = RestAssured.get(hubUrl+"/status");

            // Ensure the response is successful (status code 200)
            response.then().statusCode(200);

            // Get the raw response body as a string
            String responseBody = response.getBody().asString();

            // Use JsonPath to parse the response body
            JsonPath jsonPath = new JsonPath(responseBody);

            // Extract the list of browser names from all slots in the first node
            List<String> browserNames = jsonPath.getList("value.nodes[0].slots.stereotype.browserName");
            // Check if there are at least 3 browser names,
            // otherwise limit the list size, cus reponse return 12 value , look like duplicate
            if (browserNames.size() > 3) {
                return browserNames.subList(0, 3); // Return only the first 3 browser names
            } else {
                return browserNames; // Return the list as is if it has less than 3
            }


        } catch (Exception e) {
            System.err.println("Error fetching Selenium Grid status: " + e.getMessage());
            return null;
        }
    }
}
