package test.pivotal.pal.trackerapi

import io.pivotal.pal.tracker.PalTrackerApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(PalTrackerApplication::class), webEnvironment = RANDOM_PORT)
class SecurityApiTest {

    @LocalServerPort
    private lateinit var port: String
    private lateinit var authorizedRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var unAuthorizedRestTemplate: TestRestTemplate

    @Before
    fun setUp() {
        val builder = RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthorization("user", "password")

        authorizedRestTemplate = TestRestTemplate(builder)
    }

    @Test
    fun unauthorizedTest() {
        val response = this.unAuthorizedRestTemplate.getForEntity("/", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun authorizedTest() {
        val response = this.authorizedRestTemplate.getForEntity("/", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
