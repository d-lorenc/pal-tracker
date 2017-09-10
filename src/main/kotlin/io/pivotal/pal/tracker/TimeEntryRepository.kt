package io.pivotal.pal.tracker

interface TimeEntryRepository {
    fun find(id: Long): TimeEntry?
    fun list(): List<TimeEntry>
    fun create(timeEntry: TimeEntry): TimeEntry
    fun update(id: Long, timeEntry: TimeEntry): TimeEntry?
    fun delete(id: Long)
}