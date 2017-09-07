package io.pivotal.pal.tracker

import java.util.ArrayList
import java.util.HashMap

class InMemoryTimeEntryRepository : TimeEntryRepository {

    private val timeEntries = HashMap<Long, TimeEntry>()

    override fun create(timeEntry: TimeEntry): TimeEntry {
        val updatedTimeEntry = timeEntry.copy(id = (timeEntries.size + 1).toLong())
        timeEntries[updatedTimeEntry.id!!] = updatedTimeEntry
        return updatedTimeEntry
    }

    override fun find(id: Long) = timeEntries[id]

    override fun list() = ArrayList(timeEntries.values)

    override fun update(id: Long, timeEntry: TimeEntry): TimeEntry {
        val updatedTimeEntry = timeEntry.copy(id = id)
        timeEntries.replace(id, updatedTimeEntry)
        return updatedTimeEntry
    }

    override fun delete(id: Long) {
        timeEntries.remove(id)
    }
}
