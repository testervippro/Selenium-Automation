
package com.thoaikx.enums;

import java.util.Map;

import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;


public enum Target {

    LOCAL("local"), LOCAL_SUITE("local-suite"), SELENIUM_GRID("selenium-grid");

    private final String value;
    private static final Map<String, Target> ENUM_MAP;

    Target(String value) {
        this.value = value;
    }
   public   String getValue ( ){
        return  this.value.toLowerCase();
     }
    Target valueOf(){
        return  this;
    }


    static {
        ENUM_MAP = Stream.of(Target.values())
                .collect(toMap(Target::getValue, Target::valueOf));
    }

    public static Target get(String value)  {
        if (!ENUM_MAP.containsKey(value.toLowerCase()))
            throw new IllegalArgumentException(
                    String.format("Value %s not valid. Use one of the TARGET enum values", value));

        return ENUM_MAP.get(value.toLowerCase());
    }
}

