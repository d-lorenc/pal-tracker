package io.pivotal.pal.tracker

data class TimeEntry(
        val id: Long?,
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
