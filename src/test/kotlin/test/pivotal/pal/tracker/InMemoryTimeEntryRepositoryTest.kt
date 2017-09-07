package test.pivotal.pal.tracker

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository
import io.pivotal.pal.tracker.TimeEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Arrays.asList

class InMemoryTimeEntryRepositoryTest {

    @Test
    fun create() {
        val repo = InMemoryTimeEntryRepository()
        val createdTimeEntry = repo.create(TimeEntry(123, 456, "today", 8))

        val expected = TimeEntry(1L, 123, 456, "today", 8)
        assertThat(createdTimeEntry).isEqualTo(expected)

        val readEntry = repo.find(createdTimeEntry.id!!)
        assertThat(readEntry).isEqualTo(expected)
    }

    @Test
    fun find() {
        val repo = InMemoryTimeEntryRepository()
        repo.create(TimeEntry(123, 456, "today", 8))

        val expected = TimeEntry(1L, 123, 456, "today", 8)
        val readEntry = repo.find(1L)
        assertThat(readEntry).isEqualTo(expected)
    }

    @Test
    fun list() {
        val repo = InMemoryTimeEntryRepository()
        repo.create(TimeEntry(123, 456, "today", 8))
        repo.create(TimeEntry(789, 654, "yesterday", 4))

        val expected = asList(
                TimeEntry(1L, 123, 456, "today", 8),
                TimeEntry(2L, 789, 654, "yesterday", 4)
        )
        assertThat(repo.list()).isEqualTo(expected)
    }

    @Test
    fun update() {
        val repo = InMemoryTimeEntryRepository()
        val (id) = repo.create(TimeEntry(123, 456, "today", 8))

        val updatedEntry = repo.update(
                id!!,
                TimeEntry(321, 654, "tomorrow", 5))

        val expected = TimeEntry(id, 321, 654, "tomorrow", 5)
        assertThat(updatedEntry).isEqualTo(expected)
        assertThat(repo.find(id)).isEqualTo(expected)
    }

    @Test
    fun delete() {
        val repo = InMemoryTimeEntryRepository()
        val (id) = repo.create(TimeEntry(123, 456, "today", 8))

        repo.delete(id!!)
        assertThat(repo.list()).isEmpty()
    }
}
