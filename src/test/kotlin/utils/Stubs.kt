package utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import objects.Endpoints.someLovelyEndpoint
import objects.Paths.jsonFiles


class Stubs {

    fun wireMockEndpoints(wireMockServer: WireMockServer) {
        setupStub(wireMockServer, someLovelyEndpoint, 200, "some-lovely-endpoint.json")
    }

    private fun setupStub(wireMockServer: WireMockServer, endpoint: String, code: Int, body: String) {
        wireMockServer.stubFor(
            get(urlEqualTo(endpoint))
                .willReturn(
                    aResponse()
                        .withStatus(code)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(DataUtil().readTextFromFile(jsonFiles+body))
                )
        )
    }
}