package io.pivotal.pal.tracker

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnvController(
        @Value("\${PORT:NOT SET}") port: String,
        @Value("\${MEMORY_LIMIT:NOT SET}") memoryLimit: String ,
        @Value("\${CF_INSTANCE_INDEX:NOT SET}") cfInstanceIndex: String,
        @Value("\${CF_INSTANCE_ADDR:NOT SET}") cfInstanceAddr: String) {

    private val env: Map<String, String> = mapOf(
            "PORT" to port,
            "MEMORY_LIMIT" to memoryLimit,
            "CF_INSTANCE_INDEX" to cfInstanceIndex,
            "CF_INSTANCE_ADDR" to cfInstanceAddr
    )

    @GetMapping("/env")
    fun getEnv() = env
}
