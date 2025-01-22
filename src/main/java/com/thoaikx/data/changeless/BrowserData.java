
package com.thoaikx.data.changeless;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BrowserData {

    private BrowserData() {
    }

    public static String createtempFolder() throws IOException {
        // Create a temporary directory for the Chrome user data
        Path tempDir = Files.createTempDirectory("chrome-user-data-");
        return tempDir.toString();
    }
    public static final String START_MAXIMIZED = "--start-maximized";
    public static final String DISABLE_INFOBARS = "--disable-infobars";
    public static final String DISABLE_NOTIFICATIONS = "--disable-notifications";
    public static final String REMOTE_ALLOW_ORIGINS = "--remote-allow-origins=*";
    public static final String GENERIC_HEADLESS = "-headless";
    public static  final String USERDATA;

    static {
        try {
            USERDATA = createtempFolder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String CHROME_HEADLESS = "--headless=new";
}
