package test.pivotal.pal.trackerapi

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
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(PalTrackerApplication::class), webEnvironment = RANDOM_PORT)
class WelcomeApiTest {

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
    fun exampleTest() {
        val body = this.restTemplate.getForObject("/", String::class.java)
        assertThat(body).isEqualTo("Hello from test")
    }
}
