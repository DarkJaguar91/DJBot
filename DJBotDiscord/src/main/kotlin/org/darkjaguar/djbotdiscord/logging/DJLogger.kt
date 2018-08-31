package org.darkjaguar.djbotdiscord.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class DJLogger(private val logger: Logger) {
    fun info(error: Throwable? = null, message: () -> String) {
        if (logger.isInfoEnabled) {
            logger.info(message())
        }
    }

    fun error(error: Throwable? = null, message: () -> String) {
        if (logger.isErrorEnabled) {
            logger.error(message())
        }
    }
}

private val loggers = HashMap<String, DJLogger>()

val <T : Any> T.logger: DJLogger
    get() {
        val name = if (this::class.isCompanion) this::class.java.enclosingClass.canonicalName else this::class.java.canonicalName
        return loggers.getOrPut(name) { DJLogger(LoggerFactory.getLogger(name)) }
    }

inline fun <reified T : Any> withCustomLogger(logger: Logger, noinline block: () -> Unit) {
    withCustomLogger(T::class, logger, block)
}

fun withCustomLogger(clazz: KClass<*>, logger: Logger, block: () -> Unit) {
    val name = if (clazz.isCompanion) clazz.java.enclosingClass!!.canonicalName else clazz.java.canonicalName
    loggers[name] = DJLogger(logger)
    try {
        block()
    } finally {
        loggers.remove(name)
    }
}
