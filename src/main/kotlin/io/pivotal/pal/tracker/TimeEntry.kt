package io.pivotal.pal.tracker

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "time_entries")
data class TimeEntry(
        @Id @GeneratedValue val id: Long?,
        val projectId: Long,
        val userId: Long,
        val date: String?,
        val hours: Int
) {

    constructor(projectId: Long, userId: Long, date: String, hours: Int) : this(
            id = null,
            projectId = projectId,
            userId = userId,
            date = date,
            hours = hours
    )
}
