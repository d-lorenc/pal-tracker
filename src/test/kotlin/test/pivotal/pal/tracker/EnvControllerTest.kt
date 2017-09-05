package test.pivotal.pal.tracker

import io.pivotal.pal.tracker.EnvController
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EnvControllerTest {
    @Test
    fun getEnv() {
        val controller = EnvController(
                "8675",
                "12G",
                "34",
                "123.sesame.street"
        )

        val env = controller.getEnv()

        assertThat(env["PORT"]).isEqualTo("8675")
        assertThat(env["MEMORY_LIMIT"]).isEqualTo("12G")
        assertThat(env["CF_INSTANCE_INDEX"]).isEqualTo("34")
        assertThat(env["CF_INSTANCE_ADDR"]).isEqualTo("123.sesame.street")
    }
}
