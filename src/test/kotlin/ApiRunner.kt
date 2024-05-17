import com.github.tomakehurst.wiremock.WireMockServer
import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions
import org.testng.SkipException
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite
import utils.Configuration.envModel
import utils.Stubs

@CucumberOptions(
    plugin = ["io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm"],
    features = ["src/test/kotlin/features/"],
    glue = ["steps"],
    tags = ["@apiTest"]
)
class ApiRunner : AbstractTestNGCucumberTests() {
    private var wireMockServer: WireMockServer? = null

    @BeforeSuite
    fun setup() {
        if(envModel().toString() == "local") {
            println("start wiremock")
            wireMockServer = WireMockServer(8081)
            if(wireMockServer == null) {
                SkipException("Wiremock has not started")
            }
            wireMockServer?.start()
            Stubs().wireMockEndpoints(wireMockServer!!)
        }
    }

    @AfterSuite
    fun tearDown() {
        if(envModel().toString() == "local") {
            println("stop wiremock")
            wireMockServer?.stop()
        }
    }
}

