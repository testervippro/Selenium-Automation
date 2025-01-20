package com.thoaikx.enums;

public enum Browser {

    CHROME("chrome"),
    EDGE("edge"),
    FIREFOX("firefox"),
    SAFARI("safari");

    private final String value;


    Browser(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value.toLowerCase();
    }
}