package Ashok.base;

import helperMethods.Helpers;
import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.ITestContext;
import config.ConfigReader;
import Logging.logs;
import utils.ExcelLogger;
import utils.LogMessages;
import Email.SendEmail;

/**
 * BaseTest is the parent class for all API test cases.
 * It handles environment setup, RestAssured configuration,
 * and post-test actions such as report generation and email sending.
 */
public class BaseTest {

    /**
     * Helper utility instance to support test cases.
     */
    protected Helpers helpers;

    /**
     * Stores the current test execution environment (e.g., dev, qa, staging).
     */
    String environment;

    /**
     * Constructor initializes the Helpers singleton instance.
     */
    public BaseTest() {
        this.helpers = Helpers.getInstance(); // Ensures always initialized
    }

    /**
     * This method is executed before any test suite runs.
     * It determines the environment, loads the appropriate config,
     * sets up RestAssured base URI, and logs the test execution start.
     *
     * @param context TestNG context used to fetch environment if not passed via system property
     */
    @BeforeSuite
    public void setup(ITestContext context) {
        environment = System.getProperty("env");

        if (environment == null) {
            environment = context.getCurrentXmlTest().getParameter("env");
        }

        if (environment == null) {
            environment = "dev";
        }

        logs.logInfoMessage(BaseTest.class, "Running tests in the " + environment + " environment.");
        ConfigReader.loadProperties(environment);

        RestAssured.baseURI = ConfigReader.getKey("url");
        System.out.println("Running tests with Base URI: " + RestAssured.baseURI);
        logs.logInfoMessage(BaseTest.class, LogMessages.TEST_EXECUTION_START);
    }

    /**
     * This method is executed after all test suites are complete.
     * It resets RestAssured, generates Excel reports, and optionally sends an email with the results.
     */
    @AfterSuite
    public void tearDown() {
        RestAssured.reset();
        String path = "target/TestResults";
        ExcelLogger.generateReport(path);
        boolean sendorNot = Boolean.parseBoolean(config.ConfigReader.getKey("send_email"));
        SendEmail.sendEmailWithReport(sendorNot);
        logs.logInfoMessage(BaseTest.class, LogMessages.TEST_EXECUTION_END);
    }
}