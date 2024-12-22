package com.thoaikx;

import static java.util.Arrays.sort;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.thoaikx.config.ConfigurationManager.configuration;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(System.getProperty("${user.home"));

        try {
            // Read the JSON file and map it to a List of Person records
            //convert json  to record https://www.danvega.dev/tools/json-to-java-record
            List<Person> persons = objectMapper.readValue(
                new File("src/test/resources/data/person.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));

            // Print the list of persons
            persons.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


}
record Person(String name, int age, String city) {}

}








