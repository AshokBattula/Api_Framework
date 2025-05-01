package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The {@code TokenManager} class provides utility methods to read, write,
 * and remove key-value pairs (typically tokens) from a properties file.
 * <p>
 * This is useful for dynamically storing and retrieving access tokens
 * or other configuration data during runtime for API automation and testing.
 * <p>
 * The target file is expected to be located at: {@code src/main/resources/QA.properties}.
 */
public class TokenManager {

    // Path to the properties file
    static String filepath = "src/main/resources/QA.properties";

    /**
     * Writes or updates a key-value pair in the properties file.
     *
     * @param key   the property key to write/update
     * @param token the value (usually a token or string) associated with the key
     * @throws RuntimeException if an I/O error occurs while accessing the properties file
     */
    public static void writeProperty(String key, String token) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(filepath); // Load existing properties
            props.load(in);
            in.close();

            props.setProperty(key, token); // Set/update the property

            FileOutputStream out = new FileOutputStream(filepath); // Save updated properties
            props.store(out, null);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write property", e);
        }
    }

    /**
     * Removes a property (key and its value) from the properties file.
     *
     * @param key the property key to remove
     * @throws RuntimeException if an I/O error occurs while accessing the properties file
     */
    public static void removeProperty(String key) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(filepath);
            props.load(in);
            in.close();

            props.remove(key); // Remove the key

            FileOutputStream out = new FileOutputStream(filepath);
            props.store(out, null); // Save the updated properties
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to remove property", e);
        }
    }

    /**
     * Retrieves the value associated with a given property key.
     *
     * @param key the property key to look up
     * @return the value corresponding to the key, or {@code null} if not found
     * @throws RuntimeException if an I/O error occurs while accessing the properties file
     */
    public static String getProperty(String key) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(filepath);
            props.load(in);
            in.close();
            return props.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get property", e);
        }
    }
}
