package io.pivotal.pal.tracker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class PalTrackerApplication {
    @Bean
    fun timeEntryRepository() = InMemoryTimeEntryRepository()
}

fun main(args: Array<String>) {
    SpringApplication.run(PalTrackerApplication::class.java, *args)
}
