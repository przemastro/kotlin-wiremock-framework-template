package steps

import io.cucumber.java.After
import io.cucumber.java.Before
import org.testng.Assert
import utils.ApiRest

class ApiTestSteps : ApiRest() {

    @Before
    fun beforeScenario() {
        //Do before each scenario
    }

    @After
    fun afterScenario() {
        //Do after each scenario
    }

    @io.cucumber.java.en.When("I send {string} request to {string} endpoint")
    fun sendRequestToEndpoint(method: String, endpoint: String) {
        request(method, endpoint)
    }

    @io.cucumber.java.en.When("I send {string} request to {string} endpoint with parameters {string}")
    fun sendRequestToEndpointWithParameters(method: String, endpoint: String, parameters: String) {
        val endpointWithParameters = endpoint + parameters
        request(method, endpointWithParameters)
    }

    @io.cucumber.java.en.When("I send {string} request to {string} endpoint with body {string}")
    fun sendRequestToEndpoint(method: String, endpoint: String, body: String) {
        request(method, endpoint, body)
    }

    @io.cucumber.java.en.When("I get status code {string}")
    fun getStatusCode(code: String?) {
        Assert.assertEquals(getLastResponse()?.statusCode(), Integer.parseInt(code))
    }

    @io.cucumber.java.en.When("I verify response does not contain duplicates")
    fun verifyDuplicatesInResponse() {
        Assert.assertFalse(validateLastResponse())
    }

    @io.cucumber.java.en.When("I verify count is not equal 0")
    fun verifyCountIsNotEqualZero() {
        Assert.assertTrue(validateLastCount())
    }

    @io.cucumber.java.en.When("I send {string} request authenticated with {string} and {string} credentials to {string} endpoint")
    fun sendAuthenticatedRequest(method: String, login: String, password: String, endpoint: String) {
        getToken(login, password)
        sendAuthenticatedRequest(method, endpoint)
    }

    @io.cucumber.java.en.When("I send {string} request authenticated with {string} and {string} credentials to {string} endpoint with body {string}")
    fun sendAuthenticatedRequestWithBody(
        method: String,
        login: String,
        password: String,
        endpoint: String,
        body: String
    ) {
        getToken(login, password)
        sendAuthenticatedRequestWithBody(method, endpoint, body)
    }

    @io.cucumber.java.en.Then("I get response body with {string} {string}")
    fun verifyBodyElement(argument: String, parameter: String) {
        val lastResponseArgument = getLastResponse()?.jsonPath()?.getString(argument) ?: return
        Assert.assertTrue(
            lastResponseArgument.contains(parameter),
            "The response body is not containing expected $argument."
        )
    }

    @io.cucumber.java.en.Then("I get response body with existing {string}")
    fun verifyExistenceBodyElement(argument: String) {
        val lastResponseArgument = getLastResponse()?.jsonPath()?.getString(argument) ?: return
        Assert.assertTrue(
            lastResponseArgument.isNotEmpty(),
            "The $argument is empty."
        )
    }
}
