package Ashok.TestCases;

import Ashok.base.BaseTest;
import helperMethods.SchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ExcelLogger;
import utils.readfiles;

public class SampleOne extends BaseTest{

    private Response response;

    @Test
    public void sample() {
        String url = "<fetch from properties>";
        String requestBody = readfiles.readJsonFromFile("RequestBodies/Posts.json");
        response = helpers.sendPostWithTimeout(url, requestBody, true,false,0);
        String responseBody = response.getBody().asString();
        SchemaValidator.validateResponse(responseBody, "<Path to the Schema file>", "<Testcase Name>");
        helpers.validateResponseTime(response);
        ExcelLogger.log("<API Name>", response.getStatusCode(), response.getTime(), response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 201);
    }
}
