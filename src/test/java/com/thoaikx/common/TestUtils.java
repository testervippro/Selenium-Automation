package com.thoaikx.common;

import com.google.common.io.Files;
import com.thoaikx.record.RecorderManager;
import com.thoaikx.report.AllureManager;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static com.thoaikx.config.ConfigurationManager.configuration;


@Log4j2
public class TestUtils {

    public  static void startSeleniumGrid() throws IOException, InterruptedException {

        if (configuration().target().equalsIgnoreCase("selenium-grid -")) {
            killProcessOnPort(4444);

            AllureManager.setAllureEnvironmentInformation();

            Thread thread = new Thread(() -> {
                try {
                    CommandLine runGrid = CommandLine.parse("java -jar grid/selenium-server-4.30.0.jar standalone");

                    DefaultExecutor executorRunGrid = new DefaultExecutor();
                    executorRunGrid.setStreamHandler(new PumpStreamHandler(System.out));
                    executorRunGrid.execute(runGrid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.setDaemon(true);
            thread.start();

            // Optional wait to let the grid boot up before tests start
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private static void killProcessOnPort(int port) {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                // For Windows
                String command = String.format("cmd /c for /f \"tokens=5\" %%a in ('netstat -aon ^| findstr :%d') do taskkill /f /pid %%a", port);
                Runtime.getRuntime().exec(command);
            } else {
                // For Unix/Linux/Mac
                String[] command = {"/bin/sh", "-c", String.format("lsof -ti:%d | xargs kill -9", port)};
                Runtime.getRuntime().exec(command);
            }

            // Wait a moment to ensure the port is freed
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Failed to kill process on port " + port);
            e.printStackTrace();
        }
    }

    public static void attachLog() {
        // Attach log file
        File logFile = Path.of( "target","test_automation.log").toFile();
        log.info("Log file path: {}", logFile.getAbsolutePath());

        if (logFile.exists() && logFile.isFile()) {
            try {
                Allure.addAttachment("Execution Log", "text/plain",
                        new FileInputStream(logFile), "log");
            } catch (IOException e) {
                log.error("Failed to attach log file: ", e);
            }
        } else {
            log.info("Log file does not exist: {}", logFile.getAbsolutePath());
        }
    }

    public static void attachVideo() {
        File videoPath = new File("videos", RecorderManager.nameVideo);
        log.info("Video path: {}", videoPath.getAbsolutePath());

        if (videoPath.exists() && videoPath.isFile()) {
            try {
                Allure.addAttachment("Test Video", "video/mp4",
                        Files.asByteSource(videoPath).openStream(), "mp4");
            } catch (IOException e) {
                log.error("Failed to attach video: ", e);
            }
        } else {
            log.info("Video file does not exist: {}", videoPath.getAbsolutePath());
        }
    }
}
