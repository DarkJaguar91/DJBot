package org.darkjaguar.djbotdiscord.discord

import org.darkjaguar.djbotdiscord.events.interfaces.CommandEvent
import org.darkjaguar.djbotdiscord.utils.deleteMessage
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class ClassDiscordCommandEvent(private val provider: ClassDiscordProvider = ClassDiscordProvider()): CommandEvent {
    override val command: List<String>
        get() = listOf("discord")

    override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
        event.deleteMessage()

        if (args.isEmpty()) {
            event.message.author.orCreatePMChannel.sendMessage("Please provide a class you would like the discord link/s for.\n`/discord priest`\n`/discord warrior`")
            return
        }

        val name = args.joinToString(" ").toLowerCase()
        val discords = provider.getDiscordsFor(name)

        if (discords.isEmpty()) {
            event.message.author.orCreatePMChannel.sendMessage("Could not find any discords for class: $name")
            return
        }

        event.message.author.orCreatePMChannel.sendMessage("${name.capitalize()}: ${discords.joinToString(", ")}")
    }
}