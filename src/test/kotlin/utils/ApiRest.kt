package utils

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.json.simple.JSONObject
import utils.Configuration.dataModel
import java.util.Locale

open class ApiRest {

    private var lastResponse: Response? = null
    private var lastRequestBody: String? = null
    private var lastToken: String? = null

    /**
     * Sends request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     * @param body     Body to be attached to request.
     * @param token    Authorization token to be used.
     */
    open fun request(environment: HashMap<*, *>, method: String, endpoint: String, body: String, token: String) {
        val requestUrl: String = if (mockChecker(environment, endpoint) != "") {
            environment["wiremock_host"].toString() + endpoint
        } else {
            environment["host"].toString() + endpoint
        }
        println(requestUrl)
        lastRequestBody = body
        val request: RequestSpecification = RestAssured.given()
        request.accept("*/*")
        if (body != "") {
            request.body(body)
            request.header("Content-Type", "application/json")
        }

        lastResponse = when (method.uppercase(Locale.getDefault())) {
            "GET" -> request.get(requestUrl)
            "HEAD" -> request.head(requestUrl)
            "POST" -> request.post(requestUrl)
            "PUT" -> request.put(requestUrl)
            "DELETE" -> request.delete(requestUrl)
            "PATCH" -> request.patch(requestUrl)
            else -> null
        }
    }

    private fun mockChecker(environment: HashMap<*, *>, endpoint: String): String {
        val mocks = environment["mocked_endpoints"].toString()
            .replace("[", "")
            .replace("]", "")
        val arrOfStr: List<String> = mocks.split(", ")
        var mockedEndpoint = ""
        for (item in arrOfStr)
            if (item == endpoint) {
                mockedEndpoint = endpoint
                break
            }
        return mockedEndpoint
    }

    /**
     * Sends request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     */
    open fun request(method: String, endpoint: String) {
        request(dataModel() as HashMap<*, *>, method, endpoint, "", "")
    }

    /**
     * Sends request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     */
    open fun request(method: String, endpoint: String, body: String) {
        request(dataModel() as HashMap<*, *>, method, endpoint, body, "")
    }

    /**
     * Returns last HTTP response.
     *
     * @return lastResponse variable.
     */
    open fun getLastResponse(): Response? {
        return lastResponse
    }

    /**
     * Validate last HTTP response.
     *
     * @return lastResponse variable.
     */
    open fun validateLastResponse(): Boolean {
        return checkForDuplicates(lastResponse)
    }

    /**
     * Validate last HTTP response count.
     *
     * @return lastResponse variable.
     */
    open fun validateLastCount(): Boolean {
        val count = lastResponse?.then()?.extract()?.jsonPath()?.getInt("count")
        return count != 0
    }

    private fun <T> checkForDuplicates(vararg array: T): Boolean {
        val set: MutableSet<T> = HashSet()
        for (e in array) {
            if (set.contains(e)) return true
            if (e != null) set.add(e)
        }
        return false
    }

    /**
     * Return last received token
     *
     * @return lastResponse body token as string.
     */
    open fun getLastResponseBodyToken(): String? {
        return lastResponse?.then()?.extract()?.path<String>("token")
    }

    /**
     * Sends request to given endpoint and reads token.
     *
     * @param login    Login to be used for authentication.
     * @param password Password to be used for authentication.
     */
    private fun authentication(url: String, login: String, password: String) {
        val requestParams = JSONObject()
        requestParams["user"] = login
        requestParams["password"] = password
        requestParams["type"] = "ot"
        val request: RequestSpecification = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(requestParams.toJSONString())
        lastResponse = request.post("$url/api/v1/user/login")
        lastToken = getLastResponseBodyToken()
    }

    /**
     * Gets token.
     *
     * @param login    Login to be used for authentication.
     * @param password Password to be used for authentication.
     */
    open fun getToken(login: String, password: String): String {
        return authentication(dataModel() as String, login, password).toString()
    }

    /**
     * Sends authenticated request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     */
    fun sendAuthenticatedRequest(method: String, endpoint: String) {
        lastToken?.let { request(dataModel() as HashMap<*, *>, method, endpoint, "", it) }
    }

    /**
     * Sends authenticated request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     * @param body Body to be sent.
     */
    fun sendAuthenticatedRequestWithBody(method: String, endpoint: String, body: String) {
        lastToken?.let { request(dataModel() as HashMap<*, *>, method, endpoint, body, it) }
    }
}
