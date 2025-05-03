
package com.thoaikx.data.changeless;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.thoaikx.config.ConfigurationManager.configuration;

public final class BrowserData {

    private BrowserData() throws IOException {
    }




    public static final String START_MAXIMIZED = "--start-maximized";
    public static final String DISABLE_INFOBARS = "--disable-infobars";
    public static final String DISABLE_NOTIFICATIONS = "--disable-notifications";
    public static final String REMOTE_ALLOW_ORIGINS = "--remote-allow-origins=*";
    public static final String GENERIC_HEADLESS = "-headless";
    public static  final String USERDATA = "--user-data-dir=" + Path.of( System.getProperty("user.dir"),"userdata").toString();

    public static final String CHROME_HEADLESS = "--headless=new";
}
