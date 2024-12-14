package com.thoaikx;

import static java.util.Arrays.sort;

import java.text.Collator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.thoaikx.config.ConfigurationManager.configuration;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class Main {
    public static void main(String[] args) {
        System.out.println(Parts.valueOf("skin"));

    }
}

enum Parts {

    SKIN, Muscles, Bones, Organs, Tissue;

}