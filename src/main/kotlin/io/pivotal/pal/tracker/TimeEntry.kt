package io.pivotal.pal.tracker

class TimeEntry {
    var id: Long = 0
    var projectId: Long = 0
        private set
    var userId: Long = 0
        private set
    var date: String? = null
        private set
    var hours: Int = 0
        private set

    constructor() {}

    constructor(projectId: Long, userId: Long, date: String, hours: Int) {
        this.projectId = projectId
        this.userId = userId
        this.date = date
        this.hours = hours
    }

    constructor(id: Long, projectId: Long, userId: Long, date: String, hours: Int) {
        this.id = id
        this.projectId = projectId
        this.userId = userId
        this.date = date
        this.hours = hours
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val timeEntry = o as TimeEntry?

        if (id != timeEntry!!.id) return false
        if (projectId != timeEntry.projectId) return false
        if (userId != timeEntry.userId) return false
        if (hours != timeEntry.hours) return false
        return if (date != null) date == timeEntry.date else timeEntry.date == null
    }

    override fun hashCode(): Int {
        var result = (id xor id.ushr(32)).toInt()
        result = 31 * result + (projectId xor projectId.ushr(32)).toInt()
        result = 31 * result + (userId xor userId.ushr(32)).toInt()
        result = 31 * result + if (date != null) date!!.hashCode() else 0
        result = 31 * result + hours
        return result
    }

    override fun toString(): String {
        return "TimeEntry{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", hours=" + hours +
                '}'
    }
}