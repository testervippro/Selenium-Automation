package com.thoaikx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Test01 {
    public static void main(String[] args) throws IOException, InterruptedException {
            // Define the target directory for Allure results
            Path atargetDirectory = Path.of(System.getProperty("user.dir"), "target");
            System.out.println("Target Directory: " + atargetDirectory);

            Path targetDirectory = Path.of(System.getProperty("user.dir"), "target", "allure-results");

            try {
                // Execute the Allure serve command
                Process process = new ProcessBuilder("/bin/bash", "-c", "cd " + targetDirectory + " && allure serve")
                        .start();

                // Wait for the process to complete and check the exit code
                int exitCode = process.waitFor();
                System.out.println(exitCode == 0 ? "Allure report served successfully." : "Failed with exit code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


