package io.pivotal.pal.tracker

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
class TimeEntryController(private val timeEntriesRepo: TimeEntryRepository) {

    @PostMapping
    fun create(@RequestBody timeEntry: TimeEntry): ResponseEntity<TimeEntry> {
        val createdTimeEntry = timeEntriesRepo.create(timeEntry)

        return ResponseEntity(createdTimeEntry, HttpStatus.CREATED)
    }

    @GetMapping("{id}")
    fun read(@PathVariable id: Long): ResponseEntity<TimeEntry> {
        val timeEntry = timeEntriesRepo.find(id)
        return if (timeEntry != null) {
            ResponseEntity(timeEntry, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    fun list(): ResponseEntity<List<TimeEntry>> {
        return ResponseEntity(timeEntriesRepo.list(), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody timeEntry: TimeEntry): ResponseEntity<TimeEntry> {
        val updatedTimeEntry = timeEntriesRepo.update(id, timeEntry)
        return if (updatedTimeEntry != null) {
            ResponseEntity(updatedTimeEntry, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<TimeEntry> {
        timeEntriesRepo.delete(id)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}