package com.thoaikx.config;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config;


@LoadPolicy(LoadType.MERGE)
@Config.Sources({
    "system:properties",
    "classpath:general.properties",
    "classpath:selenium-grid.properties"})
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
    
} 
