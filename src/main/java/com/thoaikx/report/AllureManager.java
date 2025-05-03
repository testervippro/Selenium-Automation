
package com.thoaikx.report;

import com.thoaikx.enums.Target;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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


}
