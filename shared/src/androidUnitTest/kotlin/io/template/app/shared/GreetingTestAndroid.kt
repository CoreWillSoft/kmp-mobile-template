package io.template.app.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTestsAndroid {

    @Test
    fun testHello() {
        assertTrue(Greeting().greet().contains("Android"))
    }
}
