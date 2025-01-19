package com.thoaikx.processsbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerManager {


    public static void executeCommand(String command) throws IOException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/bash", "-c", command); // Use "bash -c" for Linux/Mac; for Windows, use "cmd.exe", "/c"

        Process process = processBuilder.start();

        // Read the output of the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

    }

    public static void executeCommandAndWaitForStart(String command, String successMessage) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/bash", "-c", command); // Use "bash -c" for Linux/Mac; for Windows, use "cmd.exe", "/c"

        Process process = processBuilder.start();

        // Read the output of the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        boolean started = false;

        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Print the output for debugging

            // Check if the success message is found in the output
            if (line.contains(successMessage)) {
                started = true;
                System.out.println("Success: " + successMessage);
                break; // Exit the loop once the container is started
            }
        }

        // If the container didn't start, throw an exception
        if (!started) {
            throw new RuntimeException("Failed to start container: " + successMessage + " not found in logs.");
        }

    }

}
