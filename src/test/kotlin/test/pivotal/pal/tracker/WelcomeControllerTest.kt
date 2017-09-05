package test.pivotal.pal.tracker

import io.pivotal.pal.tracker.WelcomeController
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WelcomeControllerTest {

    @Test
    fun itSaysHello() {
        val controller = WelcomeController("A welcome message")

        assertThat(controller.sayHello()).isEqualTo("A welcome message")
    }
}
