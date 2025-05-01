package helperMethods;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import config.ConfigReader;
import org.testng.Assert;

import java.net.SocketTimeoutException;

public class Helpers {

    /**
     * Gets the name of the current test case by scanning the stack trace for test-related classes.
     *
     * @return the name of the test case class and method.
     */
    private static String getTestCaseName() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement elem : stackTrace) {
                if (elem.getClassName().contains("tests") || elem.getClassName().toLowerCase().contains("test")) {
                    return elem.getClassName() + "." + elem.getMethodName();
                }
            }
        } catch (Exception ignored) {}
        return "UnknownTestCase";
    }

    /**
     * Builds a request specification with optional timeout and optional body.
     *
     * @param body       JSON string body to be sent.
     * @param hasBody    whether the request should contain a body.
     * @param useTimeout whether timeout should be applied from config.
     * @return configured RequestSpecification object.
     */
    private static io.restassured.specification.RequestSpecification buildRequest(String body, boolean hasBody, boolean useTimeout) {
        io.restassured.specification.RequestSpecification request = given();

        if (useTimeout) {
            int timeout = Integer.parseInt(ConfigReader.getKey("timeout"));
            request.config(RestAssured.config()
                    .httpClient(HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", timeout)
                            .setParam("http.socket.timeout", timeout)
                            .setParam("http.connection-manager.timeout", (long) timeout)));
        }

        request.contentType(ContentType.JSON);

        if (hasBody) {
            request.body(body);
        }

        return request;
    }

    /** Sends a GET request.
     * @param url API endpoint URL.
     * @param body Optional request body.
     * @param hasBody Whether to include body.
     * @param useTimeout Whether to apply timeout.
     * @return Response object.
     */
    public Response sendGet(String url, String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout).when().get(url);
        } catch (Exception e) {
            handleException("GET", url, e);
            return null;
        }
    }

    /** Sends a POST request.
     * @param url API endpoint URL.
     * @param body JSON body.
     * @param hasBody Whether to include body.
     * @param useTimeout Whether to apply timeout.
     * @return Response object.
     */
    public Response sendPost(String url, String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout).when().post(url);
        } catch (Exception e) {
            handleException("POST", url, e);
            return null;
        }
    }

    /** Sends a PUT request.
     * @param url API endpoint.
     * @param body JSON body.
     * @param hasBody Whether to include body.
     * @param useTimeout Whether to apply timeout.
     * @return Response object.
     */
    public Response sendPut(String url, String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout).when().put(url);
        } catch (Exception e) {
            handleException("PUT", url, e);
            return null;
        }
    }

    /** Sends a DELETE request.
     * @param url API endpoint.
     * @param body JSON body.
     * @param hasBody Whether to include body.
     * @param useTimeout Whether to apply timeout.
     * @return Response object.
     */
    public Response sendDelete(String url, String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout).when().delete(url);
        } catch (Exception e) {
            handleException("DELETE", url, e);
            return null;
        }
    }

    /** Sends a GET request with Authorization header.
     * @param body JSON body.
     * @param hasBody Whether body is present.
     * @param useTimeout Apply timeout.
     * @param CustomTimeout Currently unused.
     * @return Response object.
     */
    public Response sendPostWithHeaders(String body, boolean hasBody, boolean useTimeout, boolean CustomTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().get(ConfigReader.getKey("url"));
        } catch (Exception e) {
            handleException("GET", ConfigReader.getKey("url"), e);
            return null;
        }
    }

    /** Sends a POST request with Authorization header.
     * @param body JSON body.
     * @param hasBody Whether body is present.
     * @param useTimeout Apply timeout.
     * @return Response object.
     */
    public Response sendPostWithHeaders(String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().post(ConfigReader.getKey("url"));
        } catch (Exception e) {
            handleException("POST", ConfigReader.getKey("url"), e);
            return null;
        }
    }

    /** Sends a PUT request with Authorization header.
     * @param body JSON body.
     * @param hasBody Whether body is present.
     * @param useTimeout Apply timeout.
     * @return Response object.
     */
    public Response sendPutWithHeaders(String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().put(ConfigReader.getKey("url"));
        } catch (Exception e) {
            handleException("PUT", ConfigReader.getKey("url"), e);
            return null;
        }
    }

    /** Sends a DELETE request with Authorization header.
     * @param body JSON body.
     * @param hasBody Whether body is present.
     * @param useTimeout Apply timeout.
     * @return Response object.
     */
    public Response sendDelWithHeaders(String body, boolean hasBody, boolean useTimeout) {
        try {
            return buildRequest(body, hasBody, useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().delete(ConfigReader.getKey("url"));
        } catch (Exception e) {
            handleException("DELETE", ConfigReader.getKey("url"), e);
            return null;
        }
    }

    /** Sends a POST request with custom timeout handling.
     * @param url URL to send request to.
     * @param body Request body.
     * @param useTimeout Apply timeout.
     * @param useCustomTimeout Use custom timeout value.
     * @param customTime Custom timeout in ms.
     * @return Response object.
     */
    public Response sendPostWithTimeout(String url, String body, boolean useTimeout, boolean useCustomTimeout, int customTime) {
        Response response = null;
        try {
            response = buildRequest(body, body != null && !body.isEmpty(), useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().post(url);

            if (useTimeout) {
                if (useCustomTimeout) {
                    validateResponseTimeWithCustomTimeout(response, customTime);
                } else {
                    validateResponseTime(response);
                }
            }

            return response;
        } catch (Exception e) {
            handleException("POST", url, e);
            return null;
        }
    }

    /** Same as above but misuses POST for GET — consider fixing.
     * @see sendPostWithTimeout
     */
    public Response sendGetWithTimeout(String url, String body, boolean useTimeout, boolean useCustomTimeout, int customTime) {
        Response response = null;
        try {
            response = buildRequest(body, body != null && !body.isEmpty(), useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().post(url);

            if (useTimeout) {
                if (useCustomTimeout) {
                    validateResponseTimeWithCustomTimeout(response, customTime);
                } else {
                    validateResponseTime(response);
                }
            }

            return response;
        } catch (Exception e) {
            handleException("GET", url, e);
            return null;
        }
    }

    /** Same as above — currently all methods use POST incorrectly for PUT, GET, DELETE. */
    public Response sendPutWithTimeout(String url, String body, boolean useTimeout, boolean useCustomTimeout, int customTime) {
        Response response = null;
        try {
            response = buildRequest(body, body != null && !body.isEmpty(), useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().post(url);

            if (useTimeout) {
                if (useCustomTimeout) {
                    validateResponseTimeWithCustomTimeout(response, customTime);
                } else {
                    validateResponseTime(response);
                }
            }

            return response;
        } catch (Exception e) {
            handleException("PUT", url, e);
            return null;
        }
    }

    /** Same issue as above — POST method is being used for DELETE here. */
    public Response sendDeleteWithTimeout(String url, String body, boolean useTimeout, boolean useCustomTimeout, int customTime) {
        Response response = null;
        try {
            response = buildRequest(body, body != null && !body.isEmpty(), useTimeout)
                    .header("Authorization", ConfigReader.getKey("token"))
                    .when().post(url);

            if (useTimeout) {
                if (useCustomTimeout) {
                    validateResponseTimeWithCustomTimeout(response, customTime);
                } else {
                    validateResponseTime(response);
                }
            }

            return response;
        } catch (Exception e) {
            handleException("DELETE", url, e);
            return null;
        }
    }

    /** Asserts response status code.
     * @param response Response object.
     * @param expectedCode Expected HTTP status.
     */
    public void assertStatusCode(Response response, int expectedCode) {
        if (response != null) {
            response.then().statusCode(expectedCode);
        } else {
            Assert.fail("Response was null, possibly due to timeout or connection issue.");
        }
    }

    /** Validates response time using default timeout from config.
     * @param response Response object.
     */
    public void validateResponseTime(Response response) {
        try {
            if (response == null) {
                System.out.println(getTestCaseName() + " - Response was null (likely due to timeout)");
                Assert.fail("No response received.");
                return;
            }
        } catch (Exception e) {
            System.out.println("TestCase failed because of Timeout of API");
        }

        try {
            long responseTime = response.getTime();
            long maxAllowedTime = Long.parseLong(ConfigReader.getKey("timeout"));

            if (responseTime > maxAllowedTime) {
                System.err.println(getTestCaseName() + " - API exceeded allowed time. Response time: " + responseTime + "ms (Allowed: " + maxAllowedTime + "ms)");
                Assert.fail("API exceeded allowed time.");
            }
        } catch (Exception e) {
            System.err.println(getTestCaseName() + " - Error validating response time.");
            Assert.fail("Error in response time validation.");
        }
    }

    /** Validates response time against custom timeout.
     * @param response Response object.
     * @param customTimeoutMs Custom timeout in milliseconds.
     */
    public void validateResponseTimeWithCustomTimeout(Response response, long customTimeoutMs) {
        try {
            if (response == null) {
                System.out.println(getTestCaseName() + " - Response was null (likely due to timeout)");
                Assert.fail("No response received.");
                return;
            }

            long responseTime = response.getTime();

            if (responseTime > customTimeoutMs) {
                System.err.println(getTestCaseName() + " - API exceeded custom allowed time. Response time: " + responseTime + "ms (Allowed: " + customTimeoutMs + "ms)");
                Assert.fail("API exceeded custom allowed time.");
            }

        } catch (Exception e) {
            System.err.println(getTestCaseName() + " - Error validating custom timeout for response.");
            Assert.fail("Error in custom response time validation.");
        }
    }

    /** Handles exceptions centrally with stack trace reporting.
     * @param methodType HTTP method.
     * @param url Request URL.
     * @param e Exception caught.
     */
    private void handleException(String methodType, String url, Exception e) {
        String testName = getTestCaseName();
        if (e instanceof SocketTimeoutException || e.getMessage().contains("timed out")) {
            System.out.println(testName + " - " + methodType + " request to [" + url + "] timed out.");
        } else {
            System.out.println(testName + " - " + methodType + " request to [" + url + "] failed with error: " + e.getMessage());
        }
        System.out.println(testName + " has been timed out");
    }

    /**
     * Singleton-style instance generator for Helpers.
     *
     * @return a new instance of Helpers.
     */
    public static Helpers getInstance() {
        return new Helpers();
    }
}
