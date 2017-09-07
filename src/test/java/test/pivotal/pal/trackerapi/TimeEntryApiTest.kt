package test.pivotal.pal.trackerapi

import com.jayway.jsonpath.JsonPath.parse
import com.mysql.cj.jdbc.MysqlDataSource
import io.pivotal.pal.tracker.PalTrackerApplication
import io.pivotal.pal.tracker.TimeEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(PalTrackerApplication::class), webEnvironment = RANDOM_PORT)
class TimeEntryApiTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val timeEntry = TimeEntry(123, 456, "today", 8)

    @Before
    fun setUp() {
        val dataSource = MysqlDataSource()
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"))

        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.execute("TRUNCATE time_entries")
    }

    @Test
    fun testCreate() {
        val createResponse = restTemplate.postForEntity("/time-entries", timeEntry, String::class.java)


        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val createJson = parse(createResponse.body)
        assertThat(createJson.read("$.id", Long::class.java)).isGreaterThan(0)
        assertThat(createJson.read("$.projectId", Long::class.java)).isEqualTo(123L)
        assertThat(createJson.read("$.userId", Long::class.java)).isEqualTo(456L)
        assertThat(createJson.read("$.date", String::class.java)).isEqualTo("today")
        assertThat(createJson.read("$.hours", Long::class.java)).isEqualTo(8)
    }

    @Test
    fun testList() {
        val id = createTimeEntry()


        val listResponse = restTemplate.getForEntity("/time-entries", String::class.java)


        assertThat(listResponse.statusCode).isEqualTo(HttpStatus.OK)

        val listJson = parse(listResponse.body)

        val timeEntries = listJson.read("$[*]", Collection::class.java)
        assertThat(timeEntries.size).isEqualTo(1)

        val readId = listJson.read("$[0].id", Long::class.java)
        assertThat(readId).isEqualTo(id)
    }

    @Test
    fun testRead() {
        val id = createTimeEntry()


        val readResponse = this.restTemplate.getForEntity("/time-entries/" + id!!, String::class.java)


        assertThat(readResponse.statusCode).isEqualTo(HttpStatus.OK)
        val readJson = parse(readResponse.body)
        assertThat(readJson.read("$.id", Long::class.java)).isEqualTo(id)
        assertThat(readJson.read("$.projectId", Long::class.java)).isEqualTo(123L)
        assertThat(readJson.read("$.userId", Long::class.java)).isEqualTo(456L)
        assertThat(readJson.read("$.date", String::class.java)).isEqualTo("today")
        assertThat(readJson.read("$.hours", Long::class.java)).isEqualTo(8)
    }

    @Test
    fun testUpdate() {
        val id = createTimeEntry()
        val updatedTimeEntry = TimeEntry(2, 3, "tomorrow", 9)


        val updateResponse = restTemplate.exchange("/time-entries/" + id!!, HttpMethod.PUT, HttpEntity(updatedTimeEntry, null), String::class.java)


        assertThat(updateResponse.statusCode).isEqualTo(HttpStatus.OK)

        val updateJson = parse(updateResponse.body)
        assertThat(updateJson.read("$.id", Long::class.java)).isEqualTo(id)
        assertThat(updateJson.read("$.projectId", Long::class.java)).isEqualTo(2L)
        assertThat(updateJson.read("$.userId", Long::class.java)).isEqualTo(3L)
        assertThat(updateJson.read("$.date", String::class.java)).isEqualTo("tomorrow")
        assertThat(updateJson.read("$.hours", Long::class.java)).isEqualTo(9)
    }

    @Test
    fun testDelete() {
        val id = createTimeEntry()


        val deleteResponse = restTemplate.exchange("/time-entries/" + id!!, HttpMethod.DELETE, null, String::class.java)


        assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        val deletedReadResponse = restTemplate.getForEntity("/time-entries/" + id, String::class.java)
        assertThat(deletedReadResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    private fun createTimeEntry(): Long? {
        return restTemplate.postForObject("/time-entries", timeEntry, TimeEntry::class.java).id
    }
}
