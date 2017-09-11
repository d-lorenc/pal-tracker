package test.pivotal.pal.trackerapi

import com.jayway.jsonpath.JsonPath.parse
import io.pivotal.pal.tracker.PalTrackerApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(PalTrackerApplication::class), webEnvironment = RANDOM_PORT)
class HealthApiTest {

    private lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    private lateinit var port: String

    @Before
    fun setUp() {
        val builder = RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthorization("user", "password")

        restTemplate = TestRestTemplate(builder)
    }

    @Test
    fun healthTest() {
        val response = this.restTemplate.getForEntity("/health", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val healthJson = parse(response.body)

        assertThat(healthJson.read("$.status", String::class.java)).isEqualTo("UP")
        assertThat(healthJson.read("$.db.status", String::class.java)).isEqualTo("UP")
        assertThat(healthJson.read("$.diskSpace.status", String::class.java)).isEqualTo("UP")
    }
}
