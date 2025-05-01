package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The {@code ConfigReader} class is responsible for loading configuration
 * properties based on the specified environment (e.g., QA, DEV, PROD).
 * It provides methods to load a `.properties` file and fetch property values by key.
 *
 * <p>Example usage:
 * <pre>
 *     ConfigReader.loadProperties("qa");
 *     String baseUrl = ConfigReader.getKey("base.url");
 * </pre>
 */
public class ConfigReader {

    /** Properties object to store configuration key-value pairs. */
    private static Properties prop = new Properties();

    /**
     * Loads properties from an environment-specific file.
     * The file must be located in {@code src/main/resources/} and named in lowercase
     * (e.g., {@code qa.properties}, {@code dev.properties}, {@code prod.properties}).
     *
     * @param environment the environment name (e.g., "QA", "DEV", "PROD")
     * @throws RuntimeException if the properties file cannot be found or read
     */
    public static void loadProperties(String environment) {
        String fileName = "src/main/resources/" + environment.toLowerCase() + ".properties";

        try (FileInputStream input = new FileInputStream(fileName)) {
            prop.load(input);
            System.out.println("Properties loaded from: " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
        }
    }

    /**
     * Retrieves the value associated with the given key from the loaded properties.
     *
     * @param key the key whose value is to be retrieved
     * @return the value for the given key, or {@code null} if the key is not found
     */
    public static String getKey(String key) {
        return prop.getProperty(key);
    }
}
