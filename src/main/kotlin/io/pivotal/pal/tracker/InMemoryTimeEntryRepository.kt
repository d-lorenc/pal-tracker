package io.pivotal.pal.tracker

import java.util.*

class InMemoryTimeEntryRepository : TimeEntryRepository {

    private val timeEntries = HashMap<Long, TimeEntry>()

    override fun create(timeEntry: TimeEntry): TimeEntry {
        timeEntry.id = (timeEntries.size + 1).toLong()
        timeEntries.put(timeEntry.id, timeEntry)
        return timeEntry
    }

    override fun find(id: Long): TimeEntry? {
        return timeEntries.get(id)
    }

    override fun list(): List<TimeEntry> {
        return ArrayList(timeEntries.values)
    }

    override fun update(id: Long, timeEntry: TimeEntry): TimeEntry {
        timeEntries.replace(id, timeEntry)
        timeEntry.id = id
        return timeEntry
    }

    override fun delete(id: Long) {
        timeEntries.remove(id)
    }
}