package io.template.app.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTests {

    @Test
    fun testGreet() {
        assertTrue(Greeting().greet().isNotEmpty())
    }
}
