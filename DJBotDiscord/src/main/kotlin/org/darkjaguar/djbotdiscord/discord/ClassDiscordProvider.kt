package org.darkjaguar.djbotdiscord.discord

class ClassDiscordProvider {
    private val discords = mapOf(
            "warrior" to listOf("https://discord.gg/0pYY7932lTH4FHW6"),
            "paladin" to listOf("https://discord.gg/0dvRDgpa5xZHFfnD"),
            "hunter" to listOf("https://discord.gg/yqer4BX", "https://discord.gg/G3tYdTG"),
            "rogue" to listOf("https://discord.gg/0h08tydxoNhfDVZf"),
            "priest" to listOf("https://discord.gg/WarcraftPriests", "https://discord.gg/nExdySC", "https://discord.gg/focusedwill"),
            "death knight" to listOf("https://discord.gg/acherus"),
            "dk" to listOf("https://discord.gg/acherus"),
            "shaman" to listOf("https://discord.gg/0VcupJEQX0HuE5HH", "https://discord.gg/AcTek6e"),
            "mage" to listOf("https://discord.gg/0gLMHikX2aZ23VdA"),
            "warlock" to listOf("https://discord.gg/0onXDymd9Wpc2CEu"),
            "monk" to listOf("https://discord.gg/0dkfBMAxzTkWj21F"),
            "druid" to listOf("https://discord.gg/0dWu0WkuetF87H9H"),
            "demon hunter" to listOf("https://discord.gg/zGGkNGC")
    )

    fun getDiscordsFor(name: String): List<String> {
        return discords[name.toLowerCase()] ?:
                listOf()
    }
}
