package test.pivotal.pal.tracker


import com.mysql.cj.jdbc.MysqlDataSource
import io.pivotal.pal.tracker.JdbcTimeEntryRepository
import io.pivotal.pal.tracker.TimeEntry
import io.pivotal.pal.tracker.TimeEntryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate

class JdbcTimeEntryRepositoryTest {

    private lateinit var subject: TimeEntryRepository
    private lateinit var jdbcTemplate: JdbcTemplate

    @Before
    fun setUp() {
        val dataSource = MysqlDataSource()
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"))

        subject = JdbcTimeEntryRepository(dataSource)

        jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.execute("DELETE FROM time_entries")
    }

    @Test
    fun createInsertsATimeEntryRecord() {
        val newTimeEntry = TimeEntry(123, 321, "today", 8)
        val entry = subject.create(newTimeEntry)

        val foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", entry!!.id)

        assertThat(foundEntry["id"]).isEqualTo(entry.id)
        assertThat(foundEntry["project_id"]).isEqualTo(123L)
        assertThat(foundEntry["user_id"]).isEqualTo(321L)
        assertThat(foundEntry["date"]).isEqualTo("today")
        assertThat(foundEntry["hours"]).isEqualTo(8)
    }

    @Test
    fun createReturnsTheCreatedTimeEntry() {
        val newTimeEntry = TimeEntry(123, 321, "today", 8)
        val entry = subject.create(newTimeEntry)

        assertThat(entry!!.id).isNotNull()
        assertThat(entry.projectId).isEqualTo(123)
        assertThat(entry.userId).isEqualTo(321)
        assertThat(entry.date).isEqualTo("today")
        assertThat(entry.hours).isEqualTo(8)
    }

    @Test
    fun findFindsATimeEntry() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, date, hours) " + "VALUES (999, 123, 321, 'today', 8)"
        )

        val timeEntry = subject.find(999L)

        assertThat(timeEntry!!.id).isEqualTo(999L)
        assertThat(timeEntry.projectId).isEqualTo(123L)
        assertThat(timeEntry.userId).isEqualTo(321L)
        assertThat(timeEntry.date).isEqualTo("today")
        assertThat(timeEntry.hours).isEqualTo(8)
    }

    @Test
    fun findReturnsNullWhenNotFound() {
        val timeEntry = subject.find(999L)

        assertThat(timeEntry).isNull()
    }

    @Test
    fun listFindsAllTimeEntries() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, date, hours) " + "VALUES (999, 123, 321, 'today', 8), (888, 456, 678, 'yesterday', 9)"
        )

        val timeEntries = subject.list()
        assertThat(timeEntries.size).isEqualTo(2)

        var timeEntry = timeEntries[0]
        assertThat(timeEntry.id).isEqualTo(888L)
        assertThat(timeEntry.projectId).isEqualTo(456L)
        assertThat(timeEntry.userId).isEqualTo(678L)
        assertThat(timeEntry.date).isEqualTo("yesterday")
        assertThat(timeEntry.hours).isEqualTo(9)

        timeEntry = timeEntries[1]
        assertThat(timeEntry.id).isEqualTo(999L)
        assertThat(timeEntry.projectId).isEqualTo(123L)
        assertThat(timeEntry.userId).isEqualTo(321L)
        assertThat(timeEntry.date).isEqualTo("today")
        assertThat(timeEntry.hours).isEqualTo(8)
    }

    @Test
    fun updateReturnsTheUpdatedRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, date, hours) " + "VALUES (1000, 123, 321, 'today', 8)")

        val timeEntryUpdates = TimeEntry(456, 987, "tomorrow", 10)

        val updatedTimeEntry = subject.update(1000L, timeEntryUpdates)

        assertThat(updatedTimeEntry!!.id).isEqualTo(1000L)
        assertThat(updatedTimeEntry.projectId).isEqualTo(456L)
        assertThat(updatedTimeEntry.userId).isEqualTo(987L)
        assertThat(updatedTimeEntry.date).isEqualTo("tomorrow")
        assertThat(updatedTimeEntry.hours).isEqualTo(10)
    }

    @Test
    fun updateUpdatesTheRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, date, hours) " + "VALUES (1000, 123, 321, 'today', 8)")

        val updatedTimeEntry = TimeEntry(456, 322, "tomorrow", 10)

        val timeEntry = subject.update(1000L, updatedTimeEntry)

        val foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", timeEntry!!.id)

        assertThat(foundEntry["id"]).isEqualTo(timeEntry.id)
        assertThat(foundEntry["project_id"]).isEqualTo(456L)
        assertThat(foundEntry["user_id"]).isEqualTo(322L)
        assertThat(foundEntry["date"]).isEqualTo("tomorrow")
        assertThat(foundEntry["hours"]).isEqualTo(10)
    }

    @Test
    @Throws(Exception::class)
    fun deleteRemovesTheRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, date, hours) " + "VALUES (999, 123, 321, 'today', 8)"
        )

        subject.delete(999L)

        val foundEntry = jdbcTemplate.queryForMap("Select count(*) count from time_entries where id = ?", 999)
        assertThat(foundEntry["count"]).isEqualTo(0L)
    }
}
