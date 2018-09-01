package org.darkjaguar.djbotdiscord.logging.ansi

import java.util.Locale


object AnsiOutput {

    private val ENCODE_JOIN = ";"

    private var enabled = Enabled.DETECT

    private var consoleAvailable: Boolean? = null

    private var ansiCapable: Boolean? = null

    private val OPERATING_SYSTEM_NAME = System.getProperty("os.name")
        .toLowerCase(Locale.ENGLISH)

    private val ENCODE_START = "\u001b["

    private val ENCODE_END = "m"

    private val RESET = "0;" + AnsiColor.DEFAULT

    private val isEnabled: Boolean
        get() {
            if (enabled == Enabled.DETECT) {
                if (ansiCapable == null) {
                    ansiCapable = detectIfAnsiCapable()
                }
                return ansiCapable!!
            }
            return enabled == Enabled.ALWAYS
        }

    /**
     * Sets if ANSI output is enabled.
     * @param enabled if ANSI is enabled, disabled or detected
     */
    fun setEnabled(enabled: Enabled) {
        AnsiOutput.enabled = enabled
    }

    /**
     * Sets if the System.console() is known to be available.
     * @param consoleAvailable if the console is known to be available or `null` to
     * use standard detection logic.
     */
    fun setConsoleAvailable(consoleAvailable: Boolean?) {
        AnsiOutput.consoleAvailable = consoleAvailable
    }

    internal fun getEnabled(): Enabled {
        return AnsiOutput.enabled
    }

    /**
     * Encode a single [AnsiElement] if output is enabled.
     * @param element the element to encode
     * @return the encoded element or an empty string
     */
    fun encode(element: AnsiElement): String {
        return if (isEnabled) {
            ENCODE_START + element + ENCODE_END
        } else ""
    }

    /**
     * Create a new ANSI string from the specified elements. Any [AnsiElement]s will
     * be encoded as required.
     * @param elements the elements to encode
     * @return a string of the encoded elements
     */
    fun toString(vararg elements: Any): String {
        val sb = StringBuilder()
        if (isEnabled) {
            buildEnabled(sb, elements)
        } else {
            buildDisabled(sb, elements)
        }
        return sb.toString()
    }

    private fun buildEnabled(sb: StringBuilder, elements: Array<out Any>) {
        var writingAnsi = false
        var containsEncoding = false
        for (element in elements) {
            if (element is AnsiElement) {
                containsEncoding = true
                if (!writingAnsi) {
                    sb.append(ENCODE_START)
                    writingAnsi = true
                } else {
                    sb.append(ENCODE_JOIN)
                }
            } else {
                if (writingAnsi) {
                    sb.append(ENCODE_END)
                    writingAnsi = false
                }
            }
            sb.append(element)
        }
        if (containsEncoding) {
            sb.append(if (writingAnsi) ENCODE_JOIN else ENCODE_START)
            sb.append(RESET)
            sb.append(ENCODE_END)
        }
    }

    private fun buildDisabled(sb: StringBuilder, elements: Array<out Any>) {
        for (element in elements) {
            if (element !is AnsiElement && element != null) {
                sb.append(element)
            }
        }
    }

    private fun detectIfAnsiCapable(): Boolean {
        try {
            if (java.lang.Boolean.FALSE == consoleAvailable) {
                return false
            }
            return if (consoleAvailable == null && System.console() == null) {
                false
            } else !OPERATING_SYSTEM_NAME.contains("win")
        } catch (ex: Throwable) {
            return false
        }

    }

    /**
     * Possible values to pass to [AnsiOutput.setEnabled]. Determines when to output
     * ANSI escape sequences for coloring application output.
     */
    enum class Enabled {

        /**
         * Try to detect whether ANSI coloring capabilities are available. The default
         * value for [AnsiOutput].
         */
        DETECT,

        /**
         * Enable ANSI-colored output.
         */
        ALWAYS,

        /**
         * Disable ANSI-colored output.
         */
        NEVER

    }

}
