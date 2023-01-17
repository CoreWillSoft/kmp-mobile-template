package io.template.app.common.boot

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class LoggerInitializer(enableLogger: Boolean) {
    init {
        if (enableLogger) Napier.base(DebugAntilog())
    }
}
