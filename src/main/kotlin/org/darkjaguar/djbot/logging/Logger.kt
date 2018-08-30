package org.darkjaguar.djbot.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val loggers = HashMap<String, Logger>()

val <T : Any> T.logger: Logger
    get() {
        val name = this::class.java.canonicalName
        return loggers.getOrPut(name) { LoggerFactory.getLogger(name) }
    }
