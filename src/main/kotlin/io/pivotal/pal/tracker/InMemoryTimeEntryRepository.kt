package io.pivotal.pal.tracker

import java.util.HashMap

class InMemoryTimeEntryRepository : TimeEntryRepository {

    private val timeEntries = HashMap<Long, TimeEntry>()

    override fun find(id: Long) = timeEntries[id]

    override fun list() = timeEntries.values.toList()

    override fun create(timeEntry: TimeEntry): TimeEntry {
        val nextId = (timeEntries.size + 1).toLong()
        val updatedTimeEntry = timeEntry.copy(id = nextId)
        timeEntries[nextId] = updatedTimeEntry
        return updatedTimeEntry
    }

    override fun update(id: Long, timeEntry: TimeEntry): TimeEntry? {
        val updatedTimeEntry = timeEntry.copy(id = id)
        timeEntries.replace(id, updatedTimeEntry)
        return updatedTimeEntry
    }

    override fun delete(id: Long) {
        timeEntries.remove(id)
    }
}
