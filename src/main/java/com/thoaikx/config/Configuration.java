package com.thoaikx.config;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config;


//The "system:properties" source in configuration frameworks
// (like MicroProfile Config, owner, or QConfig) refers to the JVM
// system properties.
// These are key-value pairs that can be passed to the Java application
// at runtime using the -D flag.
@LoadPolicy(LoadType.MERGE)
@Config.Sources({
    "system:properties",
    "classpath:config.properties"})
public interface Configuration  extends Config{

    @Key("target")
    String target();
    

    @Key("browser")
    String browser();

    @Key("headless")
    Boolean headless();

    @Key("url.base")
    String url();

    @Key("timeout")
    int timeout();

    @Key("grid.url")
    String gridUrl();

    @Key("grid.port")
    String gridPort();

    @Key("auto.report")
    boolean autoReport();

    @Key("grid.separate")
    boolean separatePort();
    
} 
