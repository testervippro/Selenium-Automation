package com.thoaikx.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.thoaikx.data.changeless.CONSTANTS;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;


public class DataProviderUtils {

    public static void main(String[] args) throws IOException {

        //System.out.println(configuration().pathData());
        Object[][] data =readCsvData("src/test/resources/data/username.csv");

        for (Object[] row : data) {

            System.out.println(Arrays.toString(row));
        }

        String jsonFile = "src/test/resources/data/user.json"; // cross-platform path

        Object[][] testData = readJsonData(jsonFile);
        for (Object[] row : testData) {
            System.out.println(Arrays.toString(row));
        }
    }


    // Dynamic way dont need know number of column before
    @DataProvider(name = CONSTANTS.READ_CSV)
    public static Object[][] readCsvData(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(
                String.valueOf(path)))) {

            // Skip the first line (header)
            return reader.lines()
                    .skip(1) // <-- Skipping header
                    .map(line -> {
                        String[] parts = line.split(",");
                        Object[] row = new Object[parts.length];
                        for (int i = 0; i < parts.length; i++) {
                            row[i] = parseValue(parts[i].trim());
                        }
                        return row;
                    })
                    .toArray(Object[][]::new);
        }
    }

    @DataProvider(name = CONSTANTS.READ_JSON)
    public static Object[][] readJsonData(String jsonFilePath) throws IOException {
        // Read JSON file as a string
        String content = Files.readString(Paths.get(jsonFilePath));

        // Parse the content as a JsonArray
        JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();

        Object[][] data = new Object[jsonArray.size()][];

        int index = 0;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            List<Object> values = new ArrayList<>();

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isBoolean()) values.add(primitive.getAsBoolean());
                    else if (primitive.isNumber()) values.add(primitive.getAsNumber());
                    else if (primitive.isString()) values.add(primitive.getAsString());
                } else {
                    values.add(value.toString()); // fallback for objects/arrays
                }
            }

            data[index++] = values.toArray();
        }

        return data;
    }

    private static Object parseValue(String value) {
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                return value; // Return as String
            }
        }
    }

}
