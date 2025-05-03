package com.thoaikx.processsbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessManager {


    public static void executeCommand(String command) throws IOException, InterruptedException {
        // Check the operating system and build the command appropriately
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            // Windows: Use cmd.exe to execute the command
            processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Linux/macOS: Use bash to execute the command
            processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
        } else {
            // If the OS is not supported, throw an exception
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }

        // Execute the process and handle the output
        executeProcess(processBuilder);
    }

    // Helper method to execute the process and capture output
    private static void executeProcess(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        // Redirect the error stream to the standard output stream
        processBuilder.redirectErrorStream(true);

        // Start the process
        Process process = processBuilder.start();

        // Capture the output of the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Read and print the output line by line
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the command to finish and capture the exit code
        int exitCode = process.waitFor();
        System.out.println(" command exited with code: " + exitCode);
    }
}



