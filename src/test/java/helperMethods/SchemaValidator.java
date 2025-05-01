package helperMethods;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchemaValidator {

    // Thread-safe list for failed test cases
    private static final List<String> failedTestCases = Collections.synchronizedList(new ArrayList<>());

    // Validates JSON response against schema
    public static void validateResponse(String responseJson, String schemaFileName, String testCaseName) {
        JSONObject responseObject = new JSONObject(responseJson);
        InputStream schemaStream = SchemaValidator.class.getResourceAsStream("/schemas/" + schemaFileName);

        if (schemaStream == null) {
            throw new RuntimeException("Schema file not found: " + schemaFileName);
        }

        JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
        Schema schema = SchemaLoader.load(rawSchema);

        try {
            schema.validate(responseObject);
            System.out.println(testCaseName + " passed.");
        } catch (Exception e) {
            String errorMessage = "Validation failed for " + testCaseName + ": " + e.getMessage();
            System.out.println(errorMessage);
            failedTestCases.add(errorMessage);
        }
    }

    // Displays all failed test cases
    public static void displayFailedTestCases() {
        if (!failedTestCases.isEmpty()) {
            System.out.println("\nFailed Test Cases:");
            for (int i = 0; i < failedTestCases.size(); i++) {
                System.out.println((i + 1) + ". " + failedTestCases.get(i));
            }
        } else {
            System.out.println("\nAll test cases passed.");
        }
    }

    // Clears the list of failed test cases
    public static void clearFailedTestCases() {
        failedTestCases.clear();
    }
}
