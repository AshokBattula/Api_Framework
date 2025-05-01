package utils;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

/**
 * The {@code readfiles} class provides utility methods for reading JSON files
 * from the classpath and resolving dynamic URLs using property files.
 * <p>
 * This class simplifies test setup by allowing external data and configuration
 * to be loaded at runtime.
 */
public class readfiles {

    /**
     * Reads a JSON file from the classpath and returns its content as a string.
     *
     * @param filePath the relative path to the JSON file within the {@code resources} directory
     * @return the content of the JSON file as a {@code String}
     * @throws RuntimeException if the file is not found in the classpath
     */
    public static String readJsonFromFile(String filePath) {
        InputStream is = readfiles.class.getClassLoader().getResourceAsStream(filePath);
        if (is == null) {
            throw new RuntimeException("JSON file not found at path: " + filePath);
        }
        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
        return scanner.useDelimiter("\\A").next();
    }

    /**
     * Reads the `qa.properties` file from the classpath and resolves the URL
     * by replacing the placeholder `${inputCity}` with the provided input.
     *
     * @param input the value to replace the `${inputCity}` placeholder in the URL
     * @return the resolved URL string with the placeholder substituted
     * @throws RuntimeException if the `qa.properties` file is missing or the `url` key is not found
     */
    public static String ResolvedUrl(String input) {
        try (InputStream is = readfiles.class.getClassLoader().getResourceAsStream("qa.properties")) {
            if (is == null) {
                throw new RuntimeException("Properties file not found in classpath");
            }

            Properties props = new Properties();
            props.load(is);

            String rawUrl = props.getProperty("url");
            if (rawUrl == null) {
                throw new RuntimeException("Key 'url' not found in qa.properties");
            }

            String resolvedUrl = rawUrl.replace("${inputCity}", input);
            System.out.println(resolvedUrl);
            return resolvedUrl;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
