package utils;

/**
 * The {@code LogMessages} class provides a centralized collection of constant log messages
 * used throughout the test automation framework.
 *
 * <p>This class is intended to standardize logging across test execution phases such as setup,
 * execution, teardown, and results reporting. Using these constants helps improve consistency,
 * readability, and maintainability of logs.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 *     System.out.println(LogMessages.TEST_EXECUTION_START + testName);
 *     System.out.println(LogMessages.TEST_PASSED + testName);
 * </pre>
 */
public class LogMessages {

    /** Message indicating the start of a test execution. */
    public static final String TEST_EXECUTION_START = "Test execution started: ";

    /** Message indicating the end of a test execution. */
    public static final String TEST_EXECUTION_END = "Test execution finished: ";

    /** Message indicating the test passed successfully. */
    public static final String TEST_PASSED = "Test passed successfully: ";

    /** Message indicating the test failed. */
    public static final String TEST_FAILED = "Test failed: ";

    /** Message indicating the test was skipped. */
    public static final String TEST_SKIPPED = "Test skipped: ";

    /** Message indicating the start of test setup. */
    public static final String TEST_SETUP_START = "Test setup started.";

    /** Message indicating the end of test setup. */
    public static final String TEST_SETUP_END = "Test setup completed.";

    /** Message indicating the start of test teardown. */
    public static final String TEST_TEARDOWN_START = "Test teardown started.";

    /** Message indicating the end of test teardown. */
    public static final String TEST_TEARDOWN_END = "Test teardown completed.";
}
