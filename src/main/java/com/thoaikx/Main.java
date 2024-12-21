package com.thoaikx;

import static java.util.Arrays.sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.thoaikx.config.ConfigurationManager.configuration;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        System.out.println(System.getProperty("user.home"));
        Map<String, String> environment = processBuilder.environment();
        environment.forEach((key, value) -> System.out.println(key +""+ value));
        // Define the PowerShell command
        String cmd = "Get-ExecutionPolicy";



}
}








