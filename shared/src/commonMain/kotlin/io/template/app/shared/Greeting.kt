package io.template.app.shared

import io.github.aakira.napier.Napier

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
