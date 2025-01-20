package com.thoaikx.config;

import com.thoaikx.enums.Browser;
import com.thoaikx.enums.Target;
import com.thoaikx.processsbuilder.ProcessManager;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
public class MavenCommandBuilder {

    private String profile;
    private String suite;
    private Target target;
    private Browser browser;
    private Long timeout;
    private String baseUrl;
    private Boolean headless;
    private Boolean autoReport;
    private String gridUrl;
    private Integer gridPort;
    private Boolean gridSeparate;

    // Execute the Maven command
    public int execute() {
        // Determine the Maven wrapper command based on the OS
        String mavenCommand = isWindows() ? "mvnw.cmd" : "./mvnw";

        // Build the Maven command
        List<String> commandParts = new ArrayList<>();
        commandParts.add(mavenCommand);
        commandParts.add("clean");
        commandParts.add("test");

        // Add profile if provided
        if (profile != null && !profile.isEmpty()) {
            commandParts.add("-P");
            commandParts.add(profile);
        }

        // Add suite if provided
        if (suite != null && !suite.isEmpty()) {
            commandParts.add("-Dsuite=" + suite);
        }

        // Add target if provided
        if (target != null ) {
            commandParts.add("-Dtarget=" + target.getValue());
        }

        // Add browser if provided and target is "local"
        if (browser != null  ) {
            commandParts.add("-Dbrowser=" + browser.getValue());
        } else {
            commandParts.add("-Dbrowser=" + "chrome");
        }

        // Add timeout if provided
        if (timeout != null) {
            commandParts.add("-Dtimeout=" + timeout);
        }

        // Add base URL if provided
        if (baseUrl != null && !baseUrl.isEmpty()) {
            commandParts.add("-Durl.base=" + baseUrl);
        }

        // Add headless mode if provided
        if (headless != null) {
            commandParts.add("-Dheadless=" + headless.toString());
        }

        // Add auto report if provided
        if (autoReport != null) {
            commandParts.add("-Dauto.report=" + autoReport.toString());
        }

        // Add Selenium Grid configuration if target is "selenium-grid"
        if ("selenium-grid".equals(target)) {
            if (gridUrl != null && !gridUrl.isEmpty()) {
                commandParts.add("-Dgrid.url=" + gridUrl);
            }
            if (gridPort != null) {
                commandParts.add("-Dgrid.port=" + gridPort);
            }
            if (gridSeparate != null) {
                commandParts.add("-Dgrid.separate=" + gridSeparate.toString());
            }
        }

        // Join the command parts into a single string
        String command = String.join(" ", commandParts);

        // Execute the command using ProcessManager
        try {
            System.out.println("Executing command: " + command);
            ProcessManager.executeCommand(command);
            return 0; // Return 0 to indicate success
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        }
    }

    // Check if the OS is Windows
    private boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    // Example usage
    public static void main(String[] args) {
        MavenCommandBuilder command = MavenCommandBuilder.builder()
                .profile("web-execution")
                .suite("local-suite")
                .target(Target.LOCAL_SUITE) // Execution target
                .browser(Browser.FIREFOX) // Browser for local execution
                .timeout(60000L) // Global test timeout
                .baseUrl("https://rahulshettyacademy.com/angularpractice/") // Base URL
                .headless(true) // Headless mode
                .autoReport(false) // Auto reporting
                .gridUrl("localhost") // Selenium Grid URL
                .gridPort(4444) // Selenium Grid port
                .gridSeparate(false) // Separate ports for each browser
                .build();

        int exitCode = command.execute();
        System.out.println("Maven test execution completed with exit code: " + exitCode);
    }
}