package io.pivotal.pal.tracker

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class TimeEntryHealthIndicator(private val timeEntryRepo: TimeEntryRepository) : HealthIndicator {

    override fun health(): Health {
        val builder = Health.Builder()

        if (timeEntryRepo.count() < MAX_TIME_ENTRIES) {
            builder.up()
        } else {
            builder.down()
        }

        return builder.build()
    }

    companion object {
        private val MAX_TIME_ENTRIES = 5
    }
}
