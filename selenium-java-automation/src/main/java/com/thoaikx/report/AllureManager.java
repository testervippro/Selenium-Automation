
package com.thoaikx.report;

import com.thoaikx.enums.Target;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.lang.ProcessBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class AllureManager {

    public static void setAllureEnvironmentInformation() {
        var basicInfo = new HashMap<>(Map.of(
                "Test URL", configuration().url(),
                "Target execution", configuration().target(),
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
        String targetDirectory = System.getProperty("user.dir") + "\\target";
        String currentFolder = System.getProperty("user.dir"); // Using user.dir for flexibility



        ProcessBuilder mvnClean = new ProcessBuilder("cmd.exe", "/c",
            "cd " + currentFolder + " && mvn clean");

        ProcessBuilder generateReport = new ProcessBuilder("cmd.exe", "/c",
            "cd " + targetDirectory + " && allure serve && exit");
        Process process = generateReport.start();

        process.waitFor();

    }

}
