package io.pivotal.pal.tracker

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnvController(
        @Value("\${PORT:NOT SET}") private val port: String,
        @Value("\${MEMORY_LIMIT:NOT SET}") private val memoryLimit: String ,
        @Value("\${CF_INSTANCE_INDEX:NOT SET}") private val cfInstanceIndex: String,
        @Value("\${CF_INSTANCE_ADDR:NOT SET}") private val cfInstanceAddr: String) {

    @GetMapping("/env")
    fun getEnv() = mapOf(
            "PORT" to port,
            "MEMORY_LIMIT" to memoryLimit,
            "CF_INSTANCE_INDEX" to cfInstanceIndex,
            "CF_INSTANCE_ADDR" to cfInstanceAddr
    )
}
