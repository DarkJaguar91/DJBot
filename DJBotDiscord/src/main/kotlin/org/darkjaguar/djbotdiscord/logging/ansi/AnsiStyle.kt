package org.darkjaguar.djbotdiscord.logging.ansi

enum class AnsiStyle(val code: String) : AnsiElement {
    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4")
}
