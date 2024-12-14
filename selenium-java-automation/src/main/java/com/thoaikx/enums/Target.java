
package com.thoaikx.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum Target {

    LOCAL("local"), LOCAL_SUITE("local-suite"), SELENIUM_GRID("selenium-grid");

    private final String value;
    private static final Map<String, Target> ENUM_MAP;

    Target(String value) {
        this.value = value;
    }

    String value() {
        return this.value;
    }

    static {
        Map<String, Target> map = stream(Target.values())
                .collect(Collectors.toMap(Target::value, target -> target));

        ENUM_MAP = Collections.unmodifiableMap(map);

    }

    public static Target get(String value) {
        if (!ENUM_MAP.containsKey(value.toLowerCase()))
            throw new IllegalArgumentException(
                    String.format("Value %s not valid. Use one of the TARGET enum values", value));

        return ENUM_MAP.get(value.toLowerCase());
    }
}
