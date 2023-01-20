package io.template.app.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTestsIos {

    @Test
    fun testHello() {
        assertTrue("iOS" in Greeting().greet())
    }
}
