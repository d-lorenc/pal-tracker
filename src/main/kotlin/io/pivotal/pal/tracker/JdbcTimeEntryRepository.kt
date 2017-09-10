package io.pivotal.pal.tracker

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.Statement.RETURN_GENERATED_KEYS
import javax.sql.DataSource

@Component
class JdbcTimeEntryRepository(dataSource: DataSource) : TimeEntryRepository {

    private val jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)

    private val timeEntryRowMapper = { rs: ResultSet, _: Int ->
        TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getString("date"),
                rs.getInt("hours")
        )
    }

    private val timeEntryExtractor = { rs: ResultSet ->
        if (rs.next()) timeEntryRowMapper(rs, 1) else null
    }

    override fun find(id: Long) = jdbcTemplate.query(
            "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
            arrayOf(id),
            timeEntryExtractor)

    override fun list() = jdbcTemplate.query(
            "SELECT id, project_id, user_id, date, hours FROM time_entries",
            timeEntryRowMapper)

    override fun create(timeEntry: TimeEntry): TimeEntry {
        val generatedKeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " + "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            )

            statement.setLong(1, timeEntry.projectId)
            statement.setLong(2, timeEntry.userId)
            statement.setString(3, timeEntry.date)
            statement.setInt(4, timeEntry.hours)

            statement
        }, generatedKeyHolder)

        return find(generatedKeyHolder.key.toLong())!!
    }

    override fun update(id: Long, timeEntry: TimeEntry): TimeEntry? {
        jdbcTemplate.update("UPDATE time_entries " +
                "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                "WHERE id = ?",
                timeEntry.projectId,
                timeEntry.userId,
                timeEntry.date,
                timeEntry.hours,
                id)

        return find(id)
    }

    override fun delete(id: Long) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id)
    }
}
