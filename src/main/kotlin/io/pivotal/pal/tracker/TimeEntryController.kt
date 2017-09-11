package io.pivotal.pal.tracker

import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.boot.actuate.metrics.GaugeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/time-entries")
class TimeEntryController(
        private val timeEntriesRepo: TimeEntryRepository,
        private val counter: CounterService,
        private val gauge: GaugeService
) {

    @PostMapping
    fun create(@RequestBody timeEntry: TimeEntry): ResponseEntity<TimeEntry> {
        val createdTimeEntry = timeEntriesRepo.save(timeEntry)
        counter.increment("TimeEntry.created")
        gauge.submit("timeEntries.count", timeEntriesRepo.count().toDouble())
        return ResponseEntity(createdTimeEntry, HttpStatus.CREATED)
    }

    @GetMapping("{id}")
    fun read(@PathVariable id: Long): ResponseEntity<TimeEntry> {
        val timeEntry = timeEntriesRepo.findOne(id)
        return if (timeEntry != null) {
            counter.increment("TimeEntry.read")
            ResponseEntity(timeEntry, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    fun list(): ResponseEntity<Iterable<TimeEntry>> {
        counter.increment("TimeEntry.listed")
        return ResponseEntity(timeEntriesRepo.findAll(), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody timeEntry: TimeEntry): ResponseEntity<TimeEntry> {
        val updatedTimeEntry = timeEntriesRepo.save(timeEntry.copy(id = id))
        return if (updatedTimeEntry != null) {
            counter.increment("TimeEntry.updated")
            ResponseEntity(updatedTimeEntry, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<TimeEntry> {
        timeEntriesRepo.delete(id)
        counter.increment("TimeEntry.deleted")
        gauge.submit("timeEntries.count", timeEntriesRepo.count().toDouble())

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
