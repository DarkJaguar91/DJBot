package org.darkjaguar.djbotdiscord.logging

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.pattern.AbstractPatternConverter
import org.apache.logging.log4j.core.pattern.ConverterKeys
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter
import org.apache.logging.log4j.core.pattern.PatternConverter
import org.apache.logging.log4j.core.pattern.PatternFormatter
import org.darkjaguar.djbotdiscord.logging.ansi.AnsiColor
import org.darkjaguar.djbotdiscord.logging.ansi.AnsiElement
import org.darkjaguar.djbotdiscord.logging.ansi.AnsiOutput
import org.darkjaguar.djbotdiscord.logging.ansi.AnsiStyle
import java.util.Collections
import java.util.HashMap


@Plugin(name = "color", category = PatternConverter.CATEGORY)
@ConverterKeys("clr", "color")
class ColorConverter private constructor(private val formatters: List<PatternFormatter>, private val styling: AnsiElement?) :
    LogEventPatternConverter("style", "style") {

    override fun handlesThrowable(): Boolean {
        for (formatter in this.formatters) {
            if (formatter.handlesThrowable()) {
                return true
            }
        }
        return super.handlesThrowable()
    }

    override fun format(event: LogEvent, toAppendTo: StringBuilder) {
        val buf = StringBuilder()
        for (formatter in this.formatters) {
            formatter.format(event, buf)
        }
        if (buf.isNotEmpty()) {
            var element: AnsiElement? = this.styling
            if (element == null) {
                // Assume highlighting
                element = LEVELS[event.level.intLevel()]
                element = if (element != null) element else AnsiColor.GREEN
            }
            appendAnsiString(toAppendTo, buf.toString(), element)
        }
    }

    protected fun appendAnsiString(
        toAppendTo: StringBuilder, `in`: String,
        element: AnsiElement
    ) {
        toAppendTo.append(AnsiOutput.toString(element, `in`))
    }

    companion object {

        private val ELEMENTS: Map<String, AnsiElement>

        init {
            val ansiElements = HashMap<String, AnsiElement>()
            ansiElements["faint"] = AnsiStyle.FAINT
            ansiElements["red"] = AnsiColor.RED
            ansiElements["green"] = AnsiColor.GREEN
            ansiElements["yellow"] = AnsiColor.YELLOW
            ansiElements["blue"] = AnsiColor.BLUE
            ansiElements["magenta"] = AnsiColor.MAGENTA
            ansiElements["cyan"] = AnsiColor.CYAN
            ELEMENTS = Collections.unmodifiableMap(ansiElements)
        }

        private val LEVELS: Map<Int, AnsiElement>

        init {
            val ansiLevels = HashMap<Int, AnsiElement>()
            ansiLevels[Level.FATAL.intLevel()] = AnsiColor.RED
            ansiLevels[Level.ERROR.intLevel()] = AnsiColor.RED
            ansiLevels[Level.WARN.intLevel()] = AnsiColor.YELLOW
            LEVELS = Collections.unmodifiableMap(ansiLevels)
        }

        /**
         * Creates a new instance of the class. Required by Log4J2.
         * @param config the configuration
         * @param options the options
         * @return a new instance, or `null` if the options are invalid
         */
        fun newInstance(config: Configuration, options: Array<String?>): ColorConverter? {
            if (options.isEmpty()) {
                AbstractPatternConverter.LOGGER.error(
                    "Incorrect number of options on style. " + "Expected at least 1, received {}",
                    options.size
                )
                return null
            }
            if (options[0] == null) {
                AbstractPatternConverter.LOGGER.error("No pattern supplied on style")
                return null
            }
            val parser = PatternLayout.createPatternParser(config)
            val formatters = parser.parse(options[0])
            val element = if (options.size != 1) ELEMENTS[options[1]] else null
            return ColorConverter(formatters, element)
        }
    }

}
