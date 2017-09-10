package test.pivotal.pal.tracker

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.pivotal.pal.tracker.TimeEntry
import io.pivotal.pal.tracker.TimeEntryController
import io.pivotal.pal.tracker.TimeEntryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus
import java.util.Arrays.asList

class TimeEntryControllerTest {

    private lateinit var timeEntryRepository: TimeEntryRepository
    private lateinit var controller: TimeEntryController

    @Before
    fun setUp() {
        timeEntryRepository = mock()
        controller = TimeEntryController(timeEntryRepository)
    }

    @Test
    fun testCreate() {
        val expected = TimeEntry(1L, 123, 456, "today", 8)
        doReturn(expected)
                .`when`(timeEntryRepository)
                .save(any<TimeEntry>())

        val response = controller.create(TimeEntry(123, 456, "today", 8))

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun testRead() {
        val expected = TimeEntry(1L, 123, 456, "today", 8)
        doReturn(expected)
                .`when`(timeEntryRepository)
                .findOne(1L)

        val response = controller.read(1L)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun testRead_NotFound() {
        doReturn(null)
                .`when`(timeEntryRepository)
                .findOne(1L)

        val response = controller.read(1L)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testList() {
        val expected = asList(
                TimeEntry(1L, 123, 456, "today", 8),
                TimeEntry(2L, 789, 321, "yesterday", 4)
        )
        doReturn(expected).`when`(timeEntryRepository).findAll()

        val response = controller.list()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun testUpdate() {
        val expected = TimeEntry(1L, 987, 654, "yesterday", 4)
        doReturn(expected)
                .`when`(timeEntryRepository)
                .save(any<TimeEntry>())

        val response = controller.update(1L, expected)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun testUpdate_NotFound() {
        doReturn(null)
                .`when`(timeEntryRepository)
                .save(any<TimeEntry>())

        val response = controller.update(1L, TimeEntry(1L, 123, 456, "today", 8))
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testDelete() {
        val response = controller.delete(1L)
        verify(timeEntryRepository).delete(1L)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}
