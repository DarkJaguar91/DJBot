package org.darkjaguar.djbotdiscord.events.interfaces

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

interface CommandEvent {
    val command: List<String>

    fun onEvent(event: MessageReceivedEvent, args: List<String>)
}