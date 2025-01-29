
package com.thoaikx.report;

import com.thoaikx.enums.Target;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.lang.ProcessBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.thoaikx.processsbuilder.ProcessManager;
import org.apache.commons.io.FileUtils;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class AllureManager {



    public static void setAllureEnvironmentInformation() {
        var basicInfo = new HashMap<>(Map.of(
                "Test URL", configuration().url(),
                "Target execution", configuration().target() ,
                "Global timeout", String.valueOf(configuration().timeout()),
                "Headless mode", String.valueOf(configuration().headless()),
                "Local browser", configuration().browser()));

        if (configuration().target().equals(Target.SELENIUM_GRID.name())) {
            var gridMap = Map.of("Grid URL", configuration().gridUrl(), "Grid port", configuration().gridPort());
            basicInfo.putAll(gridMap);
        }

        AllureEnvironmentWriter.allureEnvironmentWriter(ImmutableMap.copyOf(basicInfo));
    }

    public static void generateReport() throws IOException, InterruptedException {
        //current directory
        Path targetDirectory = Path.of(System.getProperty("user.dir") ,"target");

        String cmd = "cd " + targetDirectory + " && allure generate";
        ProcessManager.executeCommand(cmd);



    }


    public static void deleteOldReport() throws IOException {
        Path targetPath = Path.of(System.getProperty("user.dir"), "target");
        String targetDirectory = targetPath.toString();

        Path oldReportAllure = Path.of(targetDirectory +"allure-results");
        Path oldReportSureFire = Path.of(targetDirectory +"surefire-reports");
        FileUtils.deleteDirectory(oldReportAllure.toFile());
        FileUtils.deleteDirectory(oldReportSureFire.toFile());
    }

    public static void allureOpen() {
        Thread dt = new Thread( () -> {
            if(Boolean.valueOf(configuration().autoOpenReport()))
            {
                try {
                    AllureManager.generateReport();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        dt.setDaemon(true);
        dt.start();
         
    }

}
